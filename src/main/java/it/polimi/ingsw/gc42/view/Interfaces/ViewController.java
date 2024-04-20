package it.polimi.ingsw.gc42.view.Interfaces;

import it.polimi.ingsw.gc42.model.classes.game.Player;

public interface ViewController {
    void showSecretObjectivesSelectionDialog();
    void showStarterCardSelectionDialog();
    void showTokenSelectionDialog();
    Player getOwner();
    void askToDrawOrGrab();
}
