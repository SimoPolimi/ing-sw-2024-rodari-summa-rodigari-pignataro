package it.polimi.ingsw.gc42.model.classes.cards;

import java.util.HashMap;

/**
 * Implementation of a Gold Card for Model
 */
public class GoldCard extends Card{
    // Attributes
    private final HashMap<String, Integer> costs = new HashMap<>();
    private Objective objective;
    private int earnedPoints;
    private KingdomResource permanentResource;

    // Constructor Methods
    /**
     * Constructor Method with the x and y coordinates
     * @param frontSide: Side shown on the front of the Card
     * @param backSide: Side shown on the back of the Card
     * @param isFrontFacing: true if the frontSide is shown, false otherwise
     * @param id: unique identifier for the specific Gold Card
     * @param x: horizontal coordinate for the Card's position on the table
     * @param y: vertical coordinate for the Card's position on the table
     * @param permanentResource: KingdomResource displayed at the center of the back CardSide, also used to determine the Kingdom of the Card.
     * @param plantKingdomPoints: Plant Type points necessary to place the Card on the table (0 if not necessary)
     * @param animalKingdomPoints: Animal Type points necessary to place the Card on the table (0 if not necessary)
     * @param fungiKingdomPoints: Fungi Type points necessary to place the Card on the table (0 if not necessary)
     * @param insectKingdomPoints: Insect Type points necessary to place the Card on the table (0 if not necessary)
     * @param objective: the Condition used to calculate how many points the player earns.
     * @param earnedPoints: points the card gives once it's placed (0 if it doesn't give points)
     * @param frontImage: a String containing the Description of the Objective, displayed in the GUI.
     * @param backImage: a String containing the Description of the Objective, displayed in the GUI.
     */
    public GoldCard(CardSide frontSide, CardSide backSide, boolean isFrontFacing, int id, int x, int y,KingdomResource permanentResource, int plantKingdomPoints,
                    int animalKingdomPoints, int fungiKingdomPoints, int insectKingdomPoints, Objective objective, int earnedPoints, String frontImage, String backImage) {
        super(frontSide, backSide, isFrontFacing, id, x, y, frontImage, backImage);
        initMap();
        setCost(KingdomResource.FUNGI, fungiKingdomPoints);
        setCost(KingdomResource.PLANT, plantKingdomPoints);
        setCost(KingdomResource.ANIMAL, animalKingdomPoints);
        setCost(KingdomResource.INSECT, insectKingdomPoints);
        this.objective = objective;
        this.earnedPoints = earnedPoints;
        this.permanentResource = permanentResource;
    }

    /**
     * Constructor Method with the x and y coordinates
     * @param frontSide: Side shown on the front of the Card
     * @param backSide: Side shown on the back of the Card
     * @param isFrontFacing: true if the frontSide is shown, false otherwise
     * @param id: unique identifier for the specific Gold Card
     * @param permanentResource: KingdomResource displayed at the center of the back CardSide, also used to determine the Kingdom of the Card.
     * @param plantKingdomPoints: Plant Type points necessary to place the Card on the table (0 if not necessary)
     * @param animalKingdomPoints: Animal Type points necessary to place the Card on the table (0 if not necessary)
     * @param fungiKingdomPoints: Fungi Type points necessary to place the Card on the table (0 if not necessary)
     * @param insectKingdomPoints: Insect Type points necessary to place the Card on the table (0 if not necessary)
     * @param objective: the Condition used to calculate how many points the player earns.
     * @param earnedPoints: points the card gives once it's placed (0 if it doesn't give points)
     * @param frontImage: a String containing the Description of the Objective, displayed in the GUI.
     * @param backImage: a String containing the Description of the Objective, displayed in the GUI.
     */
    public GoldCard(CardSide frontSide, CardSide backSide, boolean isFrontFacing, int id, KingdomResource permanentResource, int plantKingdomPoints, int animalKingdomPoints,
                    int fungiKingdomPoints, int insectKingdomPoints, Objective objective, int earnedPoints, String frontImage, String backImage) {
        super(frontSide, backSide, isFrontFacing, id, frontImage, backImage);
        initMap();
        setCost(KingdomResource.FUNGI, fungiKingdomPoints);
        setCost(KingdomResource.PLANT, plantKingdomPoints);
        setCost(KingdomResource.ANIMAL, animalKingdomPoints);
        setCost(KingdomResource.INSECT, insectKingdomPoints);
        this.objective = objective;
        this.earnedPoints = earnedPoints;
        this.permanentResource = permanentResource;
    }

    // Getter and Setter

    /**
     * Getter Method for permanentResource
     * @return the KingdomResource displayed at the center of the back CardSide, also used to determine the Kingdom of the Card.
     */
    public KingdomResource getPermanentResource() {
        return permanentResource;
    }

    /**
     * Setter Method for permanentResource.
     * @param permanentResource: the KingdomResource displayed at the center of the back CardSide, also used to determine the Kingdom of the Card.
     */
    public void setPermanentResource(KingdomResource permanentResource) {
        this.permanentResource = permanentResource;
    }

    /**
     * Getter Method for plantKingdomPoints
     * @return the number of Plant Type Points the Card requires in order to be placed (0 if not needed)
     */
    public int getPlantKingdomPoints() {
        return costs.get(KingdomResource.PLANT.toString());
    }

    /**
     * Setter Method for plantKingdomPoints
     * @param plantKingdomPoints: number of Plant Type points required in order to be placed (0 if not needed)
     */
    public void setPlantKingdomPoints(int plantKingdomPoints) {
        setCost(KingdomResource.PLANT, plantKingdomPoints);
    }

    /**
     * Getter Method for animalKingdomPoints
     * @return the number of Animal Type Points the Card requires in order to be placed (0 if not needed)
     */
    public int getAnimalKingdomPoints() {
        return costs.get(KingdomResource.ANIMAL.toString());
    }

    /**
     * Setter Method for animalKingdomPoints
     * @param animalKingdomPoints: number of Animal Type points required in order to be placed (0 if not needed)
     */
    public void setAnimalKingdomPoints(int animalKingdomPoints) {
        setCost(KingdomResource.ANIMAL, animalKingdomPoints);
    }

    /**
     * Getter Method for fungiKingdomPoints
     * @return the number of Fungi Type Points the Card requires in order to be placed (0 if not needed)
     */
    public int getFungiKingdomPoints() {
        return costs.get(KingdomResource.FUNGI.toString());
    }

    /**
     * Setter Method for fungiKingdomPoints
     * @param fungiKingdomPoints: number of FUngi Type Points required in order to be placed (0 if not needed)
     */
    public void setFungiKingdomPoints(int fungiKingdomPoints) {
        setCost(KingdomResource.FUNGI, fungiKingdomPoints);
    }

    /**
     * Getter Method for insectKingdomPoints
     * @return the number of Insect Type Points the Card requires in order to be placed (0 if not needed)
     */
    public int getInsectKingdomPoints() {
        return costs.get(KingdomResource.INSECT.toString());
    }

    /**
     * Setter Method for insectKingdomPoints
     * @param insectKingdomPoints: number of Insect Type points required in order to be placed (0 if not needed)
     */
    public void setInsectKingdomPoints(int insectKingdomPoints) {
        setCost(KingdomResource.INSECT, insectKingdomPoints);
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
     * (0 if the Card does not give points)
     */
    public void setEarnedPoints(int earnedPoints) {
        this.earnedPoints = earnedPoints;
    }

    // Methods
    private void initMap() {
        costs.put(KingdomResource.FUNGI.toString(), 0);
        costs.put(KingdomResource.ANIMAL.toString(), 0);
        costs.put(KingdomResource.PLANT.toString(), 0);
        costs.put(KingdomResource.INSECT.toString(), 0);
    }

    private void setCost(KingdomResource kingdom, int cost) {
        costs.replace(kingdom.toString(), cost);
    }
}


