package it.polimi.ingsw.gc42.model.classes.game;

import it.polimi.ingsw.gc42.controller.GameStatus;
import it.polimi.ingsw.gc42.model.classes.PlayingDeck;
import it.polimi.ingsw.gc42.model.classes.cards.*;
import it.polimi.ingsw.gc42.model.exceptions.IllegalPlacementException;
import it.polimi.ingsw.gc42.model.interfaces.*;

import java.util.ArrayList;

/**
 * Player class. It implement the Observable class
 */
public class Player implements Observable {
    /**
     * Implement method getter for nickname
     *
     * @return nickname
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Implement method setter for nickname
     *
     * @param nickname: player's name
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * Implement method getter for token
     *
     * @return token
     */
    public Token getToken() {
        return token;
    }

    /**
     * Implement method setter for token
     *
     * @param token: player's token
     */
    public void setToken(Token token) {
        this.token = token;
        notifyListeners("Token Updated");
    }

    /**
     * Implement method getter for points
     *
     * @return points
     */
    public int getPoints() {
        return points;
    }

    /**
     * Implement method setter for points.
     * If points > 20 notify that player reached the amount to win
     *
     * @param points: player's points
     */
    public void setPoints(int points) {
        this.points = points;
        if (points >= 20) {
            notifyListeners("Player reached 20 points");
        }
    }

    /**
     * Implement method getter for isFirst
     *
     * @return isFirst
     */
    public boolean isFirst() {
        return isFirst;
    }

    /**
     * Implement method setter for isFirst
     *
     * @param first: boolean for knowing if you are the first to play
     */
    public void setFirst(boolean first) {
        isFirst = first;
    }

    /**
     * Implement method getter for secretObjective
     *
     * @return secretObjective
     */
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

    public Player(String nickname) {
        this.nickname = nickname;
        this.isFirst = false;
        this.points = 0;
        this.token = null;
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
                for (Listener l : listeners) {
                    if (l instanceof StatusListener) {
                        l.onEvent();
                    }
                }
                break;
            case "Token Updated":
                for (Listener l : listeners) {
                    if (l instanceof TokenListener) {
                        l.onEvent();
                    }
                }
        }
    }

    public void setStarterCard(StarterCard card) {
        try {
            playCard(card, 0, 0);
        } catch (IllegalPlacementException e) {
            e.printStackTrace();
        }
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
    public void playCard(PlayableCard card, int x, int y) throws IllegalPlacementException {
        try {
            card.setX(x);
            card.setY(y);
            if (card instanceof GoldCard && ((GoldCard) card).getObjective() instanceof CornerCountObjective) {
                ((CornerCountObjective) ((GoldCard) card).getObjective()).setCoordinates(new Coordinates(x, y));
            }
            playField.addCard(card, x, y);
        } catch (IllegalPlacementException e) {
            card.setX(0);
            card.setY(0);
            // Handle exception
            throw new IllegalPlacementException();
        }
        hand.remove(card);
        notifyListeners("Hand Updated");
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

    /**
     * Returns the card in the selected slot provided that a card is present
     *
     * @param slot the slot of the player's hand that contains the card that is returned
     * @return the PlayingCard in position slot of hand or null when there is no card in said position.
     * If the argument is not valid, IllegalArgumentException is thrown
     */
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

    public void addHandCard(PlayableCard card) {
        hand.add(card);
    }
}
