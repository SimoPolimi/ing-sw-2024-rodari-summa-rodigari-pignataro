package it.polimi.ingsw.gc42.network;

import java.io.IOException;
import java.lang.reflect.Executable;
import java.rmi.AlreadyBoundException;

public interface NetworkController {
    String getIpAddress();
    String getPort();
    void setWhenReady(Runnable runnable);
    void start() throws IOException, AlreadyBoundException;
}
