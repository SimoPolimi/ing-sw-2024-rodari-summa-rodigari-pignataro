package it.polimi.ingsw.gc42.model.classes.cards;

import java.util.ArrayList;

/**
 * Model implementation of a specific type of Condition/Objective, that requires to count the number of Resources
 * inside the Corners of the Cards inside the Play Area that are visible.
 */
public class ItemCountObjective extends CountObjective{
    // Attributes
    private Resource resource;

    // Constructor Method

    /**
     * Constructor Method.
     * @param points: the number of points the Condition gives every time it's satisfied.
     * @param number: the number of Corners that need to be counted to satisfy the Condition once.
     * @param resource: the Resource that needs to be counted.
     * @param description: a String containing the Description of the Objective, displayed in the GUI.
     */
    public ItemCountObjective(int points, int number, Resource resource, String name, String description) {
        super(points, number, name, description);
        this.resource = resource;
    }

    // Getters and Setters

    /**
     * Getter Method for resource.
     * @return the Resource that the Objective is counting.
     */
    public Resource getResource() {
        return resource;
    }

    /**
     * Setter Method for resource.
     * @param resource: the Resource that the Objective is counting.
     */
    public void setResource(Resource resource) {
        this.resource = resource;
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
            if ((card.getShowingSide().getTopLeftCorner() instanceof ResourceCorner
                    && ((ResourceCorner) card.getShowingSide().getTopLeftCorner()).getResource() == resource
                    && !card.getShowingSide().getTopLeftCorner().isCovered()) ||
                    (card.getShowingSide().getTopRightCorner() instanceof ResourceCorner &&
                            ((ResourceCorner) card.getShowingSide().getTopRightCorner()).getResource() == resource
                            && !card.getShowingSide().getTopRightCorner().isCovered()) ||
                    (card.getShowingSide().getBottomLeftCorner() instanceof  ResourceCorner
                            && ((ResourceCorner) card.getShowingSide().getBottomLeftCorner()).getResource() == resource
                            && !card.getShowingSide().getBottomLeftCorner().isCovered()) ||
                    (card.getShowingSide().getBottomRightCorner() instanceof ResourceCorner
                            && ((ResourceCorner) card.getShowingSide().getBottomRightCorner()).getResource() == resource
                            && !card.getShowingSide().getBottomRightCorner().isCovered())) {
                count++;
            }
        }
        return count;
    }
}
