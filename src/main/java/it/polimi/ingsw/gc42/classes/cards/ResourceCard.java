package it.polimi.ingsw.gc42.classes.cards;

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
     * Constructor Method
     * @param frontSide            CardSide to be used as the front side of the Card
     * @param backSide             CardSide to be used as the front side of the Card
     * @param isFrontFacing        boolean to specify which side of the Card is visible ( True -> front, False -> back )
     * @param id                   unique identifier for the specific Card
     * @param x                    horizontal coordinate for the Card's position on the table (null if not placed)
     * @param y                    vertical coordinate for the Card's position on the table (null if not placed)
     * @param permanentResource    KingdomResource shown on the back side of the Card
     * @param earnedPoints         number of points obtained for placing the card
     */
    public ResourceCard(CardSide frontSide, CardSide backSide, boolean isFrontFacing, int id, int x, int y, KingdomResource permanentResource, int earnedPoints) {
        super(frontSide, backSide, isFrontFacing, id, x, y);
        this.permanentResource = permanentResource;
        this.earnedPoints = earnedPoints;
    }

    // Getter and Setter

    /**
     * Getter Method for permanentResource
     * @return permanentResource: the resource that can't be covered, shown on the Card's back side
     */
    public KingdomResource getPermanentResource() {
        return permanentResource;
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
     * @return earnedPoints: number of points earned by the player when placing the Card
     */
    public int getEarnedPoints() {
        return earnedPoints;
    }

    /**
     * Setter method for earnedPoints
     * @param earnedPoints: number of points earned by the player when placing the Card
     */
    public void setEarnedPoints(int earnedPoints) {
        this.earnedPoints = earnedPoints;
    }
}
