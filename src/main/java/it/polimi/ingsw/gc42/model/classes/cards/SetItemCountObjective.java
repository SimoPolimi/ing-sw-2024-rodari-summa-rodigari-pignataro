package it.polimi.ingsw.gc42.model.classes.cards;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class SetItemCountObjective extends CountObjective{

    // Constructor Method
    public SetItemCountObjective(int points, int number, String description) {
        super(points, number, description);
    }

    // Methods
    @Override
    protected int check(ArrayList<Card> playArea) {
        HashMap<Resource, Integer> items = new HashMap<>();
        items.put(Resource.FEATHER, 0);
        items.put(Resource.POTION, 0);
        items.put(Resource.SCROLL, 0);
        for (Card card : playArea) {
            if (card.getShowingSide().getUpperLeftCorner() instanceof ResourceCorner) {
                items.put(((ResourceCorner) card.getShowingSide().getUpperLeftCorner()).getResource(),
                        items.get(((ResourceCorner) card.getShowingSide().getUpperLeftCorner()).getResource()) + 1);
            }
            if (card.getShowingSide().getUpperRightCorner() instanceof ResourceCorner) {
                items.put(((ResourceCorner) card.getShowingSide().getUpperRightCorner()).getResource(),
                        items.get(((ResourceCorner) card.getShowingSide().getUpperRightCorner()).getResource()) + 1);
            }
            if (card.getShowingSide().getBottomLeftCorner() instanceof ResourceCorner) {
                items.put(((ResourceCorner) card.getShowingSide().getBottomLeftCorner()).getResource(),
                        items.get(((ResourceCorner) card.getShowingSide().getBottomLeftCorner()).getResource()) + 1);
            }
            if (card.getShowingSide().getBottomRightCorner() instanceof ResourceCorner) {
                items.put(((ResourceCorner) card.getShowingSide().getBottomRightCorner()).getResource(),
                        items.get(((ResourceCorner) card.getShowingSide().getBottomRightCorner()).getResource()) + 1);
            }
        }
        return (int) Math.floor((Collections.min(items.values()).doubleValue() / items.size()));
    }
}
