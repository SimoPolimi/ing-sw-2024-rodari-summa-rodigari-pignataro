package it.polimi.ingsw.gc42.model.classes.cards;

/**
 * Implementation of CardSide for Model.
 * A CardSide contains the four corners displayed on the side of a Card.
 * Each Corner can be either a KingdomCorner, a ResourceCorner, an EmptyCorner or null, in case there is no Corner.
 */
public class CardSide {
    // Attributes
    private Corner topLeftCorner;
    private Corner topRightCorner;
    private Corner bottomLeftCorner;
    private Corner bottomRightCorner;

    // Constructor Method

    /**
     * CardSide constructor.
     * If there is no corner (cards cannot be placed on top of that corner) pass a null parameter.
     * @param topLeftCorner: Corner to be placed in the upper left part of the CardSide
     * @param topRightCorner: Corner to be placed in the upper right part of the CardSide
     * @param bottomLeftCorner: Corner to be placed in the bottom left part of the CardSide
     * @param bottomRightCorner: Corner to be placed in the bottom right part of the CardSide
     */
    public CardSide(Corner topLeftCorner, Corner topRightCorner, Corner bottomLeftCorner, Corner bottomRightCorner) {
        this.topLeftCorner = topLeftCorner;
        this.topRightCorner = topRightCorner;
        this.bottomLeftCorner = bottomLeftCorner;
        this.bottomRightCorner = bottomRightCorner;
    }

    // Getters and Setters

    /**
     * Getter method for topLeftCorner
     * @return the Corner shown in the upper left part of the CardSide
     */
    public Corner getTopLeftCorner() {
        return topLeftCorner;
    }

    /**
     * Setter method for topLeftCorner
     * @param topLeftCorner: the Corner that will be placed in the upper left part of the CardSide
     */
    public void setTopLeftCorner(Corner topLeftCorner) {
        this.topLeftCorner = topLeftCorner;
    }

    /**
     * Getter method for topRightCorner
     * @return the Corner shown in the upper right part of the CardsSide
     */
    public Corner getTopRightCorner() {
        return topRightCorner;
    }

    /**
     * Setter method for upperRightCorner
     * @param topRightCorner: the Corner that will be placed in the upper right part of the CardSide
     */
    public void setTopRightCorner(Corner topRightCorner) {
        this.topRightCorner = topRightCorner;
    }

    /**
     * Getter method for bottomLeftCorner
     * @return bottomLeftCorner: the Corner shown in the lower left part of the CardSide
     */
    public Corner getBottomLeftCorner() {
        return bottomLeftCorner;
    }

    /**
     * Setter method for bottomLeftCorner
     * @param bottomLeftCorner: the Corner that will be placed in the lower left part of the CardSide
     */
    public void setBottomLeftCorner(Corner bottomLeftCorner) {
        this.bottomLeftCorner = bottomLeftCorner;
    }

    /**
     * Getter method for bottomRightCorner
     * @return bottomRightCorner: the Corner shown in the lower right part of the CardSide
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
