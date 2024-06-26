package it.polimi.ingsw.gc42.model.classes.cards;

import java.io.Serializable;

/**
 * Implementation of Corner for Model.
 * A Corner is a part of the CardSide of a Card.
 * Each CardSide contains 4 Corners, so each Card contains 8 Corners.
 */
public class Corner implements Serializable {
    // Attributes
    private boolean isCovered;
    private Item item;

    // Constructor

    /**
     * Constructor Method with the Item inside the Corner (KingdomResource or Resource)
     *
     * @param item the Item inside the Corner
     */
    public Corner(Item item) {
        this.item = item;
        this.isCovered = false;
    }

    /**
     * Constructor Method with the option to define the isCovered status
     * USE ONLY if you really need to specify the isCovered status.
     * OTHERWISE USE the default constructor.
     *
     * @param isCovered  boolean that shows if that Corner is covered or if it's shown.
     *                   Only shown or un-covered Corners are used to calculate points and determine if a certain
     *                   objective is met or not. Covered Corners are ignored
     * @param item the Item inside the Corner
     */
    public Corner(Item item, boolean isCovered) {
        this.item = item;
        this.isCovered = isCovered;
    }

    /**
     * Constructor Method with the option to define the isCovered status
     * DEFAULT Constructor Method.
     * USE THIS to create a generic Corner.
     * isCovered is ALWAYS set to false (default value).
     */
    public Corner() {
        this.item = null;
        this.isCovered = false;
    }

    // Getters and Setters Methods


    /**
     * Getter Method for item
     * @return the item, Resource or Kingdom contained inside the Corner
     */
    public Item getItem() {
        return item;
    }

    /**
     * Setter Method for item
     * @param item the Resource or Kingdom to put inside the Corner
     */
    public void setItem(Item item) {
        this.item = item;
    }

    /**
     * Getter Method for isCovered
     *
     * @return a boolean that shows if a Corner is placed on top of this Corner, covering it
     */
    public boolean isCovered() {
        return isCovered;
    }

    /**
     * Setter Method for isCovered
     *
     * @param covered: boolean that shows if a Corner is placed on top of this Corner, covering it
     */
    public void setCovered(boolean covered) {
        isCovered = covered;
    }
}
