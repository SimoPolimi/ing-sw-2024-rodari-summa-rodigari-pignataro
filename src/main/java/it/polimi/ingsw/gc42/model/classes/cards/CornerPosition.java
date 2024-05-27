package it.polimi.ingsw.gc42.model.classes.cards;

/**
 * Enumeration of all the possible placements of a Corner inside a CardSide.
 */
public enum CornerPosition {
    TOP_LEFT(0, 1),
    TOP_RIGHT(1, 0),
    BOTTOM_LEFT(-1, 0),
    BOTTOM_RIGHT(0,-1);

    private final int xoffset;
    private final int yoffset;
    CornerPosition(int xoffset, int yoffset) {
        this.xoffset = xoffset;
        this.yoffset = yoffset;
    }

    public int getXOffset() { return xoffset; }
    public int getYOffset() { return yoffset; }
}
