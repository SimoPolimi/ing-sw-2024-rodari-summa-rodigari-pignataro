package it.polimi.ingsw.gc42.model.classes.cards;

import java.util.ArrayList;

public class KingdomCountObjective extends CountObjective{
    // Attributes
    private KingdomResource kingdom;

    // Constructor Method
    public KingdomCountObjective(int points, int number, KingdomResource kingdom) {
        super(points, number);
        this.kingdom = kingdom;
    }

    // Getters and Setters
    public KingdomResource getKingdom() {
        return kingdom;
    }

    public void setKingdom(KingdomResource kingdom) {
        this.kingdom = kingdom;
    }

    // Methods
    @Override
    protected int check(ArrayList<Card> playArea) {
        int count = 0;
        for (Card card: playArea) {
            if ((card.getShowingSide().getUpperLeftCorner() instanceof KingdomCorner &&
                    ((KingdomCorner) card.getShowingSide().getUpperLeftCorner()).getKingdom() == kingdom) ||
                    (card.getShowingSide().getUpperRightCorner() instanceof KingdomCorner
                            && ((KingdomCorner) card.getShowingSide().getUpperRightCorner()).getKingdom() == kingdom) ||
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
