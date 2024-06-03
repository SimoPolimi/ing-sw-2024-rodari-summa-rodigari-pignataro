package it.polimi.ingsw.gc42.view.Interfaces;

import it.polimi.ingsw.gc42.model.classes.cards.CardType;
import it.polimi.ingsw.gc42.model.classes.game.ChatMessage;
import it.polimi.ingsw.gc42.model.classes.game.Player;
import it.polimi.ingsw.gc42.model.classes.game.Token;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

public interface ViewController extends Serializable {
    void showSecretObjectivesSelectionDialog();
    void showStarterCardSelectionDialog();
    void showTokenSelectionDialog();
    int getOwner();
    void askToDrawOrGrab();
    void notifyGameIsStarting();
    void notifyDeckChanged(CardType type);
    void notifySlotCardChanged(CardType type, int slot);
    void notifyPlayersPointsChanged(Token token, int newPoints);
    void notifyNumberOfPlayersChanged();
    void notifyPlayersTokenChanged(int playerID);
    void notifyPlayersPlayAreaChanged(int playerID);
    void notifyPlayersHandChanged(int playerID);
    void notifyHandCardWasFlipped(int playedID, int cardID);
    void notifyPlayersObjectiveChanged(int playerID);
    void notifyCommonObjectivesChanged();
    void notifyTurnChanged();
    void showWaitingForServerDialog();
    void getReady(int numberOfPlayers);
    void notifyLastTurn() throws RemoteException;
    void notifyEndGame(ArrayList<HashMap<String, String>> points) throws RemoteException;
    void notifyNewMessage(ChatMessage message);
}
