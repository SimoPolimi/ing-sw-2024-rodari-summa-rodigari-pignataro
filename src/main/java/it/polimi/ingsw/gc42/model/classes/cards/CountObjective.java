package it.polimi.ingsw.gc42.model.classes.cards;

/**
 * Model representation of a specific subsection of Objectives that require to count the number of something inside the Play Area.
 * Abstract Class: can't create a CountObjective, but one of its children: CornerCountObjective, KingdomCountObjective,
 * ResourceCountObjective or SetItemCountObjective.
 */
public abstract class CountObjective extends Objective {
    // Attributes
    private int number;

    // Constructor Method

    /**
     * Constructor Method
     * @param points: the number of points the Objective gives every time the Condition is met.
     * @param number: the number of times the thing has to be counted fot the Condition to be satisfied once.
     * @param description: a String containing the description of the Objective, used to display it in the GUI.
     */
    public CountObjective(int points, int number, String name, String description) {
        super(points, name, description);
        this.number = number;
    }

    // Getters and Setters

    /**
     * Getter Method for number
     * @return the number of times the thing has to be counted fot the Condition to be satisfied once.
     */
    public int getNumber() {
        return number;
    }

    /**
     * Setter Method for number
     * @param number: the number of times the thing has to be counted fot the Condition to be satisfied once.
     */
    public void setNumber(int number) {
        this.number = number;
    }

}
