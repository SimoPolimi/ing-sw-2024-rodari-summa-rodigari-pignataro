package it.polimi.ingsw.gc42.model.classes.cards;

import java.util.ArrayList;

public abstract class Objective {
    // Attributes
    private int points;

    // Constructor Method
    public Objective(int points) {
        this.points = points;
    }

    // Getters and Setters
    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    // Methods
    protected abstract int check(ArrayList<Card> playArea);

    public int calculatePoints(ArrayList<Card> playArea) {
        return points * check(playArea);
    }
}
