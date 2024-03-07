package it.polimi.ingsw.gc42.classes;

/**
 * Implementation of Corner for Model
 * A Corner is a part of the Side of a Card.
 * Each Side contains 4 Corners, and each Card contains 8 Corners.
 */
public class Corner {
    // Attributes
    protected boolean isCovered;

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
