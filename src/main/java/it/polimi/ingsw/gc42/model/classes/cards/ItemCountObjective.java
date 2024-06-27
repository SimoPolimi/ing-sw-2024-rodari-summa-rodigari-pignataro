package it.polimi.ingsw.gc42.model.classes.cards;

import java.util.ArrayList;

/**
 * Model implementation of a specific type of Condition/Objective, that requires to count the number of Resources
 * inside the Corners of the Cards inside the Play Area that are visible.
 */
public class ItemCountObjective extends CountObjective{
    // Attributes
    private final Item item;

    // Constructor Method

    /**
     * Constructor Method.
     * @param points: the number of points the Condition gives every time it's satisfied.
     * @param number: the number of Corners that need to be counted to satisfy the Condition once.
     * @param item: the Resource or Kingdom that needs to be counted.
     * @param description: a String containing the Description of the Objective, displayed in the GUI.
     */
    public ItemCountObjective(int points, int number, Item item, String name, String description) {
        super(points, number, name, description);
        this.item = item;
    }

    // Getters and Setters

    /**
     * Getter Method for item.
     * @return the item, Resource or Kingdom that the Objective is counting.
     */
    public Item getItem() {
        return item;
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
        int count = 0;
        for (PlayableCard card: playArea) {
            if ((card.getShowingSide().topLeftCorner() != null &&
                    (card.getShowingSide().topLeftCorner()).getItem() == item
                    && !card.getShowingSide().topLeftCorner().isCovered())) {
                count++;
            }
            if (card.getShowingSide().topRightCorner() != null &&
                    (card.getShowingSide().topRightCorner()).getItem() == item
                    && !card.getShowingSide().topRightCorner().isCovered()) {
                count++;
            }
            if (card.getShowingSide().bottomLeftCorner() != null
                    && (card.getShowingSide().bottomLeftCorner()).getItem() == item
                    && !card.getShowingSide().bottomLeftCorner().isCovered()) {
                count++;
            }
            if (card.getShowingSide().bottomRightCorner() != null
                    && (card.getShowingSide().bottomRightCorner()).getItem() == item
                    && !card.getShowingSide().bottomRightCorner().isCovered()) {
                count++;
            }
            if (!card.isFrontFacing() && card instanceof GoldCard && card.getPermanentResources().getFirst().equals(item)) {
                count++;
            }
            if (!card.isFrontFacing() && card instanceof ResourceCard && card.getPermanentResources().getFirst().equals(item)) {
                count++;
            }
            if (card.isFrontFacing() && card instanceof StarterCard) {
                for (Item i: card.getPermanentResources()) {
                    if (i == item) {
                        count++;
                    }
                }
            }
        }
        return Math.floorDiv(count, getNumber());
    }
}
