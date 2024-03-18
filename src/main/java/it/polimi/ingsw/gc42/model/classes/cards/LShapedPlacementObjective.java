package it.polimi.ingsw.gc42.model.classes.cards;

import java.util.ArrayList;

/**
 * Model implementation of a specific type of Condition/Objective, that requires the Cards to be placed
 * in a shape, consisting of two Cards (of the same Type) on top of each other and another one (of a different Type)
 * attached on a Corner, inside the Play Area.
 */
public class LShapedPlacementObjective extends PlacementObjective {
    // Attributes
    private KingdomResource secondaryType;
    private CornerPosition positionCornerCard;

    // Constructor Method
    /**
     * Constructor Method
     * @param points: the number of points the Condition gives every time it's satisfied
     * @param primaryType: the KingdomResource indicating the color of the two identical Cards required for the Objective.
     * @param secondaryType: the KingdomResource indicating the color of the third Card required for the Objective.
     * @param positionCornerCard: the CornerPosition indicating where the third Card is positioned.
     * @param description: a String containing the Description of the Objective, displayed in the GUI.
     */
    public LShapedPlacementObjective(int points, KingdomResource primaryType, KingdomResource secondaryType, CornerPosition positionCornerCard, String description) {
        super(points,description, primaryType);
        this.secondaryType = secondaryType;
        this.positionCornerCard = positionCornerCard;
    }

    // Getters and Setters

    /**
     * Getter Method for secondaryType.
     * @return the KingdomResource indicating the color of the third Card required for the Objective.
     */
    public KingdomResource getSecondaryType() {
        return secondaryType;
    }

    /**
     * Setter Method for secondaryType.
     * @param secondaryType: the KingdomResource indicating the color of the third Card required for the Objective.
     */
    public void setSecondaryType(KingdomResource secondaryType) {
        this.secondaryType = secondaryType;
    }

    /**
     * Getter Method for positionedCornerCard.
     * @return the CornerPosition indicating where the third Card is positioned.
     */
    public CornerPosition getPositionCornerCard() {
        return positionCornerCard;
    }

    /**
     * Setter Method for positionedCornerCard.
     * @param positionCornerCard: the CornerPosition indicating where the third Card is positioned.
     */
    public void setPositionCornerCard(CornerPosition positionCornerCard) {
        this.positionCornerCard = positionCornerCard;
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
        // TODO: Implement
        return 0;
    }
}
