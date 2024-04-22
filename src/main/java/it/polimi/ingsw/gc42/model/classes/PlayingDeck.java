package it.polimi.ingsw.gc42.model.classes;

import it.polimi.ingsw.gc42.model.classes.cards.*;
import it.polimi.ingsw.gc42.model.interfaces.Listener;
import it.polimi.ingsw.gc42.model.interfaces.Observable;
import it.polimi.ingsw.gc42.model.interfaces.ResourceSlot1Listener;
import it.polimi.ingsw.gc42.model.interfaces.ResourceSlot2Listener;

import java.util.ArrayList;

/**
 * Implementation of the playing decks
 */
public class PlayingDeck {
    // Attributes
    private Card slot1;
    private Card slot2;
    private Deck deck;

    // Constructor Method

    /**
     * Constructor Method for PlayingDecks
     *
     * @param slot1: the Card that goes into the slot n.1
     * @param slot2: the Card that goes into the slot n.2
     * @param deck:  Deck containing the Cards
     */
    public PlayingDeck(Card slot1, Card slot2, Deck deck) {
        this.slot1 = slot1;
        this.slot2 = slot2;
        this.deck = deck;
    }

    // Getters and Setters

    /**
     * Getter Method for slot1 and slot2.
     *
     * @param slot: int value to identify the slot to get.
     * @return the Card contained in the corresponding slot.
     * @throws IllegalArgumentException if any number different from 1 or 2 is passed.
     */
    public Card getSlot(int slot) throws IllegalArgumentException {
        if (slot == 1) {
            return slot1;
        } else if (slot == 2) {
            return slot2;
        } else throw new IllegalArgumentException("There is no such slot in this PlayingDeck");
    }

    /**
     * Setter Method for slot1 and slot2.
     *
     * @param card: the Card to set inside one of the slots.
     * @param slot: an int value to identify the slot to set the Card into.
     * @throws IllegalArgumentException if any number different from 1 or 2 is passed.
     */
    public void setSlot(Card card, int slot) throws IllegalArgumentException {
        if (slot == 1) {
            this.slot1 = card;
        } else if (slot == 2) {
            this.slot2 = card;
        } else throw new IllegalArgumentException("There is no such slot in this PLayingDeck");
    }

    /**
     * Getter Method for Deck
     *
     * @return the Deck contained into the PlayingDeck
     */
    public Deck getDeck() {
        return deck;
    }

    /**
     * Setter Method for Deck
     *
     * @param deck: the Deck that goes into the PlayingDeck
     */
    public void setDeck(Deck deck) {
        this.deck = deck;
    }
}
