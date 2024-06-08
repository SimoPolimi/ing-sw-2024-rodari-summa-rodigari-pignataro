package it.polimi.ingsw.gc42.view;

import it.polimi.ingsw.gc42.controller.GameStatus;
import it.polimi.ingsw.gc42.model.classes.cards.*;
import it.polimi.ingsw.gc42.model.classes.game.*;
import it.polimi.ingsw.gc42.network.*;
import it.polimi.ingsw.gc42.network.interfaces.NetworkController;
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

public class GameTerminal extends Application implements ViewController {
    private boolean exit = false;
    private NetworkController controller;
    private Player player;
    private Scanner scanner = new Scanner(System.in);
    private int playerID;
    private boolean isYourTurn = false;
    private int extremeDX;
    private int extremeSX;
    private int extremeUP;
    private int extremeDOWN;

    private boolean isShowingGameCreationScreen = false;
    private boolean isWaiting = false;
    private boolean isGameCreator = false;

    private final BlockingDeque<Runnable> actions = new LinkedBlockingDeque<>();
    private TerminalInputHandler inputHandler;

    private ArrayList<ChatMessage> chat;

    private String color(String str, UiColors fg) {
        return fg.toString() + str + UiColors.RESET;
    }

    private String color(String str, UiColors fg, UiColors bg) {
        return fg.toString() + bg.toString() + str + UiColors.RESET;
    }

    public void addToActionQueue(Runnable runnable) {
        actions.add(runnable);
    }

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

        ExecutorService pool = Executors.newCachedThreadPool();
        inputHandler = new TerminalInputHandler(scanner);
        pool.submit(inputHandler);
        actions.add(() -> {
            System.out.println("Welcome to Codex Naturalis!");
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

    private void createNewGame() throws RemoteException, AlreadyBoundException {
        boolean exit = false;
        while (!exit) {
            System.out.println("No games available! Press c to create a new one!");
            Scanner scanner = new Scanner(System.in);
            String input = scanner.next();
            if (input.equals("c")) {
                controller.getNewGameController();
                setViewController();
                controller.addPlayer(player);
                playerID = controller.getIndexOfPlayer(player.getNickname());
                controller.setCurrentStatus(GameStatus.WAITING_FOR_PLAYERS);
                isGameCreator = true;
                exit = true;
            } else {
                System.out.println("Invalid input!");
            }
        }

        exit = false;
        while (!exit) {
            System.out.println("Insert a name for this game:");
            String input = scanner.next();
            if (!input.isEmpty()) {
                controller.setName(input);
                exit = true;
            }
        }

        System.out.println("Game created. Waiting for other players to join");
        chat = new ArrayList<>(controller.getFullChat());
        isShowingGameCreationScreen = true;
    }

    private void setViewController() {
        try {
            controller.setViewController(new QueuedClientController(this));
        } catch (AlreadyBoundException | RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    private void play() throws IOException, InterruptedException {
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
                        playCard(scanner.next());
                        break;
                    case "2":
                        System.out.println("Select the number of the card you want to flip (0 to cancel)");
                        flipCard(scanner.next());
                        printHandCards();
                        returnToMenu();
                        break;
                    case "3":
                        System.out.println(controller.getSecretObjective(playerID).getObjective().getName());
                        //printSecretObjective(controller.getSecret);
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
                        printCommonObjective();
                        System.out.println();
                        returnToMenu();
                        break;
                    case "9":
                        printScoreboard(createScoreboard());
                        returnToMenu();
                        break;
                    case "31":
                        /*ArrayList<PlayableCard> cards = new ArrayList<>();
                        PlayableCard card = (PlayableCard) controller.getGame().getStarterDeck().draw();
                        card.setX(0);
                        card.setY(0);
                        card.flip();
                        card.getShowingSide().getTopLeftCorner().setCovered(true);
                        card.getShowingSide().getBottomRightCorner().setCovered(true);
                        card.getShowingSide().getBottomLeftCorner().setCovered(true);
                        card.getShowingSide().getBottomRightCorner().setCovered(true);
                        cards.add(card);
                        card = (PlayableCard) controller.getGame().getResourcePlayingDeck().getDeck().draw();
                        card.setX(1);
                        card.setY(0);
                        card.flip();
                        card.getShowingSide().getBottomLeftCorner().setCovered(true);
                        cards.add(card);
                        card = (PlayableCard) controller.getGame().getResourcePlayingDeck().getDeck().draw();
                        card.setX(0);
                        card.setY(1);
                        card.flip();
                        card.getShowingSide().getBottomRightCorner().setCovered(true);
                        cards.add(card);
                        card = (PlayableCard) controller.getGame().getResourcePlayingDeck().getDeck().draw();
                        card.setX(-1);
                        card.setY(0);
                        card.flip();
                        card.getShowingSide().getTopRightCorner().setCovered(true);
                        cards.add(card);
                        card = (PlayableCard) controller.getGame().getResourcePlayingDeck().getDeck().draw();
                        card.setX(0);
                        card.setY(-1);
                        card.flip();
                        card.getShowingSide().getTopLeftCorner().setCovered(true);
                        cards.add(card);
                        String[][] matrix = createCardMatrix((StarterCard)cards.get(0));
                        matrix = updateCardMatrix(matrix ,cards.get(1), cards);
                        matrix = updateCardMatrix(matrix, cards.get(2), cards);
                        matrix = updateCardMatrix(matrix, cards.get(3), cards);
                        matrix = updateCardMatrix(matrix, cards.get(4), cards);
                        printPlayArea(matrix);
                        //printCardMatrix(getMatrix(cards), cards);
                        //printPlayArea();*/
                        actions.add(() -> {
                            try {
                                play();
                            } catch (IOException | InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        });
                        break;
                    default:
                        System.out.println(color("Unknown command", UiColors.RED));
                        actions.add(() -> {
                            try {
                                play();
                            } catch (IOException | InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        });
                        break;
                }
            }
        });

        /*actions.add(() -> {
            try {
                play();
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        });*/
    }

    private void returnToMenu() {
        System.out.println("Digit 0 to return to menu");
        inputHandler.listen(new TerminalListener() {
            @Override
            public void onEvent(String input) {
                if (input.equals("0")) {
                    actions.add(() -> {
                        try {
                            play();
                        } catch (IOException | InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    });
                } else {
                    System.err.println("Invalid input!");
                    actions.add(() -> returnToMenu());
                }
            }
        });
    }

    private void printMenu() {
        System.out.println("Menu:");
        System.out.println("1) Select card to play");
        System.out.println("2) Flip card");
        System.out.println("3) Show objective's descriptions");
        System.out.println("4) Show chat");
        System.out.println("5) Digit 5 and write the message");
        System.out.println("6) Exit");
        // Test, will be deleted later
        System.out.println("7) Show ranking");
        System.out.println("8) Digit to show common objective");
        System.out.println("9) Digit to show scoreboard");
        System.out.println("31) Print PlayArea2 [test]");
        System.out.println("Digit a number to select the action.");
        System.out.println();
    }

    private void printPlayer() {
        System.out.println("Player n° " + controller.getPlayerTurn());
        ArrayList<HashMap<String, String>> info = controller.getPlayersInfo();
        System.out.println("Nickname: " + info.get(playerID-1).get("Nickname"));
        String string = "  ";
        Token token = controller.getPlayerToken(playerID);
        if (null != token) {
            switch (token) {
                case Token.RED -> string = "🔴";
                case Token.BLUE -> string = "🔵";
                case Token.GREEN -> string = "🟢";
                case Token.YELLOW -> string = "🟡";
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

    private void playCard(String input) {
        switch (input) {
            case "1", "2", "3" -> {
                // Play card
                boolean valid = false;
                // loops until the input is accepted
                while (!valid) {
                    int i = 1;
                    ArrayList<Coordinates> availablePlacements = controller.getAvailablePlacements(playerID);
                    for (Coordinates coord : availablePlacements) {
                        System.out.println(i + ") " + coord.getX() + " " + coord.getY());
                        //stampare la starter card e poi stampare gli spazio in cui possiamo aggiungere la nuova carta
                        printCard(controller.getPlayersLastPlayedCard(playerID));
                        i++;
                    }
                    // TODO: Handle str to int conversion exceptions or use nextInt() (there are still exceptions to be handled in this case)
                    String inputCoord = scanner.next();
                    if (Integer.valueOf(inputCoord) < 1 || Integer.valueOf(inputCoord) > availablePlacements.size()) {
                        System.out.println(color("Invalid coordinate choice! Retry...", UiColors.RED));
                    } else {
                        valid = true;
                        //TODO: Fix
                        controller.playCard(1, availablePlacements.get(Integer.valueOf(inputCoord) - 1).getX(), availablePlacements.get(Integer.valueOf(inputCoord) - 1).getY());
                    }
                }
            }
            case "0" -> {
            } // do nothing and exit
            default -> System.out.println(color("Invalid choice!", UiColors.RED));
        }
    }

    private void flipCard(String input) {

    }

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
                        break;
                }
            }
        });
    }

    @Override
    public void showTokenSelectionDialog() {
        System.out.println("--- Select your token ---");
        System.out.println("Digit 1 to choose: 🔴");
        System.out.println("Digit 2 to choose: 🔵");
        System.out.println("Digit 3 to choose: 🟢");
        System.out.println("Digit 4 to choose: 🟡");
        String input = "";
        inputHandler.listen(new TerminalListener() {
            @Override
            public void onEvent(String input) {
                switch (input) {
                    case "1":
                        System.out.println("You chose 🔴");
                        controller.setPlayerToken(playerID, Token.RED);
                        inputHandler.unlisten(this);
                        break;
                    case "2":
                        System.out.println("You chose 🔵");
                        controller.setPlayerToken(playerID, Token.BLUE);
                        inputHandler.unlisten(this);
                        break;
                    case "3":
                        System.out.println("You chose 🟢");
                        controller.setPlayerToken(playerID, Token.GREEN);
                        inputHandler.unlisten(this);
                        break;
                    case "4":
                        System.out.println("You chose 🟡");
                        controller.setPlayerToken(playerID, Token.YELLOW);
                        inputHandler.unlisten(this);
                        break;
                    default:
                        System.out.println(color("Invalid choice! Retry...", UiColors.RED));
                        break;
                }
            }
        });
    }

    @Override
    public int getOwner() {
        return playerID;
    }

    @Override
    public void askToDrawOrGrab() {
        Card card1, card2;
        card1 = controller.getDeck(CardType.RESOURCECARD).getFirst();
        card2 = controller.getDeck(CardType.GOLDCARD).getFirst();
        for (int line = 1; line < 6; line++) {
            System.out.println(getPrintCardLine((PlayableCard) card1, line, true, null) +
                    "\t" + (getPrintCardLine((PlayableCard) card2, line, true, null)));
        }
        System.out.println();
        for (int slot = 1; slot < 3; slot++) {
            card1 = controller.getSlotCard(CardType.RESOURCECARD, slot);
            card2 = controller.getSlotCard(CardType.GOLDCARD, slot);
            for (int line = 1; line < 6; line++) {
                System.out.println(getPrintCardLine((PlayableCard) card1, line, true, null) +
                        "\t" + (getPrintCardLine((PlayableCard) card2, line, true, null)));
            }
            System.out.println();
        }
        System.out.println("Digit gr to grab a card from resource deck");
        System.out.println("Digit gg to grab a card from gold deck");
        System.out.println("Digit 1r to choose first resource card");
        System.out.println("Digit 1g to choose first gold card");
        System.out.println("Digit 2r to choose second resource card");
        System.out.println("Digit 2g to choose second gold card");
    }

    @Override
    public void notifyGameIsStarting() {

    }

    @Override
    public void notifyDeckChanged(CardType type) {
        //Unnecessary
    }

    @Override
    public void notifySlotCardChanged(CardType type, int slot) {
        //Unnecessary
    }

    @Override
    public void notifyPlayersPointsChanged(Token token, int newPoints) {
        showRanking();
    }

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

    @Override
    public void notifyPlayersTokenChanged(int playerID) {
        if (this.playerID != playerID) {
            // Only prints the other Players' Tokens
            Token token = controller.getPlayerToken(playerID);
            if (null != token) {
                ArrayList<HashMap<String, String>> info = controller.getPlayersInfo();
                switch (token) {
                    case Token.RED -> System.out.println(info.get(playerID - 1).get("Nickname") + " choose: 🔴");
                    case Token.BLUE -> System.out.println(info.get(playerID - 1).get("Nickname") + " choose: 🔵");
                    case Token.GREEN -> System.out.println(info.get(playerID - 1).get("Nickname") + " choose: 🟢");
                    case Token.YELLOW -> System.out.println(info.get(playerID - 1).get("Nickname") + " choose: 🟡");
                }
            }
        }
    }

    @Override
    public void notifyPlayersPlayAreaChanged(int playerID) {
        if (playerID == this.playerID) {
            //printPlayArea();
        }
    }

    @Override
    public void notifyPlayersHandChanged(int playerID) {
        if (this.playerID == playerID) {
            // Only shows the User's one (the others should be secret!)
            int handSize = controller.getPlayersHandSize(playerID);
            if (handSize == 3) {
                System.out.println("Your Hand:");
                // Only shows the updated Hand in meaningful situations
               printHandCards();
            }
        }
    }

    @Override
    public void notifyHandCardWasFlipped(int playedID, int cardID) {
        if (playedID == this.playerID) {
            for (int i = 0; i < controller.getPlayersHandSize(playerID); i++) {
                printCard(controller.getPlayersHandCard(playerID, i));
            }
        }
    }

    @Override
    public void notifyPlayersObjectiveChanged(int playerID) {
        if (this.playerID == playerID) {
            // Only shows the User's one, not the others (it should be secret!)
            System.out.println("Objective chosen: " + controller.getSecretObjective(playerID).getObjective().getName());
        }
    }

    @Override
    public void notifyCommonObjectivesChanged() {
        for (int i = 0; i < 2; i++) {
            printCard( (PlayableCard) controller.getSlotCard(CardType.OBJECTIVECARD, i));
        }
    }

    @Override
    public void notifyTurnChanged() {
        if (playerID == controller.getPlayerTurn()) {
            this.isYourTurn = true;
            actions.add(() -> {
                try {
                    play();
                } catch (IOException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        } else {
            this.isYourTurn = false;
        }

    }

    @Override
    public void showWaitingForServerDialog() {
        System.out.println("Server Waiting...");
    }

    @Override
    public void getReady(int numberOfPlayers) {
        if (isGameCreator) {
            controller.setCurrentStatus(GameStatus.READY);
        }
        controller.setPlayerStatus(playerID, GameStatus.READY);
        System.out.println("Getting ready...");
    }

    private String getPrintCardLine(PlayableCard card, int line, boolean printCoveredCorners, ArrayList<PlayableCard> cards) {
        String string = "";
        if (null != card) {
            switch (line) {
                case 1 -> {
                    if (printCoveredCorners) {
                        string = getCornerPrint(card, card.getShowingSide().getTopLeftCorner());
                        for (int i = 0; i < 7; i++) {
                            if ((i == 0 || i == 6) && card instanceof GoldCard) {
                                string += "🟨";
                            } else {
                                string += getCardColor(card);
                            }
                        }
                        string += getCornerPrint(card, card.getShowingSide().getTopRightCorner());
                    } else {
                        if (!isThereACardIn(card.getX(), card.getY() + 1, cards)) {
                            string += getCornerPrint(card, card.getShowingSide().getTopLeftCorner());
                        } else if (null == card.getShowingSide().getTopLeftCorner()) {
                            // If there is no Corner, then it must be covering the other card's corner
                            string += getCornerPrint(card, card.getShowingSide().getTopLeftCorner());
                        }
                        for (int i = 0; i < 7; i++) {
                            string += getCardColor(card);
                        }
                        if (!isThereACardIn(card.getX() + 1, card.getY(), cards)) {
                            string += getCornerPrint(card, card.getShowingSide().getTopRightCorner());
                        } else if (null == card.getShowingSide().getTopRightCorner()) {
                            // If there is no Corner, then it must be covering the other card's corner
                            string += getCornerPrint(card, card.getShowingSide().getTopRightCorner());
                        }
                    }
                }
                case 2, 4 -> {
                    if (card instanceof GoldCard) {
                        string += "🟨";
                        for (int i = 0; i < 7; i++) {
                            string += getCardColor(card);
                        }
                        string += "🟨";
                    } else {
                        for (int i = 0; i < 9; i++) {
                            string += getCardColor(card);
                        }
                    }
                }
                case 3 -> {
                    for (int i = 0; i < 3; i++) {
                        string += getCardColor(card);
                    }
                    if ((!(card instanceof StarterCard) && !card.isFrontFacing()) || (card.isFrontFacing() && card instanceof StarterCard)) {
                        switch (card.getPermanentResources().size()) {
                            case 1 -> {
                                string += getCardColor(card) + getItemPrint(card.getPermanentResources().get(0)) + getCardColor(card);
                            }
                            case 2 -> {
                                string += getItemPrint(card.getPermanentResources().get(1)) + getCardColor(card)
                                        + getItemPrint(card.getPermanentResources().get(1));
                            }
                            case 3 -> {
                                string += getItemPrint(card.getPermanentResources().get(1))
                                        + getItemPrint(card.getPermanentResources().get(1))
                                        + getItemPrint(card.getPermanentResources().get(2));
                            }
                            default -> {
                                for (int i = 0; i < 3; i++) {
                                    string += getCardColor(card);
                                }
                            }
                        }
                    } else {
                        for (int i = 0; i < 3; i++) {
                            string += getCardColor(card);
                        }
                    }
                    for (int i = 0; i < 3; i++) {
                        string += getCardColor(card);
                    }
                }
                case 5 -> {
                    if (printCoveredCorners) {
                        string = getCornerPrint(card, card.getShowingSide().getBottomLeftCorner());
                        for (int i = 0; i < 7; i++) {
                            if ((i == 0 || i == 6) && card instanceof GoldCard) {
                                string += "🟨";
                            } else {
                                string += getCardColor(card);
                            }
                        }
                        string += getCornerPrint(card, card.getShowingSide().getBottomRightCorner());
                    } else {
                        if (!isThereACardIn(card.getX(), card.getY() - 1, cards)) {
                            string += getCornerPrint(card, card.getShowingSide().getBottomLeftCorner());
                        } else if (null == card.getShowingSide().getBottomLeftCorner()) {
                            // If there is no Corner, then it must be covering the other card's corner
                            string += getCornerPrint(card, card.getShowingSide().getBottomLeftCorner());
                        }
                        for (int i = 0; i < 7; i++) {
                            string += getCardColor(card);
                        }
                        if (!isThereACardIn(card.getX() - 1, card.getY(), cards)) {
                            string += getCornerPrint(card, card.getShowingSide().getBottomRightCorner());
                        } else if (null == card.getShowingSide().getBottomRightCorner()) {
                            // If there is no Corner, then it must be covering the other card's corner
                            string += getCornerPrint(card, card.getShowingSide().getBottomRightCorner());
                        }
                    }
                }
            }

            // TODO: Backup just in case
            /*switch (line) {
                case 1 -> {
                    if (printCoveredCorners) {
                        string = getCornerPrint(card.getShowingSide().getTopLeftCorner());
                        if (getCornerPrint(card.getShowingSide().getTopLeftCorner()).equals("◻") || getCornerPrint(card.getShowingSide().getTopLeftCorner()).equals("X")) {
                            string += "―";
                        }
                        string += "――――――";
                        if (getCornerPrint(card.getShowingSide().getTopRightCorner()).equals("X")) {
                            string += " ";
                        }
                        string += getCornerPrint(card.getShowingSide().getTopRightCorner());
                    } else {
                        if (null != card.getShowingSide().getTopLeftCorner() && !card.getShowingSide().getTopLeftCorner().isCovered()) {
                            string = getCornerPrint(card.getShowingSide().getTopLeftCorner());
                        } else string += "◻-";
                        string += "--------";
                        if (null != card.getShowingSide().getTopRightCorner() && !card.getShowingSide().getTopRightCorner().isCovered()) {
                            string += getCornerPrint(card.getShowingSide().getTopRightCorner());
                        } else string+= "-◻";
                    }
                }
                case 2, 4 -> {
                    string = "│            │";
                }
                case 3 -> {
                    if (!card.isFrontFacing() || (card.isFrontFacing() && card instanceof StarterCard)) {
                        string = "│  ";
                        switch (card.getPermanentResources().size()) {
                            case 1 -> {
                                string += "    " + getItemPrint(card.getPermanentResources().get(0)) + "   ";
                            }
                            case 2 -> {
                                string += "   " + getItemPrint(card.getPermanentResources().get(1))
                                        + getItemPrint(card.getPermanentResources().get(1)) + "   ";
                            }
                            case 3 -> {
                                string += "  " + getItemPrint(card.getPermanentResources().get(1))
                                        + getItemPrint(card.getPermanentResources().get(1))
                                        + getItemPrint(card.getPermanentResources().get(2)) + "  ";
                            }
                            default -> {string += "         ";}
                        }
                        string += "  │";
                    } else string = "│            │";
                }
                case 5 -> {
                    if (printCoveredCorners) {
                        string = getCornerPrint(card.getShowingSide().getBottomLeftCorner());
                        if (getCornerPrint(card.getShowingSide().getBottomLeftCorner()).equals("◻") || getCornerPrint(card.getShowingSide().getBottomLeftCorner()).equals("X")) {
                            string += "―";
                        }
                        string += "――――――";
                        if (getCornerPrint(card.getShowingSide().getBottomRightCorner()).equals("X")) {
                            string += " ";
                        }
                        string += getCornerPrint(card.getShowingSide().getBottomRightCorner());
                    } else {
                        if (null != card.getShowingSide().getBottomLeftCorner() && !card.getShowingSide().getBottomLeftCorner().isCovered()) {
                            string = getCornerPrint(card.getShowingSide().getBottomLeftCorner());
                        } else string += "◻-";
                        string += "--------";
                        if (null != card.getShowingSide().getBottomRightCorner() &&!card.getShowingSide().getBottomRightCorner().isCovered()) {
                            string += getCornerPrint(card.getShowingSide().getBottomRightCorner());
                        } else string+= "-◻";
                    }
                }
            }*/
        } else {
            if (printCoveredCorners) {
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

    private String getCornerPrint(PlayableCard card, Corner corner) {
        String string = "";
        if (null != corner) {
            if (null != corner.getItem()) {
                string = getItemPrint(corner.getItem());
            } else {
                //string = "◻";
                string = "⚪";
            }
        } else string = getCardColor(card);
        return string;
    }

    private String getItemPrint(Item item) {
        String string = "  ";
        switch (item) {
            //Without Emoji
                    /*case KingdomResource.FUNGI -> string = "ନ";
                    case KingdomResource.ANIMAL -> string = "♘";
                    case KingdomResource.INSECT -> string = "¥";
                    case KingdomResource.PLANT -> string = "⚜";
                    case Resource.FEATHER -> string = "ϡ";
                    case Resource.POTION -> string = "Ỗ";
                    case Resource.SCROLL -> string = "";
                    default -> {}*/
            // With Emoji
            case KingdomResource.FUNGI -> string = "🍄";
            case KingdomResource.ANIMAL -> string = "🐺";
            case KingdomResource.INSECT -> string = "🦋";
            case KingdomResource.PLANT -> string = "🌳";
            case Resource.FEATHER -> string = "🪶";
            case Resource.POTION -> string = "🍷";
            case Resource.SCROLL -> string = "📜";
            default -> {
                string = "  ";
            }
        }
        return string;
    }

    public void printCard(PlayableCard card) {
        System.out.println(getPrintCardLine(card, 1, true, null));
        System.out.println(getPrintCardLine(card, 2, true, null));
        System.out.println(getPrintCardLine(card, 3, true, null));
        System.out.println(getPrintCardLine(card, 4, true, null));
        System.out.println(getPrintCardLine(card, 5, true, null));
    }

    private String getCardColor(PlayableCard card) {
        String string = "";
        if (card instanceof ResourceCard || card instanceof GoldCard) {
            switch (card.getPermanentResources().getFirst()) {
                case KingdomResource.PLANT -> string = "🟩";
                case KingdomResource.ANIMAL -> string = "🟦";
                case KingdomResource.FUNGI -> string = "🟥";
                case KingdomResource.INSECT -> string = "🟪";
                default -> string = "";
            }
        } else if (card instanceof StarterCard) {
            string = "🟨";
        } else {
            string = "⚪";
        }
        return string;
    }

    private Coordinates convertToAbsoluteCoordinates(Coordinates coordinates) {
        int x = coordinates.getX() - coordinates.getY();
        int y = coordinates.getX() + coordinates.getY();
        return new Coordinates(x, y);
    }

    private Coordinates convertToMatrixCoordinates(Coordinates coordinates, int matrixSize) {
        int shift = (matrixSize - 1) / 2;
        int x = coordinates.getX() + shift;
        int y = shift - coordinates.getY();
        coordinates.setX(x);
        coordinates.setY(y);
        return coordinates;
    }
    private Coordinates convertMatrixCoordinates(Coordinates coordinates, int matrixSizeX, int matrixSizeY) {
        int shiftX = (matrixSizeX - 1) / 2;
        int shiftY = (matrixSizeY - 1) / 2;
        int x = coordinates.getX() + shiftX;
        int y = shiftY - coordinates.getY();
        coordinates.setX(x);
        coordinates.setY(y);
        return coordinates;
    }

    private String[][] createCardMatrix(StarterCard starter){
        String[][] playArea = new String[801][1441];
        String line = "";
        int firstColumn = 717;
        int firstLine = 399;
        for (int j = 1; j <= 5; j++) {
            line = getPrintCardLine(starter,j, true,null);
            for (int i = 0; i < line.length(); i += 2) {
                String string = new String(String.valueOf(line.charAt(i)));
                string += line.charAt(i+1);
                playArea[firstLine][firstColumn] = string;
                firstColumn++;
            }
            firstLine++;
            firstColumn = 717;
        }
      for (int i = 0; i < 801; i++){
          for (int j = 0; j < 1441; j++){
              if (playArea[i][j] == null){
                  playArea[i][j] = "⬛";
              }
          }
      }
        //extreme DX
        extremeDX  = 725;
        //extreme SX
        extremeSX = 717;
        //extreme UP
        extremeUP = 399;
        //extreme DOWN
        extremeDOWN = 403;
        return playArea;
    }
    private String[][] addCardToMatrix(String[][] matrix, PlayableCard card, int firstLine, int firstColumn, int centerX){
        String line = "";
        for (int j = 1; j <= 5; j++) {
            line = getPrintCardLine(card,j, true,null);
            for (int i = 0; i < line.length(); i += 2) {
                String string = new String(String.valueOf(line.charAt(i)));
                if (string.equals("⚪")){
                    matrix[firstLine][firstColumn] = "⚪";
                    i--;
                    firstColumn++;
                }else {
                    string += line.charAt(i + 1);
                    matrix[firstLine][firstColumn] = string;
                    firstColumn++;
                }
            }
            firstLine++;
            firstColumn = centerX - 4;
        }
        return matrix;
    }
    private String[][] updateCardMatrix(String[][] matrix, PlayableCard card, ArrayList<PlayableCard> cards){
        Coordinates coordinates = convertMatrixCoordinates(convertToAbsoluteCoordinates(card.getCoordinates()), 801,1441);
        //card down sx
        if(isThereACardIn(card.getX()-1,card.getY(), cards)){
            int centerX = coordinates.getY() + 10;
            int centerY = coordinates.getX() - 4;
            int firstColumn = centerX - 4;
            int firstLine = centerY -2;
            matrix = addCardToMatrix(matrix,card,firstLine,firstColumn,centerX);
            extremeDX = centerX + 4;
            extremeUP = centerY - 2;
            return matrix;
        }
        //card up dx
        if(isThereACardIn(card.getX()+1,card.getY(), cards)){
            int centerX = coordinates.getY() - 8;
            int centerY = coordinates.getX() + 6;
            int firstColumn = centerX - 4;
            int firstLine = centerY - 2;
            matrix = addCardToMatrix(matrix,card,firstLine,firstColumn,centerX);
            extremeSX = centerX - 4;
            extremeDOWN = centerY + 2;
            return matrix;
        }
        //card up sx
        if(isThereACardIn(card.getX(),card.getY()-1, cards)){
            int centerX = coordinates.getY() + 10;
            int centerY = coordinates.getX() + 6;
            int firstColumn = centerX - 4;
            int firstLine = centerY -2;
            matrix = addCardToMatrix(matrix,card,firstLine,firstColumn,centerX);
            extremeDX = centerX + 4;
            extremeDOWN = centerY + 2;
            return matrix;
        }
        //card down dx
        if(isThereACardIn(card.getX(),card.getY()+1, cards)){
            int centerX = coordinates.getY() - 8;
            int centerY = coordinates.getX() - 4;
            int firstColumn = centerX - 4;
            int firstLine = centerY -2;
            matrix = addCardToMatrix(matrix,card,firstLine,firstColumn,centerX);
            extremeSX = centerX - 4;
            extremeUP = centerY - 2;
            return matrix;
        }
        return matrix;
    }
    private void printPlayArea(String[][] playArea){
        for (int i = extremeUP - 5; i <= extremeDOWN + 5; i++){
            String line = "";
            for (int j = extremeSX - 5; j <= extremeDX + 5; j++){
                line += playArea[i][j];
            }
            System.out.println(line);
        }
    }

    private PlayableCard[][] getMatrix(ArrayList<PlayableCard> playedCards) {
        int size = (2 * playedCards.size()) + 1;
        PlayableCard[][] matrix = new PlayableCard[size][size];
        // Puts all the played cards inside the Matrix
        for (PlayableCard card : playedCards) {
            Coordinates coord = convertToMatrixCoordinates(convertToAbsoluteCoordinates(card.getCoordinates()), size);
            matrix[coord.getX()][coord.getY()] = card;
        }
        return matrix;
    }

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
    private void printScoreboard(String[][] scoreboard){

        for (int i = 0; i < 41; i++){
            String string = "";
            for (int j = 0; j < 26; j++){
                string += scoreboard[i][j];
            }
            System.out.println(string);
        }

    }

    private void printCardMatrix(PlayableCard[][] matrix, ArrayList<PlayableCard> cards) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 1; j <= 5; j++) {
                // It takes 5 prints to completely print a Card
                String string = "";
                for (int k = 0; k < matrix[i].length; k++) {
                    /*if (i > 0 && i < matrix.length - 1) {
                        if (j == 1) {
                            if (null != matrix[i][k]) {
                                string += getPrintCardLine(matrix[i][k], 1, false, cards);
                            } else {
                                string += getPrintCardLine(matrix[i - 1][k], 5, false, cards);
                            }
                        } /*else if (j == 5) {
                            if (null == matrix[i][k] && null != matrix[i+1][k]) {
                                string += getPrintCardLine(matrix[i+1][k], 1, false, cards);
                            } else {
                                //string += getPrintCardLine(matrix[i][k], j, false, cards);
                            }
                        }*/ /*else {
                            if (k > 0 && null != matrix[i][k - 1]) {
                                string += " ";
                            }
                            string += " ";
                            if (k < matrix.length - 1 && null != matrix[i][k + 1]) {
                                string += " ";
                            }
                            string += getPrintCardLine(matrix[i][k], j, false, cards);
                        }*/

                        string += getPrintCardLine(matrix[i][k], j, true, cards);

                }
                System.out.println(string);
            }
        }
    }

    /*private void printPlayArea() {
        printCardMatrix(getMatrix(player.getPlayField().getPlayedCards()), player.getPlayField().getPlayedCards());
    }*/

    private boolean isThereACardIn(int x, int y, ArrayList<PlayableCard> cards) {
        for (PlayableCard card : cards) {
            if (card.getX() == x && card.getY() == y) {
                return true;
            }
        }
        return false;
    }

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

    private void askForNickName() {
        System.out.println("--- Insert Nickname ---");
        inputHandler.listen(new TerminalListener() {
            @Override
            public void onEvent(String input) {
                switch (input) {
                    case "":
                        break;
                    default:
                        player = new Player(input);
                        ArrayList<HashMap<String, String>> games = null;
                        try {
                            games = controller.getAvailableGames();
                        } catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }
                        if (games.isEmpty()) {
                            try {
                                createNewGame();
                            } catch (RemoteException | AlreadyBoundException e) {
                                throw new RuntimeException(e);
                            }
                            isWaiting = true;
                            // Setups the User's Input Handler
                        } else {
                            ArrayList<HashMap<String, String>> finalGames = games;
                            actions.add(() -> askToJoinGame(finalGames));
                        }
                }
            }
        });
    }

    private void askToJoinGame(ArrayList<HashMap<String, String>> games) {
        System.out.println("Games available:");
        for (HashMap<String, String> game : games) {
            int gameID = games.indexOf(game) + 1;
            System.out.println(gameID + ")" + " " + game.get("Name") + ", " + game.get("NumberOfPlayers") + " players, " + game.get("Status"));
        }
        System.out.println("\nPress c to create a new one");
        inputHandler.listen(new TerminalListener() {
            @Override
            public void onEvent(String input) {
                if (input.equals("c")) {
                    try {
                        createNewGame();
                    } catch (RemoteException | AlreadyBoundException e) {
                        throw new RuntimeException(e);
                    }
                    isWaiting = true;
                } else {
                    try {
                        int gameID = Integer.parseInt(input);
                        controller.pickGame(gameID - 1);
                        setViewController();
                        controller.addPlayer(player);
                        playerID = controller.getIndexOfPlayer(player.getNickname());
                        controller.setPlayerStatus(playerID, GameStatus.WAITING_FOR_SERVER);
                        System.out.println("Waiting...");
                        isWaiting = true;
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

    private String printObjectiveLine(ObjectiveCard card, int line) {
        String string = "";
        switch (card.getId()) {
            case 87:
                switch (line) {
                    case 1, 5:
                        string = "🟥🟨🟨🟨🟨🟨🟨🟨🟥";
                        break;
                    case 2:
                        string = "🟨⚪⚪ 2⚪🟥⚪⚪🟨";
                        break;
                    case 3:
                        string = "🟨⚪⚪⚪🟥⚪⚪⚪🟨";
                        break;
                    case 4:
                        string = "🟨⚪⚪🟥⚪⚪⚪⚪🟨";
                        break;
                }
                break;
            case 88:
                switch (line){
                    case 1, 5:
                        string = "🟩🟨🟨🟨🟨🟨🟨🟨🟩";
                        break;
                    case 2:
                        string = "🟨⚪⚪🟩⚪ 2⚪⚪🟨";
                        break;
                    case 3:
                        string = "🟨⚪⚪⚪🟩⚪⚪⚪🟨";
                        break;
                    case 4:
                        string = "🟨⚪⚪⚪⚪🟩⚪⚪🟨";
                        break;

                }
                break;
            case 89:
                switch (line){
                    case 1, 5:
                        string = "🟦🟨🟨🟨🟨🟨🟨🟨🟦";
                        break;
                    case 2:
                        string = "🟨⚪⚪ 2⚪🟦⚪⚪🟨";
                        break;
                    case 3:
                        string = "🟨⚪⚪⚪🟦⚪⚪⚪🟨";
                        break;
                    case 4:
                        string = "🟨⚪⚪🟦⚪⚪⚪⚪🟨";
                        break;
                }
                break;
            case 90:
                switch (line){
                    case 1, 5:
                        string = "🟪🟨🟨🟨🟨🟨🟨🟨🟪";
                        break;
                    case 2:
                        string = "🟨⚪⚪🟪⚪ 2⚪⚪🟨";
                        break;
                    case 3:
                        string = "🟨⚪⚪⚪🟪⚪⚪⚪🟨";
                        break;
                    case 4:
                        string = "🟨⚪⚪⚪⚪🟪⚪⚪🟨";
                        break;
                }
                break;
            case 91:
                switch (line){
                    case 1, 5:
                        string = "🟥🟨🟨🟨🟨🟨🟨🟨🟥";
                        break;
                    case 2:
                        string = "🟨⚪⚪🟥⚪ 3⚪⚪🟨";
                        break;
                    case 3:
                        string = "🟨⚪⚪🟥⚪⚪⚪⚪🟨";
                        break;
                    case 4:
                        string = "🟨⚪⚪⚪🟩⚪⚪⚪🟨";
                        break;
                }
                break;
            case 92:
                switch (line){
                    case 1, 5:
                        string = "🟩🟨🟨🟨🟨🟨🟨🟨🟩";
                        break;
                    case 2:
                        string = "🟨⚪⚪ 3⚪🟩⚪⚪🟨";
                        break;
                    case 3:
                        string = "🟨⚪⚪⚪⚪🟩⚪⚪🟨";
                        break;
                    case 4:
                        string = "🟨⚪⚪⚪🟪⚪⚪⚪🟨";
                        break;
                }
                break;
            case 93:
                switch (line){
                    case 1, 5:
                        string = "🟦🟨🟨🟨🟨🟨🟨🟨🟦";
                        break;
                    case 2:
                        string = "🟨⚪⚪⚪🟥⚪⚪⚪🟨";
                        break;
                    case 3:
                        string = "🟨⚪⚪🟦⚪⚪⚪⚪🟨";
                        break;
                    case 4:
                        string = "🟨⚪⚪🟦⚪ 3⚪⚪🟨";
                        break;
                }
                break;
            case 94:
                switch (line){
                    case 1,5:
                        string = "🟪🟨🟨🟨🟨🟨🟨🟨🟪";
                    break;
                    case 2:
                        string = "🟨⚪⚪⚪🟦⚪⚪⚪🟨";
                        break;
                    case 3:
                        string = "🟨⚪⚪⚪⚪🟪⚪⚪🟨";
                        break;
                    case 4:
                        string = "🟨⚪⚪ 3⚪🟪⚪⚪🟨";
                        break;
                }
                break;
            case 95:
                switch (line){
                    case 1, 5:
                        string = "🟥🟨🟨🟨🟨🟨🟨🟨🟥";
                        break;
                    case 2:
                        string = "🟨⚪⚪⚪ 2⚪⚪⚪🟨";
                        break;
                    case 3:
                        string = "🟨⚪⚪⚪🍄⚪⚪⚪🟨";
                        break;
                    case 4:
                        string = "🟨⚪⚪🍄⚪🍄⚪⚪🟨";
                        break;
                }
                break;
            case 96:
                switch (line){
                    case 1, 5:
                        string = "🟩🟨🟨🟨🟨🟨🟨🟨🟩";
                        break;
                    case 2:
                        string = "🟨⚪⚪⚪ 2⚪⚪⚪🟨";
                        break;
                    case 3:
                        string = "🟨⚪⚪⚪🌳⚪⚪⚪🟨";
                        break;
                    case 4:
                        string = "🟨⚪⚪🌳⚪🌳⚪⚪🟨";
                        break;
                }
                break;
            case 97:
                switch (line){
                    case 1, 5:
                        string = "🟦🟨🟨🟨🟨🟨🟨🟨🟦";
                        break;
                    case 2:
                        string = "🟨⚪⚪⚪ 2⚪⚪⚪🟨";
                        break;
                    case 3:
                        string = "🟨⚪⚪⚪🐺⚪⚪⚪🟨";
                        break;
                    case 4:
                        string = "🟨⚪⚪🐺⚪🐺⚪⚪🟨";
                        break;
                }
                break;
            case 98:
                switch (line){
                    case 1, 5:
                        string = "🟪🟨🟨🟨🟨🟨🟨🟨🟪";
                        break;
                    case 2:
                        string = "🟨⚪⚪⚪ 2⚪⚪⚪🟨";
                        break;
                    case 3:
                        string = "🟨⚪⚪⚪🦋⚪⚪⚪🟨";
                        break;
                    case 4:
                        string = "🟨⚪⚪🦋⚪🦋⚪⚪🟨";
                        break;
                }
                break;
            case 99:
                switch (line){
                    case 1, 5:
                        string = "🟨🟨🟨🟨🟨🟨🟨🟨🟨";
                        break;
                    case 2:
                        string = "🟨⚪⚪⚪ 3⚪⚪⚪🟨";
                        break;
                    case 3:
                        string = "🟨⚪⚪📜🍷🪶⚪⚪🟨";
                        break;
                    case 4:
                        string = "🟨⚪⚪⚪⚪⚪⚪⚪🟨";
                        break;
                }
                break;
            case 100:
                switch (line){
                    case 1, 5:
                        string = "🟨🟨🟨🟨🟨🟨🟨🟨🟨";
                        break;
                    case 2:
                        string = "🟨⚪⚪⚪ 2⚪⚪⚪🟨";
                        break;
                    case 3:
                        string = "🟨⚪⚪📜⚪📜⚪⚪🟨";
                        break;
                    case 4:
                        string = "🟨⚪⚪⚪⚪⚪⚪⚪🟨";
                        break;
                }
                break;
            case 101:
                switch (line){
                    case 1, 5:
                        string = "🟨🟨🟨🟨🟨🟨🟨🟨🟨";
                        break;
                    case 2:
                        string = "🟨⚪⚪⚪ 2⚪⚪⚪🟨";
                        break;
                    case 3:
                        string = "🟨⚪⚪🍷⚪🍷⚪⚪🟨";
                        break;
                    case 4:
                        string = "🟨⚪⚪⚪⚪⚪⚪⚪🟨";
                        break;
                }
                break;
            case 102:
                switch (line){
                    case 1, 5:
                        string = "🟨🟨🟨🟨🟨🟨🟨🟨🟨";
                        break;
                    case 2:
                        string = "🟨⚪⚪⚪ 2⚪⚪⚪🟨";
                        break;
                    case 3:
                        string = "🟨⚪⚪🪶⚪🪶⚪⚪🟨";
                        break;
                    case 4:
                        string = "🟨⚪⚪⚪⚪⚪⚪⚪🟨";
                        break;
                }
                break;
        }
        return string;
    }

    private void printSecretObjective(ObjectiveCard card){
        for (int line = 1; line < 6; line++) {
            System.out.println(printObjectiveLine(card, line));
        }
    }
    private void printCommonObjective(){
        for (int line = 1; line < 6; line++) {
            /*System.out.println(printObjectiveLine((ObjectiveCard) controller.getGame().getObjectivePlayingDeck().getSlot(1), line) +
                    "\t"+printObjectiveLine((ObjectiveCard) controller.getGame().getObjectivePlayingDeck().getSlot(2), line) );*/
        }
    }
    private void printHandCards(){
        for (int line = 1; line < 6; line++){
            System.out.println(getPrintCardLine(controller.getPlayersHandCard(playerID, 0), line, true, null) +
                    "\t" + getPrintCardLine(controller.getPlayersHandCard(playerID, 1), line, true, null) +
                    "\t" + getPrintCardLine(controller.getPlayersHandCard(playerID, 2), line, true, null));
        }
    }

    @Override
    public void notifyLastTurn() throws RemoteException {
        // TODO: Implement
    }

    @Override
    public void notifyEndGame(ArrayList<HashMap<String, String>> points) throws RemoteException {
        // TODO: Implement
    }

    @Override
    public void notifyNewMessage(ChatMessage message) {
        chat.add(message);
        actions.add(() -> printMessage(message));
    }

    private void printMessage(ChatMessage message) {
        if (message.getSender().equals("Server")) {
            System.out.println(UiColors.MAGENTA + "[" + message.getDateTime().getHour() + ":" + message.getDateTime().getMinute()
                    + "] " + message.getSender() + " - " + message.getText() + UiColors.RESET);
        } else {
            System.out.println(UiColors.CYAN + "[" + message.getDateTime().getHour() + ":" + message.getDateTime().getMinute()
                    + "] " + message.getSender() + " - " + message.getText() + UiColors.RESET);
        }
    }

    @Override
    public String getPlayerNickname() {
        return player.getNickname();
    }

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
}