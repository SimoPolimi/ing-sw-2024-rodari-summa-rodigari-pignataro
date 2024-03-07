package it.polimi.ingsw.gc42.classes;

import java.util.ArrayList;

public class Hand {

    private ArrayList<Card> cards;
    public ArrayList<Card> getCards() {
        return cards;
    }
    public void setCards(ArrayList<Card> cards) {
        this.cards = cards;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Class constructor
     * @param cards cards in player's hand
     */
    public Hand(ArrayList<Card> cards) {
        this.cards = cards;
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Getter Method for startingHand
     * //@return the Cards belonging to the Starting Hand
     */
    public void getStartingHand() {

    }
}
