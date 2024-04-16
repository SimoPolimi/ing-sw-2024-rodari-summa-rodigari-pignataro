package it.polimi.ingsw.gc42.view;

import it.polimi.ingsw.gc42.controller.GameController;
import it.polimi.ingsw.gc42.model.classes.cards.Coordinates;
import it.polimi.ingsw.gc42.model.classes.game.Player;
import it.polimi.ingsw.gc42.view.Interfaces.ViewController;
import javafx.application.Application;
import javafx.stage.Stage;

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
                    play();
                    exit = true;
                    break;
            }
        }
    }

    private void play() {
        // This stage is never showed
        controller = new GameController();

        while (!exit) {
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
                    // TODO: chat
                    System.out.println();
                    break;
                case "5":
                    // TODO: send msg
                    scanner.next();
                    break;
                case "6":
                    exit = true;
                    return;
                default:
                    System.out.println("Unknown command");
                    break;
            }
            // TODO: clear screen
            System.out.println("-------------------------------------------------------------------------------");
        }
    }

    private void printMenu() {
        System.out.println("Menu:");
        System.out.println("1) Select card to play");
        System.out.println("2) Flip card");
        System.out.println("3) Show objective's descriptions");
        System.out.println("4) Show chat");
        System.out.println("5) To send a message, digit 'send ' + your text");
        System.out.println("6) Exit");
        System.out.println("Digit a number to select the action.");
        System.out.println();
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
        // TODO: Implement
    }
}
