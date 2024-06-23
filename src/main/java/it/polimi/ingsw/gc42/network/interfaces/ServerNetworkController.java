package it.polimi.ingsw.gc42.network.interfaces;

import it.polimi.ingsw.gc42.network.GameCollection;

import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * Common Interface for the Classes that host a Connection inside the Server.
 */
public interface ServerNetworkController {
    /**
     * Getter Method for the IP Address
     * @return the Server IP Address
     */
    String getIpAddress();

    /**
     * Getter Method for the Port
     * @return the Port (in String format)
     */
    String getPort();

    /**
     * Receives a Runnable to execute after opening the Server, mainly to trigger UI updates
     * @param runnable the code to run
     */
    void setWhenReady(Runnable runnable);

    /**
     * Starts the Server
     * @throws IOException if the file can't be found
     * @throws AlreadyBoundException if there is already a Server opened on that IP and Port
     */
    void start() throws IOException, AlreadyBoundException;

    /**
     * Stop the Server
     * @throws NotBoundException if there is no Server opened
     * @throws RemoteException in case of a Connection Error
     */
    void stop() throws NotBoundException, RemoteException;

    /**
     * Setter Method for the List of Games
     * @param collection the List of Games
     */
    void setCollection(GameCollection collection);
}
