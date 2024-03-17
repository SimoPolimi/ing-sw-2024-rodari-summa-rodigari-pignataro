package it.polimi.ingsw.gc42.model.classes.cards;

import java.util.ArrayList;

public abstract class CountObjective extends Objective {
    // Attributes
    private int number;

    // Constructor Method

    public CountObjective(int points, int number, String description) {
        super(points, description);
        this.number = number;
    }

    // Getters and Setters

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

}
