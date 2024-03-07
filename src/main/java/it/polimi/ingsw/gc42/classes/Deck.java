package it.polimi.ingsw.gc42.classes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

public class Deck {
    // Attributes
    private List<Card> cards = new ArrayList<>();
    private int counter;

    // Constructor
    public Deck(List<Card> cards, int counter) {
        this.cards = cards;
        this.counter = counter;
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
                notifyEndOfDeck();
            }
            return card;
        } catch (NoSuchElementException e) {
            //TODO: Remove after handling
            e.printStackTrace();
        }
    }

    public void putDown() {
        //TODO: Implement
    }

    private void notifyEndOfDeck() {
        //TODO: Implement (needs Game object)
    }
}
