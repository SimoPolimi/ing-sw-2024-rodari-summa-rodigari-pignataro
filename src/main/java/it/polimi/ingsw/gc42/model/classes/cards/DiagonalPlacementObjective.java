package it.polimi.ingsw.gc42.model.classes.cards;

import java.util.ArrayList;

public class DiagonalPlacementObjective extends PlacementObjective {
    // Attributes
    private boolean isLeftToRight;

    // Constructor Method
    public DiagonalPlacementObjective(int points, KingdomResource primaryType, boolean isLeftToRight) {
        super(points, primaryType);
        this.isLeftToRight = isLeftToRight;
    }

    // Getters and Setters
    public boolean isLeftToRight() {
        return isLeftToRight;
    }

    public void setLeftToRight(boolean leftToRight) {
        isLeftToRight = leftToRight;
    }

    // Methods
    @Override
    protected int check(ArrayList<Card> playArea) {
        //TODO: Implement
        return 0;
    }
}
