package it.polimi.ingsw.gc42.model.classes.cards;

import javafx.scene.image.Image;

/**
 * Implementation of ResourceCard for Model
 * A type of Card that can be placed without conditions.
 * Its corners can be empty or hold a resource.
 * On the backSide there is always a KingdomResource in the center, making it impossible to cover.
 * Placing a ResourceCard can earn the player up to 1 point.
 */
public class ResourceCard extends Card{
    // Attributes
    private KingdomResource permanentResource;
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
     * @param permanentResource: KingdomResource shown on the back side of the Card
     * @param earnedPoints: number of points obtained for placing the card
     * @param frontImage: a String containing the Description of the Objective, displayed in the GUI.
     * @param backImage: a String containing the Description of the Objective, displayed in the GUI.
     */
    public ResourceCard(CardSide frontSide, CardSide backSide, boolean isFrontFacing, int id, int x, int y, KingdomResource permanentResource, int earnedPoints, String frontImage, String backImage) {
        super(frontSide, backSide, isFrontFacing, id, x, y, frontImage, backImage);
        this.permanentResource = permanentResource;
        this.earnedPoints = earnedPoints;
    }

    /**
     * Constructor Method without x and y coordinates.
     * USE THIS.
     * @param frontSide: CardSide to be used as the front side of the Card
     * @param backSide: CardSide to be used as the front side of the Card
     * @param isFrontFacing: boolean to specify which side of the Card is visible ( True -> front, False -> back )
     * @param id: unique identifier for the specific Card
     * @param permanentResource: KingdomResource shown on the back side of the Card
     * @param earnedPoints: number of points obtained for placing the card
     * @param frontImage: a String containing the Description of the Objective, displayed in the GUI.
     * @param backImage: a String containing the Description of the Objective, displayed in the GUI.
     */
    public ResourceCard(CardSide frontSide, CardSide backSide, boolean isFrontFacing, int id, KingdomResource permanentResource, int earnedPoints, String frontImage, String backImage) {
        super(frontSide, backSide, isFrontFacing, id, frontImage, backImage);
        this.permanentResource = permanentResource;
        this.earnedPoints = earnedPoints;
    }
    // Getter and Setter

    /**
     * Getter Method for permanentResource
     * @return the resource that can't be covered, shown on the Card's back side
     */
    public KingdomResource getPermanentResource() {
        if (!isFrontFacing()) {
            return permanentResource;
        } else {
            return null;
        }
    }

    /**
     * Setter Method for permanentResource
     * @param permanentResource: the resource that can't be covered, shown on the Card's back side
     */
    public void setPermanentResource(KingdomResource permanentResource) {
        this.permanentResource = permanentResource;
    }

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