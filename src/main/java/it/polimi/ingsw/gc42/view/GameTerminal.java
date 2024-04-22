package it.polimi.ingsw.gc42.view;

import it.polimi.ingsw.gc42.controller.GameController;
import it.polimi.ingsw.gc42.model.classes.cards.*;
import it.polimi.ingsw.gc42.model.classes.game.Chat;
import it.polimi.ingsw.gc42.model.classes.game.Message;
import it.polimi.ingsw.gc42.model.classes.game.Player;
import it.polimi.ingsw.gc42.model.interfaces.ReadyToChooseSecretObjectiveListener;
import it.polimi.ingsw.gc42.view.Classes.ClearScreen;
import it.polimi.ingsw.gc42.view.Interfaces.ViewController;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

public class GameTerminal extends Application implements ViewController {
    private boolean exit = false;
    private GameController controller;
    private Player player;
    private Scanner scanner = new Scanner(System.in);


    @Override
    public void start(Stage stage) throws Exception {
        controller = new GameController();

        System.out.println("Welcome to Codex Naturalis!");
        String input = "";
        while (!exit) {
            System.out.println("Insert Nickname:");
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
                    controller.addPlayer(player);
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
                        System.out.println(message.getSender().getNickname()+": " +message.getText()+"/* " +message.getDateTime().toString());
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
                    for (Card card: controller.getGame().getResourcePlayingDeck().getDeck().getCopy()) {
                        printCard((PlayableCard) card);
                        System.out.println();
                    }
                    break;
                case "8":
                    for (Card card: controller.getGame().getGoldPlayingDeck().getDeck().getCopy()) {
                        printCard((PlayableCard) card);
                        System.out.println();
                    }
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
                default:
                    System.out.println("Unknown command");
                    break;
            }
            ClearScreen.main();
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
        System.out.println("Digit a number to select the action.");
        System.out.println();
    }
    private void printPlayer(){
        System.out.println("Player n° " + controller.getGame().getPlayerTurn());
        System.out.println("Nickname: "+ controller.getGame().getCurrentPlayer().getNickname());
        System.out.println("Token: "+ controller.getGame().getCurrentPlayer().getToken());
        System.out.println("Point: "+ controller.getGame().getCurrentPlayer().getPoints());
        //TODO set the secretObjective
        System.out.println("Secret objective: "+ controller.getGame().getCurrentPlayer().getSecretObjective());
        System.out.println("Digit I to open the inventory");
    }

    private void playCard(String input) {
        if (input.equals("1") || input.equals("2") || input.equals("3")) {
            // Play card
            int i = 0;
            ArrayList<Coordinates> availablePlacements = player.getPlayField().getAvailablePlacements();
            for (Coordinates coord : availablePlacements) {
                System.out.println(i + ") " + coord.getX() + " " + coord.getY());
                i++;
            }
            String inputCoord = scanner.next();
            if (Integer.valueOf(inputCoord) < 1 || Integer.valueOf(inputCoord) > availablePlacements.size()) {
                System.out.println("Invalid coordinate");
            } else {
                controller.playCard(player.getHandCard(Integer.parseInt(input)), availablePlacements.get(Integer.valueOf(inputCoord) - 1).getX(), availablePlacements.get(Integer.valueOf(inputCoord) - 1).getY());
            }
        } else if (input.equals("0")) {
            return;
        } else {
            System.out.println("Invalid input");
            return;
        }
        return;
    }

    private void flipCard(String input) {

    }

    @Override
    public void showSecretObjectivesSelectionDialog() {
        //TODO: Implement
    }

    @Override
    public void showStarterCardSelectionDialog() {
        //TODO: Implement

    }

    @Override
    public void showTokenSelectionDialog() {

    }

    @Override
    public Player getOwner() {
        return player;
    }

    @Override
    public void askToDrawOrGrab() {

    }

    private String getPrintCardLine(PlayableCard card, int line, boolean printCoveredCorners) {
        String string = "";
        if (null != card) {
            switch (line) {
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
            }
        } else {
            for (int i = 0; i < 11; i++) {
                string += " ";
            }
        }
        return string;
    }

    private String getCornerPrint(Corner corner) {
        String string = "";
        if (null != corner) {
            if (null != corner.getItem()) {
                string = getItemPrint(corner.getItem());
            } else string = "◻";
        } else string = "X";
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
            case KingdomResource.PLANT -> string = "🌲";
            case Resource.FEATHER -> string = "🪶";
            case Resource.POTION -> string = "🍷";
            case Resource.SCROLL -> string = "📜";
            default -> {string = "  ";}
        }
        return string;
    }

    public void printCard(PlayableCard card) {
        System.out.println(getPrintCardLine(card, 1, true));
        System.out.println(getPrintCardLine(card, 2, true));
        System.out.println(getPrintCardLine(card, 3, true));
        System.out.println(getPrintCardLine(card, 4, true));
        System.out.println(getPrintCardLine(card, 5, true));
    }

}
