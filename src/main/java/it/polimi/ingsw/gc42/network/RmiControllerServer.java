package it.polimi.ingsw.gc42.network;

import it.polimi.ingsw.gc42.model.classes.game.Player;
import it.polimi.ingsw.gc42.network.interfaces.ServerNetworkController;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ExportException;
import java.rmi.server.UnicastRemoteObject;
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
        try {
            registry = LocateRegistry.createRegistry(port);
        } catch (ExportException e) {
            registry = LocateRegistry.getRegistry(port);
        }
        server = new ServerManager(port);
        // Puts the shared GameCollection inside the ServerManager
        server.setCollection(games);
        registry.rebind("ServerManager", server);
        ipAddress = InetAddress.getLocalHost().getHostAddress();
        // Executes the GUI refresh Code to show the IP and Port in Server's GUI
        onReady.run();
    }

    @Override
    public void stop() throws NotBoundException, RemoteException {
        registry.unbind("ServerManager");
        UnicastRemoteObject.unexportObject(server, true);
        registry = null;
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
