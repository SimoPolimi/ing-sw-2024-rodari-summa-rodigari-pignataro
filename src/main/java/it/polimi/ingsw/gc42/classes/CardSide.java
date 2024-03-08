package it.polimi.ingsw.gc42.classes;

public class CardSide {
    // Attributes
    private Corner upperLeftCorner;
    private Corner upperRightCorner;
    private Corner bottomLeftCorner;
    private Corner bottomRightCorner;

    // Constructor Method

    public CardSide(Corner upperLeftCorner, Corner upperRightCorner, Corner bottomLeftCorner, Corner bottomRightCorner) {
        this.upperLeftCorner = upperLeftCorner;
        this.upperRightCorner = upperRightCorner;
        this.bottomLeftCorner = bottomLeftCorner;
        this.bottomRightCorner = bottomRightCorner;
    }

    // Getters and Setters

    public Corner getUpperLeftCorner() {
        return upperLeftCorner;
    }

    public void setUpperLeftCorner(Corner upperLeftCorner) {
        this.upperLeftCorner = upperLeftCorner;
    }

    public Corner getUpperRightCorner() {
        return upperRightCorner;
    }

    public void setUpperRightCorner(Corner upperRightCorner) {
        this.upperRightCorner = upperRightCorner;
    }

    public Corner getBottomLeftCorner() {
        return bottomLeftCorner;
    }

    public void setBottomLeftCorner(Corner bottomLeftCorner) {
        this.bottomLeftCorner = bottomLeftCorner;
    }

    public Corner getBottomRightCorner() {
        return bottomRightCorner;
    }

    public void setBottomRightCorner(Corner bottomRightCorner) {
        this.bottomRightCorner = bottomRightCorner;
    }
}
