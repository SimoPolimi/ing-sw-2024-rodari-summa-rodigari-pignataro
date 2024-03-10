package it.polimi.ingsw.gc42.classes.cards;

public class CardSide {
    // Attributes
    // Should upper be renamed to top for better consistency with bottom?
    private Corner upperLeftCorner;
    private Corner upperRightCorner;
    private Corner bottomLeftCorner;
    private Corner bottomRightCorner;

    // Constructor Method

    /**
     * CardSide constructor
     * @param upperLeftCorner        Corner to be placed in the upper left part of the CardSide
     * @param upperRightCorner       Corner to be placed in the upper right part of the CardSide
     * @param bottomLeftCorner       Corner to be placed in the bottom left part of the CardSide
     * @param bottomRightCorner      Corner to be placed in the bottom right part of the CardSide
     */
    public CardSide(Corner upperLeftCorner, Corner upperRightCorner, Corner bottomLeftCorner, Corner bottomRightCorner) {
        this.upperLeftCorner = upperLeftCorner;
        this.upperRightCorner = upperRightCorner;
        this.bottomLeftCorner = bottomLeftCorner;
        this.bottomRightCorner = bottomRightCorner;
    }

    // Getters and Setters

    /**
     * Getter method for upperLeftCorner
     * @return upperLeftCorner: the Corner shown in the upper left part of the CardSide
     */
    public Corner getUpperLeftCorner() {
        return upperLeftCorner;
    }

    /**
     * Setter method for upperLeftCorner
     * @param upperLeftCorner: the Corner that will be placed in the upper left part of the CardSide
     */
    public void setUpperLeftCorner(Corner upperLeftCorner) {
        this.upperLeftCorner = upperLeftCorner;
    }

    /**
     * Getter method for upperRightCorner
     * @return upperRightCorner: the Corner shown in the upper right part of the CardsSide
     */
    public Corner getUpperRightCorner() {
        return upperRightCorner;
    }

    /**
     * Setter method for upperRightCorner
     * @param upperRightCorner: the Corner that will be placed in the upper right part of the CardSide
     */
    public void setUpperRightCorner(Corner upperRightCorner) {
        this.upperRightCorner = upperRightCorner;
    }

    /**
     * Getter method for bottomLeftCorner
     * @return bottomLeftCorner: the Corner shown in the bottom left part of the CardSide
     */
    public Corner getBottomLeftCorner() {
        return bottomLeftCorner;
    }

    /**
     * Setter method for bottomLeftCorner
     * @param bottomLeftCorner: the Corner that will be placed in the bottom left part of the CardSide
     */
    public void setBottomLeftCorner(Corner bottomLeftCorner) {
        this.bottomLeftCorner = bottomLeftCorner;
    }

    /**
     * Getter method for bottomRightCorner
     * @return bottomRightCorner: the Corner shown in the bottom right part of the CardSide
     */
    public Corner getBottomRightCorner() {
        return bottomRightCorner;
    }

    /**
     * Setter method for bottomRightCorner
     * @param bottomRightCorner: the Corner that will be placed in the bottom right part of the CardSide
     */
    public void setBottomRightCorner(Corner bottomRightCorner) {
        this.bottomRightCorner = bottomRightCorner;
    }
}
