package it.polimi.ingsw.gc42.model.interfaces;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.server.UnicastRemoteObject;
import java.util.EventListener;

public interface Listener extends EventListener, Serializable, Remote {

    void onEvent();
}
