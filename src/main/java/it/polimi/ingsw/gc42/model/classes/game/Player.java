package it.polimi.ingsw.gc42.model.classes.game;

import it.polimi.ingsw.gc42.model.classes.cards.ObjectiveEnum;
import it.polimi.ingsw.gc42.model.classes.cards.StarterCard;
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
    }

    public boolean isFirst() {
        return isFirst;
    }

    public void setFirst(boolean first) {
        isFirst = first;
    }

    public ObjectiveEnum getObjective() {
        return secretObjectiveEnum;
    }

    public void setObjective(ObjectiveEnum objectiveEnum) {
        this.secretObjectiveEnum = objectiveEnum;
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
    private ObjectiveEnum secretObjectiveEnum;
    private Hand hand;
    private PlayField playField;
    private Game game;

    public Player(boolean isFirst, int points, Token token, ObjectiveEnum objectiveEnum, Hand hand, Game game) {

        this.isFirst = isFirst;
        this.points = points;
        this.token = token;
        this.secretObjectiveEnum = objectiveEnum;
        this.hand = hand;
        this.game = game;

    }

    //TODO javadoc notifyMaxPointsReached
    public void notifyMaxPointsReached() {
        notifyWinner();
    }

    private void notifyWinner() {
        //TODO write method
        notifyListeners();
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

    public void drawStarterCard(){
        playField.setStarterCard((StarterCard) game.getStarterDeck().draw());
    }
}
