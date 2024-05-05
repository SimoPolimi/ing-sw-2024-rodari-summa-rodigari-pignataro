package it.polimi.ingsw.gc42.controller.network;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketControllerServer implements ServerNetworkController {
    private String ipAddress;
    private int port;
    private Runnable onReady;
    private GameCollection games;
    private ServerManager server;

    private void translate(String message) {
        // TODO: message parsing and method calling
        System.out.println(message + "A"); // Test
    }

    @Override
    public String getIpAddress() {
        return ipAddress;
    }

    @Override
    public String getPort() {
        return String.valueOf(port);
    }

    @Override
    public void setWhenReady(Runnable runnable) {
        this.onReady = runnable;
    }

    @Override
    public void start() throws IOException {
        // TODO: Create connection
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(0);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        ipAddress = InetAddress.getLocalHost().getHostAddress();
        port = serverSocket.getLocalPort();
        System.out.println("Server socket created");

        // Creates its own ServerManager
        server = new ServerManager(port);
        // Puts the shared GameCollection inside the ServerManager
        server.setCollection(games);

        // Executes the GUI refresh Code to show the IP and Port in Server's GUI
        onReady.run();

        // Listens on its port for new connections
        ExecutorService pool = Executors.newCachedThreadPool(); // Create a thread pool to handle the message flow between the server and every client
        pool.submit(() -> {
            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    System.out.println("Received a connection");
                    pool.submit(() -> {
                        try {
                            Scanner in = new Scanner(socket.getInputStream());
                            PrintWriter out = new PrintWriter(socket.getOutputStream()); // May not be necessary
                            while (true) {
                                String line = in.nextLine();
                                // By creating a thread it is possible to receive and translate a new message
                                // even if the previous translation isn't finished, though this approach
                                // can be risky because there could be a chain of "order sensitive" operations.
                                // Another approach could be putting the messages in a queue and running translate()
                                // every time the queue is not empty
                                pool.submit(() -> translate(line));
                            }
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                    break; // Server is closed
                }
            }
        });
    }

    @Override
    public void stop() {
        // TODO: Close connection
    }

    @Override
    public void setCollection(GameCollection collection) {
        this.games = collection;
    }

    // TODO: Write Socket Methods to send and receive messages.
    // When a message is received, the same action is executed on server.
    // When it has to call the addView() Method, a new RemoteViewController is created HERE.
    // Inside the @Override methods of this new RemoteViewController there will be calls to
    // SocketControllerServer's methods to send messages to Client.
    // The newly created RemoteViewController is saved inside the GameController, the same way
    // RMI does: server.addView(...).
}
