package it.polimi.ingsw.gc42.classes;

public class ObjectiveCard {
    // Attributes
    private int points;
    private Objective objective;

    // Constructor Method
    public ObjectiveCard(int points, Objective objective) {
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
