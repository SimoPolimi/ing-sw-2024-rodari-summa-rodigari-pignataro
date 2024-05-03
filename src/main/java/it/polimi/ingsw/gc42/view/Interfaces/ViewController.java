package it.polimi.ingsw.gc42.view.Interfaces;

import it.polimi.ingsw.gc42.model.classes.cards.CardType;
import it.polimi.ingsw.gc42.model.classes.game.Player;

import java.io.Serializable;

public interface ViewController extends Serializable {
    void showSecretObjectivesSelectionDialog();
    void showStarterCardSelectionDialog();
    void showTokenSelectionDialog();
    Player getOwner();
    void askToDrawOrGrab();
    void notifyGameIsStarting();
    void notifyDeckChanged(CardType type);
    void notifySlotCardChanged(CardType type, int slot);
    void notifyPlayersPointsChanged();
    void notifyNumberOfPlayersChanged();
    void notifyPlayersTokenChanged(int playerID);
    void notifyPlayersPlayAreaChanged(int playerID);
    void notifyPlayersHandChanged(int playerID);
    void notifyPlayersObjectiveChanged(int playerID);
    void notifyCommonObjectivesChanged();
    void notifyTurnChanged();
    void showWaitingForServerDialog();
    void getReady();
}
