package it.polimi.ingsw.gc42.classes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Implementation of Deck for Model
 * A Deck is a group of Cards, all belonging the same Type
 */
public class Deck {
    // Attributes
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

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public CardType getCardType() {
        return cardType;
    }

    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }

    // Methods
    public void shuffle() {
        Collections.shuffle(cards);
    }

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

    public void putDown() {
        //TODO: Implement
    }

    private void notifyEndOfDeck() throws NoSuchDeckTypeException{
        switch (cardType){
            case RESOURCECARD -> game.setResourceDeckEmpty(true);
            case GOLDCARD -> game.setGoldDeckEmpty(true);
            default -> throw new NoSuchDeckTypeException("Tried to notify the end of a non existing deck");
        }
    }
}
