package it.polimi.ingsw.gc42.classes.game;

import it.polimi.ingsw.gc42.classes.cards.Card;
import it.polimi.ingsw.gc42.classes.cards.GoldCard;
import it.polimi.ingsw.gc42.classes.cards.ResourceCard;

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
     *
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
     *
     * @param card the Card the Player wants to play
     * @param x    coordinate x of the position where the card will be placed
     * @param y    coordinate y of the position where the card will be placed
     */
    public void playCard(Card card, int x, int y) {

    }

    /**
     * Draws a ResourceCard from the resource deck
     *
     * @param number the slot from where the Card is drawn
     * @return the ResourceCard drawn
     */
    public ResourceCard drawResourceCard(int number) {
        return null;
    }

    /**
     * Draws a GoldCard from the gold deck
     *
     * @param number the slot from where the Card is drawn
     * @return the GoldCard drawn
     */
    public GoldCard drawGoldCard(int number) {
        return null;
    }

    /**
     * Draws 2 ResourceCard and 1 GoldCard and puts them in the Player's Hand
     */
    public void drawStartingHand() {
        // TODO: draw
    }

}
