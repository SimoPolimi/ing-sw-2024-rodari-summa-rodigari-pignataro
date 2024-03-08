package it.polimi.ingsw.gc42.classes.cards;

/**
 * Implementation of a Gold Card for Model
 */
public class GoldCard extends Card{
    // Attributes
    private int plantKingdomPoints;
    private int animalKingdomPoints;
    private int fungiKingdomPoints;
    private int insectKingdomPoints;
    private Objective objective;
    private int earnedPoints;

    // Constructor Method

    /**
     * Constructor Method
     * @param frontSide: Side shown on the front of the Card
     * @param backSide: Side shown on the back of the Card
     * @param isFrontFacing: true if the frontSide is shown, false otherwise
     * @param id: unique identifier for the specific Gold Card
     * @param x: horizontal coordinate for the Card's position on the table
     * @param y: vertical coordinate for the Card's position on the table
     * @param plantKingdomPoints: Plant Type points necessary to place the Card on the table (0 if not necessary)
     * @param animalKingdomPoints: Animal Type points necessary to place the Card on the table (0 if not necessary)
     * @param fungiKingdomPoints: Fungi Type points necessary to place the Card on the table (0 if not necessary)
     * @param insectKingdomPoints: Insect Type points necessary to place the Card on the table (0 if not necessary)
     * @param objective: Objective that defines how the earned points are calculated (null if not present)
     * @param earnedPoints: points the card gives once it's placed (0 if it doesn't give points)
     */
    public GoldCard(CardSide frontSide, CardSide backSide, boolean isFrontFacing, int id, int x, int y, int plantKingdomPoints, int animalKingdomPoints, int fungiKingdomPoints, int insectKingdomPoints, Objective objective, int earnedPoints) {
        super(frontSide, backSide, isFrontFacing, id, x, y);
        this.plantKingdomPoints = plantKingdomPoints;
        this.animalKingdomPoints = animalKingdomPoints;
        this.fungiKingdomPoints = fungiKingdomPoints;
        this.insectKingdomPoints = insectKingdomPoints;
        this.objective = objective;
        this.earnedPoints = earnedPoints;
    }

    // Getter and Setter

    /**
     * Getter Method for plantKingdomPoints
     * @return the number of Plant Type Points the Card requires in order to be placed (0 if not needed)
     */
    public int getPlantKingdomPoints() {
        return plantKingdomPoints;
    }

    /**
     * Setter Method for plantKingdomPoints
     * @param plantKingdomPoints: number of Plant Type points required in order to be placed (0 if not needed)
     */
    public void setPlantKingdomPoints(int plantKingdomPoints) {
        this.plantKingdomPoints = plantKingdomPoints;
    }

    /**
     * Getter Method for animalKingdomPoints
     * @return the number of Animal Type Points the Card requires in order to be placed (0 if not needed)
     */
    public int getAnimalKingdomPoints() {
        return animalKingdomPoints;
    }

    /**
     * Setter Method for animalKingdomPoints
     * @param animalKingdomPoints: number of Animal Type points required in order to be placed (0 if not needed)
     */
    public void setAnimalKingdomPoints(int animalKingdomPoints) {
        this.animalKingdomPoints = animalKingdomPoints;
    }

    /**
     * Getter Method for fungiKingdomPoints
     * @return the number of Fungi Type Points the Card requires in order to be placed (0 if not needed)
     */
    public int getFungiKingdomPoints() {
        return fungiKingdomPoints;
    }

    /**
     * Setter Method for fungiKingdomPoints
     * @param fungiKingdomPoints: number of FUngi Type Points required in order to be placed (0 if not needed)
     */
    public void setFungiKingdomPoints(int fungiKingdomPoints) {
        this.fungiKingdomPoints = fungiKingdomPoints;
    }

    /**
     * Getter Method for insectKingdomPoints
     * @return the number of Insect Type Points the Card requires in order to be placed (0 if not needed)
     */
    public int getInsectKingdomPoints() {
        return insectKingdomPoints;
    }

    /**
     * Setter Method for insectKingdomPoints
     * @param insectKingdomPoints: number of Insect Type points required in order to be placed (0 if not needed)
     */
    public void setInsectKingdomPoints(int insectKingdomPoints) {
        this.insectKingdomPoints = insectKingdomPoints;
    }

    /**
     * Getter Method for objective
     * @return a reference to the Objective used for the earned points calculation (null if none)
     */
    public Objective getObjective() {
        return objective;
    }

    /**
     * Setter Method for objective
     * @param objective: reference to the Objective used for the earned points calculation (null if none)
     */
    public void setObjective(Objective objective) {
        this.objective = objective;
    }

    /**
     * Getter Method for earnedPoints
     * @return the number of points the Card gives once placed (before any calculation made with the objective)
     * (0 if the Card does not give points)
     */
    public int getEarnedPoints() {
        return earnedPoints;
    }

    /**
     * Setter Method for earnedPoints
     * @param earnedPoints: the number of points the Card gives once placed (before any calculation made with the objective)
     *      * (0 if the Card does not give points)
     */
    public void setEarnedPoints(int earnedPoints) {
        this.earnedPoints = earnedPoints;
    }
}
