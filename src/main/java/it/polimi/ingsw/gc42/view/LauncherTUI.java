package it.polimi.ingsw.gc42.view;

import it.polimi.ingsw.gc42.network.Server;
import it.polimi.ingsw.gc42.network.ServerTUI;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.Scanner;

public class LauncherTUI extends Application {

    public static void main(String[] args) {
        System.out.println("Welcome to Codex Naturalis!");
        boolean exit = true;
        do {
            exit = true;
            System.out.println("Please choose one of the following options:");
            System.out.println("- GUI: launch in Graphical Mode");
            System.out.println("- TUI: launch in Textual Mode");
            System.out.println("- Server: launch the Server in GUI Mode");
            System.out.println("- Server_TUI: launch the Server in TUI Mode");
            System.out.println("- Exit: exit program");
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            switch (input.toLowerCase()) {
                case "gui" -> GameWindow.main(args);
                case "tui", "cli" -> GameTerminal.main(args);
                case "server" -> Server.main(args);
                case "server_tui", "server-tui", "servertui" -> ServerTUI.main(args);
                case "exit", "quit", "q" -> System.out.println("Goodbye!");
                default -> {
                    System.err.println("Invalid input");
                    exit = false;
                }
            }
        } while (!exit);
        System.exit(0);


        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

    }
}
