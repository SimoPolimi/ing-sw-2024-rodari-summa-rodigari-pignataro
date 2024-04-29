package it.polimi.ingsw.gc42.network;

import it.polimi.ingsw.gc42.controller.GameController;
import it.polimi.ingsw.gc42.model.classes.game.Player;

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
    String ipAddress;
    private int port;
    private ArrayList<Player> users;
    private Runnable onReady;
    private Registry registry;
    private ServerManager server;
    private final GameCollection games = new GameCollection();

    public RmiControllerServer() throws RemoteException {}

    public GameController getGameController(int index) {
        try {
            return games.get(index);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void start() throws IOException, AlreadyBoundException {
        System.out.println("Constructing Server Implementation...");

        System.out.println("Binding Server Implementation to registry...");
        // Temporarily creates a Socket with port 0 because it gets assigned a random available port
        ServerSocket serverSocket = new ServerSocket(0);
        // Use that port in the RMI connection
        port = serverSocket.getLocalPort();
        // Closes the socket because it's not needed anymore
        serverSocket.close();
        // Uses that port in combination with the current IP Address to create an RMI Registry
        registry = LocateRegistry.createRegistry(port);
        server = new ServerManager(port);
        server.setCollection(games);
        registry.bind("ServerManager", server);
        ipAddress = InetAddress.getLocalHost().getHostAddress();
        System.out.println("Open for connections at: " + ipAddress
                + ", " + port);

        System.out.println("Waiting for invocations from clients...");;
        onReady.run();

    }

    @Override
    public void stop() throws NotBoundException, RemoteException {
        registry.unbind("GameControllers");
        System.out.println("Server stopped");
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
