package it.polimi.ingsw.gc42.network;

import it.polimi.ingsw.gc42.controller.GameController;
import it.polimi.ingsw.gc42.model.interfaces.Listener;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteCollection extends Remote {
    /*void add(RemoteObject obj) throws RemoteException;
    void remove(RemoteObject obj) throws RemoteException;
    int size() throws RemoteException;
    RemoteObject get(int index) throws RemoteException;
    RemoteCollection getCopy() throws RemoteException;
    RemoteObject createNewGame() throws RemoteException;*/

    void add(GameController obj) throws RemoteException;
    void remove(GameController obj) throws RemoteException;
    int size() throws RemoteException;
    GameController get(int index) throws RemoteException;
    RemoteCollection getCopy() throws RemoteException;
    GameController createNewGame() throws RemoteException;
}
