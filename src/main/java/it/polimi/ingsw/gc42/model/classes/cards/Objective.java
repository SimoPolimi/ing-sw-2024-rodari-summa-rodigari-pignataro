package it.polimi.ingsw.gc42.model.classes.cards;

import java.util.ArrayList;

public abstract class Objective {
    // Attributes
    private int points;
    private String description;

    // Constructor Method
    public Objective(int points, String description) {
        this.points = points;
        this.description = description;
    }

    // Getters and Setters
    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Methods
    protected abstract int check(ArrayList<Card> playArea);

    public int calculatePoints(ArrayList<Card> playArea) {
        return points * check(playArea);
    }
}
