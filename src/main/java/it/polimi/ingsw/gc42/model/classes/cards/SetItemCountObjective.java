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
            if (card.getShowingSide().topLeftCorner() != null && card.getShowingSide().topLeftCorner().getItem() instanceof Resource
                    && !card.getShowingSide().topLeftCorner().isCovered()) {
                items.put((card.getShowingSide().topLeftCorner()).getItem(),
                        items.get((card.getShowingSide().topLeftCorner()).getItem()) + 1);
            }
            if (card.getShowingSide().topRightCorner() != null && card.getShowingSide().topRightCorner().getItem() instanceof Resource
                    && !card.getShowingSide().topRightCorner().isCovered()) {
                items.put((card.getShowingSide().topRightCorner()).getItem(),
                        items.get((card.getShowingSide().topRightCorner()).getItem()) + 1);
            }
            if (card.getShowingSide().bottomLeftCorner() != null && card.getShowingSide().bottomLeftCorner().getItem() instanceof Resource
                    && !card.getShowingSide().bottomLeftCorner().isCovered()) {
                items.put((card.getShowingSide().bottomLeftCorner()).getItem(),
                        items.get((card.getShowingSide().bottomLeftCorner()).getItem()) + 1);
            }
            if (card.getShowingSide().bottomRightCorner() != null && card.getShowingSide().bottomRightCorner().getItem() instanceof Resource
                    && !card.getShowingSide().bottomRightCorner().isCovered()) {
                items.put((card.getShowingSide().bottomRightCorner()).getItem(),
                        items.get((card.getShowingSide().bottomRightCorner()).getItem()) + 1);
            }
        }
        return (int) Math.floor((Collections.min(items.values()).doubleValue() / items.size()));
    }
}
