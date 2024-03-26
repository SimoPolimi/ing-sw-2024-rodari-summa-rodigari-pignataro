package it.polimi.ingsw.gc42.model.classes.cards;

/**
 * Model representation of a specific subsection of Objectives that requires to count the number of times the
 * Player has placed his Cards in a specific configuration.
 * Abstract Class: can't create a CountObjective, but one of its children: DiagonalPlacingObjective
 * or LShapedPlacingObjective.
 */
public abstract class PlacementObjective extends Objective {
    // Attributes
    private KingdomResource primaryType;

    // Constructor Method
    /**
     * Constructor Method
     * @param points: the number of points the Objective gives every time the Condition is met.
     * @param description: a String containing the description of the Objective, used to display it in the GUI.
     * @param primaryType: the KingdomResource indicating the color of the Cards required for the Objective.
     */
    public PlacementObjective(int points, String name, String description, KingdomResource primaryType) {
        super(points, name, description);
        this.primaryType = primaryType;
    }

    // Getters and Setters

    /**
     * Getter Method for primaryType.
     * @return the KingdomResource indicating the color of the Cards required for the Objective.
     */
    public KingdomResource getPrimaryType() {
        return primaryType;
    }

    /**
     * Setter Method for primaryType.
     * @param primaryType: the KingdomResource indicating the color of the Cards required for the Objective.
     */
    public void setPrimaryType(KingdomResource primaryType) {
        this.primaryType = primaryType;
    }
}
