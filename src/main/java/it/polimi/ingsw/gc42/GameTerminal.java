package it.polimi.ingsw.gc42;

import javafx.application.Application;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.Scanner;

public class GameTerminal extends Application {
    private boolean exit = false;
    @Override
    public void start(Stage stage) throws Exception {
        // This stage is never showed

        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to Codex Naturalis!");
        while (!exit) {
            switch (scanner.next().toLowerCase(Locale.ROOT)) {
                case "exit", "quit", "esci":
                    return;
                default:
                    System.out.println("Comando non riconosciuto!");
                    break;
            }
        }
    }
}
