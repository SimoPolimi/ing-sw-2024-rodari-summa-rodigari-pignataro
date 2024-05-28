package it.polimi.ingsw.gc42.model.classes.cards;

/**
 * Enumeration of all the possible placements of a Corner inside a CardSide.
 */
public enum LPlacementOffset {
    TOP_LEFT(0, 1, -1),
    TOP_RIGHT(1, 0, -1),
    BOTTOM_LEFT(-1, 0, 1),
    BOTTOM_RIGHT(0,-1, 1);

    private final int xoffset;
    private final int yoffset;
    private final int inlineOffset;
    LPlacementOffset(int xoffset, int yoffset, int inlineOffset) {
        this.xoffset = xoffset;
        this.yoffset = yoffset;
        this.inlineOffset = inlineOffset;
    }

    public int getXOffset() { return xoffset; }
    public int getYOffset() { return yoffset; }
    public int getInlineOffset() { return inlineOffset; }
}
