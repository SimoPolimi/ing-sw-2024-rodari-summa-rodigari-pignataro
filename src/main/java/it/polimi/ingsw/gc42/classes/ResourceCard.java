package it.polimi.ingsw.gc42.classes;

import javafx.geometry.Side;

public class ResourceCard extends Card{
    // Attributes
    private Resource permanentResource;
    private int earnedPoints;

    // Constructor Method
    public ResourceCard(CardSide frontSide, CardSide backSide, boolean isFrontFacing, int id, int x, int y, Resource permanentResource, int earnedPoints) {
        super(frontSide, backSide, isFrontFacing, id, x, y);
        this.permanentResource = permanentResource;
        this.earnedPoints = earnedPoints;
    }

    // Getter and Setter
    public Resource getPermanentResource() {
        return permanentResource;
    }

    public void setPermanentResource(Resource permanentResource) {
        this.permanentResource = permanentResource;
    }

    public int getEarnedPoints() {
        return earnedPoints;
    }

    public void setEarnedPoints(int earnedPoints) {
        this.earnedPoints = earnedPoints;
    }
}
