package it.polimi.ingsw.gc42.classes.game;

import it.polimi.ingsw.gc42.classes.cards.Objective;
import it.polimi.ingsw.gc42.interfaces.PlayerLister;
import it.polimi.ingsw.gc42.interfaces.Observable;

import java.util.ArrayList;
import java.util.EventListener;

public class Player implements Observable {
    private ArrayList<PlayerLister> listeners = new ArrayList<>();
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
        return objective;
    }

    public void setObjective(Objective objective) {
        this.objective = objective;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private boolean isFirst;
    private Token token;
    private int points;
    private Objective objective;
    private Hand hand;
    private PlayField playField;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public Player(boolean isFirst, int points, Token token, Objective objective) {

        this.isFirst = isFirst;
        this.points = points;
        this.token = token;
        this.objective = objective;

    }

    public void notifyMaxPointsReached() {
        notifyWinner();
    }

    public void setStartingHand(){
        // TODO: draw
    }

    private void notifyWinner(){
        //TODO write method

    }

    @Override
    public void register(EventListener listener) {
        listeners.add((PlayerLister) listener);
    }


    public void eventHappens() {
        for (PlayerLister p: listeners) {
            p.onWinner();
        }
    }
}
