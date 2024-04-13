package it.polimi.ingsw.gc42.model.classes.cards;

/**
 * Implementation of an Objective Card for Model.
 */
public class ObjectiveCard extends Card {
    // Attributes
    private int points;
    private Objective objective;

    // Constructor Method
    /**
     * Constructor Method
     * @param id: unique identifier for the specific Objective Card
     * @param objective: the Objective that is used to calculate the total earned points
     * @param frontImage: a String containing the Description of the Objective, displayed in the GUI.
     * @param backImage: a String containing the Description of the Objective, displayed in the GUI.
     */
    public ObjectiveCard(int id, Objective objective, String frontImage, String backImage) {
        super(true, id, frontImage, backImage);
        this.objective = objective;
    }

    // Getters and Setters

    /**
     * Getter Method for objective.
     * @return the Objective that is used to calculate the total earned points
     */
    public Objective getObjective() {
        return objective;
    }

    /**
     * Setter Method for objective.
     * @param objective: the Objective that is used to calculate the total earned points
     */
    public void setObjective(Objective objective) {
        this.objective = objective;
    }
}
