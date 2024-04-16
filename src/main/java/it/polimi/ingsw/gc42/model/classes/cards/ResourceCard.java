package it.polimi.ingsw.gc42.model.classes.cards;

import javafx.scene.image.Image;

import java.util.ArrayList;

/**
 * Implementation of ResourceCard for Model
 * A type of Card that can be placed without conditions.
 * Its corners can be empty or hold a resource.
 * On the backSide there is always a KingdomResource in the center, making it impossible to cover.
 * Placing a ResourceCard can earn the player up to 1 point.
 */
public class ResourceCard extends PlayableCard{
    // Attributes
    private int earnedPoints;

    // Constructor Method

    /**
     * Constructor Method with x and y coordinates.
     * USE THIS ONLY if you need to specify the Card's coordinates.
     * @param frontSide: CardSide to be used as the front side of the Card
     * @param backSide: CardSide to be used as the front side of the Card
     * @param isFrontFacing: boolean to specify which side of the Card is visible ( True -> front, False -> back )
     * @param id: unique identifier for the specific Card
     * @param x: horizontal coordinate for the Card's position on the table (null if not placed)
     * @param y: vertical coordinate for the Card's position on the table (null if not placed)
     * @param permanentResources: ArrayList of Items containing the Permanent Resource(s) shown on the back side of the Card
     * @param earnedPoints: number of points obtained for placing the card
     * @param frontImage: a String containing the Description of the Objective, displayed in the GUI.
     * @param backImage: a String containing the Description of the Objective, displayed in the GUI.
     */
    public ResourceCard(CardSide frontSide, CardSide backSide, boolean isFrontFacing, int id, int x, int y,
                        ArrayList<Item> permanentResources, int earnedPoints, String frontImage, String backImage) {
        super(frontSide, backSide, isFrontFacing, permanentResources, id, x, y, frontImage, backImage);
        this.earnedPoints = earnedPoints;
    }

    /**
     * Constructor Method without x and y coordinates.
     * USE THIS.
     * @param frontSide: CardSide to be used as the front side of the Card
     * @param backSide: CardSide to be used as the front side of the Card
     * @param isFrontFacing: boolean to specify which side of the Card is visible ( True -> front, False -> back )
     * @param id: unique identifier for the specific Card
     * @param permanentResources: ArrayList of Items containing the Permanent Resource(s) shown on the back side of the Card
     * @param earnedPoints: number of points obtained for placing the card
     * @param frontImage: a String containing the Description of the Objective, displayed in the GUI.
     * @param backImage: a String containing the Description of the Objective, displayed in the GUI.
     */
    public ResourceCard(CardSide frontSide, CardSide backSide, boolean isFrontFacing, int id,
                        ArrayList<Item> permanentResources, int earnedPoints, String frontImage, String backImage) {
        super(frontSide, backSide, isFrontFacing, permanentResources, id, frontImage, backImage);
        this.earnedPoints = earnedPoints;
    }
    // Getter and Setter

    /**
     * Getter method for earnedPoints
     * @return the number of points earned by the player when placing the Card
     */
    public int getEarnedPoints() {
        return earnedPoints;
    }

    /**
     * Setter method for earnedPoints
     * @param earnedPoints: the number of points earned by the player when placing the Card
     */
    public void setEarnedPoints(int earnedPoints) {
        this.earnedPoints = earnedPoints;
    }
}