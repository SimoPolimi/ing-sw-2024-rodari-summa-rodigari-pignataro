package it.polimi.ingsw.gc42.model.classes.game;

import it.polimi.ingsw.gc42.controller.GameStatus;
import it.polimi.ingsw.gc42.model.classes.Deck;
import it.polimi.ingsw.gc42.model.classes.PlayingDeck;
import it.polimi.ingsw.gc42.model.classes.cards.*;
import it.polimi.ingsw.gc42.model.exceptions.IllegalActionException;
import it.polimi.ingsw.gc42.model.exceptions.IllegalPlacementException;
import it.polimi.ingsw.gc42.model.exceptions.PlacementConditionNotMetException;
import it.polimi.ingsw.gc42.model.interfaces.*;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Player class. It implements the Observable class
 */
public class Player implements Observable, Serializable {

    // Attributes
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

    private StarterCard temporaryStarterCard;

    // Constructor Methods
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
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        setStatus(GameStatus.READY_TO_CHOOSE_SECRET_OBJECTIVE);
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
        notifyListeners("Points updated");
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

    /**
     * Implement method setter for secretObjective
     *
     * @param objectiveCard The ObjectiveCard to be setted
     */
    public void setSecretObjective(ObjectiveCard objectiveCard) {
        this.secretObjective = objectiveCard;
        notifyListeners("Secret Objective Selected");
    }

    /**
     * Implement method getter for getStatus
     *
     * @return status
     */
    public GameStatus getStatus() {
        return status;
    }

    /**
     * Implement method setter for gameStatus
     *
     * @param status Game's status
     */
    public void setStatus(GameStatus status) {
        this.status = status;
        notifyListeners("My Status has changed");
    }

    /**
     * Implement method getter for playField
     *
     * @return playField
     */
    public PlayField getPlayField() {
        return playField;
    }

    public StarterCard getTemporaryStarterCard() {
        return temporaryStarterCard;
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
                break;
            case "Points updated":
                for (Listener l : listeners) {
                    if (l instanceof PointsListener) {
                        l.onEvent();
                    }
                }
                break;
        }
    }

    /**
     * Plays the StarterCard contained in temporaryStarterCard in position [x=0,y=0]
     */
    public void setStarterCard() {
        try {
            playCard(-1, 0, 0);
        } catch (IllegalPlacementException | PlacementConditionNotMetException | IllegalActionException e) {
            e.printStackTrace();
        }
    }

    /**
     * Setter Method for temporaryStarterCard.
     * For tests purposes.
     * @param starter
     */
    public void setTemporaryStarterCard(StarterCard starter) {
        this.temporaryStarterCard = starter;
    }

    public void drawSecretObjectives(PlayingDeck playingDeck) {
        tempObjective1 = (ObjectiveCard) playingDeck.getDeck().draw();
        tempObjective2 = (ObjectiveCard) playingDeck.getDeck().draw();
    }

    public ArrayList<ObjectiveCard> getTemporaryObjectiveCards() {
        ArrayList<ObjectiveCard> cards = new ArrayList<>();
        cards.add(tempObjective1);
        cards.add(tempObjective2);
        return cards;
    }

    public void drawTemporaryStarterCard(Deck starterDeck) {
        temporaryStarterCard = (StarterCard) starterDeck.draw();
    }

    /**
     * Draws 2 ResourceCard and 1 GoldCard and puts them in the Player's Hand
     */
    public void drawStartingHand(PlayingDeck resource, PlayingDeck gold) {
        try {
            drawCard(resource);
            drawCard(resource);
            drawCard(gold);
            setStatus(GameStatus.PLAYING);
        }catch (IllegalActionException e){
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    /**
     * Move a Card from the Player's Hand to the Player's Field in position (x,y)
     *
     * @param handCard: the index of the Card the Player wants to play
     * @param x    coordinate x of the position where the card will be placed
     * @param y    coordinate y of the position where the card will be placed
     */
    public void playCard(int handCard, int x, int y) throws IllegalPlacementException, PlacementConditionNotMetException, IllegalActionException {
        PlayableCard card;
        if (handCard == -1) {
            card = temporaryStarterCard;
        } else {
            card = hand.get(handCard - 1);
        }
        try {
            int points = 0;
            if(hand.size() == 3 || card instanceof StarterCard){
                card.setX(x);
                card.setY(y);
                if (card.canBePlaced(playField.getPlayedCards())) {
                    if (card.isFrontFacing()) {
                        points += card.getEarnedPoints();
                    }
                    playField.addCard(card, x, y);
                    if (card instanceof GoldCard) {
                        if (null != ((GoldCard) card).getObjective()) {
                            if (((GoldCard) card).getObjective() instanceof CornerCountObjective) {
                                ((CornerCountObjective) ((GoldCard) card).getObjective()).setCoordinates(new Coordinates(x, y));
                            }
                            points += ((GoldCard) card).getObjective().calculatePoints(playField.getPlayedCards());
                        }
                    }
                    setPoints(getPoints() + points);
                } else throw new PlacementConditionNotMetException();
            }else throw new IllegalActionException();
        } catch (IllegalPlacementException e) {
            // Handle exception
            throw new IllegalPlacementException();
        }
        hand.remove(card);
        notifyListeners("Hand Updated");
    }

    /**
     * Draws a Card from the top of the specified deck and puts it in the Player's hand
     *
     * @param playingDeck the deck from where the Card is drawn
     */
    public void drawCard(PlayingDeck playingDeck) throws IllegalActionException, IllegalArgumentException {
        if(hand.size() <= 2 ) {
            if(playingDeck.getDeck().getNumberOfCards() >= 0) {
                hand.add((PlayableCard) playingDeck.getDeck().draw());
                if (status != GameStatus.READY_TO_DRAW_STARTING_HAND) {
                    notifyListeners("Hand Updated");
                } else if (hand.size() == 3) {
                    notifyListeners("Hand Updated");
                }
            }else throw new IllegalArgumentException("Empty deck");
        } else throw new IllegalActionException();
    }

    /**
     * Grabs a Card from the specified slot and puts it in the Player's hand
     * @param playingDeck the PlayingDeck associated to the Slots where the Player grabs the Card from
     * @param slot the slot from where the card is grabbed
     * @throws IllegalActionException
     */
    //TODO: needed exception description in javadoc????
    public void grabCard(PlayingDeck playingDeck, int slot) throws IllegalActionException, IllegalArgumentException {
        if(hand.size() <= 2) {
            if (playingDeck.getSlot(slot) != null) {
                    hand.add((PlayableCard) playingDeck.getSlot(slot));
                    notifyListeners("Hand Updated");
            } else {
                throw new IllegalArgumentException("Empty slot");
            }
        } else throw new IllegalActionException();
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

    /**
     * Setter Method for a specific Card inside the Hand.
     * PLEASE NOTE: This Overrides the current Card in that slot.
     * Use only if needed. Usage outside of Tests is not recommended.
     * @param slot: the slot to put the Card in
     * @param card: the Card to put it place
     */
    public void setHandCard(int slot, PlayableCard card) {
        if (hand.isEmpty()) {
            hand.add(card);
        } else if (slot >= 0 && slot < hand.size()) {
            hand.set(slot, card);
        }
    }

    // TODO: redo description javadoc

    /**
     * Custom method that replaces size()
     *
     * @return The Hand's size
     */
    public int getHandSize() {
        return hand.size();
    }
}
