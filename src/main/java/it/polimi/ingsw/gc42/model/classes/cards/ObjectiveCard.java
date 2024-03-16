package it.polimi.ingsw.gc42.model.classes.cards;

/**
 * Implementation of an Objective Card for Model
 */
public class ObjectiveCard extends Card {
    // Attributes
    private int points;
    private ObjectiveEnum objectiveEnum;

    // Constructor Method

    /**
     * Constructor Method
     * @param frontSide: Side shown on the front of the Card
     * @param backSide: Side shown on the back of the Card
     * @param isFrontFacing: true if the Card is showing its front side, false otherwise
     * @param id: unique identifier for the specific Objective Card
     * @param points: points the Card gives for each time the Objective is met
     * @param objectiveEnum: Objective that is used to calculate the total earned points
     */
    public ObjectiveCard(CardSide frontSide, CardSide backSide, boolean isFrontFacing, int id, int points, ObjectiveEnum objectiveEnum, String frontImage, String backImage) {
        super(frontSide, backSide, isFrontFacing, id, frontImage, backImage);
        this.points = points;
        this.objectiveEnum = objectiveEnum;
    }

    // Getters and Setters

    /**
     * Getter Method for points
     * @return the number of points the player earns each time the Objective is met
     */
    public int getPoints() {
        return points;
    }

    /**
     * Setter Method for points
     * @param points: the number of points the player earns each time the Objective is met
     */
    public void setPoints(int points) {
        this.points = points;
    }

    /**
     *
     * @return
     */
    public ObjectiveEnum getObjective() {
        return objectiveEnum;
    }

    public void setObjective(ObjectiveEnum objectiveEnum) {
        this.objectiveEnum = objectiveEnum;
    }
}
