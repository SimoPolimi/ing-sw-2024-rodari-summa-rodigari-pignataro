package it.polimi.ingsw.gc42.classes;

import javafx.geometry.Side;

public class StarterCard extends Card{
    // Attributes
    private Resource permanentResourceOne;
    private Resource permanentResourceTwo;

    // Constructor Method
    public StarterCard(CardSide frontSide, CardSide backSide, boolean isFrontFacing, int id, int x, int y, Resource permanentResourceOne, Resource permanentResourceTwo) {
        super(frontSide, backSide, isFrontFacing, id, x, y);
        this.permanentResourceOne = permanentResourceOne;
        this.permanentResourceTwo = permanentResourceTwo;
    }

    // Getter and Setter
    public Resource getPermanentResourceOne() {
        return permanentResourceOne;
    }

    public void setPermanentResourceOne(Resource permanentResourceOne) {
        this.permanentResourceOne = permanentResourceOne;
    }

    public Resource getPermanentResourceTwo() {
        return permanentResourceTwo;
    }

    public void setPermanentResourceTwo(Resource permanentResourceTwo) {
        this.permanentResourceTwo = permanentResourceTwo;
    }
}
