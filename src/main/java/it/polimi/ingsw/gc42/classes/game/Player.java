package it.polimi.ingsw.gc42.classes.game;

import it.polimi.ingsw.gc42.classes.cards.Objective;
import it.polimi.ingsw.gc42.interfaces.Listener;
import it.polimi.ingsw.gc42.interfaces.Observable;

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

    public Hand getHand() {
        return hand;
    }

    public void setHand(Hand hand) {
        this.hand = hand;
    }

    public PlayField getPlayField() {
        return playField;
    }

    public void setPlayField(PlayField playField) {
        this.playField = playField;
    }

    private ArrayList<Listener> listeners = new ArrayList<>();

    private boolean isFirst;
    private Token token;
    private int points;
    private Objective secretObjective;
    private Hand hand;
    private PlayField playField;

    public Player(boolean isFirst, int points, Token token, Objective objective, Hand hand) {

        this.isFirst = isFirst;
        this.points = points;
        this.token = token;
        this.secretObjective = objective;
        this.hand = hand;

    }

    //TODO javadoc notifyMaxPointsReached
    public void notifyMaxPointsReached() {
        notifyWinner();
    }

    private void notifyWinner() {
        //TODO write method

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
}
