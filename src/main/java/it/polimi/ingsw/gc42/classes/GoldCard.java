package it.polimi.ingsw.gc42.classes;

import javafx.geometry.Side;

public class GoldCard extends Card{
    // Attributes
    private int plantKingdomPoints;
    private int animalKingdomPoints;
    private int fungiKingdomPoints;
    private int insectKingdomPoints;
    private Objective objective;

    // Constructor Method

    public GoldCard(Side frontSide, Side backSide, boolean isFrontFacing, int id, int x, int y, int plantKingdomPoints, int animalKingdomPoints, int fungiKingdomPoints, int insectKingdomPoints, Objective objective) {
        super(frontSide, backSide, isFrontFacing, id, x, y);
        this.plantKingdomPoints = plantKingdomPoints;
        this.animalKingdomPoints = animalKingdomPoints;
        this.fungiKingdomPoints = fungiKingdomPoints;
        this.insectKingdomPoints = insectKingdomPoints;
        this.objective = objective;
    }

    // Getter and Setter

    public int getPlantKingdomPoints() {
        return plantKingdomPoints;
    }

    public void setPlantKingdomPoints(int plantKingdomPoints) {
        this.plantKingdomPoints = plantKingdomPoints;
    }

    public int getAnimalKingdomPoints() {
        return animalKingdomPoints;
    }

    public void setAnimalKingdomPoints(int animalKingdomPoints) {
        this.animalKingdomPoints = animalKingdomPoints;
    }

    public int getFungiKingdomPoints() {
        return fungiKingdomPoints;
    }

    public void setFungiKingdomPoints(int fungiKingdomPoints) {
        this.fungiKingdomPoints = fungiKingdomPoints;
    }

    public int getInsectKingdomPoints() {
        return insectKingdomPoints;
    }

    public void setInsectKingdomPoints(int insectKingdomPoints) {
        this.insectKingdomPoints = insectKingdomPoints;
    }

    public Objective getObjective() {
        return objective;
    }

    public void setObjective(Objective objective) {
        this.objective = objective;
    }
}
