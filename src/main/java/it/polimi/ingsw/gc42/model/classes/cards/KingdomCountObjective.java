package it.polimi.ingsw.gc42.model.classes.cards;

import java.util.ArrayList;

/**
 * Model implementation of a specific type of Condition/Objective, that requires to count the number of KingdomResources
 * inside the Corners of the Cards inside the Play Area that are visible.
 */
public class KingdomCountObjective extends CountObjective{
    // Attributes
    private KingdomResource kingdom;

    // Constructor Method
    /**
     * Constructor Method.
     * @param points: the number of points the Condition gives every time it's satisfied.
     * @param number: the number of Corners that need to be counted to satisfy the Condition once.
     * @param kingdom: the KingdomResource that needs to be counted.
     * @param description: a String containing the Description of the Objective, displayed in the GUI.
     */
    public KingdomCountObjective(int points, int number, KingdomResource kingdom, String description) {
        super(points, number, description);
        this.kingdom = kingdom;
    }

    // Getters and Setters

    /**
     * Getter Method for kingdom.
     * @return the KingdomResource that the Objective is counting.
     */
    public KingdomResource getKingdom() {
        return kingdom;
    }

    /**
     * Setter Method for kingdom.
     * @param kingdom: the KingdomResource that the Objective is counting.
     */
    public void setKingdom(KingdomResource kingdom) {
        this.kingdom = kingdom;
    }

    // Methods
    /**
     * Method inherited from Objective.
     * Checks if the Condition has been met and how many times.
     * @param playArea: the ArrayList of Cards containing all the Cards the Player has played.
     * @return the number of times the Condition has been satisfied (0 if it hasn't been).
     */
    @Override
    protected int check(ArrayList<Card> playArea) {
        int count = 0;
        for (Card card: playArea) {
            if ((card.getShowingSide().getTopLeftCorner() instanceof KingdomCorner &&
                    ((KingdomCorner) card.getShowingSide().getTopLeftCorner()).getKingdom() == kingdom) ||
                    (card.getShowingSide().getTopRightCorner() instanceof KingdomCorner
                            && ((KingdomCorner) card.getShowingSide().getTopRightCorner()).getKingdom() == kingdom) ||
                    (card.getShowingSide().getBottomLeftCorner() instanceof KingdomCorner
                            && ((KingdomCorner) card.getShowingSide().getBottomLeftCorner()).getKingdom() == kingdom) ||
                    (card.getShowingSide().getBottomRightCorner() instanceof KingdomCorner
                            && ((KingdomCorner) card.getShowingSide().getBottomRightCorner()).getKingdom() == kingdom) ||
                    (card instanceof GoldCard && ((GoldCard) card).getPermanentResource() == kingdom) ||
                    (card instanceof ResourceCard && ((ResourceCard) card).getPermanentResource() == kingdom)) {
                count++;
            }
        }
        return count;
    }
}
