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

    public Hand(ArrayList<Card> cards) {
        this.cards = cards;
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void getStartingHand() {

    }
}
