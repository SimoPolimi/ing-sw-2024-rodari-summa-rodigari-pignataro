package it.polimi.ingsw.gc42.view;

import it.polimi.ingsw.gc42.network.Server;
import it.polimi.ingsw.gc42.network.ServerTUI;

/**
 * This class launches the Launcher.
 * It's needed because JAR Files have problems launching JavaFx Applications directly, so an intermediary is needed.
 */
public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            Launcher.main(args);
        } else if (args[0].equals("server")) {
            Server.main(args);
        } else if (args[0].equals("server_tui")) {
            ServerTUI.main(args);
        } else if(args[0].equals("launcher_tui")) {
            LauncherTUI.main(args);
        } else if (args[0].equals("gui")) {
            GameWindow.main(args);
        } else if (args[0].equals("tui")) {
            GameTerminal.main(args);
        } else System.err.println("Invalid command");
    }
}
