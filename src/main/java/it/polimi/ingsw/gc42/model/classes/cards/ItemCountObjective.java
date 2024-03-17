package it.polimi.ingsw.gc42.model.classes.cards;

import java.util.ArrayList;

public class ItemCountObjective extends CountObjective{
    // Attributes
    private Resource resource;

    // Constructor Method
    public ItemCountObjective(int points, int number, Resource resource, String description) {
        super(points, number, description);
        this.resource = resource;
    }

    // Getters and Setters
    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    // Methods
    @Override
    protected int check(ArrayList<Card> playArea) {
        int count = 0;
        for (Card card: playArea) {
            if ((card.getShowingSide().getUpperLeftCorner() instanceof ResourceCorner
                    && ((ResourceCorner) card.getShowingSide().getUpperLeftCorner()).getResource() == resource) ||
                    (card.getShowingSide().getUpperRightCorner() instanceof ResourceCorner &&
                            ((ResourceCorner) card.getShowingSide().getUpperRightCorner()).getResource() == resource) ||
                    (card.getShowingSide().getBottomLeftCorner() instanceof  ResourceCorner
                            && ((ResourceCorner) card.getShowingSide().getBottomLeftCorner()).getResource() == resource) ||
                    (card.getShowingSide().getBottomRightCorner() instanceof ResourceCorner
                            && ((ResourceCorner) card.getShowingSide().getBottomRightCorner()).getResource() == resource)) {
                count++;
            }
        }
        return count;
    }
}
