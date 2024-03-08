package it.polimi.ingsw.gc42.interfaces;

import it.polimi.ingsw.gc42.classes.cards.CardType;
import it.polimi.ingsw.gc42.exceptions.NoSuchDeckTypeException;

import java.util.EventListener;

public interface DeckListener extends EventListener {
    void onDeckEmpty(CardType type) throws NoSuchDeckTypeException;
}
