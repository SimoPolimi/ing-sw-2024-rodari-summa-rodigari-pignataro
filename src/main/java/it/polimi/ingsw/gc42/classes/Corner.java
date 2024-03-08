package it.polimi.ingsw.gc42.classes;

/**
 * Implementation of Corner for Model
 * A Corner is a part of the Side of a Card.
 * Each Side contains 4 Corners, and each Card contains 8 Corners.
 */
public class Corner {
    // Attributes
    protected boolean isCovered;

    // Constructor

    /**
     * Constructor Method with the option to define the isCovered status
     * USE ONLY if you really need to specify the isCovered status.
     * OTHERWISE USE the default constructor
     * @param isCovered: boolean that shows if that Corner is covered or if it's shown.
     *                 Only shown or un-covered Corners are used to calculate points and determine if a certain
     *                 objective is met or not. Covered Corners are ignored
     */
    public Corner(boolean isCovered) {
        this.isCovered = isCovered;
    }

    /**
     * DEFAULT Constructor Method
     * USE THIS to create a generic Corner
     * isCovered is ALWAYS set to false (default value)
     */
    public Corner() {
        this.isCovered = false;
    }

    // Getters and Setters Methods

    /**
     * Getter Method for isCovered
     * @return isCovered: boolean that shows if a Corner is placed on top of this Corner, covering it
     */
    public boolean isCovered() {
        return isCovered;
    }

    /**
     * Setter Method for isCovered
     * @param covered: boolean that shows if a Corner is placed on top of this Corner, covering it
     */
    public void setCovered(boolean covered) {
        isCovered = covered;
    }
}
