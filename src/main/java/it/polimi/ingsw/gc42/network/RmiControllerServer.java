package it.polimi.ingsw.gc42.network;

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

/**
 * This class handles the RMI Server
 */
public class RmiControllerServer implements ServerNetworkController, Serializable {
    private String ipAddress;
    private final int port = 23689;
    private Runnable onReady;
    private Registry registry;
    private ServerManager server;
    private GameCollection games;

    /**
     * Constructor Method
     */
    public RmiControllerServer() {}

    /**
     * Starts the Server
     * @throws IOException if the file can't be found
     * @throws AlreadyBoundException if there is already a Server opened on that IP and Port
     */
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

    /**
     * Initializes the Connection
     * @throws IOException if the file can't be found
     * @throws AlreadyBoundException if there is already a Server opened on that IP and Port
     */
    private void connect() throws IOException, AlreadyBoundException {
        try {
            registry = LocateRegistry.createRegistry(port);
        } catch (ExportException e) {
            registry = LocateRegistry.getRegistry(port);
        }
        server = new ServerManager();
        // Puts the shared GameCollection inside the ServerManager
        server.setCollection(games);
        registry.rebind("ServerManager", server);
        ipAddress = InetAddress.getLocalHost().getHostAddress();
        // Executes the GUI refresh Code to show the IP and Port in Server's GUI
        onReady.run();
    }

    /**
     * Stop the Server
     * @throws NotBoundException if there is no Server opened
     * @throws RemoteException in case of a Connection Error
     */
    @Override
    public void stop() throws NotBoundException, RemoteException {
        registry.unbind("ServerManager");
        UnicastRemoteObject.unexportObject(server, true);
        registry = null;
        System.out.println("Server stopped");
    }

    /**
     * Setter Method for the List of Games
     * @param collection the List of Games
     */
    @Override
    public void setCollection(GameCollection collection) {
        this.games = collection;
    }

    /**
     * Getter Method for the IP Address
     * @return the Server IP Address
     */
    @Override
    public String getIpAddress() {
        return  ipAddress;
    }

    /**
     * Getter Method for the Port
     * @return the Port (in String format)
     */
    @Override
    public String getPort() {
        return Integer.toString(port);
    }

    /**
     * Receives a Runnable to execute after opening the Server, mainly to trigger UI updates
     * @param runnable the code to run
     */
    @Override
    public void setWhenReady(Runnable runnable) {
        this.onReady = runnable;
    }
}
