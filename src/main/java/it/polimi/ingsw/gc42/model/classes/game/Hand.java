package it.polimi.ingsw.gc42.model.classes.game;

import it.polimi.ingsw.gc42.model.classes.cards.Card;
import it.polimi.ingsw.gc42.model.classes.*;
import it.polimi.ingsw.gc42.model.classes.Deck;
import it.polimi.ingsw.gc42.model.classes.PlayingDeck;
import it.polimi.ingsw.gc42.model.classes.cards.CardType;


import java.util.ArrayList;

public class Hand {

    private ArrayList<Card> cards = new ArrayList<>();

    public ArrayList<Card> getCards() {
        return cards;
    }

    /**
     * Draws 2 ResourceCard and 1 GoldCard and puts them in the Player's Hand
     */
    public void drawStartingHand(Game game) {
        drawCard(game.getResourcePlayingDeck());
        drawCard(game.getResourcePlayingDeck());
        drawCard(game.getGoldPlayingDeck());
    }

    /**
     * Move a Card from the Player's Hand to the Player's Field in position (x,y)
     *
     * @param card the Card the Player wants to play
     * @param x    coordinate x of the position where the card will be placed
     * @param y    coordinate y of the position where the card will be placed
     */
    public void playCard(Card card, int x, int y, PlayField playField) {
        card.setX(x);
        card.setY(y);
        playField.getPlayedCards().add(card);
    }

    /**
     * Draws a Card from the top of the specified deck
     *
     * @param playingDeck the deck from where the Card is drawn
     */
    public void drawCard(PlayingDeck playingDeck) {
        cards.add(playingDeck.getDeck().draw());
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

            if(i==1){
                cards.add(deck.getSlot1());
                deck.putDown(i);
            }
            else
            {
                cards.add(deck.getSlot2());
                deck.putDown(i);
            }
        return null;
    }


}
