package it.polimi.ingsw.gc42.interfaces;

import it.polimi.ingsw.gc42.classes.CardType;
import it.polimi.ingsw.gc42.classes.NoSuchPlayerException;

import java.util.EventListener;

public interface PlayerLister extends EventListener {
    void onWinner() throws NoSuchPlayerException;

}
