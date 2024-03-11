package it.polimi.ingsw.gc42.model.classes;

import it.polimi.ingsw.gc42.model.classes.cards.*;

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
     * Getter Method for Slot n.1
     *
     * @return the Card contained in Slot n.1
     */
    public Card getSlot1() {
        return this.slot1;
    }

    /**
     * Setter Method for Slot n.1
     *
     * @param card: the Card that goes in Slot n.1
     */
    public void setSlot1(Card card) {
        this.slot1 = card;
    }

    /**
     * Getter Method for Slot n.2
     *
     * @return the Card contained in Slot n.1
     */
    public Card getSlot2() {
        return this.slot2;
    }

    /**
     * Setter Method for Slot n.2
     *
     * @param card: the Card that goes in Slot n.1
     */
    public void setSlot2(Card card) {
        this.slot2 = card;
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

    /**
     * Move a Card from the top of the Deck to one of the Slots in the Visible Stage
     */
    public void putDown() {
        //TODO: Implement putDown
    }

}
