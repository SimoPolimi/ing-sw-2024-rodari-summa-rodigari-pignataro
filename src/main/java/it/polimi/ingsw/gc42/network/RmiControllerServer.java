package it.polimi.ingsw.gc42.network;

import it.polimi.ingsw.gc42.model.classes.game.Player;
import it.polimi.ingsw.gc42.network.interfaces.ServerNetworkController;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

public class RmiControllerServer implements ServerNetworkController, Serializable {
    private String ipAddress;
    private int port = 23689;
    private ArrayList<Player> users;
    private Runnable onReady;
    private Registry registry;
    private ServerManager server;
    private GameCollection games;

    public RmiControllerServer() throws RemoteException {}

    @Override
    public void start() throws IOException, AlreadyBoundException {
        Thread thread = new Thread(() -> {
            try {
                connect();
            } catch (IOException | AlreadyBoundException e) {
                throw new RuntimeException(e);
            }
        });
        thread.start();
    }

    private void connect() throws IOException, AlreadyBoundException {
        System.out.println("Constructing Server Implementation...");

        System.out.println("Binding Server Implementation to registry...");
        // Temporarily creates a Socket with port 0 because it gets assigned a random available port
        //ServerSocket serverSocket = new ServerSocket(0);
        // Use that port in the RMI connection
        //port = serverSocket.getLocalPort();
        // Closes the socket because it's not needed anymore
        //serverSocket.close();
        // Uses that port in combination with the current IP Address to create an RMI Registry
        registry = LocateRegistry.createRegistry(port);
        server = new ServerManager(port);
        // Puts the shared GameCollection inside the ServerManager
        server.setCollection(games);
        registry.bind("ServerManager", server);
        ipAddress = InetAddress.getLocalHost().getHostAddress();
        System.out.println("Open for connections at: " + ipAddress
                + ", " + port);

        System.out.println("Waiting for invocations from clients...");
        // Executes the GUI refresh Code to show the IP and Port in Server's GUI
        onReady.run();
    }

    @Override
    public void stop() throws NotBoundException, RemoteException {
        registry.unbind("ServerManager");
        System.out.println("Server stopped");
    }

    @Override
    public void setCollection(GameCollection collection) {
        this.games = collection;
    }

    private void registerUser(Player player) {
        users.add(player);
    }

    @Override
    public String getIpAddress() {
        return  ipAddress;
    }

    @Override
    public String getPort() {
        return Integer.toString(port);
    }

    @Override
    public void setWhenReady(Runnable runnable) {
        this.onReady = runnable;
    }
}
