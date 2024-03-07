package it.polimi.ingsw.gc42.classes;

import javafx.geometry.Side;

public class ObjectiveCard extends Card {
    // Attributes
    private int points;
    private Objective objective;

    // Constructor Method

    public ObjectiveCard(Side frontSide, Side backSide, boolean isFrontFacing, int id, int x, int y, int points, Objective objective) {
        super(frontSide, backSide, isFrontFacing, id, x, y);
        this.points = points;
        this.objective = objective;
    }

    // Getters and Setters

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public Objective getObjective() {
        return objective;
    }

    public void setObjective(Objective objective) {
        this.objective = objective;
    }
}
