package it.polimi.ingsw.gc42.network.interfaces;

import it.polimi.ingsw.gc42.network.GameCollection;

import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public interface ServerNetworkController {
    String getIpAddress();
    String getPort();
    void setWhenReady(Runnable runnable);
    void start() throws IOException, AlreadyBoundException;
    void stop() throws NotBoundException, RemoteException;
    void setCollection(GameCollection collection);
}
