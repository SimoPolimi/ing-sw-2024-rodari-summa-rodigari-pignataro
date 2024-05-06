package it.polimi.ingsw.gc42.network.interfaces;


import it.polimi.ingsw.gc42.model.classes.cards.CardType;
import it.polimi.ingsw.gc42.model.classes.game.Player;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteViewController extends Remote {

    void showSecretObjectivesSelectionDialog() throws RemoteException;
    void showStarterCardSelectionDialog() throws RemoteException;
    void showTokenSelectionDialog() throws RemoteException;
    Player getOwner() throws RemoteException;
    void askToDrawOrGrab() throws RemoteException;

    void notifyGameIsStarting() throws RemoteException;
    void notifyDeckChanged(CardType type) throws RemoteException;
    void notifySlotCardChanged(CardType type, int slot) throws RemoteException;
    void notifyPlayersPointsChanged() throws RemoteException;
    void notifyNumberOfPlayersChanged() throws RemoteException;
    void notifyPlayersTokenChanged(int playerID) throws RemoteException;
    void notifyPlayersPlayAreaChanged(int playerID) throws RemoteException;
    void notifyPlayersHandChanged(int playerID) throws RemoteException;
    void notifyHandCardWasFlipped(int playedID, int cardID) throws RemoteException;
    void notifyPlayersObjectiveChanged(int playerID) throws RemoteException;
    void notifyCommonObjectivesChanged() throws RemoteException;
    void notifyTurnChanged() throws RemoteException;
    void getReady() throws RemoteException;
}
