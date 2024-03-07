package it.polimi.ingsw.gc42.classes;

public class KingdomCorner extends Corner{
    // Attributes
    private Kingdom kingdom;

    // Constructor Method
    public KingdomCorner(Kingdom kingdom) {
        this.kingdom = kingdom;
        this.isCovered = false;
    }

    // Getter and Setter
    public Kingdom getKingdom() {
        return kingdom;
    }

    public void setKingdom(Kingdom kingdom) {
        this.kingdom = kingdom;
    }
}
