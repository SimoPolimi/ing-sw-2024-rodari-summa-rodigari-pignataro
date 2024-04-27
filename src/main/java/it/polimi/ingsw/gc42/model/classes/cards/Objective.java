package it.polimi.ingsw.gc42.model.classes.cards;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Model representation for all the Objectives.
 * Abstract Class: can't create an Objective, but one of its children.
 */
public abstract class Objective implements Serializable {
    // Attributes
    private int points;
    private String name;
    private String description;

    // Constructor Method
    /**
     * Constructor Method
     * @param points: the number of points the Objective gives every time the Condition is met.
     * @param description: a String containing the description of the Objective, used to display it in the GUI.
     */
    public Objective(int points,String name, String description) {
        this.points = points;
        this.name = name;
        this.description = description;
    }

    // Getters and Setters

    /**
     * Getter Method for points.
     * @return the number of points the Objective gives every time the Condition is met.
     */
    public int getPoints() {
        return points;
    }

    /**
     * Setter Method for points.
     * @param points: the number of points the Objective gives every time the Condition is met.
     */
    public void setPoints(int points) {
        this.points = points;
    }

    /**
     * Getter Method for name.
     * @return a String containing the name of the Objective, used to display it in the GUI.
     */
    public String getName() {
        return name;
    }

    /**
     * Setter Method for name.
     * @param name: a String containing the name of the Objective, used to display it in the GUI.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter Method for description.
     * @return a String containing the description of the Objective, used to display it in the GUI.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Setter Method for description.
     * @param description: a String containing the description of the Objective, used to display it in the GUI.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    // Methods
    /**
     * Checks if the Condition has been met and how many times.
     * Abstract Method: its children need to implement it.
     * @param playArea: the ArrayList of Cards containing all the Cards the Player has played.
     * @return the number of times the Condition has been satisfied (0 if it hasn't been).
     */
    protected abstract int check(ArrayList<PlayableCard> playArea);

    /**
     * Calculates the number of points the Player has earned, based on the Cards he has played.
     * @param playArea: the ArrayList of Cards containing all the Cards the Player has played.
     * @return the total number of points the Player has earned.
     */
    public int calculatePoints(ArrayList<PlayableCard> playArea) {
        return points * check(playArea);
    }
}
