package it.polimi.ingsw.gc42.model.classes;

import it.polimi.ingsw.gc42.model.classes.cards.Card;
import it.polimi.ingsw.gc42.model.classes.cards.CardType;
import it.polimi.ingsw.gc42.model.interfaces.Listener;
import it.polimi.ingsw.gc42.model.interfaces.Observable;

import java.util.*;

/**
 * Implementation of Deck for Model
 * A Deck is a group of Cards, all belonging the same Type
 */
public class Deck implements Observable {
    // Attributes
    private ArrayList<Listener> listeners = new ArrayList<>();
    private List<Card> cards;
    private int counter;
    private CardType cardType;

    // Constructor

    /**
     * Constructor Method
     *
     * @param cards:    ArrayList containing the Card that make up the Deck
     * @param counter:  number of Cards contained inside the Deck
     * @param cardType: Type of the Cards contained inside the Deck
     */
    public Deck(ArrayList<Card> cards, int counter, CardType cardType) {
        this.cards = cards;
        this.counter = counter;
        this.cardType = cardType;
    }

    // Getter and Setter

    /**
     * Getter Method for cards
     *
     * @return cards: ArrayList of Cards
     */
    public List<Card> getCards() {
        return cards;
    }

    /**
     * Setter Method for cards
     *
     * @param cards: ArrayList of Cards
     */
    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    /**
     * Getter Method for counter
     *
     * @return counter: number of Cards remaining inside the Deck
     */
    public int getCounter() {
        return counter;
    }

    /**
     * Setter Method for counter
     *
     * @param counter: number of Cards remaining inside the Deck
     */
    public void setCounter(int counter) {
        this.counter = counter;
    }

    /**
     * Getter Method for cardType
     *
     * @return the type of Cards the Deck is made of
     */
    public CardType getCardType() {
        return cardType;
    }

    /**
     * Setter Method for cardType
     *
     * @param cardType: the type of Cards the Deck is made of
     */
    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }

    // Methods


    public static Deck initDeck(CardType type) {
        //TODO: Fully Implement
        ArrayList<Card> cards = new ArrayList<>();
        Deck deck = new Deck(cards, cards.size(), type);
        deck.shuffle();
        return deck;
    }

    /**
     * Shuffles in random order the Cards inside the Deck
     */
    public void shuffle() {
        Collections.shuffle(cards);
    }

    /**
     * Draws/Extracts a Card from the Deck.
     * The drawn Card is the one at the TOP of the Deck.
     *
     * @return Card
     * @throws NoSuchElementException if there are 0 Cards left inside the Deck
     */
    public Card draw() throws NoSuchElementException {
        try {
            Card card = cards.getFirst();
            cards.removeFirst();
            counter--;
            if (counter == 0) {
                notifyListeners();
            }
            return card;
        } catch (NoSuchElementException e) {
            //TODO: Remove after handling
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void setListener(Listener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(Listener listener) {
        listeners.remove(listener);
    }

    @Override
    public void notifyListeners() {
        for (Listener d : listeners) {
            d.onEvent();
        }
    }
}