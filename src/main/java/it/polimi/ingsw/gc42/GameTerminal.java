package it.polimi.ingsw.gc42;

import javafx.application.Application;
import javafx.stage.Stage;

import java.util.Scanner;

public class GameTerminal extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        // This stage is never showed

        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to Codex Naturalis!");
        scanner.next();
    }
}
