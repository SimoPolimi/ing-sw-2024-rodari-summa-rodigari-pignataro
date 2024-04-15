package it.polimi.ingsw.gc42.model.classes.cards;

/**
 * Implementation of cards coordinates. We use this system for put down the card on the player field
 */
public class Coordinates {
    // Attributes
    private int x;
    private int y;

    /**
     * Constructor method
     * @param x: coordinates x
     * @param y: coordinates y
     */
    // Constructor Methods
    public Coordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Empty constructor, sets x=y=0
     */
    public Coordinates() {
        this.x = 0;
        this.y = 0;
    }

    // Getters and Setters

    /**
     * Implement method getter for coordinate x
     * @return x
     */
    public int getX() {
        return x;
    }

    /**
     * Implement method setter for coordinate x
     * @param x: coordinate x
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Implement method getter for coordinate y
     * @return y
     */
    public int getY() {
        return y;
    }

    /**
     * Implement method setter for coordinate y
     * @param y: coordinate y
     */
    public void setY(int y) {
        this.y = y;
    }
}
