package it.polimi.ingsw.gc42.model.classes.cards;

import java.io.Serializable;

/**
 * Implementation of CardSide for Model.
 * A CardSide contains the four corners displayed on the side of a Card.
 * Each Corner can be either a KingdomCorner, a ResourceCorner, an EmptyCorner or null, in case there is no Corner.
 *
 * @param topLeftCorner Corner in the upper left vertex of the CardSide
 * @param bottomLeftCorner Corner in the lower left vertex of the CardSide
 * @param topRightCorner Corner in the upper right vertex of the CardSide
 * @param bottomRightCorner Corner in the lower right vertex of the CardSide
 */
public record CardSide(Corner topLeftCorner, Corner topRightCorner, Corner bottomLeftCorner,
                       Corner bottomRightCorner) implements Serializable {
    // Constructor Method

    /**
     * CardSide constructor.
     * If there is no corner (cards cannot be placed on top of that corner) pass a null parameter.
     *
     * @param topLeftCorner:     Corner to be placed in the upper left part of the CardSide
     * @param topRightCorner:    Corner to be placed in the upper right part of the CardSide
     * @param bottomLeftCorner:  Corner to be placed in the bottom left part of the CardSide
     * @param bottomRightCorner: Corner to be placed in the bottom right part of the CardSide
     */
    public CardSide {
    }

    // Getters and Setters

    /**
     * Getter method for topLeftCorner
     *
     * @return the Corner shown in the upper left part of the CardSide
     */
    @Override
    public Corner topLeftCorner() {
        return topLeftCorner;
    }

    /**
     * Getter method for topRightCorner
     *
     * @return the Corner shown in the upper right part of the CardsSide
     */
    @Override
    public Corner topRightCorner() {
        return topRightCorner;
    }

    /**
     * Getter method for bottomLeftCorner
     *
     * @return bottomLeftCorner: the Corner shown in the lower left part of the CardSide
     */
    @Override
    public Corner bottomLeftCorner() {
        return bottomLeftCorner;
    }

    /**
     * Getter method for bottomRightCorner
     *
     * @return bottomRightCorner: the Corner shown in the lower right part of the CardSide
     */
    @Override
    public Corner bottomRightCorner() {
        return bottomRightCorner;
    }

}
