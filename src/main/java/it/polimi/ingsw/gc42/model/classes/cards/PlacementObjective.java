package it.polimi.ingsw.gc42.model.classes.cards;

public abstract class PlacementObjective extends Objective {
    // Attributes
    private KingdomResource primaryType;

    // Constructor Method
    public PlacementObjective(int points, String description, KingdomResource primaryType) {
        super(points, description);
        this.primaryType = primaryType;
    }

    // Getters and Setters
    public KingdomResource getPrimaryType() {
        return primaryType;
    }

    public void setPrimaryType(KingdomResource primaryType) {
        this.primaryType = primaryType;
    }
}
