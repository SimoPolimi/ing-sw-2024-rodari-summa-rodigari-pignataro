package it.polimi.ingsw.gc42.network;

import java.io.IOException;
import java.lang.reflect.Executable;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public interface NetworkController {
    String getIpAddress();
    String getPort();
    void setWhenReady(Runnable runnable);
    void start() throws IOException, AlreadyBoundException;
    void stop() throws NotBoundException, RemoteException;
}
