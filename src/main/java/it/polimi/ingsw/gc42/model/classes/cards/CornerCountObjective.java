package it.polimi.ingsw.gc42.model.classes.cards;

import java.util.ArrayList;

/**
 * Model implementation of a specific type of Condition/Objective, that requires to count the number of Corner the
 * current Card is covering on the nearby Cards in the Play Area.
 */
public class CornerCountObjective extends CountObjective{
    // Attributes
    private Coordinates coordinates;

    // Constructor Method

    /**
     * Constructor Method for CornerCountObjective
     * @param points: the number of points the Condition gives every time it's satisfied
     * @param number: the number of Corners that need to be counted to satisfy the Condition once.
     * @param description: a String containing the Description of the Objective, displayed in the GUI.
     */
    public CornerCountObjective(int points, int number, Coordinates coordinates, String name,  String description) {
        super(points, number, name, description);
        this.coordinates = coordinates;
    }

    // Getters and Setters

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
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
        int x = coordinates.getX();
        int y = coordinates.getY();
        int points = 0;
        for(PlayableCard c : playArea) {
            if(c.getX() == x+1 && c.getY() == y && c.getShowingSide().getBottomLeftCorner() != null && c.getShowingSide().getBottomLeftCorner().isCovered()) {
                points++;
            }
            if(c.getX() == x && c.getY() == y+1 && c.getShowingSide().getBottomRightCorner() != null && c.getShowingSide().getBottomRightCorner().isCovered()) {
                points++;
            }
            if(c.getX() == x-1 && c.getY() == y && c.getShowingSide().getTopRightCorner() != null && c.getShowingSide().getTopRightCorner().isCovered()) {
                points++;
            }
            if(c.getX() == x && c.getY() == y-1 && c.getShowingSide().getTopLeftCorner() != null && c.getShowingSide().getTopLeftCorner().isCovered()) {
                points++;
            }
        }
        return points;
    }
}
