package it.polimi.ingsw.gc42.classes.game;

import it.polimi.ingsw.gc42.classes.cards.GoldCard;
import it.polimi.ingsw.gc42.classes.cards.Objective;
import it.polimi.ingsw.gc42.classes.cards.ResourceCard;
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
        return objective;
    }

    public void setObjective(Objective objective) {
        this.objective = objective;
    }

    private ArrayList<Listener> listeners = new ArrayList<>();

    private boolean isFirst;
    private Token token;
    private int points;
    private Objective objective;
    private Hand hand;
    private PlayField playField;

    public Player(boolean isFirst, int points, Token token, Objective objective) {

        this.isFirst = isFirst;
        this.points = points;
        this.token = token;
        this.objective = objective;

    }

    //TODO javadoc notifyMaxPointsReached
    public void notifyMaxPointsReached() {
        notifyWinner();
    }

    /**
     * Draws 2 ResourceCard and 1 GoldCard and puts them in the Player's Hand
     */
    public void setStartingHand(){
        // TODO: draw
    }

    private void notifyWinner(){
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
        for (Listener p: listeners) {
            p.onEvent();
        }
    }

    /**
     * Draws a ResourceCard from the resource deck
     * @param number the slot from where the Card is drawn
     * @return the ResourceCard drawn
     */
    public ResourceCard drawResourceCard(int number){
        return null;
    }

    /**
     * Draws a GoldCard from the gold deck
     * @param number the slot from where the Card is drawn
     * @return the GoldCard drawn
     */
    public GoldCard drawGoldCard(int number){
        return null;
    }
}
