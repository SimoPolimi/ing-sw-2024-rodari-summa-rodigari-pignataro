package it.polimi.ingsw.gc42.model.classes.game;

import it.polimi.ingsw.gc42.model.classes.PlayingDeck;
import it.polimi.ingsw.gc42.model.classes.cards.*;
import it.polimi.ingsw.gc42.model.interfaces.Listener;
import it.polimi.ingsw.gc42.model.interfaces.Observable;

import java.util.ArrayList;

public class Player implements Observable {

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
        if (points >= 20) {
            notifyListeners();
        }
    }

    public boolean isFirst() {
        return isFirst;
    }

    public void setFirst(boolean first) {
        isFirst = first;
    }

    public Objective getObjective() {
        return secretObjective;
    }

    public void setObjective(Objective objective) {
        this.secretObjective = objective;
    }

    public ArrayList<Card> getHand() {
        return hand;
    }

    public PlayField getPlayField() {
        return playField;
    }

    private final ArrayList<Listener> listeners = new ArrayList<>();

    private boolean isFirst;
    private Token token;
    private int points;
    private Objective secretObjective;
    private final ArrayList<Card> hand = new ArrayList<>();
    private final PlayField playField = new PlayField();
    private Game game;

    public Player(boolean isFirst, int points, Token token, Objective objective, Game game) {

        this.isFirst = isFirst;
        this.points = points;
        this.token = token;
        this.secretObjective = objective;
        this.game = game;

    }

    public Player(Token token) {
        this.isFirst = false;
        this.points = 0;
        this.token = token;
        this.secretObjective = null;


    }

    @Override
    public void setListener(Listener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(Listener listener) {
        listeners.remove(listener);
    }


    public void notifyListeners() {
        for (Listener p : listeners) {
            p.onEvent();
        }
    }

    public void drawStarterCard() {
        playField.setStarterCard((StarterCard) game.getStarterDeck().draw());
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
    public void playCard(Card card, int x, int y) {
        card.setX(x);
        card.setY(y);
        playField.addCard(card, x, y);
        hand.remove(card);
    }

    /**
     * Draws a Card from the top of the specified deck
     *
     * @param playingDeck the deck from where the Card is drawn
     */
    public void drawCard(PlayingDeck playingDeck) {
        hand.add(playingDeck.getDeck().draw());
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

        if (i == 1) {
            hand.add(deck.getSlot1());
            deck.putDown(i);
        } else {
            hand.add(deck.getSlot2());
            deck.putDown(i);
        }
        return null;
    }
}
