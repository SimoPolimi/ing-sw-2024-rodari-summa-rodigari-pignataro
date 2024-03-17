package it.polimi.ingsw.gc42.model.classes.cards;

import java.util.ArrayList;

/**
 * Model implementation of a specific type of Condition/Objective, that requires the Cards to be placed
 * in the shape of a Diagonal inside the Play Area.
 */
public class DiagonalPlacementObjective extends PlacementObjective {
    // Attributes
    private boolean isLeftToRight;

    // Constructor Method

    /**
     * Constructor Method
     * @param points: the number of points the Condition gives every time it's satisfied
     * @param primaryType: the KingdomResource indicating the color of the Cards required for the Objective.
     * @param isLeftToRight: a boolean indicating if the placement is Top Left to Bottom Right (\) if true, or
     *                     Top Right to Bottom Left (/) if false.
     * @param description: a String containing the Description of the Objective, displayed in the GUI.
     */
    public DiagonalPlacementObjective(int points, KingdomResource primaryType, boolean isLeftToRight, String description) {
        super(points,description, primaryType);
        this.isLeftToRight = isLeftToRight;
    }

    // Getters and Setters

    /**
     * Getter Method for isLeftToRight
     * @return a boolean indicating if the placement is Top Left to Bottom Right (\) if true, or
     *                     Top Right to Bottom Left (/) if false.
     */
    public boolean isLeftToRight() {
        return isLeftToRight;
    }

    /**
     * Setter Method for isLeftToRight
     * @param leftToRight: a boolean indicating if the placement is Top Left to Bottom Right (\) if true, or
     *                   Top Right to Bottom Left (/) if false.
     */
    public void setLeftToRight(boolean leftToRight) {
        isLeftToRight = leftToRight;
    }

    // Methods

    /**
     * Method inherited from Objective.
     * Checks if the Condition has been met and how many times.
     * @param playArea: the ArrayList of Cards containing all the Cards the Player has played.
     * @return the number of times the Condition has been satisfied (0 if it hasn't been).
     */
    @Override
    protected int check(ArrayList<Card> playArea) {
        //TODO: Implement
        return 0;
    }
}
