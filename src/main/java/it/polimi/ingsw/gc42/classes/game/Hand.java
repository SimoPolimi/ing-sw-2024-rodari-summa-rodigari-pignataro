package it.polimi.ingsw.gc42.classes.game;

import it.polimi.ingsw.gc42.classes.cards.Card;
import it.polimi.ingsw.gc42.classes.*;


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
     * Draws 2 ResourceCard and 1 GoldCard and puts them in the Player's Hand
     */
    public void drawStartingHand() {
        // TODO: draw
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
     * Draws a Card from the top of the specified deck
     *
     * @param deck the deck from where the Card is drawn
     * @return the Card drawn
     */
    public Card drawCard(Deck deck) {
        return null;
    }


    //TODO redo javadoc (param: deck)

    /**
     * Grabs a card of the type of the specified Deck from one of its slots
     *
     * @param deck the type of the card the Player wants to draw
     * @param i    the Slot from where the Player wants to grab the Card
     * @return
     */
    public Card grabCard(PlayingDeck deck, int i) {
        // Logic
        /*
            if(i==1){
                hand.add(deck.getSlot1)
                putDwon(slot1)
            }
            else
            {
                hand.add(deck.getSlot2)
                putDwon(slot2)
            }*/
        return null;
    }


}
