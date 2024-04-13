package it.polimi.ingsw.gc42.model.classes.game;

import it.polimi.ingsw.gc42.controller.GameStatus;
import it.polimi.ingsw.gc42.model.classes.PlayingDeck;
import it.polimi.ingsw.gc42.model.classes.cards.*;
import it.polimi.ingsw.gc42.model.interfaces.*;

import java.util.ArrayList;

public class Player implements Observable {
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

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
            notifyListeners("Player reached 20 points");
        }
    }

    public boolean isFirst() {
        return isFirst;
    }

    public void setFirst(boolean first) {
        isFirst = first;
    }

    public ObjectiveCard getSecretObjective() {
        return secretObjective;
    }

    public void setSecretObjective(ObjectiveCard objectiveCard) {
        this.secretObjective = objectiveCard;
    }

    public GameStatus getStatus() {
        return status;
    }

    public void setStatus(GameStatus status) {
        this.status = status;
        notifyListeners("My Status has changed");
    }

    public PlayField getPlayField() {
        return playField;
    }

    private final ArrayList<Listener> listeners = new ArrayList<>();
    private String nickname;
    private boolean isFirst;
    private Token token;
    private int points;
    private ObjectiveCard secretObjective;
    private final ArrayList<PlayableCard> hand = new ArrayList<>();
    private final PlayField playField = new PlayField();

    private ObjectiveCard tempObjective1;
    private ObjectiveCard tempObjective2;
    private GameStatus status;

    public Player(String nickname, boolean isFirst, int points, Token token, ObjectiveCard objectiveCard, Game game) {
        this.nickname = nickname;
        this.isFirst = isFirst;
        this.points = points;
        this.token = token;
        this.secretObjective = objectiveCard;
        setStatus(GameStatus.NOT_IN_GAME);

    }

    public Player(Token token) {
        this.nickname = "Bot";
        this.isFirst = false;
        this.points = 0;
        this.token = token;
        this.secretObjective = null;
        setStatus(GameStatus.NOT_IN_GAME);
    }

    @Override
    public void setListener(Listener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(Listener listener) {
        listeners.remove(listener);
    }

    @Override
    public void notifyListeners(String context) {
        switch (context) {
            case "Player reached 20 points": {
                for (Listener l : listeners) {
                    if (l instanceof PlayerListener) {
                        l.onEvent();
                    }
                }
                break;
            }
            case "Hand Updated": {
                for (Listener l : listeners) {
                    if (l instanceof HandListener) {
                        l.onEvent();
                    }
                }
                break;
            }
            case "Ready to choose a Secret Objective":
                for (Listener l : listeners) {
                    if (l instanceof ReadyToChooseSecretObjectiveListener) {
                        l.onEvent();
                    }
                }
                break;
            case "Secret Objective Selected":
                for (Listener l : listeners) {
                    if (l instanceof SecretObjectiveListener) {
                        l.onEvent();
                    }
                }
                break;
            case "My Status has changed":
                for (Listener l: listeners) {
                    if (l instanceof StatusListener) {
                        l.onEvent();
                    }
                }
        }
    }

    public void setStarterCard(StarterCard card) {
        playCard(card, 0, 0);
    }

    public void drawSecretObjectives(PlayingDeck playingDeck) {
        tempObjective1 = (ObjectiveCard) playingDeck.getDeck().draw();
        tempObjective2 = (ObjectiveCard) playingDeck.getDeck().draw();
        notifyListeners("Ready to choose a Secret Objective");
    }

    public ArrayList<ObjectiveCard> getTemporaryObjectiveCards() {
        ArrayList<ObjectiveCard> cards = new ArrayList<>();
        cards.add(tempObjective1);
        cards.add(tempObjective2);
        return cards;
    }

    /**
     * Draws 2 ResourceCard and 1 GoldCard and puts them in the Player's Hand
     */
    public void drawStartingHand(PlayingDeck resource, PlayingDeck gold) {
        drawCard(resource);
        drawCard(resource);
        drawCard(gold);
    }

    /**
     * Move a Card from the Player's Hand to the Player's Field in position (x,y)
     *
     * @param card the Card the Player wants to play
     * @param x    coordinate x of the position where the card will be placed
     * @param y    coordinate y of the position where the card will be placed
     */
    public void playCard(PlayableCard card, int x, int y) {
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
        hand.add((PlayableCard) playingDeck.getDeck().draw());
        notifyListeners("Hand Updated");
    }


    //TODO redo javadoc (param: deck)

    /**
     * Grabs a card of the type of the specified Deck from one of its slots
     *
     * @param deck the type of the card the Player wants to draw
     * @param i    the Slot from where the Player wants to grab the Card
     */
    public void grabCard(PlayingDeck deck, int i) {
        hand.add((PlayableCard) deck.grabCard(i));
        notifyListeners("PlayArea Updated");
    }

    public PlayableCard getHandCard(int slot) {
        if (slot >= 0 && slot < hand.size()) {
            return hand.get(slot);
        } else if (slot > hand.size() - 1 && slot < 3) {
            return null;
        } else throw new IllegalArgumentException("No such Card in Hand");
    }

    public int getHandSize() {
        return hand.size();
    }
}
