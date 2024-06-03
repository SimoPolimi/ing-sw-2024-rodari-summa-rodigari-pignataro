package it.polimi.ingsw.gc42.network.interfaces;


import it.polimi.ingsw.gc42.model.classes.cards.CardType;
import it.polimi.ingsw.gc42.model.classes.game.ChatMessage;
import it.polimi.ingsw.gc42.model.classes.game.Player;
import it.polimi.ingsw.gc42.model.classes.game.Token;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

public interface RemoteViewController extends Remote {

    void showSecretObjectivesSelectionDialog() throws RemoteException;
    void showStarterCardSelectionDialog() throws RemoteException;
    void showTokenSelectionDialog() throws RemoteException;
    void askToDrawOrGrab(int playerID) throws RemoteException;

    void notifyGameIsStarting() throws RemoteException;
    void notifyDeckChanged(CardType type) throws RemoteException;
    void notifySlotCardChanged(CardType type, int slot) throws RemoteException;
    void notifyPlayersPointsChanged(Token token, int newPoints) throws RemoteException;
    void notifyNumberOfPlayersChanged() throws RemoteException;
    void notifyPlayersTokenChanged(int playerID) throws RemoteException;
    void notifyPlayersPlayAreaChanged(int playerID) throws RemoteException;
    void notifyPlayersHandChanged(int playerID) throws RemoteException;
    void notifyHandCardWasFlipped(int playedID, int cardID) throws RemoteException;
    void notifyPlayersObjectiveChanged(int playerID) throws RemoteException;
    void notifyCommonObjectivesChanged() throws RemoteException;
    void notifyTurnChanged() throws RemoteException;
    void getReady(int numberOfPlayers) throws RemoteException;

    void notifyLastTurn() throws RemoteException;
    void notifyEndGame(ArrayList<HashMap<String, String>> points) throws RemoteException;

    void notifyNewMessage(ChatMessage message) throws RemoteException;
}
