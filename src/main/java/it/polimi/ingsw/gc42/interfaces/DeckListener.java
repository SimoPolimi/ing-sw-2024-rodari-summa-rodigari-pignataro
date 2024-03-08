package it.polimi.ingsw.gc42.interfaces;

import it.polimi.ingsw.gc42.classes.CardType;
import it.polimi.ingsw.gc42.classes.NoSuchDeckTypeException;

import java.util.EventListener;

public interface DeckListener extends EventListener {
    void onDeckEmpty(CardType type) throws NoSuchDeckTypeException;
}
