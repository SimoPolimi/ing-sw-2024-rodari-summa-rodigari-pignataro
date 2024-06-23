package it.polimi.ingsw.gc42.view;

import it.polimi.ingsw.gc42.controller.GameStatus;
import it.polimi.ingsw.gc42.model.classes.cards.*;
import it.polimi.ingsw.gc42.model.classes.game.*;
import it.polimi.ingsw.gc42.network.*;
import it.polimi.ingsw.gc42.network.interfaces.NetworkController;
import it.polimi.ingsw.gc42.view.Classes.Characters;
import it.polimi.ingsw.gc42.view.Classes.NetworkMode;
import it.polimi.ingsw.gc42.view.Classes.TerminalCharacters;
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

import static it.polimi.ingsw.gc42.model.classes.cards.KingdomResource.*;
import static it.polimi.ingsw.gc42.model.classes.cards.Resource.*;

/**
 * This class handles the whole TUI behavior and output system.
 * All the TUI is contained inside this class.
 * All that's needed to run a TUI is to call .start() on a GameTerminal object.
 */
public class GameTerminal extends Application implements ViewController {
    // Attributes
    private boolean exit = false;
    private boolean isAdvancedGraphicsMode = true;
    private boolean isColorTerminalSupported = true;
    private NetworkController controller;
    private Player player;
    private Scanner scanner = new Scanner(System.in);
    private int playerID;
    private boolean isYourTurn = false;

    private final ArrayList<Token> availableToken = new ArrayList<>();

    private final ArrayList<HashMap<String, Integer>> printingExtremes = new ArrayList<>();

    private final ArrayList<String[][]> playAreas = new ArrayList<>();

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
    private String color(String str, UiColors fg) {
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
        playAreas.add(null);
        playAreas.add(null);
        playAreas.add(null);
        playAreas.add(null);

        // Initializes the Printing Extremes
        for (int i = 0; i < 4; i++) {
            HashMap<String, Integer> map = new HashMap<>();
            map.put("UP", 0);
            map.put("DOWN", 800);
            map.put("LEFT", 0);
            map.put("RIGHT", 1440);
            printingExtremes.add(map);
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
                            controller.getNewGameController();
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
                controller.getNewGameController();
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
                                        controller.sendMessage(playerID, input);
                                        returnToMenu();
                                    } catch (RemoteException e) {
                                        throw new RuntimeException(e);
                                    }
                                });
                            }
                        });
                        break;
                    case "6":
                        exit = true;
                        break;
                    case "i":
                        System.out.println("Inventory");
                        System.out.println("Kingdom resource");
                        // Missing methods, will add later
                        // TODO: Add methods
                        /*System.out.println(("Animal:" + controller.getGame().getCurrentPlayer().getPlayField().getNumberOf(KingdomResource.ANIMAL)));
                        System.out.println(("Plant:" + controller.getGame().getCurrentPlayer().getPlayField().getNumberOf(KingdomResource.PLANT)));
                        System.out.println(("Fungi:" + controller.getGame().getCurrentPlayer().getPlayField().getNumberOf(KingdomResource.FUNGI)));
                        System.out.println(("Insect:" + controller.getGame().getCurrentPlayer().getPlayField().getNumberOf(KingdomResource.INSECT)));
                        System.out.println("Resource");
                        System.out.println(("Feather:" + controller.getGame().getCurrentPlayer().getPlayField().getNumberOf(Resource.FEATHER)));
                        System.out.println(("Potion:" + controller.getGame().getCurrentPlayer().getPlayField().getNumberOf(Resource.POTION)));
                        System.out.println(("Scroll:" + controller.getGame().getCurrentPlayer().getPlayField().getNumberOf(Resource.SCROLL)));*/
                        break;
                    case "7":
                        showRanking();
                        returnToMenu();
                        break;
                    case "8":
                        printCommonObjectives();
                        System.out.println();
                        returnToMenu();
                        break;
                    case "9":
                        printScoreboard(createScoreboard());
                        returnToMenu();
                        break;
                    case "10":
                        printPlayArea(playAreas.get(playerID-1), playerID);
                        returnToMenu();
                        break;
                    case "11":
                        System.out.println("--- Your Table ---");
                        printPlayArea(playAreas.get(playerID-1), playerID);
                        System.out.println("--- Others ---");
                        ArrayList<HashMap<String, String>> info = controller.getPlayersInfo();
                        for (int i = 0; i < 4; i++) {
                            if (i+1 != playerID && null != playAreas.get(i)) {
                                System.out.println(info.get(i).get("Nickname") + ":");
                                printPlayArea(playAreas.get(i), i+1);
                            }
                        }
                        returnToMenu();
                        break;
                    case "12":
                        askForGraphicsMode();
                        break;
                    case "13":
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
                    for (String[][] playArea: playAreas) {
                        if (null != playArea) {
                            recreatePlayArea(playArea);
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
                    returnToMenu();
                    // Refreshes the PlayAreas
                    for (String[][] playArea: playAreas) {
                        if (null != playArea) {
                            recreatePlayArea(playArea);
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
                    returnToMenu();
                    // Refreshes the PlayAreas
                    for (String[][] playArea: playAreas) {
                        if (null != playArea) {
                            recreatePlayArea(playArea);
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
        System.out.println("6) Exit");
        System.out.println("7) Show ranking");
        System.out.println("8) Show the Common Objectives");
        System.out.println("9) Show Scoreboard");
        System.out.println("10) Print your Table");
        System.out.println("11) Print all Tables");
        System.out.println("12) Change Graphics Mode");
        if (!isAdvancedGraphicsMode) {
            System.out.println("13) Show a legend with character descriptions");
        }
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
                            playCard(input);
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
                    //TODO: Fix
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
        printCard(starterCard);
        starterCard.flip();
        System.out.println("Digit b to choose back side");
        printCard(starterCard);

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
            Card card1, card2;
            card1 = controller.getDeck(CardType.RESOURCECARD).getFirst();
            card2 = controller.getDeck(CardType.GOLDCARD).getFirst();
            card1.flip();
            card2.flip();
            System.out.println("Decks");
            System.out.println("Resource\t\t\tGold");
            for (int line = 1; line < 6; line++) {
                System.out.println(getPrintCardLine((PlayableCard) card1, line, isColorTerminalSupported) +
                        "\t" + (getPrintCardLine((PlayableCard) card2, line, isColorTerminalSupported)));
            }
            System.out.println();
            System.out.println("Slots:");
            System.out.println("Resource\t\t\tGold");
            for (int slot = 1; slot < 3; slot++) {
                card1 = controller.getSlotCard(CardType.RESOURCECARD, slot);
                card2 = controller.getSlotCard(CardType.GOLDCARD, slot);
                for (int line = 1; line < 6; line++) {
                    System.out.println(getPrintCardLine((PlayableCard) card1, line, isColorTerminalSupported) +
                            "\t" + (getPrintCardLine((PlayableCard) card2, line, isColorTerminalSupported)));
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
                        case "gr" -> controller.drawCard(playerID, CardType.RESOURCECARD);
                        case "gg" -> controller.drawCard(playerID, CardType.GOLDCARD);
                        case "1r" -> controller.grabCard(playerID, CardType.RESOURCECARD, 1);
                        case "1g" -> controller.grabCard(playerID, CardType.GOLDCARD, 1);
                        case "2r" -> controller.grabCard(playerID, CardType.RESOURCECARD, 2);
                        case "2g" -> controller.grabCard(playerID, CardType.GOLDCARD, 2);
                        default -> {
                            System.err.println("Invalid input!");
                            actions.add(() -> askToDrawOrGrab());
                        }
                    }
                }
            });
        }
    }

    // Not needed
    //TODO: Remove
    @Override
    public void notifyGameIsStarting() {

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

    /**
     * Shows the updated Ranking of all Player's points
     * @param token the Token of the Player who updated its Points.
     * @param newPoints the updated Points' value
     */
    @Override
    public void notifyPlayersPointsChanged(Token token, int newPoints) {
        actions.add(this::showRanking);
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
        ArrayList<PlayableCard> cards = controller.getPlayersPlayfield(playerID);
        if (null != playAreas.get(playerID - 1)) {
            playAreas.set(playerID-1, updateCardMatrix(playAreas.get(playerID-1), cards.getLast(), cards, playerID));
        } else {
            playAreas.set(playerID-1, createCardMatrix((StarterCard) cards.getFirst(), playerID));
        }
        if (this.playerID == playerID) {
            printPlayArea(playAreas.get(playerID-1), playerID);
        }
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
            printCard( (PlayableCard) controller.getSlotCard(CardType.OBJECTIVECARD, i));
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
     * Creates a formatted String representation of one of the Card's lines.
     * A Card has 5 lines, because it's printed in 5 different Lines.
     *
     * @param card  the Card to print
     * @param line  an int value (1-5) corresponding to the Line to print
     * @param printColors a boolean value indicating if it needs to override the global isColorTerminalSupported or use that one instead
     * @return the formatted String, in ASCII art or Emojis based on the Graphics Mode.
     */
    private String getPrintCardLine(PlayableCard card, int line, boolean printColors) {
        String string = "";
        if (null != card) {
            if (card.getId() == -1){
                if (line == 3){
                    for (int i = 0; i < 3; i++) {
                        string += terminalCharacters.getCharacter(Characters.EMPTY_CORNER, printColors);
                    }
                    if (String.valueOf(card.getX()).length() == 1){
                        string += card.getX() + " ";
                    }
                    else{
                        string += String.valueOf(card.getX());
                    }
                    string += terminalCharacters.getCharacter(Characters.EMPTY_CORNER, printColors);
                    if (String.valueOf(card.getY()).length() == 1){
                        string += card.getY() + " ";
                    }
                    else{
                        string += String.valueOf(card.getY());
                    }
                    for (int i = 0; i < 3; i++) {
                        string += terminalCharacters.getCharacter(Characters.EMPTY_CORNER, printColors);
                    }
                }
                else {
                    for (int i = 0; i < 9; i++) {
                        string += terminalCharacters.getCharacter(Characters.EMPTY_CORNER, printColors);
                    }
                }
            }
            else {
                switch (line) {
                    case 1 -> {
                        if (0 != card.getEarnedPoints()) {
                            // This Card has Points, that will be displayed on the texture
                            string = getCornerPrint(card, card.getShowingSide().getTopLeftCorner(), printColors);
                            for (int i = 0; i < 7; i++) {
                                if ((i == 0 || i == 6) && card instanceof GoldCard) {
                                    string += terminalCharacters.getCharacter(Characters.YELLOW_SQUARE, printColors);
                                } else if (i == 3 && card.isFrontFacing()) {
                                    // Print points
                                    if (printColors) {
                                        string += color(card.getEarnedPoints() + " ", UiColors.YELLOW);
                                    } else {
                                        string += card.getEarnedPoints() + " ";
                                    }
                                } else {
                                    string += getCardColor(card, printColors);
                                }
                            }
                            string += getCornerPrint(card, card.getShowingSide().getTopRightCorner(), printColors);
                        } else if (card instanceof GoldCard && null != ((GoldCard) card).getObjective()) {
                            // This Card has some Points related to a Condition, that will be displayed on the texture
                            string = getCornerPrint(card, card.getShowingSide().getTopLeftCorner(), printColors);
                            for (int i = 0; i < 7; i++) {
                                if (i == 0 || i == 6) {
                                    string += terminalCharacters.getCharacter(Characters.YELLOW_SQUARE, printColors);
                                } else if (i == 2 && card.isFrontFacing()) {
                                    // Print points
                                    if (((GoldCard) card).getObjective() instanceof CornerCountObjective) {
                                        string += terminalCharacters.getCharacter(Characters.EMPTY_CORNER, printColors);
                                    } else {
                                        switch (((ItemCountObjective) ((GoldCard) card).getObjective()).getItem()) {
                                            case FUNGI ->
                                                    string += terminalCharacters.getCharacter(Characters.FUNGI, printColors);
                                            case PLANT ->
                                                    string += terminalCharacters.getCharacter(Characters.PLANT, printColors);
                                            case ANIMAL ->
                                                    string += terminalCharacters.getCharacter(Characters.ANIMAL, printColors);
                                            case INSECT ->
                                                    string += terminalCharacters.getCharacter(Characters.INSECT, printColors);
                                            case POTION ->
                                                    string += terminalCharacters.getCharacter(Characters.POTION, printColors);
                                            case FEATHER ->
                                                    string += terminalCharacters.getCharacter(Characters.FEATHER, printColors);
                                            case SCROLL ->
                                                    string += terminalCharacters.getCharacter(Characters.SCROLL, printColors);
                                            default ->
                                                    throw new IllegalStateException("Unexpected value: " + ((ItemCountObjective) ((GoldCard) card).getObjective()).getItem());
                                        }
                                    }
                                } else if (i == 4 && card.isFrontFacing()) {
                                    // Print points
                                    if (printColors) {
                                        string += color(((GoldCard) card).getObjective().getPoints() + " ", UiColors.YELLOW);
                                    } else {
                                        string += ((GoldCard) card).getObjective().getPoints() + " ";
                                    }
                                } else {
                                    string += getCardColor(card, printColors);
                                }
                            }
                            string += getCornerPrint(card, card.getShowingSide().getTopRightCorner(), printColors);
                        } else {
                            string = getCornerPrint(card, card.getShowingSide().getTopLeftCorner(), printColors);
                            for (int i = 0; i < 7; i++) {
                                if ((i == 0 || i == 6) && card instanceof GoldCard) {
                                    string += terminalCharacters.getCharacter(Characters.YELLOW_SQUARE, printColors);
                                } else {
                                    string += getCardColor(card, printColors);
                                }
                            }
                            string += getCornerPrint(card, card.getShowingSide().getTopRightCorner(), printColors);
                        }
                    }
                    case 2, 4 -> {
                        if (card instanceof GoldCard) {
                            string += terminalCharacters.getCharacter(Characters.YELLOW_SQUARE, printColors);
                            for (int i = 0; i < 7; i++) {
                                string += getCardColor(card, printColors);
                            }
                            string += terminalCharacters.getCharacter(Characters.YELLOW_SQUARE, printColors);
                        } else {
                            for (int i = 0; i < 9; i++) {
                                string += getCardColor(card, printColors);
                            }
                        }
                    }
                    case 3 -> {
                        for (int i = 0; i < 3; i++) {
                            string += getCardColor(card, printColors);
                        }
                        if ((!(card instanceof StarterCard) && !card.isFrontFacing()) || (card.isFrontFacing() && card instanceof StarterCard)) {
                            switch (card.getPermanentResources().size()) {
                                case 1 -> {
                                    string += getCardColor(card, printColors) + getItemPrint(card.getPermanentResources().get(0), printColors) + getCardColor(card, printColors);
                                }
                                case 2 -> {
                                    string += getItemPrint(card.getPermanentResources().get(0), printColors) + getCardColor(card, printColors)
                                            + getItemPrint(card.getPermanentResources().get(1), printColors);
                                }
                                case 3 -> {
                                    string += getItemPrint(card.getPermanentResources().get(0), printColors)
                                            + getItemPrint(card.getPermanentResources().get(1), printColors)
                                            + getItemPrint(card.getPermanentResources().get(2), printColors);
                                }
                                default -> {
                                    for (int i = 0; i < 3; i++) {
                                        string += getCardColor(card, printColors);
                                    }
                                }
                            }
                        } else {
                            for (int i = 0; i < 3; i++) {
                                string += getCardColor(card, printColors);
                            }
                        }
                        for (int i = 0; i < 3; i++) {
                            string += getCardColor(card, printColors);
                        }
                    }
                    case 5 -> {
                        string = getCornerPrint(card, card.getShowingSide().getBottomLeftCorner(), printColors);
                        if (card instanceof GoldCard) {
                            // Read costs
                            ArrayList<Item> costs = new ArrayList<>();
                            for (int i = 0; i < ((GoldCard) card).getCost(FUNGI); i++) {
                                costs.add(FUNGI);
                            }
                            for (int i = 0; i < ((GoldCard) card).getCost(PLANT); i++) {
                                costs.add(PLANT);
                            }
                            for (int i = 0; i < ((GoldCard) card).getCost(ANIMAL); i++) {
                                costs.add(ANIMAL);
                            }
                            for (int i = 0; i < ((GoldCard) card).getCost(INSECT); i++) {
                                costs.add(INSECT);
                            }

                            if (!card.isFrontFacing()) {
                                costs = new ArrayList<>();
                            }
                            string += terminalCharacters.getCharacter(Characters.YELLOW_SQUARE, printColors);
                            switch (costs.size()) {
                                case 0 -> {
                                    string += getCardColor(card, printColors);
                                    string += getCardColor(card, printColors);
                                    string += getCardColor(card, printColors);
                                    string += getCardColor(card, printColors);
                                    string += getCardColor(card, printColors);
                                }
                                case 1 -> {
                                    string += getCardColor(card, printColors);
                                    string += getCardColor(card, printColors);
                                    string += getItemPrint(costs.getFirst(), printColors);
                                    string += getCardColor(card, printColors);
                                    string += getCardColor(card, printColors);

                                }
                                case 2 -> {
                                    string += getCardColor(card, printColors);
                                    string += getItemPrint(costs.getFirst(), printColors);
                                    string += getCardColor(card, printColors);
                                    string += getItemPrint(costs.get(1), printColors);
                                    string += getCardColor(card, printColors);
                                }
                                case 3 -> {
                                    string += getCardColor(card, printColors);
                                    string += getItemPrint(costs.getFirst(), printColors);
                                    string += getItemPrint(costs.get(1), printColors);
                                    string += getItemPrint(costs.get(2), printColors);
                                    string += getCardColor(card, printColors);
                                }
                                case 4 -> {
                                    string += getItemPrint(costs.getFirst(), printColors);
                                    string += getItemPrint(costs.get(1), printColors);
                                    string += getItemPrint(costs.get(2), printColors);
                                    string += getItemPrint(costs.get(3), printColors);
                                    string += getCardColor(card, printColors);
                                }
                                case 5 -> {
                                    string += getItemPrint(costs.getFirst(), printColors);
                                    string += getItemPrint(costs.get(1), printColors);
                                    string += getItemPrint(costs.get(2), printColors);
                                    string += getItemPrint(costs.get(3), printColors);
                                    string += getItemPrint(costs.get(4), printColors);
                                }
                            }
                            string += terminalCharacters.getCharacter(Characters.YELLOW_SQUARE, printColors);
                            string += getCornerPrint(card, card.getShowingSide().getBottomRightCorner(), printColors);
                        } else {
                            for (int i = 0; i < 7; i++) {
                                string += getCardColor(card, printColors);
                            }
                            string += getCornerPrint(card, card.getShowingSide().getBottomRightCorner(), printColors);
                        }
                    }
                }
            }
        } else {
            if (true) {
                for (int i = 0; i < 20; i++) {
                    string += " ";
                }
            } else {
                for (int i = 0; i < 15; i++) {
                    string += " ";
                }
            }
        }
        return string;
    }

    /**
     * Creates a String containing a representation of a Corner, based on its type and the content inside.
     * @param card the Card that contains the Corner
     * @param corner the Corner to print
     * @param printColors a boolean value indicating if it needs to override the global isColorTerminalSupported or not
     * @return the String representation of the Corner, in Unicode characters or Emojis depending on the Graphics Mode.
     */
    private String getCornerPrint(PlayableCard card, Corner corner, boolean printColors) {
        String string = "";
        if (null != corner) {
            if (null != corner.getItem()) {
                string = getItemPrint(corner.getItem(), printColors);
            } else {
                string = terminalCharacters.getCharacter(Characters.EMPTY_CORNER, printColors);
            }
        } else string = getCardColor(card, printColors);
        return string;
    }

    /**
     * Creates a String containing a representation of an Item (KingdomResource or Resource)
     * @param item the item to print
     * @param printColors a boolean value indicating if it needs to override the global isColorTerminalSupported or not
     * @return the String representation of the Item, in Unicode characters or Emojis depending on the Graphics Mode.
     */
    private String getItemPrint(Item item, boolean printColors) {
        String string = "  ";
        switch (item) {
            case FUNGI -> string = terminalCharacters.getCharacter(Characters.FUNGI, printColors);
            case ANIMAL -> string = terminalCharacters.getCharacter(Characters.ANIMAL, printColors);
            case INSECT -> string = terminalCharacters.getCharacter(Characters.INSECT, printColors);
            case PLANT -> string = terminalCharacters.getCharacter(Characters.PLANT, printColors);
            case FEATHER -> string = terminalCharacters.getCharacter(Characters.FEATHER, printColors);
            case POTION -> string = terminalCharacters.getCharacter(Characters.POTION, printColors);
            case SCROLL -> string = terminalCharacters.getCharacter(Characters.SCROLL, printColors);
            default -> {
                string = "  ";
            }
        }
        return string;
    }

    /**
     * Prints a Card in its entirety by printing all the 5 lines the Card is made of.
     * @param card the Card to print
     */
    public void printCard(PlayableCard card) {
        System.out.println(getPrintCardLine(card, 1, isColorTerminalSupported));
        System.out.println(getPrintCardLine(card, 2, isColorTerminalSupported));
        System.out.println(getPrintCardLine(card, 3, isColorTerminalSupported));
        System.out.println(getPrintCardLine(card, 4, isColorTerminalSupported));
        System.out.println(getPrintCardLine(card, 5, isColorTerminalSupported));
    }

    /**
     * Creates a String containing the "Building Block" of a Card, the character used to visually create the Card when printing it.
     * @param card the Card to print
     * @param printColors a boolean value indicating it it needs to override the global isColorTerminalSupported or use that one instead
     * @return the Unicode Character or Emoji, depending on the Graphics Mode.
     */
    private String getCardColor(PlayableCard card, boolean printColors) {
        String string = "";
        if (card instanceof ResourceCard || card instanceof GoldCard) {
            switch (card.getPermanentResources().getFirst()) {
                case PLANT -> string = terminalCharacters.getCharacter(Characters.GREEN_SQUARE, printColors);
                case ANIMAL -> string = terminalCharacters.getCharacter(Characters.BLUE_SQUARE, printColors);
                case FUNGI -> string = terminalCharacters.getCharacter(Characters.RED_SQUARE, printColors);
                case INSECT -> string = terminalCharacters.getCharacter(Characters.PURPLE_SQUARE, printColors);
                default -> string = "";
            }
        } else if (card instanceof StarterCard) {
            string = terminalCharacters.getCharacter(Characters.YELLOW_SQUARE, printColors);
        } else {
            string = terminalCharacters.getCharacter(Characters.EMPTY_CORNER, printColors);
        }
        return string;
    }

    /**
     * Converts a Card's Coordinates from the custom 45° angled system used in Model to the standard xy system representation.
     * @param coordinates the Model's Coordinates
     * @return a Coordinates object containing the converted values.
     */
    private Coordinates convertToAbsoluteCoordinates(Coordinates coordinates) {
        int x = coordinates.getX() - coordinates.getY();
        int y = coordinates.getX() + coordinates.getY();
        return new Coordinates(x, y);
    }

    /**
     * Converts the Coordinates to a format specifically tailored to work for the matrices used to represent the PlayArea.
     * This format uses two offset values, calculated from the matrix's width and height to move the (0,0) position from the
     * top left corner to the matrix's center, and then allows all the other normalized Coordinates to be converted in matrix's
     * Coordinates.
     * IMPORTANT: This method requires normalized Coordinates, as in using the standard xy representation.
     * Model's Coordinates need to be converted to Absolute Coordinates before being passed here.
     * @param coordinates the Absolute Coordinates to convert to Matrix's Coordinates
     * @param matrixSizeX the Matrix's width
     * @param matrixSizeY the Matrix's height
     * @return the converted Coordinates
     */
    private Coordinates convertMatrixCoordinates(Coordinates coordinates, int matrixSizeX, int matrixSizeY) {
        int shiftX = (matrixSizeX) / 2;
        int shiftY = (matrixSizeY) / 2;
        int x = coordinates.getX() + shiftX;
        int y = shiftY - coordinates.getY();
        coordinates.setX(x);
        coordinates.setY(y);
        return coordinates;
    }

    /**
     * Initializes the Matrix used to represent the PlayArea, and puts the StarterCard already in there.
     * @param starter the Player's Starter Card
     * @param playerID the Player's playerID, used to initialize its Matrix's printing extremes values
     * @return the initialized Matrix of String[][]
     */
    private String[][] createCardMatrix(StarterCard starter, int playerID){
        String[][] playArea = new String[800][1440];
        String line = "";
        int firstColumn = 716;
        int firstLine = 398 ;
        for (int j = 1; j <= 5; j++) {
            line = getPrintCardLine(starter,j, false);
            for (int i = 0; i < line.length(); i += 2) {
                String string = new String(String.valueOf(line.charAt(i)));
                try {
                    string += line.charAt(i+1);
                } catch (IndexOutOfBoundsException e) {
                    //string += line.charAt(i);
                }
                playArea[firstLine][firstColumn] = string;
                firstColumn++;
            }
            firstLine++;
            firstColumn = 716;
        }
      for (int i = 0; i < 800; i++){
          for (int j = 0; j < 1440; j++){
              if (playArea[i][j] == null){
                  playArea[i][j] = "  ";
              }
          }
      }
        //extreme DX
        printingExtremes.get(playerID-1).replace("RIGHT", 725);
        //extreme SX
        printingExtremes.get(playerID-1).replace("LEFT", 717);
        //extreme UP
        printingExtremes.get(playerID-1).replace("UP", 399);
        //extreme DOWN
        printingExtremes.get(playerID-1).replace("DOWN", 403);
        return playArea;
    }

    /**
     * Updates the Matrix by adding a Card in a specific position, identified by 3 Matrix's Coordinates
     * @param matrix the Matrix to update
     * @param card the Card to add inside the Matrix (PlayArea)
     * @param firstLine the y Coordinate of the top left Corner of the Card
     * @param firstColumn the x Coordinate of the top left Corner of the Card
     * @param centerX the x Coordinate of the center of the Card
     * @return the updated Matrix
     */
    private String[][] addCardToMatrix(String[][] matrix, PlayableCard card, int firstLine, int firstColumn, int centerX){
        String line = "";
        for (int j = 1; j <= 5; j++) {
            line = getPrintCardLine(card,j, false);
            int i = 0;
            while (i < line.length()) {
                if (isAdvancedGraphicsMode) {
                    // Emoji Mode
                    String string = new String(String.valueOf(line.charAt(i)));
                    if (string.equals("🟫")) {
                        matrix[firstLine][firstColumn] = "🟫";
                        i--;
                        firstColumn++;
                    } else {
                        string += line.charAt(i + 1);
                        matrix[firstLine][firstColumn] = string;
                        firstColumn++;
                    }
                    i += 2;
                } else {
                    // Character Mode
                    String string = new String(String.valueOf(line.charAt(i)));
                    string += line.charAt(i + 1);
                    matrix[firstLine][firstColumn] = string;
                    firstColumn++;
                    i += 2;
                }
            }
            firstLine++;
            firstColumn = centerX - 4;
        }
        return matrix;
    }

    /**
     * Calculates the Coordinates where a Card has to be placed inside the Matrix
     * @param matrix the Matrix to update
     * @param card the Card to add inside the Matrix
     * @param cards the Player's PlayField
     * @param playerID the Player's playerID
     * @return the updated Matrix
     */
    private String[][] updateCardMatrix(String[][] matrix, PlayableCard card, ArrayList<PlayableCard> cards, int playerID){
        Coordinates coordinates = convertMatrixCoordinates(convertToAbsoluteCoordinates(card.getCoordinates()), 1440,800);
        Coordinates coordinates1 = convertToAbsoluteCoordinates(card.getCoordinates());
        //card down sx
        if(isThereACardIn(card.getX()-1,card.getY(), cards)){
            int centerX = coordinates.getX() + (8 * Math.abs(coordinates1.getX()));
            /*if (card.getX() == 0) {
                centerX = coordinates.getX() + 8;
            }*/
            int centerY = coordinates.getY() - (4 * Math.abs(coordinates1.getY()));
            /*if (card.getY() == 0) {
                centerY= coordinates.getY() - 4;
            }*/

            centerX -= coordinates1.getX();
            centerY+= coordinates1.getY();

            int firstColumn = centerX - 4;
            int firstLine = centerY -2;

            matrix = addCardToMatrix(matrix,card,firstLine,firstColumn,centerX);

            if (centerX + 4 > printingExtremes.get(playerID-1).get("RIGHT")) {
                printingExtremes.get(playerID - 1).replace("RIGHT", centerX + 4);
            }
            if (centerY - 2 < printingExtremes.get(playerID-1).get("UP")) {
                printingExtremes.get(playerID - 1).replace("UP", centerY - 2);
            }
            return matrix;
        }
        //card up dx
        if(isThereACardIn(card.getX()+1,card.getY(), cards)){
            int centerX = coordinates.getX() - (8 * Math.abs(coordinates1.getX()));
            /*if (card.getX() == 0) {
                centerX = coordinates.getX() - 8;
            }*/
            int centerY = coordinates.getY() + (4 * Math.abs(coordinates1.getY()));
            /*if (card.getY() == 0) {
                centerY= coordinates.getY() + 4;
            }*/

            centerX -= coordinates1.getX();
            centerY+= coordinates1.getY();

            int firstColumn = centerX - 4;
            int firstLine = centerY - 2;
            matrix = addCardToMatrix(matrix,card,firstLine,firstColumn,centerX);

            if (centerX - 4 < printingExtremes.get(playerID-1).get("LEFT")) {
                printingExtremes.get(playerID - 1).replace("LEFT", centerX - 4);
            }
            if (centerY + 2 > printingExtremes.get(playerID-1).get("DOWN")) {
                printingExtremes.get(playerID - 1).replace("DOWN", centerY + 2);
            }
            return matrix;
        }
        //card up sx
        if(isThereACardIn(card.getX(),card.getY()+1, cards)){
            int centerX = coordinates.getX() + (8 * Math.abs(coordinates1.getX()));
            /*if (card.getX() == 0) {
                centerX = coordinates.getX() + 8;
            }*/
            int centerY = coordinates.getY() + (4 * Math.abs(coordinates1.getY()));
            /*if (card.getY() == 0) {
                centerY= coordinates.getY() + 4;
            }*/

            centerX -= coordinates1.getX();
            centerY += coordinates1.getY();

            int firstColumn = centerX - 4;
            int firstLine = centerY - 2;
            matrix = addCardToMatrix(matrix,card,firstLine,firstColumn,centerX);

            if (centerX + 4 > printingExtremes.get(playerID-1).get("RIGHT")) {
                printingExtremes.get(playerID - 1).replace("RIGHT", centerX + 4);
            }
            if (centerY + 2 > printingExtremes.get(playerID-1).get("DOWN")) {
                printingExtremes.get(playerID - 1).replace("DOWN", centerY + 2);
            }
            return matrix;
        }
        //card down dx
        if(isThereACardIn(card.getX(),card.getY()-1, cards)){
            int centerX = coordinates.getX() - (8 * Math.abs(coordinates1.getX()));
            /*if (card.getX() == 0) {
                centerX = coordinates.getX() - 8;
            }*/
            int centerY = coordinates.getY() - (4 * Math.abs(coordinates1.getY()));
            /*if (card.getY() == 0) {
                centerY= coordinates.getY() - 4;
            }*/

            centerX -= coordinates1.getX();
            centerY += coordinates1.getY();

            int firstColumn = centerX - 4;
            int firstLine = centerY -2;
            matrix = addCardToMatrix(matrix,card,firstLine,firstColumn,centerX);

            if (centerX - 4 < printingExtremes.get(playerID-1).get("LEFT")) {
                printingExtremes.get(playerID - 1).replace("LEFT", centerX - 4);
            }
            if (centerY - 2 < printingExtremes.get(playerID-1).get("UP")) {
                printingExtremes.get(playerID - 1).replace("UP", centerY - 2);
            }
            return matrix;
        }
        return matrix;
    }

    /**
     * Refreshes the entire Matrix (PlayArea), fetching every single String and re-writing all of them.
     * This is intended to re-generate the Matrix after switching Graphics Mode: in this situation every String, still
     * formatted for the old Mode, is replaced by the same String but in the new Mode representation.
     * This works both ways: STANDARD -> FANCY and FANCY -> STANDARD.
     * This method can also be used to re-generate a Matrix while keeping the same Graphics Mode, in the case of errors.
     * @param playArea the updated Matrix
     */
    private void recreatePlayArea(String[][] playArea) {
        for (int i = 0; i < playArea.length; i++) {
            for (int j = 0; j < playArea[i].length; j++) {
                switch (playArea[i][j]) {
                    case "ନ ", "🍄" -> playArea[i][j] = terminalCharacters.getCharacter(Characters.FUNGI);
                    case "✿ ", "🌳" -> playArea[i][j] = terminalCharacters.getCharacter(Characters.PLANT);
                    case "♘ ", "🐺" -> playArea[i][j] = terminalCharacters.getCharacter(Characters.ANIMAL);
                    case "¥ ", "🦋" -> playArea[i][j] = terminalCharacters.getCharacter(Characters.INSECT);
                    case "∫ ", "📜" -> playArea[i][j] = terminalCharacters.getCharacter(Characters.SCROLL);
                    case "ϡ ", "🪶" -> playArea[i][j] = terminalCharacters.getCharacter(Characters.FEATHER);
                    case "Ỗ ", "🍷" -> playArea[i][j] = terminalCharacters.getCharacter(Characters.POTION);
                    case "▤ ", "🟥" -> playArea[i][j] = terminalCharacters.getCharacter(Characters.RED_SQUARE);
                    case "▥ ", "🟦" -> playArea[i][j] = terminalCharacters.getCharacter(Characters.BLUE_SQUARE);
                    case "▦ ", "🟩" -> playArea[i][j] = terminalCharacters.getCharacter(Characters.GREEN_SQUARE);
                    case "▧ ", "🟪" -> playArea[i][j] = terminalCharacters.getCharacter(Characters.PURPLE_SQUARE);
                    case "▩ ", "🟨" -> playArea[i][j] = terminalCharacters.getCharacter(Characters.YELLOW_SQUARE);
                    case "■ ", "⚪" -> playArea[i][j] = terminalCharacters.getCharacter(Characters.WHITE_SQUARE);
                    case "  ", "⬛" -> playArea[i][j] = terminalCharacters.getCharacter(Characters.EMPTY_SPACE);
                    case "▢ ", "🟫" -> playArea[i][j] = terminalCharacters.getCharacter(Characters.EMPTY_CORNER);

                }
            }
        }
    }

    /**
     * Prints a portion of the Matrix (PlayArea).
     * Printing is limited to a portion because the whole Matrix is too big.
     * Printing extremes are used to determine where to start and end printing: they are
     * updated every time a new Card is added inside the Matrix.
     * Printing extremes leave a few empty Strings around the Cards as a margin.
     * @param playArea the Matrix to print
     * @param playerID the Player's playerID associated to this Matrix
     */
    private void printPlayArea(String[][] playArea, int playerID){
        int extremeUP = printingExtremes.get(playerID-1).get("UP");
        int extremeDOWN = printingExtremes.get(playerID-1).get("DOWN");
        int extremeLEFT = printingExtremes.get(playerID-1).get("LEFT");
        int extremeRIGHT = printingExtremes.get(playerID-1).get("RIGHT");
        for (int i = extremeUP - 5; i <= extremeDOWN + 5; i++){
            String line = "";
            for (int j = extremeLEFT - 5; j <= extremeRIGHT + 5; j++){
                if (!isAdvancedGraphicsMode && isColorTerminalSupported) {
                    // Enhanced Mode (here colors are injected in the String, because the Matrix doesn't store them
                    String symbol = "";
                    switch (playArea[i][j]) {
                        case "ନ " -> symbol = terminalCharacters.getCharacter(Characters.FUNGI);
                        case "✿ " -> symbol = terminalCharacters.getCharacter(Characters.PLANT);
                        case "♘ " -> symbol = terminalCharacters.getCharacter(Characters.ANIMAL);
                        case "¥ " -> symbol = terminalCharacters.getCharacter(Characters.INSECT);
                        case "∫ " -> symbol = terminalCharacters.getCharacter(Characters.SCROLL);
                        case "ϡ " -> symbol = terminalCharacters.getCharacter(Characters.FEATHER);
                        case "Ỗ " -> symbol = terminalCharacters.getCharacter(Characters.POTION);
                        case "▤ " -> symbol = terminalCharacters.getCharacter(Characters.RED_SQUARE);
                        case "▥ " -> symbol = terminalCharacters.getCharacter(Characters.BLUE_SQUARE);
                        case "▦ " -> symbol = terminalCharacters.getCharacter(Characters.GREEN_SQUARE);
                        case "▧ " -> symbol = terminalCharacters.getCharacter(Characters.PURPLE_SQUARE);
                        case "▩ " -> symbol = terminalCharacters.getCharacter(Characters.YELLOW_SQUARE);
                        case "■ " -> symbol = terminalCharacters.getCharacter(Characters.WHITE_SQUARE);
                        case "  " -> symbol = terminalCharacters.getCharacter(Characters.EMPTY_SPACE);
                        case "▢ " -> symbol = terminalCharacters.getCharacter(Characters.EMPTY_CORNER);
                        default -> symbol = playArea[i][j];
                    }
                    line += symbol;
                } else {
                    // Fancy and Standard Mode (no character modifications required
                    line += playArea[i][j];
                }
            }
            System.out.println(line);
        }
    }

    /**
     * Initializes the Matrix containing the ScoreBoard
     * @return the Matrix
     */
    private String[][] createScoreboard(){
        String[][] scoreboard = {
                {"🟨","🟨","🟨","🟨","🟨","🟨","🟨","🟨","🟨","🟨","🟨","🟨","🟨","🟨","🟨","🟨","🟨","🟨","🟨","🟨","🟨","🟨","🟨","🟨","🟨","🟨"},
                {"🟨","🟪","🟪","🟪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","🟦","🟦","🟦","🟨"},
                {"🟨","🟪","🦋","🟪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","🟦","🐺","🟦","🟨"},
                {"🟨","🟪","🟪","🟪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪"," 2","🟦","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","🟦","🟦","🟦","🟨"},
                {"🟨","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","🔶","🔶","🟦"," 5","🔶","🔶","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","🟨"},
                {"🟨","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","🔶","⚪","⚪","⚪","⚪","⚪","⚪","🔶","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","🟨"},
                {"🟨","⚪","⚪","⚪","⚪","⚪","⚪"," 2","🟦","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪"," 2","🟦","⚪","⚪","⚪","⚪","⚪","⚪","🟨"},
                {"🟨","⚪","⚪","⚪","⚪","🔶","🔶","🟦"," 4","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","🟦"," 6","🔶","🔶","⚪","⚪","⚪","⚪","🟨"},
                {"🟨","⚪","⚪","⚪","🔶","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","🔶","⚪","⚪","⚪","🟨"},
                {"🟨","⚪"," 2","🟦","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪"," 2","🟦","⚪","🟨"},
                {"🟨","⚪","🟦"," 3","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","🟦"," 7","⚪","🟨"},
                {"🟨","⚪","🔶","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","🔶","⚪","🟨"},
                {"🟨","⚪","🔶","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪"," 2","🟦","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","🔶","⚪","🟨"},
                {"🟨","⚪","🔶","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","🟦"," 9","🔶","🔶","🔶","🔶","⚪","⚪","⚪","⚪","⚪","🔶","⚪","🟨"},
                {"🟨","⚪","🔶","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","🔶","⚪","⚪","⚪","⚪","🔶","⚪","🟨"},
                {"🟨","⚪"," 2","🟦","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","🔶","⚪","⚪"," 2","🟦","⚪","🟨"},
                {"🟨","⚪","🟦"," 2","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","🔶","🔶","🟦"," 8","⚪","🟨"},
                {"🟨","⚪","🔶","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","🔶","🔶","🔶","🔶","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","🟨"},
                {"🟨","⚪","🔶","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","🔶"," 2","🟦","🔶","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","🟨"},
                {"🟨","⚪","🔶","⚪","⚪","⚪","⚪","⚪","🔶","🔶","🔶","🔶","🟦"," 0","🔶","🔶","🔶","🔶","⚪","⚪","⚪","⚪","⚪","⚪","⚪","🟨"},
                {"🟨","⚪","🔶","⚪","⚪","⚪","🔶","🔶","⚪","⚪","⚪","🔶","🔶","🔶","🔶","⚪","⚪","⚪","🔶","🔶","⚪","⚪","⚪","⚪","⚪","🟨"},
                {"🟨","⚪"," 2","🟦","🔶","🔶","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","🔶","🔶"," 2","🟦","⚪","🟨"},
                {"🟨","⚪","🟦"," 1","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","🟦"," 9","⚪","🟨"},
                {"🟨","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","🔶","⚪","🟨"},
                {"🟨","⚪"," 1","🟦","⚪","⚪","⚪","⚪","⚪"," 1","🟦","⚪","⚪","⚪","⚪"," 1","🟦","⚪","⚪","⚪","⚪","⚪"," 1","🟦","⚪","🟨"},
                {"🟨","⚪","🟦"," 5","🔶","🔶","🔶","🔶","🔶","🟦"," 6","🔶","🔶","🔶","🔶","🟦"," 7","🔶","🔶","🔶","🔶","🔶","🟦"," 8","⚪","🟨"},
                {"🟨","⚪","🔶","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","🟨"},
                {"🟨","⚪"," 1","🟦","⚪","⚪","⚪","⚪","⚪"," 1","🟦","⚪","⚪","⚪","⚪"," 1","🟦","⚪","⚪","⚪","⚪","⚪"," 1","🟦","⚪","🟨"},
                {"🟨","⚪","🟦"," 4","🔶","🔶","🔶","🔶","🔶","🟦"," 3","🔶","🔶","🔶","🔶","🟦"," 2","🔶","🔶","🔶","🔶","🔶","🟦"," 1","⚪","🟨"},
                {"🟨","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","🔶","⚪","🟨"},
                {"🟨","⚪"," 0","🟦","⚪","⚪","⚪","⚪","⚪"," 0","🟦","⚪","⚪","⚪","⚪"," 0","🟦","⚪","⚪","⚪","⚪","⚪"," 1","🟦","⚪","🟨"},
                {"🟨","⚪","🟦"," 7","🔶","🔶","🔶","🔶","🔶","🟦"," 8","🔶","🔶","🔶","🔶","🟦"," 9","🔶","🔶","🔶","🔶","🔶","🟦"," 0","⚪","🟨"},
                {"🟨","⚪","🔶","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","🟨"},
                {"🟨","⚪"," 0","🟦","⚪","⚪","⚪","⚪","⚪"," 0","🟦","⚪","⚪","⚪","⚪"," 0","🟦","⚪","⚪","⚪","⚪","⚪"," 0","🟦","⚪","🟨"},
                {"🟨","⚪","🟦"," 6","🔶","🔶","🔶","🔶","🔶","🟦"," 5","🔶","🔶","🔶","🔶","🟦"," 4","🔶","🔶","🔶","🔶","🔶","🟦"," 3","⚪","🟨"},
                {"🟨","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","🔶","⚪","🟨"},
                {"🟨","⚪","⚪","⚪","⚪"," 0","🟦","⚪","⚪","⚪","⚪","⚪"," 0","🟦","⚪","⚪","⚪","⚪","⚪"," 0","🟦","⚪","🔶","⚪","⚪","🟨"},
                {"🟨","🟥","🟥","🟥","⚪","🟦"," 0","🔶","🔶","🔶","🔶","🔶","🟦"," 1","🔶","🔶","🔶","🔶","🔶","🟦"," 2","🔶","🟩","🟩","🟩","🟨"},
                {"🟨","🟥","🍄","🟥","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","🟩","🌳","🟩","🟨"},
                {"🟨","🟥","🟥","🟥","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","⚪","🟩","🟩","🟩","🟨"},
                {"🟨","🟨","🟨","🟨","🟨","🟨","🟨","🟨","🟨","🟨","🟨","🟨","🟨","🟨","🟨","🟨","🟨","🟨","🟨","🟨","🟨","🟨","🟨","🟨","🟨","🟨"},
        };
        /*
        "🟨🟨🟨🟨🟨🟨🟨🟨🟨🟨🟨🟨🟨🟨🟨🟨🟨🟨🟨🟨🟨🟨🟨🟨🟨🟨"
        "🟨🟪🟪🟪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪🟦🟦🟦🟨"
        "🟨🟪🦋🟪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪🟦🐺🟦🟨"
        "🟨🟪🟪🟪⚪⚪⚪⚪⚪⚪⚪⚪2️⃣🟦⚪⚪⚪⚪⚪⚪⚪⚪🟦🟦🟦🟨"
        "🟨⚪⚪⚪⚪⚪⚪⚪⚪⚪🔶🔶🟦5️⃣🔶🔶⚪⚪⚪⚪⚪⚪⚪⚪⚪🟨"
        "🟨⚪⚪⚪⚪⚪⚪⚪⚪🔶⚪⚪⚪⚪⚪⚪🔶⚪⚪⚪⚪⚪⚪⚪⚪🟨"
        "🟨⚪⚪⚪⚪⚪⚪2️⃣🟦⚪⚪⚪⚪⚪⚪⚪⚪2️⃣🟦⚪⚪⚪⚪⚪⚪🟨"
        "🟨⚪⚪⚪⚪🔶🔶🟦4️⃣⚪⚪⚪⚪⚪⚪⚪⚪🟦6️⃣🔶🔶⚪⚪⚪⚪🟨"
        "🟨⚪⚪⚪🔶⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪🔶⚪⚪⚪🟨"
        "🟨⚪2️⃣🟦⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪2️⃣🟦⚪🟨"
        "🟨⚪🟦3️⃣⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪🟦7️⃣⚪🟨"
        "🟨⚪🔶⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪🔶⚪🟨"
        "🟨⚪🔶⚪⚪⚪⚪⚪⚪⚪⚪⚪2️⃣🟦⚪⚪⚪⚪⚪⚪⚪⚪⚪🔶⚪🟨"
        "🟨⚪🔶⚪⚪⚪⚪⚪⚪⚪⚪⚪🟦9️⃣🔶🔶🔶🔶⚪⚪⚪⚪⚪🔶⚪🟨"
        "🟨⚪🔶⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪🔶⚪⚪⚪⚪🔶⚪🟨"
        "🟨⚪2️⃣🟦⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪🔶⚪⚪2️⃣🟦⚪🟨"
        "🟨⚪🟦2️⃣⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪🔶🔶🟦8️⃣⚪🟨"
        "🟨⚪🔶⚪⚪⚪⚪⚪⚪⚪⚪🔶🔶🔶🔶⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪🟨"
        "🟨⚪🔶⚪⚪⚪⚪⚪⚪⚪⚪🔶2️⃣🟦🔶⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪🟨"
        "🟨⚪🔶⚪⚪⚪⚪⚪🔶🔶🔶🔶🟦0️🔶🔶🔶🔶⚪⚪⚪⚪⚪⚪⚪🟨"
        "🟨⚪🔶⚪⚪⚪🔶🔶⚪⚪⚪🔶🔶🔶🔶⚪⚪⚪🔶🔶⚪⚪⚪⚪⚪🟨"
        "🟨⚪2️⃣🟦🔶🔶⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪🔶🔶1️⃣🟦⚪🟨"
        "🟨⚪🟦1️⃣⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪🟦9️⃣⚪🟨"
        "🟨⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪🔶⚪🟨"
        "🟨⚪1️⃣🟦⚪⚪⚪⚪⚪1️⃣🟦⚪⚪⚪⚪1️⃣🟦⚪⚪⚪⚪⚪1️⃣🟦⚪🟨"
        "🟨⚪🟦5️⃣🔶🔶🔶🔶🔶🟦6️⃣🔶🔶🔶🔶🟦7️⃣🔶🔶🔶🔶🔶🟦8️⃣⚪🟨"
        "🟨⚪🔶⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪🟨"
        "🟨⚪1️⃣🟦⚪⚪⚪⚪⚪1️⃣🟦⚪⚪⚪⚪1️⃣🟦⚪⚪⚪⚪⚪1️⃣🟦⚪🟨"
        "🟨⚪🟦4️⃣🔶🔶🔶🔶🔶🟦3️⃣🔶🔶🔶🔶🟦2️⃣🔶🔶🔶🔶🔶🟦1️⃣⚪🟨"
        "🟨⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪🔶⚪🟨"
        "🟨⚪0️⃣🟦⚪⚪⚪⚪⚪0️⃣🟦⚪⚪⚪⚪0️⃣🟦⚪⚪⚪⚪⚪1️⃣🟦⚪🟨"
        "🟨⚪🟦7️⃣🔶🔶🔶🔶🔶🟦8️⃣🔶🔶🔶🔶🟦9️⃣🔶🔶🔶🔶🔶🟦0️⃣⚪🟨"
        "🟨⚪🔶⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪🟨"
        "🟨⚪0️⃣🟦⚪⚪⚪⚪⚪0️⃣🟦⚪⚪⚪⚪0️⃣🟦⚪⚪⚪⚪⚪0️⃣🟦⚪🟨"
        "🟨⚪🟦6️⃣🔶🔶🔶🔶🔶🟦5️⃣🔶🔶🔶🔶🟦4️⃣🔶🔶🔶🔶🔶🟦3️⃣⚪🟨"
        "🟨⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪🔶⚪🟨"
        "🟨⚪⚪⚪⚪0️⃣🟦⚪⚪⚪⚪⚪0️⃣🟦⚪⚪⚪⚪⚪0️⃣🟦⚪🔶⚪⚪🟨"
        "🟨🟥🟥🟥⚪🟦0️⃣🔶🔶🔶🔶🔶🟦1️⃣🔶🔶🔶🔶🔶🟦2️⃣🔶🟩🟩🟩🟨"
        "🟨🟥🍄🟥⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪🟩🌳🟩🟨"
        "🟨🟥🟥🟥⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪🟩🟩🟩🟨"
        "🟨🟨🟨🟨🟨🟨🟨🟨🟨🟨🟨🟨🟨🟨🟨🟨🟨🟨🟨🟨🟨🟨🟨🟨🟨🟨"
        */
        return scoreboard;
    }

    /**
     * Prints the Matrix containing the ScoreBoard
     * @param scoreboard the Matrix to print
     */
    private void printScoreboard(String[][] scoreboard){

        for (int i = 0; i < 41; i++){
            String string = "";
            for (int j = 0; j < 26; j++){
                string += scoreboard[i][j];
            }
            System.out.println(string);
        }

    }

    /**
     * Checks if a Card is present in a specific set of Coordinates (Model) inside an ArrayList<Card>,
     * such as a Player's PlayField.
     * @param x the x Coordinate to check
     * @param y the y Coordinate to check
     * @param cards the PlayField to check into
     * @return a boolean value indicating if those Coordinates belong to any Card or not.
     */
    private boolean isThereACardIn(int x, int y, ArrayList<PlayableCard> cards) {
        for (PlayableCard card : cards) {
            if (card.getX() == x && card.getY() == y) {
                return true;
            }
        }
        return false;
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
     * Creates and builds a formatted String containing a visual representation of one of the Lines that make up an Objective Card.
     * Like Playable Cards, Objective Cards are also made of 5 Lines, and the one to return is passed as a parameter.
     * The String is formatted in ASCII art or with Emojis, based on the Graphics Mode selected.
     * @param card the Objective Card to print
     * @param line the Line to print
     * @return the formatted String
     */
    private String printObjectiveLine(ObjectiveCard card, int line) {
        String string = "";
        switch (card.getId()) {
            case 87:
                switch (line) {
                    case 1, 5:
                        // 🟥🟨🟨🟨🟨🟨🟨🟨🟥
                        string = terminalCharacters.getCharacter(Characters.RED_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.RED_SQUARE);
                        break;
                    case 2:
                        // 🟨⚪⚪ 2⚪🟥⚪⚪🟨
                        string = terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                " 2" +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.RED_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE);
                        break;
                    case 3:
                        // 🟨⚪⚪⚪🟥⚪⚪⚪🟨
                        string = terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.RED_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE);
                        break;
                    case 4:
                        // 🟨⚪⚪🟥⚪⚪⚪⚪🟨
                        string = terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.RED_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.RED_SQUARE);
                        break;
                }
                break;
            case 88:
                switch (line){
                    case 1, 5:
                        // 🟩🟨🟨🟨🟨🟨🟨🟨🟩
                        string = terminalCharacters.getCharacter(Characters.GREEN_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.GREEN_SQUARE);
                        break;
                    case 2:
                        // 🟨⚪⚪🟩⚪ 2⚪⚪🟨
                        string = terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.GREEN_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                " 2" +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE);
                        break;
                    case 3:
                        // 🟨⚪⚪⚪🟩⚪⚪⚪🟨
                        string = terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.GREEN_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE);
                        break;
                    case 4:
                        // 🟨⚪⚪⚪⚪🟩⚪⚪🟨
                        string = terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.GREEN_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE);
                        break;

                }
                break;
            case 89:
                switch (line){
                    case 1, 5:
                        // 🟦🟨🟨🟨🟨🟨🟨🟨🟦
                        string = terminalCharacters.getCharacter(Characters.BLUE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.BLUE_SQUARE);
                        break;
                    case 2:
                        // 🟨⚪⚪ 2⚪🟦⚪⚪🟨
                        string = terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                " 2" +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.BLUE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE);
                        break;
                    case 3:
                        // 🟨⚪⚪⚪🟦⚪⚪⚪🟨
                        string = terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.BLUE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE);
                        break;
                    case 4:
                        // 🟨⚪⚪🟦⚪⚪⚪⚪🟨
                        string = terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.BLUE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE);
                        break;
                }
                break;
            case 90:
                switch (line){
                    case 1, 5:
                        // 🟪🟨🟨🟨🟨🟨🟨🟨🟪
                        string = terminalCharacters.getCharacter(Characters.PURPLE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.PURPLE_SQUARE);
                        break;
                    case 2:
                        // 🟨⚪⚪🟪⚪ 2⚪⚪🟨
                        string = terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.PURPLE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                " 2" +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE);
                        break;
                    case 3:
                        // 🟨⚪⚪⚪🟪⚪⚪⚪🟨
                        string = terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.PURPLE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE);
                        break;
                    case 4:
                        // 🟨⚪⚪⚪⚪🟪⚪⚪🟨
                        string = terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.PURPLE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE);
                        break;
                }
                break;
            case 91:
                switch (line){
                    case 1, 5:
                        // 🟥🟨🟨🟨🟨🟨🟨🟨🟥
                        string = terminalCharacters.getCharacter(Characters.RED_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.RED_SQUARE);
                        break;
                    case 2:
                        // 🟨⚪⚪🟥⚪ 3⚪⚪🟨
                        string = terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.RED_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                " 3" +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE);
                        break;
                    case 3:
                        // 🟨⚪⚪🟥⚪⚪⚪⚪🟨
                        string = terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.RED_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE);
                        break;
                    case 4:
                        // 🟨⚪⚪⚪🟩⚪⚪⚪🟨
                        string = terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.GREEN_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE);
                        break;
                }
                break;
            case 92:
                switch (line){
                    case 1, 5:
                        // 🟩🟨🟨🟨🟨🟨🟨🟨🟩
                        string = terminalCharacters.getCharacter(Characters.GREEN_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.GREEN_SQUARE);
                        break;
                    case 2:
                        // 🟨⚪⚪ 3⚪🟩⚪⚪🟨
                        string = terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                " 3" +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.GREEN_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE);
                        break;
                    case 3:
                        // 🟨⚪⚪⚪⚪🟩⚪⚪🟨
                        string = terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.GREEN_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE);
                        break;
                    case 4:
                        // 🟨⚪⚪⚪🟪⚪⚪⚪🟨
                        string = terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.PURPLE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE);
                        break;
                }
                break;
            case 93:
                switch (line){
                    case 1, 5:
                        // 🟦🟨🟨🟨🟨🟨🟨🟨🟦
                        string = terminalCharacters.getCharacter(Characters.BLUE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.BLUE_SQUARE);
                        break;
                    case 2:
                        // 🟨⚪⚪⚪🟥⚪⚪⚪🟨
                        string = terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.RED_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.RED_SQUARE);
                        break;
                    case 3:
                        // 🟨⚪⚪🟦⚪⚪⚪⚪🟨
                        string = terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.BLUE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE);
                        break;
                    case 4:
                        // 🟨⚪⚪🟦⚪ 3⚪⚪🟨
                        string = terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.BLUE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                " 3" +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE);
                        break;
                }
                break;
            case 94:
                switch (line){
                    case 1,5:
                        // 🟪🟨🟨🟨🟨🟨🟨🟨🟪
                        string = terminalCharacters.getCharacter(Characters.PURPLE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.PURPLE_SQUARE);
                        break;
                    case 2:
                        // 🟨⚪⚪⚪🟦⚪⚪⚪🟨
                        string = terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.BLUE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE);
                        break;
                    case 3:
                        // 🟨⚪⚪⚪⚪🟪⚪⚪🟨
                        string = terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.PURPLE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE);
                        break;
                    case 4:
                        // 🟨⚪⚪ 3⚪🟪⚪⚪🟨
                        string = terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                " 3" +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.PURPLE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE);
                        break;
                }
                break;
            case 95:
                switch (line){
                    case 1, 5:
                        // 🟥🟨🟨🟨🟨🟨🟨🟨🟥
                        string = terminalCharacters.getCharacter(Characters.RED_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.RED_SQUARE);
                        break;
                    case 2:
                        // 🟨⚪⚪⚪ 2⚪⚪⚪🟨
                        string = terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                " 2" +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE);
                        break;
                    case 3:
                        // 🟨⚪⚪⚪🍄⚪⚪⚪🟨
                        string = terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.FUNGI) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE);
                        break;
                    case 4:
                        // 🟨⚪⚪🍄⚪🍄⚪⚪🟨
                        string = terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.FUNGI) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.FUNGI) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE);
                        break;
                }
                break;
            case 96:
                switch (line){
                    case 1, 5:
                        // 🟩🟨🟨🟨🟨🟨🟨🟨🟩
                        string = terminalCharacters.getCharacter(Characters.GREEN_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.GREEN_SQUARE);
                        break;
                    case 2:
                        // 🟨⚪⚪⚪ 2⚪⚪⚪🟨
                        string = terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                " 2" +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE);
                        break;
                    case 3:
                        // 🟨⚪⚪⚪🌳⚪⚪⚪🟨
                        string = terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.PLANT) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE);
                        break;
                    case 4:
                        // 🟨⚪⚪🌳⚪🌳⚪⚪🟨
                        string = terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.PLANT) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.PLANT) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE);
                        break;
                }
                break;
            case 97:
                switch (line){
                    case 1, 5:
                        // 🟦🟨🟨🟨🟨🟨🟨🟨🟦
                        string = terminalCharacters.getCharacter(Characters.BLUE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.BLUE_SQUARE);
                        break;
                    case 2:
                        // 🟨⚪⚪⚪ 2⚪⚪⚪🟨
                        string = terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                " 2" +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE);
                        break;
                    case 3:
                        // 🟨⚪⚪⚪🐺⚪⚪⚪🟨
                        string = terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.ANIMAL) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE);
                        break;
                    case 4:
                        // 🟨⚪⚪🐺⚪🐺⚪⚪🟨
                        string = terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.ANIMAL) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.ANIMAL) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE);
                        break;
                }
                break;
            case 98:
                switch (line){
                    case 1, 5:
                        // 🟪🟨🟨🟨🟨🟨🟨🟨🟪
                        string = terminalCharacters.getCharacter(Characters.PURPLE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.PURPLE_SQUARE);
                        break;
                    case 2:
                        // 🟨⚪⚪⚪ 2⚪⚪⚪🟨
                        string = terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                " 2" +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE);
                        break;
                    case 3:
                        // 🟨⚪⚪⚪🦋⚪⚪⚪🟨
                        string = terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.INSECT) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE);
                        break;
                    case 4:
                        // 🟨⚪⚪🦋⚪🦋⚪⚪🟨
                        string = terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.INSECT) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.INSECT) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE);
                        break;
                }
                break;
            case 99:
                switch (line){
                    case 1, 5:
                        // 🟨🟨🟨🟨🟨🟨🟨🟨🟨
                        string = terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE);
                        break;
                    case 2:
                        // 🟨⚪⚪⚪ 3⚪⚪⚪🟨
                        string = terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                " 3" +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE);
                        break;
                    case 3:
                        // 🟨⚪⚪📜🍷🪶⚪⚪🟨
                        string = terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.SCROLL) +
                                terminalCharacters.getCharacter(Characters.POTION) +
                                terminalCharacters.getCharacter(Characters.FEATHER) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE);
                        break;
                    case 4:
                        // 🟨⚪⚪⚪⚪⚪⚪⚪🟨
                        string = terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE);
                        break;
                }
                break;
            case 100:
                switch (line){
                    case 1, 5:
                        // 🟨🟨🟨🟨🟨🟨🟨🟨🟨
                        string = terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE);
                        break;
                    case 2:
                        // 🟨⚪⚪⚪ 2⚪⚪⚪🟨
                        string = terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                " 2" +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE);

                        break;
                    case 3:
                        // 🟨⚪⚪📜⚪📜⚪⚪🟨
                        string = terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.SCROLL) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.SCROLL) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE);
                        break;
                    case 4:
                        // 🟨⚪⚪⚪⚪⚪⚪⚪🟨
                        string = terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE);
                        break;
                }
                break;
            case 101:
                switch (line){
                    case 1, 5:
                        // 🟨🟨🟨🟨🟨🟨🟨🟨🟨
                        string = terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE);
                        break;
                    case 2:
                        // 🟨⚪⚪⚪ 2⚪⚪⚪🟨
                        string = terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                " 2" +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE);
                        break;
                    case 3:
                        // 🟨⚪⚪🍷⚪🍷⚪⚪🟨
                        string = terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.POTION) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.POTION) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE);
                        break;
                    case 4:
                        // 🟨⚪⚪⚪⚪⚪⚪⚪🟨
                        string = terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE);
                        break;
                }
                break;
            case 102:
                switch (line){
                    case 1, 5:
                        // 🟨🟨🟨🟨🟨🟨🟨🟨🟨
                        string = terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE);
                        break;
                    case 2:
                        // 🟨⚪⚪⚪ 2⚪⚪⚪🟨
                        string = terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                " 2" +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE);
                        break;
                    case 3:
                        // 🟨⚪⚪🪶⚪🪶⚪⚪🟨
                        string = terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.FEATHER) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.FEATHER) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE);
                        break;
                    case 4:
                        // 🟨⚪⚪⚪⚪⚪⚪⚪🟨
                        string = terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE);
                        break;
                }
                break;
        }
        return string;

    }

    /**
     * Prints an entire Objective Card, line by line.
     * @param card the Objective Card to print
     */
    private void printSecretObjective(ObjectiveCard card){
        for (int line = 1; line < 6; line++) {
            System.out.println(printObjectiveLine(card, line));
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
            System.out.println(printObjectiveLine(card1, line) +
                    "\t"+printObjectiveLine(card2, line) );
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
            System.out.println(getPrintCardLine(card1, line, isColorTerminalSupported) +
                    "\t" + getPrintCardLine(card2, line, isColorTerminalSupported) +
                    "\t" + getPrintCardLine(card3, line, isColorTerminalSupported));
        }
    }

    /**
     * Notifies the User that this is the Last Turn before the end of the Game
     * @throws RemoteException in case of a connection error.
     */
    @Override
    public void notifyLastTurn() throws RemoteException {
        // TODO: Implement
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

    private void rebuild() {
        int number = controller.getNumberOfPlayers();
        for (int i = 0; i < number; i++) {
            ArrayList<PlayableCard> playArea = controller.getPlayersPlayfield(i+1);
            for (int j = 0; j < playArea.size(); j++) {
                if (null != playAreas.get(i)) {
                    playAreas.set(i, updateCardMatrix(playAreas.get(i), playArea.get(j), playArea, i + 1));
                } else {
                    playAreas.set(i, createCardMatrix((StarterCard) playArea.getFirst(), i + 1));
                }
            }
        }
        printPlayer();
    }

    private void availablePlacement(){
        String[][] matrix = new String[800][1440];
        for (int i = 0; i < 800; i++){
            for (int j = 0; j < 1440; j++){
                matrix[i][j] = playAreas.get(playerID - 1)[i][j];
            }
        }
        ArrayList<Coordinates> availablePlacemnts = controller.getAvailablePlacements(playerID);
        HashMap<String, Integer> printingExtremesSave = (HashMap<String, Integer>) printingExtremes.get(playerID - 1).clone();
        for (Coordinates c: availablePlacemnts){
            ResourceCard card = new ResourceCard(null,null,true,-1,c.getX(),c.getY(),new ArrayList<>(),0,"/cards/card1Front.png","/cards/card1Back.png",null);
            matrix = updateCardMatrix(matrix, card, controller.getPlayersPlayfield(playerID),playerID);
        }
        printPlayArea(matrix, playerID);
        printingExtremes.set(playerID - 1, (HashMap<String, Integer>) printingExtremesSave.clone());
    }
}
