package it.polimi.ingsw.gc42.classes;

import it.polimi.ingsw.gc42.interfaces.DeckListener;
import it.polimi.ingsw.gc42.interfaces.Observable;

import java.util.*;

/**
 * Implementation of Deck for Model
 * A Deck is a group of Cards, all belonging the same Type
 */
public class Deck implements Observable {
    // Attributes
    private List<DeckListener> listeners = new ArrayList<>();
    private List<Card> cards = new ArrayList<>();
    private int counter;
    private Game game;
    private CardType cardType;

    // Constructor

    /**
     * Constructor Method
     * @param cards: ArrayList containing the Card that make up the Deck
     * @param counter: number of Cards contained inside the Deck
     * @param game: reference to the Game that is being played
     * @param cardType: Type of the Cards contained inside the Deck
     */
    public Deck(List<Card> cards, int counter, Game game, CardType cardType) {
        this.cards = cards;
        this.counter = counter;
        this.game = game;
        this.cardType = cardType;
    }

    // Getter and Setter

    /**
     * Getter Method for cards
     * @return cards: ArrayList of Cards
     */
    public List<Card> getCards() {
        return cards;
    }

    /**
     * Setter Method for cards
     * @param cards: ArrayList of Cards
     */
    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    /**
     * Getter Method for counter
     * @return counter: number of Cards remaining inside the Deck
     */
    public int getCounter() {
        return counter;
    }

    /**
     * Setter Method for counter
     * @param counter: number of Cards remaining inside the Deck
     */
    public void setCounter(int counter) {
        this.counter = counter;
    }

    /**
     * Setter Method for game
     * @return reference to the currently playing Game
     */
    public Game getGame() {
        return game;
    }

    /**
     * Setter Method for game
     * @param game: reference to the currently playing Game
     */
    public void setGame(Game game) {
        this.game = game;
    }

    /**
     * Getter Method for cardType
     * @return the type of Cards the Deck is made of
     */
    public CardType getCardType() {
        return cardType;
    }

    /**
     * Setter Method for cardType
     * @param cardType: the type of Cards the Deck is made of
     */
    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }

    // Methods

    /**
     * Shuffles in random order the Cards inside the Deck
     */
    public void shuffle() {
        Collections.shuffle(cards);
    }

    /**
     * Draws/Extracts a Card from the Deck.
     * The drawn Card is the one at the TOP of the Deck.
     * @return Card
     * @throws NoSuchElementException if there are 0 Cards left inside the Deck
     */
    public Card draw() throws NoSuchElementException{
        try {
            Card card = cards.getFirst();
            cards.removeFirst();
            counter--;
            if(counter == 0) {
                try {
                    notifyEndOfDeck();
                } catch(NoSuchDeckTypeException e) {
                    //TODO: Remove after handling
                    e.printStackTrace();
                }
            }
        } catch (NoSuchElementException e) {
            //TODO: Remove after handling
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Draws a Card and places it in one of the Slots in the Visible Stage
     */
    public void putDown() {
        //TODO: Implement
    }

    /**
     * Notifies the Game that the deck is empty.
     * Game uses this info to determine if it needs to move to the endgame section.
     * Each Deck carries with itself the information of the CardType of the Cards inside of it, so
     * only the common Decks can effectively notify.
     * @throws NoSuchDeckTypeException if a non-ResourceCard and non-GoldCard Deck tries to notify of being empty
     */
    private void notifyEndOfDeck() throws NoSuchDeckTypeException{
        switch (cardType){
            case RESOURCECARD -> game.setResourceDeckEmpty(true);
            case GOLDCARD -> game.setGoldDeckEmpty(true);
            default -> throw new NoSuchDeckTypeException("Tried to notify the end of a non existing deck");
        }
    }

    @Override
    public void register(EventListener listener) {
        listeners.add((DeckListener) listener);
    }

    @Override
    public void eventHappens() {
        for (DeckListener d: listeners) {
            try {
                d.onDeckEmpty(cardType);
            } catch (NoSuchDeckTypeException e) {
                e.printStackTrace();
                //TODO: Handle
            }
        }
    }
}
