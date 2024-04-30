package it.polimi.ingsw.gc42.view;

import it.polimi.ingsw.gc42.controller.GameController;
import it.polimi.ingsw.gc42.controller.GameStatus;
import it.polimi.ingsw.gc42.model.classes.cards.*;
import it.polimi.ingsw.gc42.model.classes.game.*;
import it.polimi.ingsw.gc42.model.interfaces.ReadyToChooseSecretObjectiveListener;
import it.polimi.ingsw.gc42.network.ClientController;
import it.polimi.ingsw.gc42.network.RemoteViewController;
import it.polimi.ingsw.gc42.view.Classes.ClearScreen;
import it.polimi.ingsw.gc42.view.Interfaces.ViewController;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Locale;
import java.util.Scanner;
import java.util.regex.Pattern;

public class GameTerminal extends Application implements ViewController {
    private boolean exit = false;
    private GameController controller;
    private Player player;
    private Scanner scanner = new Scanner(System.in);

    private String color(String str, UiColors color) {
        return color + str + UiColors.RESET;
    }

    @Override
    public void start(Stage stage) throws Exception {
        controller = new GameController("Test");
        //TODO: Temporary
        controller.addView(new ClientController(this));

        System.out.println("Welcome to Codex Naturalis!");
        String input = "";
        while (!exit) {
            System.out.println();
            System.out.println("--- Insert Nickname ---");
            input = scanner.next();
            switch (input) {
                case "":
                    break;
                default:
                    player = new Player(input);
                    player.setListener(new ReadyToChooseSecretObjectiveListener() {
                        @Override
                        public void onEvent() {
                            //TODO: implement method to print card in the terminal
                        }
                    });
                    // Temporary
                    player.setFirst(true);
                    controller.addPlayer(player);
                    controller.setCurrentStatus(GameStatus.READY);
                    player.setStatus(GameStatus.READY);
                    play();
                    exit = true;
                    break;
            }
        }
    }

    private void play() throws IOException, InterruptedException {
        // This stage is never showed
        Chat chat = controller.getGame().getChat();

        while (!exit) {
            if (player.getStatus() == GameStatus.PLAYING) {
                printPlayer();
                printMenu();

                switch (scanner.next()) {
                    case "1":
                        System.out.println("Select the number of the card you want to play (0 to cancel)");
                        playCard(scanner.next());
                        break;
                    case "2":
                        System.out.println("Select the number of the card you want to flip (0 to cancel)");
                        flipCard(scanner.next());
                        break;
                    case "3":
                        System.out.println(player.getSecretObjective().getObjective().getDescription());
                        // TODO: add common objectives
                        System.out.println();
                        break;
                    case "4":
                        for (int index = 0; index < chat.getChatSize(); index++) {
                            Message message = chat.getMessage(index);
                            System.out.println(message.getSender().getNickname() + ": " + message.getText() + "/* " + message.getDateTime().toString());
                        }
                        break;
                    case "5":
                        Message message = new Message(scanner.next(), player);
                        chat.sendMessage(message);
                        break;
                    case "6":
                        exit = true;
                        return;
                    case "7":
                        // Test, will be deleted later
                        for (Card card : controller.getGame().getResourcePlayingDeck().getDeck().getCopy()) {
                            printCard((PlayableCard) card);
                            System.out.println();
                            card.flip();
                            printCard((PlayableCard) card);
                            System.out.println();
                            card.flip();
                        }
                        break;
                    case "8":
                        for (Card card : controller.getGame().getGoldPlayingDeck().getDeck().getCopy()) {
                            printCard((PlayableCard) card);
                            System.out.println();
                            card.flip();
                            printCard((PlayableCard) card);
                            System.out.println();
                            card.flip();
                        }
                        break;
                    case "9":
                        for (Card card : controller.getGame().getStarterDeck().getCopy()) {
                            printCard((PlayableCard) card);
                            card.flip();
                            System.out.println();
                            printCard((PlayableCard) card);
                            System.out.println();
                        }
                        break;
                    case "10":
                        // Test placements
                        ArrayList<PlayableCard> cards = new ArrayList<>();
                        PlayableCard card = (PlayableCard) controller.getGame().getResourcePlayingDeck().getDeck().draw();
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
                        printCardMatrix(getMatrix(cards), cards);
                        //printPlayArea();
                        break;
                    case "i":
                        System.out.println("Inventory");
                        System.out.println("Kingdom resource");
                        System.out.println(("Animal:" + controller.getGame().getCurrentPlayer().getPlayField().getNumberOf(KingdomResource.ANIMAL)));
                        System.out.println(("Plant:" + controller.getGame().getCurrentPlayer().getPlayField().getNumberOf(KingdomResource.PLANT)));
                        System.out.println(("Fungi:" + controller.getGame().getCurrentPlayer().getPlayField().getNumberOf(KingdomResource.FUNGI)));
                        System.out.println(("Insect:" + controller.getGame().getCurrentPlayer().getPlayField().getNumberOf(KingdomResource.INSECT)));
                        System.out.println("Resource");
                        System.out.println(("Feather:" + controller.getGame().getCurrentPlayer().getPlayField().getNumberOf(Resource.FEATHER)));
                        System.out.println(("Potion:" + controller.getGame().getCurrentPlayer().getPlayField().getNumberOf(Resource.POTION)));
                        System.out.println(("Scroll:" + controller.getGame().getCurrentPlayer().getPlayField().getNumberOf(Resource.SCROLL)));
                        break;
                    default:
                        System.out.println(color("Unknown command", UiColors.RED));
                        break;
                }
            } else continue;
            System.out.print("\n\n\n");
            System.out.println("-------------------------------------------------------------------------------");
        }
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
        System.out.println("7) Print all Resource Cards [Test]");
        System.out.println("8) Print all Gold Cards [Test]");
        System.out.println("9) Print all Starter Cards [Test]");
        System.out.println("10) Print PlayArea [Test]");
        System.out.println("Digit a number to select the action.");
        System.out.println();
    }

    private void printPlayer(){
        System.out.println("Player n° " + controller.getGame().getPlayerTurn());
        System.out.println("Nickname: "+ controller.getGame().getCurrentPlayer().getNickname());
        String string = "  ";
        if (null != player.getToken()) {
            switch (player.getToken()) {
                case Token.RED -> string = "🔴";
                case Token.BLUE -> string = "🔵";
                case Token.GREEN -> string = "🟢";
                case Token.YELLOW -> string = "🟡";
            }
        }
        System.out.println("Token: "+ string);
        System.out.println("Point: "+ controller.getGame().getCurrentPlayer().getPoints());
        //TODO set the secretObjective
        if (null != player.getSecretObjective()) {
            System.out.println("Secret objective: " + controller.getGame().getCurrentPlayer().getSecretObjective().getObjective().getName());
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
                    ArrayList<Coordinates> availablePlacements = player.getPlayField().getAvailablePlacements();
                    for (Coordinates coord : availablePlacements) {
                        System.out.println(i + ") " + coord.getX() + " " + coord.getY());
                        i++;
                    }
                    // TODO: Handle str to int conversion exceptions or use nextInt() (there are still exceptions to be handled in this case)
                    String inputCoord = scanner.next();
                    if (Integer.valueOf(inputCoord) < 1 || Integer.valueOf(inputCoord) > availablePlacements.size()) {
                        System.out.println(color("Invalid coordinate choice! Retry...", UiColors.RED));
                    } else {
                        valid = true;
                        //TODO: Fix
                        controller.playCard(1, Integer.valueOf(input), availablePlacements.get(Integer.valueOf(inputCoord) - 1).getX(), availablePlacements.get(Integer.valueOf(inputCoord) - 1).getY());
                    }
                }
            }
            case "0" -> {} // do nothing and exit
            default -> System.out.println(color("Invalid choice!", UiColors.RED));
        }
    }

    private void flipCard(String input) {

    }

    @Override
    public void showSecretObjectivesSelectionDialog() {
        System.out.println("--- Choose your secret objective ---");
        System.out.println("Digit 1 to choose: " + player.getTemporaryObjectiveCards().get(0).getObjective().getName());
        System.out.println("ℹ\uFE0F " + player.getTemporaryObjectiveCards().get(0).getObjective().getDescription());
        //TODO: print card
        System.out.println("Digit 2 to choose: " + player.getTemporaryObjectiveCards().get(1).getObjective().getName());
        System.out.println("ℹ\uFE0F " + player.getTemporaryObjectiveCards().get(1).getObjective().getDescription());
        //TODO: print card
        String input = "";askToDrawOrGrab();
        boolean exit = false;
        while(!exit) {
            input = scanner.next();
            switch (input) {
                case "1":
                    player.setSecretObjective(player.getTemporaryObjectiveCards().get(0));
                    exit = true;
                    break;
                case "2":
                    player.setSecretObjective(player.getTemporaryObjectiveCards().get(1));
                    exit = true;
                    break;
                default:
                    System.out.println(color("Invalid choice! Retry...", UiColors.RED));
                    exit = false;
                    System.out.println("Digit 1 to choose: " + player.getTemporaryObjectiveCards().get(0).getObjective().getName());
                    System.out.println("ℹ\uFE0F " + player.getTemporaryObjectiveCards().get(0).getObjective().getDescription());
                    //TODO: print card
                    //System.out.println("Secret objective 2");
                    System.out.println("Digit 2 to choose: " + player.getTemporaryObjectiveCards().get(1).getObjective().getName());
                    System.out.println("ℹ\uFE0F " + player.getTemporaryObjectiveCards().get(1).getObjective().getDescription());
                    break;
            }
        }
        player.setStatus(GameStatus.READY_TO_CHOOSE_STARTER_CARD);
    }

    @Override
    public void showStarterCardSelectionDialog() {
        System.out.println("--- Choose the side of your starter card ---");
        Card starterCard  = player.getTemporaryStarterCard();
        System.out.println("Digit f to choose front side");
        printCard((PlayableCard) starterCard);
        starterCard.flip();
        System.out.println("Digit b to choose back side");
        printCard((PlayableCard) starterCard);

        String input = "";
        boolean exit = false;
        while (!exit) {
            input = scanner.next();
            switch (input) {
                case "b":
                    player.setStarterCard((StarterCard) starterCard);
                    exit = true;
                    break;
                case "f":
                    starterCard.flip();
                    player.setStarterCard((StarterCard) starterCard);
                    exit  = true;
                    break;
                default:
                    System.out.println(color("Invalid choice! Retry...", UiColors.RED));
                    exit = false;
                    System.out.println("--- Choose the side of your starter card ---");
                    starterCard.flip();
                    System.out.println("Digit f to choose front side");
                    printCard((PlayableCard) starterCard);
                    starterCard.flip();
                    System.out.println("Digit b to choose back side");
                    printCard((PlayableCard) starterCard);
                    break;
            }
        }
        player.setStatus(GameStatus.PLAYING);
        printCard(player.getPlayField().getPlayedCards().getFirst());
    }

    @Override
    public void showTokenSelectionDialog() {
        System.out.println("--- Select your token ---");
        System.out.println("Digit 1 to choose: 🔴");
        System.out.println("Digit 2 to choose: 🔵");
        System.out.println("Digit 3 to choose: 🟢");
        System.out.println("Digit 4 to choose: 🟡");
        String input = "";
        boolean exit = false;
        while(!exit){
            input = scanner.next();
            switch (input) {
                case "1":
                    player.setToken(Token.RED);
                    exit = true;
                    break;
                case "2":
                    player.setToken(Token.BLUE);
                    exit = true;
                    break;
                case "3":
                    player.setToken(Token.GREEN);
                    exit = true;
                    break;
                case "4":
                    player.setToken(Token.YELLOW);
                    exit = true;
                    break;
                default:
                    System.out.println(color("Invalid choice! Retry...", UiColors.RED));
                    exit = false;
                    System.out.println("--- Select your token ---");
                    System.out.println("Digit 1 to choose: 🔴");
                    System.out.println("Digit 2 to choose: 🔵");
                    System.out.println("Digit 3 to choose: 🟢");
                    System.out.println("Digit 4 to choose: 🟡");
                    break;
            }
        }
    }

    @Override
    public Player getOwner() {
        return player;
    }

    @Override
    public void askToDrawOrGrab() {

    }

    @Override
    public void notifyGameIsStarting() {

    }

    @Override
    public void notifyDeckChanged(CardType type) {

    }

    @Override
    public void notifySlotCardChanged(CardType type, int slot) {

    }

    @Override
    public void notifyPlayersPointsChanged() {

    }

    @Override
    public void notifyNumberOfPlayersChanged() {

    }

    @Override
    public void notifyPlayersTokenChanged(int playerID) {

    }

    @Override
    public void notifyPlayersPlayAreaChanged(int playerID) {

    }

    @Override
    public void notifyPlayersHandChanged(int playerID) {

    }

    @Override
    public void notifyPlayersObjectiveChanged(int playerID) {

    }

    @Override
    public void notifyCommonObjectivesChanged() {

    }

    @Override
    public void showWaitingForServerDialog() {

    }

    @Override
    public void getReady() {

    }

    private String getPrintCardLine(PlayableCard card, int line, boolean printCoveredCorners, ArrayList<PlayableCard> cards) {
        String string = "";
        PlayField field = player.getPlayField();
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
                    if (!card.isFrontFacing() || (card.isFrontFacing() && card instanceof StarterCard)) {
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
                string = "⬜";
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
            default -> {string = "  ";}
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
                case KingdomResource.PLANT -> string ="🟩";
                case KingdomResource.ANIMAL -> string = "🟦";
                case KingdomResource.FUNGI -> string = "🟥";
                case KingdomResource.INSECT -> string = "🟪";
                default -> string = "";
            }
        } else if (card instanceof StarterCard) {
            string = "🟨";
        } else {
            string = "⬜";
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

    private void printCardMatrix(PlayableCard[][] matrix, ArrayList<PlayableCard> cards) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 1; j < 5; j++) {
                // It takes 5 prints to completely print a Card
                String string = "";
                for (int k = 0; k < matrix[i].length; k++) {
                    if (i > 0 && i < matrix.length - 1) {
                        if (j == 1) {
                            if (null != matrix[i][k]) {
                                string += getPrintCardLine(matrix[i][k], 1, false, cards);
                            } else {
                                string += getPrintCardLine(matrix[i-1][k], 5, false, cards);
                            }
                        } /*else if (j == 5) {
                            if (null == matrix[i][k] && null != matrix[i+1][k]) {
                                string += getPrintCardLine(matrix[i+1][k], 1, false, cards);
                            } else {
                                //string += getPrintCardLine(matrix[i][k], j, false, cards);
                            }
                        }*/ else {
                            if (k > 0 && null != matrix[i][k-1]) {
                                string += " ";
                            }
                            string += " ";
                            if (k < matrix.length-1 && null != matrix[i][k+1]) {
                                string += " ";
                            }
                            string += getPrintCardLine(matrix[i][k], j, false, cards);
                        }
                    } else {
                        string += getPrintCardLine(matrix[i][k], j, false, cards);
                    }
                }
                System.out.println(string);
            }
        }
    }

    private void printPlayArea() {
        printCardMatrix(getMatrix(player.getPlayField().getPlayedCards()), player.getPlayField().getPlayedCards());
    }

    private boolean isThereACardIn(int x, int y, ArrayList<PlayableCard> cards) {
        for (PlayableCard card : cards) {
            if (card.getX() == x && card.getY() == y) {
                return true;
            }
        }
        return false;
    }
}

