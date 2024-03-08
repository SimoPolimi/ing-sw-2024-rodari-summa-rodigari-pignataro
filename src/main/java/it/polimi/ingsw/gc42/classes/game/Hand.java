package it.polimi.ingsw.gc42.classes.game;

import it.polimi.ingsw.gc42.classes.cards.Card;

import java.util.ArrayList;

public class Hand {

    private ArrayList<Card> cards;
    public ArrayList<Card> getCards() {
        return cards;
    }
    public void setCards(ArrayList<Card> cards) {
        this.cards = cards;
    }


    /**
     * Class constructor
     * @param cards cards in player's hand
     */
    public Hand(ArrayList<Card> cards) {
        this.cards = cards;
    }


    /**
     * Getter Method for startingHand
     * //@return the Cards belonging to the Starting Hand
     */
    public void getStartingHand() {

    }

    /**
     * Move a Card from the Player's Hand to the Player's Field in position (x,y)
     * @param card the Card the Player wants to play
     * @param x coordinate x of the position where the card will be placed
     * @param y coordinate y of the position where the card will be placed
     */
    public void playCard(Card card, int x, int y){

    }

}
