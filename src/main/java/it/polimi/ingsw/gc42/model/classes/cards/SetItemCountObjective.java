package it.polimi.ingsw.gc42.model.classes.cards;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Model implementation of a specific type of Condition/Objective, that requires to count the number of sets of different
 * Resource items inside the Corners of the Cards inside the Play Area that are visible.
 */
public class SetItemCountObjective extends CountObjective{

    //TODO: Update as in UML

    // Constructor Method
    /**
     * Constructor Method.
     * @param points: the number of points the Condition gives every time it's satisfied.
     * @param number: the number of Corners that need to be counted to satisfy the Condition once.
     * @param description: a String containing the Description of the Objective, displayed in the GUI.
     */
    public SetItemCountObjective(int points, int number, String name, String description) {
        super(points, number, name, description);
    }

    // Methods
    /**
     * Method inherited from Objective.
     * Checks if the Condition has been met and how many times.
     * @param playArea: the ArrayList of Cards containing all the Cards the Player has played.
     * @return the number of times the Condition has been satisfied (0 if it hasn't been).
     */
    @Override
    protected int check(ArrayList<PlayableCard> playArea) {
        HashMap<Item, Integer> items = new HashMap<>();
        items.put(Resource.FEATHER, 0);
        items.put(Resource.POTION, 0);
        items.put(Resource.SCROLL, 0);
        for (PlayableCard card : playArea) {
            if (card.getShowingSide().getTopLeftCorner() != null && card.getShowingSide().getTopLeftCorner().getItem() instanceof Resource
                    && !card.getShowingSide().getTopLeftCorner().isCovered()) {
                items.put((card.getShowingSide().getTopLeftCorner()).getItem(),
                        items.get((card.getShowingSide().getTopLeftCorner()).getItem()) + 1);
            }
            if (card.getShowingSide().getTopRightCorner() != null && card.getShowingSide().getTopRightCorner().getItem() instanceof Resource
                    && !card.getShowingSide().getTopRightCorner().isCovered()) {
                items.put((card.getShowingSide().getTopRightCorner()).getItem(),
                        items.get((card.getShowingSide().getTopRightCorner()).getItem()) + 1);
            }
            if (card.getShowingSide().getBottomLeftCorner() != null && card.getShowingSide().getBottomLeftCorner().getItem() instanceof Resource
                    && !card.getShowingSide().getBottomLeftCorner().isCovered()) {
                items.put((card.getShowingSide().getBottomLeftCorner()).getItem(),
                        items.get((card.getShowingSide().getBottomLeftCorner()).getItem()) + 1);
            }
            if (card.getShowingSide().getBottomRightCorner() != null && card.getShowingSide().getBottomRightCorner().getItem() instanceof Resource
                    && !card.getShowingSide().getBottomRightCorner().isCovered()) {
                items.put((card.getShowingSide().getBottomRightCorner()).getItem(),
                        items.get((card.getShowingSide().getBottomRightCorner()).getItem()) + 1);
            }
        }
        return (int) Math.floor((Collections.min(items.values()).doubleValue() / items.size()));
    }
}
