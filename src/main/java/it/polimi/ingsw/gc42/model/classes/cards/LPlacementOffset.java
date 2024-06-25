package it.polimi.ingsw.gc42.model.classes.cards;

/**
 * Enumeration of all the possible placements of a Corner inside a CardSide.
 */
public enum LPlacementOffset {
    TOP_LEFT(0, 1, -1),
    TOP_RIGHT(1, 0, -1),
    BOTTOM_LEFT(-1, 0, 1),
    BOTTOM_RIGHT(0,-1, 1);

    private final int xOffset;
    private final int yOffset;
    private final int inlineOffset;
    LPlacementOffset(int xOffset, int yOffset, int inlineOffset) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.inlineOffset = inlineOffset;
    }

    public int getXOffset() { return xOffset; }
    public int getYOffset() { return yOffset; }
    public int getInlineOffset() { return inlineOffset; }
}
