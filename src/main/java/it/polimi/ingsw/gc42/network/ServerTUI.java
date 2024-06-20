package it.polimi.ingsw.gc42.network;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

public class ServerTUI extends Application {
    private boolean isRunning = false;
    private RmiControllerServer rmiController;
    private SocketControllerServer socketController;
    private GameCollection collection;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println("Codex Naturalis - Server");
        enterLoop();
    }

    private void enterLoop() throws RemoteException {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;
        if (!isRunning) {
            collection = new GameCollection();
            while (!exit) {
                System.out.println("Press s to start the server, or q to close it");
                if (scanner.hasNextLine()) {
                    String input = scanner.nextLine();
                    if (input.equals("s")) {
                        startServer();
                        exit = true;
                    } else if (input.equals("q")) {
                        exit = true;
                        Platform.exit();
                    } else {
                        System.err.println("Invalid input");
                    }
                }
            }
        } else {
            while (!exit) {
                System.out.println("Press s to stop the server");
                if (scanner.hasNextLine()) {
                    String input = scanner.nextLine();
                    if (input.equals("s")) {
                        stopServer();
                        exit = true;
                    } else {
                        System.err.println("Invalid input");
                    }
                }
            }
        }
    }

    private void startServer() {
        try {
            rmiController = new RmiControllerServer();
            socketController = new SocketControllerServer();
        } catch (RemoteException e) {
            System.err.println("Server is already running!");
        }

        rmiController.setCollection(collection);
        socketController.setCollection(collection);

        AtomicBoolean isRmiReady = new AtomicBoolean(false);
        AtomicBoolean isSocketReady = new AtomicBoolean(false);

        rmiController.setWhenReady(() -> {
            System.out.println("RMI Ready!");
            System.out.println("IP Address: " + rmiController.getIpAddress());
            isRmiReady.set(true);
        });
        socketController.setWhenReady(() -> {
            System.out.println("Socket Ready!");
            System.out.println("IP Address: " + socketController.getIpAddress());
            isSocketReady.set(true);
        });

        try {
            rmiController.start();
            socketController.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (AlreadyBoundException e) {
            System.err.println("Server is already running");
            Platform.exit();
        }
        isRunning = true;
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        try {
            enterLoop();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    private void stopServer() {
        try {
            rmiController.stop();
            socketController.stop();
            isRunning = false;
        } catch (NotBoundException | RemoteException e) {
            throw new RuntimeException(e);
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        try {
            enterLoop();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
