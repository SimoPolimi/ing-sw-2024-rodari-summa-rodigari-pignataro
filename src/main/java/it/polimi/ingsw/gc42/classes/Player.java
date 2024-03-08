package it.polimi.ingsw.gc42.classes;

import it.polimi.ingsw.gc42.interfaces.PlayerLister;
import it.polimi.ingsw.gc42.interfaces.Observable;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;
import java.util.NoSuchElementException;

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

    public void notifyMaxPointsReached() throws NoSuchPlayerException {
        try{
            //TODO: write winning condition
            notifyWinner();
        } catch (NoSuchPlayerException e) {
            //TODO: Remove after handling
            e.printStackTrace();
        }
    }

    public void setStartingHand(){
        // TODO: draw
    }

    private void notifyWinner() throws NoSuchPlayerException{
        //TODO write method
        if(true) {
        }
        else {
            throw new NoSuchPlayerException("Tried to notify the victory of a non existing player");

        }
    }

    @Override
    public void register(EventListener listener) {
        listeners.add((PlayerLister) listener);
    }


    public void eventHappens() {
        for (PlayerLister p: listeners) {
            try {
                p.onWinner();
            } catch (NoSuchPlayerException e) {
                e.printStackTrace();
                //TODO: Handle
            }
        }
    }
}
