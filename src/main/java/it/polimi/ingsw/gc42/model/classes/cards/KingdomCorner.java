package it.polimi.ingsw.gc42.model.classes.cards;

/**
 * Implementation of a Corner containing a KingdomResource Type Resource for Model
 */
public class KingdomCorner extends Corner{
    // Attributes
    private KingdomResource kingdom;

    // Constructor Method

    /**
     * Constructor Method
     * @param kingdom: KingdomResource Type to assign to the Resource the Corner contains.
     *               All KingdomResource Corners are covered by default.
     */
    public KingdomCorner(KingdomResource kingdom) {
        this.kingdom = kingdom;
        this.isCovered = false;
    }

    // Getter and Setter

    /**
     * Getter Method for kingdom
     * @return the KingdomResource Type of the Resource contained inside the Corner
     */
    public KingdomResource getKingdom() {
        return kingdom;
    }

    /**
     * Setter Method for kingdom
     * @param kingdom: the KingdomResource Type of the Resource contained inside the Corner
     */
    public void setKingdom(KingdomResource kingdom) {
        this.kingdom = kingdom;
    }
}
