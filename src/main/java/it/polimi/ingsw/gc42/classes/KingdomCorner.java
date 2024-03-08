package it.polimi.ingsw.gc42.classes;

/**
 * Implementation of a Corner containing a Kingdom Type Resource for Model
 */
public class KingdomCorner extends Corner{
    // Attributes
    private Kingdom kingdom;

    // Constructor Method

    /**
     * Constructor Method
     * @param kingdom: Kingdom Type to assign to the Resource the Corner contains.
     *               All Kingdom Corners are covered by default.
     */
    public KingdomCorner(Kingdom kingdom) {
        this.kingdom = kingdom;
        this.isCovered = false;
    }

    // Getter and Setter

    /**
     * Getter Method for kingdom
     * @return the Kingdom Type of the Resource contained inside the Corner
     */
    public Kingdom getKingdom() {
        return kingdom;
    }

    /**
     * Setter Method for kingdom
     * @param kingdom: the Kingdom Type of the Resource contained inside the Corner
     */
    public void setKingdom(Kingdom kingdom) {
        this.kingdom = kingdom;
    }
}
