package it.polimi.ingsw.gc42.view;

import it.polimi.ingsw.gc42.controller.GameStatus;
import it.polimi.ingsw.gc42.model.classes.cards.*;
import it.polimi.ingsw.gc42.model.classes.game.*;
import it.polimi.ingsw.gc42.network.*;
import it.polimi.ingsw.gc42.network.interfaces.NetworkController;
import it.polimi.ingsw.gc42.view.Classes.tui.*;
import it.polimi.ingsw.gc42.view.Classes.NetworkMode;
import it.polimi.ingsw.gc42.view.Interfaces.ViewController;
import javafx.application.Application;
import javafx.stage.Stage;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.*;

/**
 * This class handles the whole TUI behavior and output system.
 * All the TUI is contained inside this class.
 * All that's needed to run a TUI is to call .start() on a GameTerminal object.
 */
public class GameTerminal extends Application implements ViewController {
    // Attributes
    private boolean isAdvancedGraphicsMode = true;
    private boolean isColorTerminalSupported = true;
    private NetworkController controller;
    private Player player;
    private Scanner scanner = new Scanner(System.in);
    private int playerID;
    private boolean isYourTurn = false;

    private final ArrayList<Token> availableToken = new ArrayList<>();

    private final ArrayList<PlayAreaTerminal> playAreas = new ArrayList<>();

    private final ArrayList<Integer> rejoinableGames = new ArrayList<>();

    private TerminalCharacters terminalCharacters;

    private boolean isShowingGameCreationScreen = false;
    private boolean isWaiting = false;
    private boolean isGameCreator = false;
    private boolean hasChosenToken = false;

    private final BlockingDeque<Runnable> actions = new LinkedBlockingDeque<>();
    private TerminalInputHandler inputHandler;

    private ArrayList<ChatMessage> chat;


    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Formats a String using the Unicode Color codes to print a colored output.
     * @param str the Text that will be printed
     * @param fg a foreground color (text color) chosen from the UiColors enum
     * @return the formatted String to print in System.out
     */
    public String color(String str, UiColors fg) {
        return fg.toString() + str + UiColors.RESET;
    }

    /**
     *      * Formats a String using the Unicode Color codes to print a colored output on a colored background
     * @param str the Text that will be printed
     * @param fg a foreground color (text color) chosen from the UiColors enum
     * @param bg a background color chosen from the UiColors enum
     * @return the formatted String to print in System.out
     */
    private String color(String str, UiColors fg, UiColors bg) {
        return fg.toString() + bg.toString() + str + UiColors.RESET;
    }

    /**
     * Adds an action inside the Queue.
     * Actions are executed one by one from this Queue, so this is how a new one can be added.
     * @param runnable the Code to execute
     */
    public void addToActionQueue(Runnable runnable) {
        actions.add(runnable);
    }

    /**
     * Starts the TUI
     * @param stage the primary stage for this application, onto which
     * the application scene can be set.
     * Applications may create other stages, if needed, but they will not be
     * primary stages. THIS STAGE WILL NOT BE SHOWN
     * @throws Exception if any issue occurs
     */
    @Override
    public void start(Stage stage) throws Exception {
        if (System.getProperty("os.name").toLowerCase().contains("windows")) {
            ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c", "chcp", "65001").inheritIO();
            Process p = null;
            try {
                p = pb.start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                p.waitFor();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));

        // Initializes the PlayAreas
        playAreas.add(new PlayAreaTerminal(this));
        playAreas.add(new PlayAreaTerminal(this));
        playAreas.add(new PlayAreaTerminal(this));
        playAreas.add(new PlayAreaTerminal(this));

        // Initializes the Printing Extremes
        for (int i = 0; i < 4; i++) {
            playAreas.get(i).setPrintingExtremes("UP", 0);
            playAreas.get(i).setPrintingExtremes("DOWN", 800);
            playAreas.get(i).setPrintingExtremes("LEFT", 0);
            playAreas.get(i).setPrintingExtremes("RIGHT", 1440);
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (controller != null) {
                try {
                    controller.disconnect();
                    System.out.println("Shutdown hook: Disconnected from network.");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }));

        // Ask for TUI Mode
        boolean exitGameModeLoop = false;
        while (!exitGameModeLoop) {
            System.out.println("This game supports 3 view modes: Fancy, Enhanced and Standard.");
            System.out.println("Some Terminals don't properly support Fancy and Enhanced Mode: if you can see the left and the middle Cards properly,");
            System.out.println("then it's safe to play, otherwise we suggest playing Standard Mode");
            System.out.println();

            System.out.println("Fancy:");
            System.out.println("🍄🟥🟥🟨🟨🟨🟩🟩🌳");
            System.out.println("🟥🟥🟨🟨⚪🟨🟨🟩🟩");
            System.out.println("🟨⚪🟨🍷📜🪶🟨⚪🟨");
            System.out.println("🟪🟪🟨🟨⚪🟨🟨🟦🟦");
            System.out.println("🦋🟪🟪🟨🟨🟨🟦🟦🐺");
            System.out.println();

            System.out.println("Enhanced:");
            System.out.println(UiColors.RED + "ନ ▤ ▤ " + UiColors.YELLOW + "▩ ▩ ▩ " + UiColors.GREEN + "▦ ▦ ✿ " + UiColors.RESET);
            System.out.println(UiColors.RED + "▤ ▤ " + UiColors.YELLOW + "▩ ▩ " + UiColors.WHITE + "■ " + UiColors.YELLOW + "▩ ▩ " + UiColors.GREEN + "▦ ▦ " + UiColors.RESET);
            System.out.println(UiColors.YELLOW + "▩ " + UiColors.WHITE + "■ " + UiColors.YELLOW + "▩ Ỗ ∫ ϡ ▩ " + UiColors.WHITE + "■ " + UiColors.YELLOW + "▩ " + UiColors.RESET);
            System.out.println(UiColors.MAGENTA + "▧ ▧ " + UiColors.YELLOW + "▩ ▩ " + UiColors.WHITE + "■ " + UiColors.YELLOW + "▩ ▩ " + UiColors.CYAN + "▥ ▥ " + UiColors.RESET);
            System.out.println(UiColors.MAGENTA + "¥ ▧ ▧ " + UiColors.YELLOW + "▩ ▩ ▩ " + UiColors.CYAN + "▥ ▥ ♘ " + UiColors.RESET);
            System.out.println();

            System.out.println("Standard:");
            System.out.println("ନ ▤ ▤ ▩ ▩ ▩ ▦ ▦ ✿ ");
            System.out.println("▤ ▤ ▩ ▩ ■ ▩ ▩ ▦ ▦ ");
            System.out.println("▩ ■ ▩ Ỗ ∫ ϡ ▩ ■ ▩ ");
            System.out.println("▧ ▧ ▩ ▩ ■ ▩ ▩ ▥ ▥ ");
            System.out.println("¥ ▧ ▧ ▩ ▩ ▩ ▥ ▥ ♘ ");



            System.out.println("\nPress f to play in Fancy Mode, e for Enhanced Mode or s for Standard Mode:");
            String s = scanner.next();
            if(s.equals("f")) {
                isAdvancedGraphicsMode = true;
                exitGameModeLoop = true;
            } else if (s.equals("e")) {
                isAdvancedGraphicsMode = false;
                isColorTerminalSupported = true;
                exitGameModeLoop = true;
            } else if (s.equals("s")) {
                isAdvancedGraphicsMode = false;
                isColorTerminalSupported = false;
                exitGameModeLoop = true;
            } else {
                System.err.println("Invalid Choice!\n");
            }

            System.out.println("\n\n\n\n");
        }

        scanner = new Scanner(System.in);

        ExecutorService pool = Executors.newCachedThreadPool();
        inputHandler = new TerminalInputHandler(scanner);
        pool.submit(inputHandler);
        actions.add(() -> {
            System.out.println("Welcome to Codex Naturalis!");
            terminalCharacters = new TerminalCharacters(isAdvancedGraphicsMode, isColorTerminalSupported);

            login();
        });

        pool.submit(() -> {
            boolean exit = false;
            while (!exit) {
                try {
                    Runnable runnable = actions.take();
                    //pool.submit(runnable);
                    runnable.run();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    /**
     * Handles the creation of a New Game.
     * Creating a New Game requires the user to confirm and to type a Name for the Game.
     * This method also handles invalid inputs.
     * The Player is then automatically added inside the newly created Game.
     * The Game is automatically set to WAITING_FOR_PLAYERS.
     * @throws RemoteException in case of a network communication error
     */
    private void createNewGame(boolean needToAsk) throws RemoteException {
        if (needToAsk) {
            System.out.println("No games available! Press c to create a new one, or press r to refresh the list!");
            inputHandler.listen(new TerminalListener() {
                @Override
                public void onEvent(String input) {
                    if (input.equals("c")) {
                        try {
                            controller.createNewGame();
                        } catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }
                        setViewController();
                        controller.addPlayer(player);
                        try {
                            playerID = controller.getIndexOfPlayer(player.getNickname());
                        } catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }
                        controller.setCurrentStatus(GameStatus.WAITING_FOR_PLAYERS);
                        isGameCreator = true;

                        System.out.println("Enter a name for this game:");
                        inputHandler.listen(new TerminalListener() {
                            @Override
                            public void onEvent(String input) {
                                if (!input.isEmpty()) {
                                    try {
                                        controller.setName(input);
                                    } catch (RemoteException e) {
                                        throw new RuntimeException(e);
                                    }
                                    inputHandler.unlisten(this);
                                    System.out.println("Game created. Waiting for other players to join");
                                    try {
                                        chat = new ArrayList<>(controller.getFullChat());
                                    } catch (RemoteException e) {
                                        throw new RuntimeException(e);
                                    }
                                    isShowingGameCreationScreen = true;
                                } else {
                                    System.err.println("Name is mandatory! Please enter a name for this game:");
                                }
                            }
                        });
                    } else if (input.equals("r")) {
                        actions.add(() -> showAvailableGames());
                    } else {
                        System.out.println("Invalid input!");
                    }
                }
            });
        } else {
            try {
                controller.createNewGame();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
            setViewController();
            controller.addPlayer(player);
            try {
                playerID = controller.getIndexOfPlayer(player.getNickname());
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
            controller.setCurrentStatus(GameStatus.WAITING_FOR_PLAYERS);
            isGameCreator = true;

            System.out.println("Enter a name for this game:");
            inputHandler.listen(new TerminalListener() {
                @Override
                public void onEvent(String input) {

                    if (!input.isEmpty()) {
                        try {
                            controller.setName(input);
                        } catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }
                        inputHandler.unlisten(this);
                        System.out.println("Game created. Waiting for other players to join");
                        try {
                            chat = new ArrayList<>(controller.getFullChat());
                        } catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }
                        isShowingGameCreationScreen = true;
                    } else {
                        System.err.println("Name is mandatory! Please enter a name for this game:");
                    }
                }
            });
        }
    }

    /**
     * Connects a new QueuedClientController to the Game inside the Server, allowing all the inputs
     * from the Server to be added to the Queue of actions.
     */
    private void setViewController() {
        try {
            controller.setViewController(new QueuedClientController(this));
        } catch (AlreadyBoundException | RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Handles the printing and input recognition of the "Playing Menu".
     * This menu shows the User all the actions he can perform during its Turn, then
     * asks the number of the action he wants to perform.
     * If the input is recognized, it adds to the Queue the appropriate method, otherwise
     * it handles the error, prints a warning, then adds another instance of this method to
     * the action Queue.
     */
    private void play() {
        System.out.print("\n\n\n");
        printPlayer();
        System.out.println("-------------------");
        showRanking();
        System.out.println("-------------------");
        printMenu();
        printHandCards();
        inputHandler.listen(new TerminalListener() {
            @Override
            public void onEvent(String input) {
                switch (input) {
                    case "1":
                        System.out.println("Select the number of the card you want to play (0 to cancel)");
                        inputHandler.listen(new TerminalListener() {
                            @Override
                            public void onEvent(String input) {
                                playCard(input);
                            }
                        });
                        break;
                    case "2":
                        printHandCards();
                        System.out.println("Select the number of the card you want to flip (0 to cancel)");
                        inputHandler.listen(new TerminalListener() {
                            @Override
                            public void onEvent(String input) {
                                if (input.equals("1") || input.equals("2") || input.equals("3")) {
                                    controller.flipCard(playerID, Integer.parseInt(input) - 1);
                                } else if (!input.equals("0")) {
                                    System.err.println("Invalid input!");
                                }
                                actions.add(GameTerminal.this::play);
                            }
                        });
                        break;
                    case "3":
                        ObjectiveCard secretObj = controller.getSecretObjective(playerID);
                        printSecretObjective(secretObj);
                        System.out.println(secretObj.getObjective().getName());
                        System.out.println(secretObj.getObjective().getDescription());
                        System.out.println();
                        returnToMenu();
                        break;
                    case "4":
                        if (!chat.isEmpty()) {
                            int number = 0;
                            if (chat.size() >= 10) {
                                number = 10;
                                System.out.println("Last " + number + " messages:");
                                for (int i = chat.size() - (number+1); i < chat.size(); i++) {
                                    printMessage(chat.get(i));
                                }
                            } else {
                                number = chat.size();
                                System.out.println("Last " + number + " messages:");
                                for (int i = 0; i < chat.size(); i++) {
                                    printMessage(chat.get(i));
                                }
                            }

                        } else {
                            System.out.println("No messages yet!");
                        }

                        returnToMenu();
                        break;
                    case "5":
                        System.out.println("Enter your message:");
                        inputHandler.listen(new TerminalListener() {
                            @Override
                            public void onEvent(String input) {
                                actions.add(() -> {
                                    try {
                                        controller.sendChatMessage(playerID, input);
                                        actions.add(GameTerminal.this::play);
                                    } catch (RemoteException e) {
                                        throw new RuntimeException(e);
                                    }
                                });
                            }
                        });
                        break;
                    case "i":
                        actions.add(() -> {
                            ArrayList<HashMap<String, String>> inventory = null;
                            try {
                                inventory = controller.getInventory();
                            } catch (RemoteException e) {
                                throw new RuntimeException(e);
                            }
                            if (null != inventory) {
                                System.out.println("Inventory");
                                System.out.println((terminalCharacters.getCharacter(Characters.FUNGI) + ": " + inventory.get(playerID - 1).get("FUNGI")));
                                System.out.println((terminalCharacters.getCharacter(Characters.PLANT) + ": " + inventory.get(playerID - 1).get("PLANT")));
                                System.out.println((terminalCharacters.getCharacter(Characters.ANIMAL) + ": " + inventory.get(playerID - 1).get("ANIMAL")));
                                System.out.println((terminalCharacters.getCharacter(Characters.INSECT) + ": " + inventory.get(playerID - 1).get("INSECT")));
                                System.out.println("-----------");
                                System.out.println((terminalCharacters.getCharacter(Characters.FEATHER) + ": " + inventory.get(playerID - 1).get("FEATHER")));
                                System.out.println((terminalCharacters.getCharacter(Characters.POTION) + ": " + inventory.get(playerID - 1).get("POTION")));
                                System.out.println((terminalCharacters.getCharacter(Characters.SCROLL) + ": " + inventory.get(playerID - 1).get("SCROLL")));
                            } else System.err.println("Network Error!");
                            returnToMenu();
                        });
                        break;
                    case "6":
                        showRanking();
                        returnToMenu();
                        break;
                    case "7":
                        printCommonObjectives();
                        System.out.println();
                        returnToMenu();
                        break;
                    case "8":
                        playAreas.get(playerID - 1).printPlayArea(terminalCharacters);
                        returnToMenu();
                        break;
                    case "9":
                        System.out.println("--- Your Table ---");
                        playAreas.get(playerID-1).printPlayArea(terminalCharacters);
                        System.out.println("--- Others ---");
                        ArrayList<HashMap<String, String>> info = controller.getPlayersInfo();
                        for (int i = 0; i < 4; i++) {
                            if (i+1 != playerID && !playAreas.get(i).isEmpty()) {
                                System.out.println(info.get(i).get("Nickname") + ":");
                                playAreas.get(i).printPlayArea(terminalCharacters);
                            }
                        }
                        returnToMenu();
                        break;
                    case "10":
                        askForGraphicsMode();
                        break;
                    case "11":
                        if (!terminalCharacters.isAdvancedGraphicsMode()) {
                            System.out.println(terminalCharacters.getCharacter(Characters.FUNGI) + " -> Fungi kingdom icon");
                            System.out.println(terminalCharacters.getCharacter(Characters.PLANT) + " -> Plant kingdom icon");
                            System.out.println(terminalCharacters.getCharacter(Characters.ANIMAL) + " -> Animal kingdom icon");
                            System.out.println(terminalCharacters.getCharacter(Characters.INSECT) + " -> Insect kingdom icon");
                            System.out.println(terminalCharacters.getCharacter(Characters.EMPTY_CORNER) + " -> Empty corner");
                            System.out.println(terminalCharacters.getCharacter(Characters.SCROLL) + " -> Scroll icon");
                            System.out.println(terminalCharacters.getCharacter(Characters.FEATHER) + " -> Feather icon");
                            System.out.println(terminalCharacters.getCharacter(Characters.POTION) + " -> Potion icon");
                            System.out.println(terminalCharacters.getCharacter(Characters.RED_SQUARE) + " -> Fungi color");
                            System.out.println(terminalCharacters.getCharacter(Characters.GREEN_SQUARE) + " -> Plant color");
                            System.out.println(terminalCharacters.getCharacter(Characters.BLUE_SQUARE) + " -> Animal color");
                            System.out.println(terminalCharacters.getCharacter(Characters.PURPLE_SQUARE) + " -> Insect color");
                            System.out.println(terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) + " -> Gold card color");
                            System.out.println(terminalCharacters.getCharacter(Characters.RED_CIRCLE) + " -> Red Token");
                            System.out.println(terminalCharacters.getCharacter(Characters.GREEN_CIRCLE) + " -> Green Token");
                            System.out.println(terminalCharacters.getCharacter(Characters.BLUE_CIRCLE) + " -> Blue Token");
                            System.out.println(terminalCharacters.getCharacter(Characters.YELLOW_CIRCLE) + " -> Yellow Token");
                        } else {
                            System.out.println(color("Unknown command", UiColors.RED));
                        }
                        break;
                    case "e":
                        System.exit(0);
                    default:
                        System.out.println(color("Unknown command", UiColors.RED));
                        actions.add(() -> play());
                        break;
                }
            }
        });
    }

    /**
     * Asks the User to pick one of the 3 Supported Graphics Modes.
     */
    private void askForGraphicsMode() {
        System.out.println("This game supports 3 view modes: Fancy, Enhanced and Standard.");
        System.out.println("Some Terminals don't properly support Fancy and Enhanced Mode: if you can see the left and the middle Cards properly,");
        System.out.println("then it's safe to play, otherwise we suggest playing Standard Mode");
        System.out.println();

        System.out.println("Fancy:");
        System.out.println("🍄🟥🟥🟨🟨🟨🟩🟩🌳");
        System.out.println("🟥🟥🟨🟨⚪🟨🟨🟩🟩");
        System.out.println("🟨⚪🟨🍷📜🪶🟨⚪🟨");
        System.out.println("🟪🟪🟨🟨⚪🟨🟨🟦🟦");
        System.out.println("🦋🟪🟪🟨🟨🟨🟦🟦🐺");
        System.out.println();

        System.out.println("Enhanced:");
        System.out.println(UiColors.RED + "ନ ▤ ▤ " + UiColors.YELLOW + "▩ ▩ ▩ " + UiColors.GREEN + "▦ ▦ ✿ " + UiColors.RESET);
        System.out.println(UiColors.RED + "▤ ▤ " + UiColors.YELLOW + "▩ ▩ " + UiColors.WHITE + "■ " + UiColors.YELLOW + "▩ ▩ " + UiColors.GREEN + "▦ ▦ " + UiColors.RESET);
        System.out.println(UiColors.YELLOW + "▩ " + UiColors.WHITE + "■ " + UiColors.YELLOW + "▩ Ỗ ∫ ϡ ▩ " + UiColors.WHITE + "■ " + UiColors.YELLOW + "▩ " + UiColors.RESET);
        System.out.println(UiColors.MAGENTA + "▧ ▧ " + UiColors.YELLOW + "▩ ▩ " + UiColors.WHITE + "■ " + UiColors.YELLOW + "▩ ▩ " + UiColors.CYAN + "▥ ▥ " + UiColors.RESET);
        System.out.println(UiColors.MAGENTA + "¥ ▧ ▧ " + UiColors.YELLOW + "▩ ▩ ▩ " + UiColors.CYAN + "▥ ▥ ♘ " + UiColors.RESET);
        System.out.println();

        System.out.println("Standard:");
        System.out.println("ନ ▤ ▤ ▩ ▩ ▩ ▦ ▦ ✿ ");
        System.out.println("▤ ▤ ▩ ▩ ■ ▩ ▩ ▦ ▦ ");
        System.out.println("▩ ■ ▩ Ỗ ∫ ϡ ▩ ■ ▩ ");
        System.out.println("▧ ▧ ▩ ▩ ■ ▩ ▩ ▥ ▥ ");
        System.out.println("¥ ▧ ▧ ▩ ▩ ▩ ▥ ▥ ♘ ");


        System.out.println("\nPress f to play in Fancy Mode, e for Enhanced Mode or s for Standard Mode:");
        inputHandler.listen(new TerminalListener() {
            @Override
            public void onEvent(String input) {
                if(input.equals("f")) {
                    isAdvancedGraphicsMode = true;
                    isColorTerminalSupported = true;
                    terminalCharacters.setAdvancedGraphicsMode(isAdvancedGraphicsMode);
                    terminalCharacters.setColorTerminalSupported(isColorTerminalSupported);
                    // Refreshes the PlayAreas
                    for (PlayAreaTerminal playArea: playAreas) {
                        if (!playArea.isEmpty()) {
                            playArea.recreatePlayArea(terminalCharacters);
                        }
                    }

                    System.out.println(terminalCharacters.getCharacter(Characters.FUNGI) +
                            terminalCharacters.getCharacter(Characters.PLANT) +
                            "Mode Changed" +
                            terminalCharacters.getCharacter(Characters.ANIMAL) +
                            terminalCharacters.getCharacter(Characters.INSECT));
                    returnToMenu();
                } else if (input.equals("e")) {
                    isAdvancedGraphicsMode = false;
                    isColorTerminalSupported = true;
                    terminalCharacters.setAdvancedGraphicsMode(isAdvancedGraphicsMode);
                    terminalCharacters.setColorTerminalSupported(isColorTerminalSupported);
                    // Refreshes the PlayAreas
                    for (PlayAreaTerminal playArea: playAreas) {
                        if (!playArea.isEmpty()) {
                            playArea.recreatePlayArea(terminalCharacters);
                        }
                    }

                    System.out.println(terminalCharacters.getCharacter(Characters.FUNGI) +
                            terminalCharacters.getCharacter(Characters.PLANT) +
                            "Mode Changed" +
                            terminalCharacters.getCharacter(Characters.ANIMAL) +
                            terminalCharacters.getCharacter(Characters.INSECT));
                    returnToMenu();
                } else if (input.equals("s")) {
                    isAdvancedGraphicsMode = false;
                    isColorTerminalSupported = false;
                    terminalCharacters.setAdvancedGraphicsMode(isAdvancedGraphicsMode);
                    terminalCharacters.setColorTerminalSupported(isColorTerminalSupported);
                    // Refreshes the PlayAreas
                    for (PlayAreaTerminal playArea: playAreas) {
                        if (!playArea.isEmpty()) {
                            playArea.recreatePlayArea(terminalCharacters);
                        }
                    }

                    System.out.println(terminalCharacters.getCharacter(Characters.FUNGI) +
                            terminalCharacters.getCharacter(Characters.PLANT) +
                            "Mode Changed" +
                            terminalCharacters.getCharacter(Characters.ANIMAL) +
                            terminalCharacters.getCharacter(Characters.INSECT));
                    returnToMenu();
                } else {
                    System.err.println("Invalid Choice!\n");
                    System.out.println("\nPress f to play in Fancy Mode, e for Enhanced Mode or s for Standard Mode:");
                }
            }
        });
    }

    /**
     * Asks the user to confirm if he wants to exit the current submenu by typing '0'.
     * Invalid inputs are handled.
     * Exiting from this menu adds an instance of play() in the Queue.
     */
    private void returnToMenu() {
        System.out.println("Digit 0 to return to menu");
        inputHandler.listen(new TerminalListener() {
            @Override
            public void onEvent(String input) {
                if (input.equals("0")) {
                    actions.add(() -> play());
                } else {
                    System.err.println("Invalid input!");
                    actions.add(() -> returnToMenu());
                }
            }
        });
    }

    /**
     * Prints the menu entries for the play() method.
     */
    private void printMenu() {
        System.out.println("Menu:");
        System.out.println("1) Play a Card");
        System.out.println("2) Flip a Card");
        System.out.println("3) Show your Secret Objective");
        System.out.println("4) Show chat");
        System.out.println("5) Send a message");
        System.out.println("6) Show ranking");
        System.out.println("7) Show the Common Objectives");
        System.out.println("8) Print your Table");
        System.out.println("9) Print all Tables");
        System.out.println("10) Change Graphics Mode");
        if (!isAdvancedGraphicsMode) {
            System.out.println("11) Show a legend with character descriptions");
        }
        System.out.println("e) Exit");
        System.out.println("Digit a number to select the action.");
        System.out.println();
    }

    /**
     * Prints the Player's info
     */
    private void printPlayer() {
        System.out.println("Player n° " + controller.getPlayerTurn());
        ArrayList<HashMap<String, String>> info = controller.getPlayersInfo();
        System.out.println("Nickname: " + info.get(playerID-1).get("Nickname"));
        String string = "  ";
        Token token = controller.getPlayerToken(playerID);
        if (null != token) {
            switch (token) {
                case Token.RED -> string = terminalCharacters.getCharacter(Characters.RED_CIRCLE);
                case Token.BLUE -> string = terminalCharacters.getCharacter(Characters.BLUE_CIRCLE);
                case Token.GREEN -> string = terminalCharacters.getCharacter(Characters.GREEN_CIRCLE);
                case Token.YELLOW -> string = terminalCharacters.getCharacter(Characters.YELLOW_CIRCLE);
            }
        }
        System.out.println("Token: " + string);
        System.out.println("Point: " + info.get(playerID-1).get("Points"));
        ObjectiveCard card = controller.getSecretObjective(playerID);
        if (null != card) {
            System.out.println("Secret objective: " + card.getObjective().getName());
        }
        System.out.println("Digit I to open the inventory");
    }

    /**
     * Reads the User's input after being asked which Card he wants to play.
     * Inputs 1, 2, 3 are accepted.
     * In this case the User will be asked where he wants to play the selected Card.
     * Input 0 is used as an "undo" input.
     * Other inputs are handled as errors.
     * @param input
     */
    private void playCard(String input) {
        switch (input) {
            case "1", "2", "3" -> {
                // Play card
                if (controller.canCardBePlayed(playerID, Integer.parseInt(input) - 1)) {
                    actions.add(() -> askForCoordinates(Integer.parseInt(input)));
                } else {
                    System.err.println("Placement Condition Not Met! Try another Card or flip this one!");
                    inputHandler.listen(new TerminalListener() {
                        @Override
                        public void onEvent(String input) {
                            if (Integer.valueOf(input) == 0) {
                                actions.add(() -> play());
                            } else {
                                playCard(input);
                            }
                        }
                    });
                }
            }
            case "0" -> {
            } // do nothing and exit
            default -> System.out.println(color("Invalid choice!", UiColors.RED));
        }
    }

    /**
     * Asks the User where he wants to play the selected Card.
     * @param cardID the Card that the User previously selected.
     */
    private void askForCoordinates(int cardID) {
        int i = 1;
        ArrayList<Coordinates> availablePlacements = controller.getAvailablePlacements(playerID);
        availablePlacement();
        System.out.println("Digit where to place");
        for (Coordinates coord : availablePlacements) {
            System.out.println(i + ") " + coord.getX() + " " + coord.getY());
            i++;
        }
        inputHandler.listen(new TerminalListener() {
            @Override
            public void onEvent(String input) {
                if (Integer.parseInt(input) < 1 || Integer.parseInt(input) > availablePlacements.size()) {
                    System.out.println(color("Invalid coordinate choice! Retry...", UiColors.RED));
                    actions.add(() -> askForCoordinates(cardID));
                } else {
                    controller.playCard(cardID, availablePlacements.get(Integer.parseInt(input) - 1).getX(), availablePlacements.get(Integer.parseInt(input) - 1).getY());
                }
            }
        });

    }

    /**
     * Asks the User to choose between 2 Objective Cards.
     * The Cards are shown as ASCII art or Emojis based on the Graphics Mode.
     * The Objectives' Names and Descriptions are also printed.
     * Handles the User's inputs: 1 or 2 are accepted, others are handled as errors.
     */
    @Override
    public void showSecretObjectivesSelectionDialog() {
        ArrayList<ObjectiveCard> cards = controller.getTemporaryObjectiveCards(playerID);
        System.out.println("--- Choose your secret objective ---");
        System.out.println("Digit 1 to choose: " + cards.get(0).getObjective().getName());
        System.out.println("ℹ\uFE0F " + cards.get(0).getObjective().getDescription());
        printSecretObjective(cards.get(0));
        System.out.println("Digit 2 to choose: " + cards.get(1).getObjective().getName());
        System.out.println("ℹ\uFE0F " + cards.get(1).getObjective().getDescription());
        printSecretObjective(cards.get(1));
        inputHandler.listen(new TerminalListener() {
            @Override
            public void onEvent(String input) {
                switch (input) {
                    case "1":
                        controller.setPlayerSecretObjective(playerID, 0);
                        controller.setPlayerStatus(playerID, GameStatus.READY_TO_CHOOSE_STARTER_CARD);
                        inputHandler.unlisten(this);
                        break;
                    case "2":
                        controller.setPlayerSecretObjective(playerID, 1);
                        controller.setPlayerStatus(playerID, GameStatus.READY_TO_CHOOSE_STARTER_CARD);
                        inputHandler.unlisten(this);
                        break;
                    default:
                        System.out.println(color("Invalid choice! Retry...", UiColors.RED));
                        break;
                }
            }
        });
    }

    /**
     * Asks the User to pick which Side he wants to play the Starter Card.
     * Both Sides are printed, in ASCII art or with Emojis based on the Graphics Mode.
     * Handles the User's inputs: f (front) or b (back) are accepted, others are handled as errors.
     */
    @Override
    public void showStarterCardSelectionDialog() {
        System.out.println("--- Choose the side of your starter card ---");
        StarterCard starterCard = controller.getTemporaryStarterCard(playerID);
        System.out.println("Digit f to choose front side");
        TerminalCard.printCard(starterCard, terminalCharacters);
        starterCard.flip();
        System.out.println("Digit b to choose back side");
        TerminalCard.printCard(starterCard, terminalCharacters);

        inputHandler.listen(new TerminalListener() {
            @Override
            public void onEvent(String input) {
                switch (input) {
                    case "b":
                        controller.flipStarterCard(playerID);
                        controller.setPlayerStarterCard(playerID);
                        controller.setPlayerStatus(playerID, GameStatus.READY_TO_DRAW_STARTING_HAND);
                        inputHandler.unlisten(this);
                        break;
                    case "f":
                        controller.setPlayerStarterCard(playerID);
                        controller.setPlayerStatus(playerID, GameStatus.READY_TO_DRAW_STARTING_HAND);
                        inputHandler.unlisten(this);
                        break;
                    default:
                        System.out.println(color("Invalid choice! Retry...", UiColors.RED));
                        actions.add(() -> showStarterCardSelectionDialog());
                        break;
                }
            }
        });
    }

    /**
     * Asks the User to pick one of the 4 Tokens.
     * All Tokens are always shown: if a Token is not available, it will be printed as "already taken!".
     * Tokens are visually printed in Unicode characters or Emojis, depending on the Graphics Mode.
     * Unavailable Token can't be picked: if the User inputs a number corresponding to an unavailable Token,
     * the input will be handled as an error.
     */
    @Override
    public void showTokenSelectionDialog() {
        // Inits the ArrayList the first time
        if (availableToken.isEmpty()) {
            availableToken.add(Token.RED);
            availableToken.add(Token.BLUE);
            availableToken.add(Token.GREEN);
            availableToken.add(Token.YELLOW);
        }

        System.out.println("--- Select your token ---");
        if(availableToken.contains(Token.RED)) {
            System.out.println("Digit 1 to choose: " + terminalCharacters.getCharacter(Characters.RED_CIRCLE));
        } else {
            System.out.println(terminalCharacters.getCharacter(Characters.RED_CIRCLE) + " already taken!");
        }
        if (availableToken.contains(Token.BLUE)) {
            System.out.println("Digit 2 to choose: " + terminalCharacters.getCharacter(Characters.BLUE_CIRCLE));
        } else {
            System.out.println(terminalCharacters.getCharacter(Characters.BLUE_CIRCLE) + " already taken!");
        }
        if (availableToken.contains(Token.GREEN)) {
            System.out.println("Digit 3 to choose: " + terminalCharacters.getCharacter(Characters.GREEN_CIRCLE));
        }else {
            System.out.println(terminalCharacters.getCharacter(Characters.GREEN_CIRCLE) + " already taken!");
        }
        if (availableToken.contains(Token.YELLOW)) {
            System.out.println("Digit 4 to choose: " + terminalCharacters.getCharacter(Characters.YELLOW_CIRCLE));
        } else {
            System.out.println(terminalCharacters.getCharacter(Characters.YELLOW_CIRCLE) + " already taken!");
        }
        inputHandler.listen(new TerminalListener() {
            @Override
            public void onEvent(String input) {
                switch (input) {
                    case "1":
                        if (availableToken.contains(Token.RED)) {
                            System.out.println("You chose " + terminalCharacters.getCharacter(Characters.RED_CIRCLE));
                            controller.setPlayerToken(playerID, Token.RED);
                            hasChosenToken = true;
                            inputHandler.unlisten(this);
                        } else System.err.println("Already Taken!");
                        break;
                    case "2":
                        if (availableToken.contains(Token.BLUE)) {
                            System.out.println("You chose " + terminalCharacters.getCharacter(Characters.BLUE_CIRCLE));
                            controller.setPlayerToken(playerID, Token.BLUE);
                            hasChosenToken = true;
                            inputHandler.unlisten(this);
                        } else System.err.println("Already Taken!");
                        break;
                    case "3":
                        if (availableToken.contains(Token.GREEN)) {
                            System.out.println("You chose " + terminalCharacters.getCharacter(Characters.GREEN_CIRCLE));
                            controller.setPlayerToken(playerID, Token.GREEN);
                            hasChosenToken = true;
                            inputHandler.unlisten(this);
                        } else System.err.println("Already Taken!");
                        break;
                    case "4":
                        if (availableToken.contains(Token.YELLOW)) {
                        System.out.println("You chose " + terminalCharacters.getCharacter(Characters.YELLOW_CIRCLE));
                        controller.setPlayerToken(playerID, Token.YELLOW);
                        hasChosenToken = true;
                        inputHandler.unlisten(this);
                        } else System.err.println("Already Taken!");
                        break;
                    default:
                        System.out.println(color("Invalid choice! Retry...", UiColors.RED));
                        break;
                }
            }
        });
    }

    /**
     * Returns the playerID of the Player using this ViewController.
     * @return the playerID
     */
    @Override
    public int getOwner() {
        return playerID;
    }

    /**
     * Prints the 2 Decks and the 4 Cards on the table (Slots) and asks the user to Draw or Grab from any of those.
     * The Deck's top Card's Back Side is printed graphically.
     * The Slot Card's Front Side is printed as well.
     * All those Cards are printed in ASCII art or with Emojis, based on the Graphics Mode.
     * User inputs are handled as well.
     * Correct inputs are translated in Server Messages to DRAW or GRAB a Card.
     */
    @Override
    public void askToDrawOrGrab() {
        if (isYourTurn) {
            System.out.println("Choose what you want to draw or grab:");
            Card card1 = null, card2 = null;
            ArrayList<Card> deck = controller.getDeck(CardType.RESOURCECARD);
            if (!deck.isEmpty()) {
                card1 = controller.getDeck(CardType.RESOURCECARD).getFirst();
                card1.flip();
            }
            deck = controller.getDeck(CardType.GOLDCARD);
            if (!deck.isEmpty()) {
                card2 = controller.getDeck(CardType.GOLDCARD).getFirst();
                card2.flip();
            }
            System.out.println("Decks");
            System.out.println("Resource\t\t\tGold");
            for (int line = 1; line < 6; line++) {
                System.out.println(TerminalCard.getPrintCardLine((PlayableCard) card1, line, isColorTerminalSupported, terminalCharacters) +
                        "\t" + (TerminalCard.getPrintCardLine((PlayableCard) card2, line, isColorTerminalSupported, terminalCharacters)));
            }
            System.out.println();
            System.out.println("Slots:");
            System.out.println("Resource\t\t\tGold");
            for (int slot = 1; slot < 3; slot++) {
                card1 = controller.getSlotCard(CardType.RESOURCECARD, slot);
                card2 = controller.getSlotCard(CardType.GOLDCARD, slot);
                for (int line = 1; line < 6; line++) {
                    System.out.println(TerminalCard.getPrintCardLine((PlayableCard) card1, line, isColorTerminalSupported, terminalCharacters) +
                            "\t" + (TerminalCard.getPrintCardLine((PlayableCard) card2, line, isColorTerminalSupported, terminalCharacters)));
                }
                System.out.println();
            }
            System.out.println("Digit gr to grab a card from resource deck");
            System.out.println("Digit gg to grab a card from gold deck");
            System.out.println("Digit 1r to choose first resource card");
            System.out.println("Digit 1g to choose first gold card");
            System.out.println("Digit 2r to choose second resource card");
            System.out.println("Digit 2g to choose second gold card");

            inputHandler.listen(new TerminalListener() {
                @Override
                public void onEvent(String input) {
                    switch (input) {
                        case "gr" -> {
                            if (!controller.getDeck(CardType.RESOURCECARD).isEmpty()) {
                                controller.drawCard(playerID, CardType.RESOURCECARD);
                            } else {
                                System.err.println("Resource Deck is empty!");
                                actions.add(() -> askToDrawOrGrab());
                            }
                        }
                        case "gg" -> {
                            if (!controller.getDeck(CardType.GOLDCARD).isEmpty()) {
                                controller.drawCard(playerID, CardType.GOLDCARD);
                            } else {
                                System.err.println("Gold Deck is empty!");
                                actions.add(() -> askToDrawOrGrab());
                            }
                        }
                        case "1r" -> {
                            if (null != controller.getSlotCard(CardType.RESOURCECARD, 1)) {
                                controller.grabCard(playerID, CardType.RESOURCECARD, 1);
                            } else {
                                System.err.println("Resource Slot 1 is empty!");
                                actions.add(() -> askToDrawOrGrab());
                            }
                        }
                        case "1g" -> {
                            if (null != controller.getSlotCard(CardType.GOLDCARD, 1)) {
                                controller.grabCard(playerID, CardType.GOLDCARD, 1);
                            } else {
                                System.err.println("Gold Slot 1 is empty!");
                                actions.add(() -> askToDrawOrGrab());
                            }
                        }
                        case "2r" -> {
                            if (null != controller.getSlotCard(CardType.RESOURCECARD, 2)) {
                                controller.grabCard(playerID, CardType.RESOURCECARD, 2);
                            } else {
                                System.err.println("Resource Slot 2 is empty!");
                                actions.add(() -> askToDrawOrGrab());
                            }
                        }
                        case "2g" -> {
                            if (null != controller.getSlotCard(CardType.GOLDCARD, 2)) {
                                controller.grabCard(playerID, CardType.GOLDCARD, 2);
                            } else {
                                System.err.println("Gold Slot 2 is empty!");
                                actions.add(() -> askToDrawOrGrab());
                            }
                        }
                        default -> {
                            System.err.println("Invalid input!");
                            actions.add(() -> askToDrawOrGrab());
                        }
                    }
                }
            });
        }
    }

    @Override
    public void notifyGameIsStarting() {
        // Not needed
    }

    @Override
    public void notifyDeckChanged(CardType type) {
        //Unnecessary because the TUI won't print the Deck every time someone draws from it.
        // Decks are only shown when needed.
    }

    @Override
    public void notifySlotCardChanged(CardType type, int slot) {
        //Unnecessary because the TUI won't print the Slot Cards every time someone grabs one of them.
        // Slots are only shown when needed.
    }

    @Override
    public void notifyPlayersPointsChanged(Token token, int newPoints) {
        // Not needed, showing the points everytime is too much
    }

    /**
     * Notifies the number of Players in the Game has changed, both increased and decreased.
     * Prints a list of all the Players inside the Game.
     * This method is only call if the User created the Game.
     * It allows him to know how many Players have joined, so that he can start the Game whenever he wants.
     * This method also asks the User to type "s" to start the Game.
     * Invalid inputs are handled.
     */
    @Override
    public void notifyNumberOfPlayersChanged() {
        if (isGameCreator) {
            System.out.println("Players:");
            ArrayList<HashMap<String, String>> info = controller.getPlayersInfo();
            for (HashMap<String, String> player : info) {
                System.out.println(player.get("Nickname"));
            }
            if (info.size() > 1) {
                System.out.println("Press s to start the game.");
                inputHandler.listen(new TerminalListener() {
                    @Override
                    public void onEvent(String input) {
                        if (input.equals("s")) {
                            actions.add(() -> {
                                controller.setCurrentStatus(GameStatus.WAITING_FOR_SERVER);
                                controller.setPlayerStatus(playerID, GameStatus.WAITING_FOR_SERVER);
                                inputHandler.unlisten(this);
                            });
                    }
                }
            });
            }
        }
    }

    /**
     * Notifies the User that another Player has picked a Token.
     * Updates the List of available Tokens.
     * If the User hasn't picked a Token yet, it then adds the showTokenSelectionDialog() method to the Queue once again,
     * so that the User can choose a Token with an updated view of all the Tokens still available.
     * @param playerID the Player who picked the Token.
     */
    @Override
    public void notifyPlayersTokenChanged(int playerID) {
        Token token = controller.getPlayerToken(playerID);
        availableToken.remove(token);
        if (this.playerID != playerID) {
            // Only prints the other Players' Tokens
            if (null != token) {
                ArrayList<HashMap<String, String>> info = controller.getPlayersInfo();
                switch (token) {
                    case Token.RED -> System.out.println(info.get(playerID - 1).get("Nickname") + " choose: " + terminalCharacters.getCharacter(Characters.RED_CIRCLE));
                    case Token.BLUE -> System.out.println(info.get(playerID - 1).get("Nickname") + " choose: " + terminalCharacters.getCharacter(Characters.BLUE_CIRCLE));
                    case Token.GREEN -> System.out.println(info.get(playerID - 1).get("Nickname") + " choose: " + terminalCharacters.getCharacter(Characters.GREEN_CIRCLE));
                    case Token.YELLOW -> System.out.println(info.get(playerID - 1).get("Nickname") + " choose: " + terminalCharacters.getCharacter(Characters.YELLOW_CIRCLE));
                }
            }
            if(!hasChosenToken) {
                showTokenSelectionDialog();
            }
        }
    }

    /**
     * Intercepts the Server's message that a Player has played a Card.
     * Updates the playArea matrix with the new Card in the right place.
     * If the playerID is the same as the User, it prints the updated playArea matrix.
     * If it's another playerID, it only updates the matrix, but it doesn't print it.
     * @param playerID the Player who placed the Card.
     */
    @Override
    public void notifyPlayersPlayAreaChanged(int playerID) {
        actions.add(() -> {
            ArrayList<PlayableCard> cards = controller.getPlayersPlayfield(playerID);
            if (!playAreas.get(playerID - 1).isEmpty()) {
                playAreas.get(playerID - 1).updateCardMatrix( cards.getLast(), cards,  terminalCharacters);
            } else {
                playAreas.get(playerID - 1).createCardMatrix((StarterCard) cards.getFirst(),  terminalCharacters);
            }
            if (this.playerID == playerID) {
                playAreas.get(playerID - 1).printPlayArea(terminalCharacters);
            }
        });
    }

    /**
     * Intercepts the Server Message that someone's Hand has been updated.
     * If the playerId is the same as the User, it prints the updated Hand; otherwise, this method does nothing.
     * Updates are shown only when the Hand goes from 2 to 3 Cards => The initial updated are ignored.
     * @param playerID the Player whose Hand was updated.
     */
    @Override
    public void notifyPlayersHandChanged(int playerID) {
        /*if (this.playerID == playerID) {
            actions.add(() -> {
                // Only shows the User's one (the others should be secret!)
                int handSize = controller.getPlayersHandSize(playerID);
                if (handSize == 3) {
                    System.out.println("Your Hand:");
                    // Only shows the updated Hand in meaningful situations
                    printHandCards();
                }
            });
        }*/
    }

    /**
     * Intercepts the Server Message that someone's Card was flipped.
     * If the playerID is the same as the User, it prints the updated Hand, otherwise it ignores it.
     * @param playedID the Player whose Card was flipped.
     * @param cardID the Card that was flipped.
     */
    @Override
    public void notifyHandCardWasFlipped(int playedID, int cardID) {
        if (this.playerID == playedID) {
            System.out.println("Hand updated:");
            actions.add(this::printHandCards);
        }
    }

    /**
     * Intercepts the Server Message that someone's Secret Objective has been updated.
     * If the playerID is the same as the User, it prints the Secret Objective, otherwise it's ignored.
     * @param playerID the Player whose Secret Objective has been updated.
     */
    @Override
    public void notifyPlayersObjectiveChanged(int playerID) {
        if (this.playerID == playerID) {
            // Only shows the User's one, not the others (it should be secret!)
            System.out.println("Objective chosen: " + controller.getSecretObjective(playerID).getObjective().getName());
        }
    }

    /**
     * Intercepts the Server Message that the 2 Common Objectives have been updated and prints them.
     */
    @Override
    public void notifyCommonObjectivesChanged() {
        for (int i = 0; i < 2; i++) {
            try {
                TerminalCard.printCard(  controller.getCommonObjective( i), terminalCharacters);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Intercepts the Server Message that the Turn changed.
     * Asks the Server whose Turn it is now: if it's the User's one, it adds the play() method to the action Queue.
     */
    @Override
    public void notifyTurnChanged() {
        if (playerID == controller.getPlayerTurn()) {
            this.isYourTurn = true;
            System.out.println(color("It's your turn!", UiColors.MAGENTA));
            actions.add(this::play);
        } else {
            this.isYourTurn = false;
            actions.add(this::showRanking);
        }

    }

    /**
     * Prints the "Waiting for Server..." text.
     */
    @Override
    public void showWaitingForServerDialog() {
        System.out.println("Waiting for Server...");
    }

    /**
     * Intercepts the Server Message that the Game is starting.
     * If the User created the Game, it sets the status to READY.
     * In all cases, it sets the Player's status to READY.
     * @param numberOfPlayers the number of Players inside the Game.
     */
    @Override
    public void getReady(int numberOfPlayers) {
        if (isGameCreator) {
            controller.setCurrentStatus(GameStatus.READY);
        }
        controller.setPlayerStatus(playerID, GameStatus.READY);
        System.out.println("Getting ready...");
    }
    /**
     * Prints the current Player's Ranking, ordered by Points.
     */
    public void showRanking() {
        ArrayList<HashMap<String, String>> playersList = new ArrayList<>();
        ArrayList<Integer> alreadySeen = new ArrayList<>();
        // Players are ordered by their points
        int numberOfPlayers = controller.getNumberOfPlayers();
        ArrayList<HashMap<String, String>> info = controller.getPlayersInfo();
        while (alreadySeen.size() != numberOfPlayers) {
            int currentMax = 1;
            for (int i = 0; i < numberOfPlayers; i++) {
                if (Integer.parseInt(info.get(i).get("Points")) >= Integer.parseInt(info.get(currentMax).get("Points")) && !alreadySeen.contains(i)) {
                    currentMax = i;
                }
            }
            playersList.add(info.get(currentMax));
            alreadySeen.add(currentMax);
        }

        System.out.println("Ranking");
        for (int i = 0; i < numberOfPlayers; i++)
            System.out.println(i + 1 + ") " + playersList.get(i).get("Nickname") + ": " + playersList.get(i).get("Points"));
    }

    /**
     * Asks the User to pick a Network Mode between RMI and Socket.
     * If the input is accepted, it calls askForIPAddress().
     * Handles invalid inputs.
     */
    public void login() {
        System.out.println("Select connection mode");
        System.out.println("Digit r to choose RMI");
        System.out.println("Digit s to choose Socket");
        inputHandler.listen(new TerminalListener() {
            @Override
            public void onEvent(String input) {
                if (input.equals("r")) {
                    inputHandler.unlisten(this);
                    askForIPAddress(NetworkMode.RMI);
                } else if (input.equals("s")) {
                    inputHandler.unlisten(this);
                    askForIPAddress(NetworkMode.SOCKET);
                } else {
                    System.out.println("Invalid input: try again!");
                }
            }
        });


    }

    /**
     * Asks the User to insert the Server's IP Address.
     * Once received an input, it attempts to connect via the previously selected Network Mode:
     * if the connection is successful, it adds a call to askForNickName() in the action Queue;
     * if the connection fails, it handles the error and asks for the IP Address again.
     * @param selectedMode the previously selected Network Mode, used to choose how to attempt establishing a connection.
     */
    private void askForIPAddress(NetworkMode selectedMode) {
        System.out.println("Digit IP Address: ");
        inputHandler.listen(new TerminalListener() {
            @Override
            public void onEvent(String input) {
                if (!input.isEmpty() && null != selectedMode) {
                    try {
                        if (selectedMode == NetworkMode.RMI) {
                            controller = new RmiClient(input);
                        } else {
                            controller = new SocketClient(input);
                        }
                        controller.connect();
                        inputHandler.unlisten(this);
                        actions.add(() -> askForNickName());
                    } catch (NotBoundException | IOException e) {
                        System.out.println("Invalid IP Address: try again!");
                    }
                }
            }
        });
    }

    /**
     * Asks the User to choose a NickName.
     * Once received an input, it asks the Server if this NickName is acceptable and available:
     * if it is, it adds a call to askToJoinGame() to the actions Queue.
     * if it's not, it handles the error and asks for a new NickName.
     */
    private void askForNickName() {
        System.out.println("--- Insert Nickname ---");
        inputHandler.listen(new TerminalListener() {
            @Override
            public void onEvent(String input) {
                switch (input) {
                    case "":
                        break;
                    default:
                        try {
                            if (controller.checkNickName(input)) {
                                controller.blockNickName(input);
                                player = new Player(input);
                                actions.add(() -> showAvailableGames());
                            } else {
                                System.err.println("This nickname is not available!");
                                actions.add(() -> askForNickName());
                            }
                        } catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }
                }
            }
        });
    }

    private void showAvailableGames() {
        ArrayList<HashMap<String, String>> games = null;
        rejoinableGames.clear();
        try {
            games = controller.getAvailableGames();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        if (games.isEmpty()) {
            try {
                createNewGame(true);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
            isWaiting = true;
            // Setups the User's Input Handler
        } else {
            ArrayList<HashMap<String, String>> finalGames = games;
            actions.add(() -> askToJoinGame(finalGames));
        }
    }

    /**
     * Handles the process of joining an existing Game.
     * Prints a list of all the available Games, then asks the User to input the number corresponding to the one he wants
     * to join.
     * It also offers the option of inputting "c" to create a new Game, in which case it adds a call to createNewGame() to
     * the actions Queue.
     * @param games the list of Available Games
     */
    private void askToJoinGame(ArrayList<HashMap<String, String>> games) {
        System.out.println("Games available:");
        for (HashMap<String, String> game : games) {
            if (game.get("Status").equals("Waiting for players")) {
                int gameID = games.indexOf(game) + 1;
                System.out.println(gameID + ")" + " " + game.get("Name") + ", " + game.get("NumberOfPlayers") + " players, " + game.get("Status"));

            } else {
                int number = Integer.parseInt(game.get("NumberOfDisconnectedPlayers"));
                if (number != 0) {
                    boolean exit = false;
                    for (int j = 0; j < number && !exit; j++) {
                        if (game.get("DisconnectedPlayer" + j).equals(player.getNickname())) {
                            int gameID = games.indexOf(game) + 1;
                            rejoinableGames.add(gameID-1);
                            System.out.println(gameID + ")" + " " + game.get("Name") + ", " + game.get("NumberOfPlayers") + " players, " + game.get("Status") + " [RE_JOIN]");
                        }
                        exit = true;
                    }
                }
            }
        }
        System.out.println("\nPress c to create a new one, or press r to refresh the list!");
        inputHandler.listen(new TerminalListener() {
            @Override
            public void onEvent(String input) {
                if (input.equals("c")) {
                    try {
                        createNewGame(false);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                    isWaiting = true;
                } else if (input.equals("r")) {
                    actions.add(() -> showAvailableGames());
                } else {
                    try {
                        int gameID = Integer.parseInt(input);
                        controller.pickGame(gameID - 1);
                        if (rejoinableGames.contains(gameID-1)) {
                            // Re-joining a Game
                            playerID = controller.getIndexOfPlayer(player.getNickname());
                            setViewController();
                            controller.rejoinGame(controller.getIndexOfPlayer(player.getNickname()));
                            rebuild();
                        } else {
                            controller.addPlayer(player);
                            playerID = controller.getIndexOfPlayer(player.getNickname());
                            setViewController();
                            controller.setPlayerStatus(playerID, GameStatus.WAITING_FOR_SERVER);
                            System.out.println("Waiting...");
                            isWaiting = true;
                        }
                        inputHandler.unlisten(this);
                        chat = new ArrayList<>(controller.getFullChat());
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid number. Try again!");
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }


    /**
     * Prints an entire Objective Card, line by line.
     * @param card the Objective Card to print
     */
    private void printSecretObjective(ObjectiveCard card){
        for (int line = 1; line < 6; line++) {
            System.out.println(TerminalCard.printObjectiveLine(card, line, terminalCharacters));
        }
    }

    /**
     * Prints the 2 Common Objectives.
     * Common Objectives are printed both visually and in details.
     * Visually, the two Cards are printed side by side, separated by a "tab".
     * The style they are printed depends on the Graphics Mode: either ASCII art or with Emojis.
     * Under them, both the Name and the Description for both Cards is printed too.
     */
    private void printCommonObjectives(){
        ObjectiveCard card1 = (ObjectiveCard) controller.getSlotCard(CardType.OBJECTIVECARD, 1);
        ObjectiveCard card2 = (ObjectiveCard) controller.getSlotCard(CardType.OBJECTIVECARD, 2);
        // Prints the Cards
        for (int line = 1; line < 6; line++) {
            System.out.println(TerminalCard.printObjectiveLine(card1, line, terminalCharacters) +
                    "\t"+ TerminalCard.printObjectiveLine(card2, line, terminalCharacters));
        }
        // Prints the info
        System.out.println("1) " + card1.getObjective().getName());
        System.out.println("\t" + card1.getObjective().getDescription());
        System.out.println();
        System.out.println("2) " + card2.getObjective().getName());
        System.out.println("\t" + card2.getObjective().getDescription());
    }

    /**
     * Prints the Player's Hand, showing his 3 Cards side by side.
     * The style depends by the Graphics Mode.
     */
    private void printHandCards(){
        PlayableCard card1 = controller.getPlayersHandCard(playerID, 0);
        PlayableCard card2 = controller.getPlayersHandCard(playerID, 1);
        PlayableCard card3 = controller.getPlayersHandCard(playerID, 2);
        for (int line = 1; line < 6; line++){
            System.out.println(TerminalCard.getPrintCardLine(card1, line, isColorTerminalSupported, terminalCharacters) +
                    "\t" + TerminalCard.getPrintCardLine(card2, line, isColorTerminalSupported, terminalCharacters) +
                    "\t" + TerminalCard.getPrintCardLine(card3, line, isColorTerminalSupported, terminalCharacters));
        }
    }

    /**
     * Notifies the User that this is the Last Turn before the end of the Game
     * @throws RemoteException in case of a connection error.
     */
    @Override
    public void notifyLastTurn() throws RemoteException {
    }

    /**
     * Notifies the User that the Game has ended.
     * It also shows all the Player's Points acquired during the match, as well as the Points that were given from
     * the Secret Objective and the 2 Common Objectives, plus the sum of all of them.
     * Finally, it shows who is/are the Winner/s.
     * @param points an ArrayList containing all the data (taken from the Server)
     * @throws RemoteException in case of a connection error.
     */
    @Override
    public void notifyEndGame(ArrayList<HashMap<String, String>> points) throws RemoteException {
        final ArrayList<HashMap<String, String>> players = points;
        actions.add(() -> {
            int number = players.size();
            printSeparator(number);
            for (int i = 0; i < number; i++) {
                String name = players.get(i).get("Nickname");
                if (name.length() > 10) {
                    String shortName = name.substring(0, 10);
                    players.get(i).replace("Nickname", shortName + "...");
                } else {
                    int length = name.length();
                    String longName = name;
                    for (int j = length ; j < 13; j++) {
                        longName += " ";
                        players.get(i).replace("Nickname", longName);
                    }
                }
            }

            printNames(players);
            printSeparator(number);

            System.out.print("| ");
            for (int i = 0; i < number; i++) {
                String pointsValue = players.get(i).get("InitialPoints");
                if (pointsValue.length() == 1) {
                    pointsValue = "0" + pointsValue;
                }
                System.out.print("Initial Points: " + pointsValue + "    | ");
            }
            System.out.println();
            printEmptyLine(number);

            System.out.print("| ");
            for (int i = 0; i < number; i++) {
                String pointsValue = players.get(i).get("SecretObjectivePoints");
                if (pointsValue.length() == 1) {
                    pointsValue = "0" + pointsValue;
                }
                System.out.print("Secret Objective: " + pointsValue + "  | ");
            }
            System.out.println();
            printEmptyLine(number);

            System.out.print("| ");
            for (int i = 0; i < number; i++) {
                String pointsValue = players.get(i).get("CommonObjective1Points");
                if (pointsValue.length() == 1) {
                    pointsValue = "0" + pointsValue;
                }
                System.out.print("Common Obj. 1: " + pointsValue + "     | ");
            }
            System.out.println();
            printEmptyLine(number);

            System.out.print("| ");
            for (int i = 0; i < number; i++) {
                String pointsValue = players.get(i).get("CommonObjective2Points");
                if (pointsValue.length() == 1) {
                    pointsValue = "0" + pointsValue;
                }
                System.out.print("Common Obj. 2: " + pointsValue + "     | ");
            }
            System.out.println();
            printEmptyLine(number);

            System.out.print("| ");
            for (int i = 0; i < number; i++) {
                String pointsValue = players.get(i).get("TotalPoints");
                if (pointsValue.length() == 1) {
                    pointsValue = "0" + pointsValue;
                }
                System.out.print("Final Points: " + pointsValue + "      | ");
            }
            System.out.println();
            printSeparator(number);

            printEmptyLine(number);

            for (int i = 0; i < number; i++) {
                System.out.print("|");
                if (players.get(i).get("IsWinner").equals("true")) {
                    System.out.print("        WINNER!        ");
                } else {
                    System.out.print("                       ");
                }
            }
            System.out.println("|");
            printEmptyLine(number);
            printSeparator(number);
        });
    }

    /**
     * Prints a line of "-" to separate each row of the table
     * @param number the number of columns (also the number of Players in the match)
     */
    private void printSeparator(int number) {
        number = (24 * number) + 1;
        for (int i = 0; i < number; i++) {
            System.out.print("-");
        }
        System.out.println();
    }

    /**
     * Prints an empty line for the table.
     * This line contains the vertical separator for the table's borders and columns separators ("|").
     * @param columns the number of columns inside the table
     */
    private void printEmptyLine(int columns) {
        for (int i = 0; i < columns; i++) {
            System.out.print("|");
            for (int j = 0; j < 23; j++) {
                System.out.print(" ");
            }
        }
        System.out.println("|");
    }

    /**
     * Prints the row containing each Player's Token and NickName.
     * @param info an ArrayList<HashMap<String, String>> containing all the data (taken from the Server)
     */
    private void printNames(ArrayList<HashMap<String, String>> info) {
        for (HashMap<String, String> player: info) {
            String token = "";
            switch (player.get("Token")) {
                case "BLUE" -> token = terminalCharacters.getCharacter(Characters.BLUE_CIRCLE);
                case "RED" -> token = terminalCharacters.getCharacter(Characters.RED_CIRCLE);
                case "GREEN" -> token = terminalCharacters.getCharacter(Characters.GREEN_CIRCLE);
                case "YELLOW" -> token = terminalCharacters.getCharacter(Characters.YELLOW_CIRCLE);
            }
            System.out.print("| " + token + " " + player.get("Nickname") + "      ");
        }
        System.out.println("|");
    }

    /**
     * Notifies the User that someone sent a new Message
     * @param message the new Message received
     */
    @Override
    public void notifyNewMessage(ChatMessage message) {
        chat.add(message);
        actions.add(() -> printMessage(message));
    }

    /**
     * Prints a Message.
     * Server's Messages are colored in Magenta, others in Cyan
     * @param message the Message to print
     */
    private void printMessage(ChatMessage message) {
        String text = "[" + message.getDateTime().getHour() + ":" + message.getDateTime().getMinute()
                + "] " + message.getSender() + " - " + message.getText();

        if (message.getSender().equals("Server")) {
            if (isColorTerminalSupported) {
                System.out.println(UiColors.MAGENTA + text + UiColors.RESET);
            } else {
                System.out.println("--- " + text);
            }
        } else {
            if (isColorTerminalSupported) {
                System.out.println(UiColors.CYAN + text + UiColors.RESET);
            } else {
                System.out.println(text);
            }
        }
    }

    /**
     * @return the User's Nickname
     */
    @Override
    public String getPlayerNickname() {
        return player.getNickname();
    }

    /**
     *
     * @return the Network Controller used for communications
     */
    @Override
    public NetworkController getNetworkController() {
        return controller;
    }

    @Override
    public void blockInput() {
        // Don't need
    }

    @Override
    public void unlockInput() {
        // Don't need
    }

    public boolean isAdvancedGraphicsMode() {
        return isAdvancedGraphicsMode;
    }

    public boolean isColorTerminalSupported() {
        return isColorTerminalSupported;
    }

    public TerminalCharacters getTerminalCharacters() {
        return terminalCharacters;
    }

    /**
     * Rebuilds the matrices after rejoining the Game
     */
    private void rebuild() {
        int number = controller.getNumberOfPlayers();
        for (int i = 0; i < number; i++) {
            ArrayList<PlayableCard> playArea = controller.getPlayersPlayfield(i+1);
            for (int j = 0; j < playArea.size(); j++) {
                if (null != playAreas.get(i)) {
                    playAreas.get(i).updateCardMatrix(playArea.get(j), playArea, terminalCharacters);
                } else {
                    playAreas.get(i).createCardMatrix((StarterCard) playArea.getFirst(),  terminalCharacters);
                }
            }
        }
        printPlayer();
    }

    /**
     * Shows the Available Placements where a Card can be played
     */
    public void availablePlacement(){
        PlayAreaTerminal temp = new PlayAreaTerminal(this);
        temp.setPlayArea(playAreas.get(playerID - 1).getCopy());
        temp.setPrintingExtremes("UP", playAreas.get(playerID-1).getPrintingExtreme("UP"));
        temp.setPrintingExtremes("DOWN", playAreas.get(playerID-1).getPrintingExtreme("DOWN"));
        temp.setPrintingExtremes("LEFT", playAreas.get(playerID-1).getPrintingExtreme("LEFT"));
        temp.setPrintingExtremes("RIGHT", playAreas.get(playerID-1).getPrintingExtreme("RIGHT"));

        ArrayList<Coordinates> availablePlacemnts = getNetworkController().getAvailablePlacements(getOwner());
        for (Coordinates c: availablePlacemnts){
            ResourceCard card = new ResourceCard(null,null,true,-1,c.getX(),c.getY(),new ArrayList<>(),0,"/cards/card1Front.png","/cards/card1Back.png",null);
            temp.updateCardMatrix(card, getNetworkController().getPlayersPlayfield(getOwner()), terminalCharacters);
        }
        temp.printPlayArea(terminalCharacters);
    }
}
