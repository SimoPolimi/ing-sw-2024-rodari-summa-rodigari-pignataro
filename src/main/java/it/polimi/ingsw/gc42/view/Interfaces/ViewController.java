package it.polimi.ingsw.gc42.view.Interfaces;

import it.polimi.ingsw.gc42.model.classes.game.Player;

import java.io.Serializable;

public interface ViewController extends Serializable {
    void showSecretObjectivesSelectionDialog();
    void showStarterCardSelectionDialog();
    void showTokenSelectionDialog();
    Player getOwner();
    void askToDrawOrGrab();
}
