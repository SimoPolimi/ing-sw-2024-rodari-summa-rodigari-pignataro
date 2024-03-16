package it.polimi.ingsw.gc42.model.classes.cards;

import java.util.ArrayList;

public class LShapedPlacementObjective extends PlacementObjective {
    // Attributes
    private KingdomResource secondaryType;
    private CornerPosition positionCornerCard;

    // Constructor Method
    public LShapedPlacementObjective(int points, KingdomResource primaryType, KingdomResource secondaryType, CornerPosition positionCornerCard) {
        super(points, primaryType);
        this.secondaryType = secondaryType;
        this.positionCornerCard = positionCornerCard;
    }

    // Getters and Setters
    public KingdomResource getSecondaryType() {
        return secondaryType;
    }

    public void setSecondaryType(KingdomResource secondaryType) {
        this.secondaryType = secondaryType;
    }

    public CornerPosition getPositionCornerCard() {
        return positionCornerCard;
    }

    public void setPositionCornerCard(CornerPosition positionCornerCard) {
        this.positionCornerCard = positionCornerCard;
    }

    // Methods
    @Override
    protected int check(ArrayList<Card> playArea) {
        // TODO: Implement
        return 0;
    }
}
