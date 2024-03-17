package it.polimi.ingsw.gc42.model.classes.cards;

import java.util.ArrayList;

/**
 * Model implementation of a specific type of Condition/Objective, that requires to count the number of Corner the
 * current Card is covering on the nearby Cards in the Play Area.
 */
public class CornerCountObjective extends CountObjective{
    // Attributes
    private Card card;

    // Constructor Method

    /**
     * Constructor Method for CornerCountObjective
     * @param points: the number of points the Condition gives every time it's satisfied
     * @param number: the number of Corners that need to be counted to satisfy the Condition once.
     * @param card: the Card that contains this Condition, used to determine who are the nearby Cards to check.
     * @param description: a String containing the Description of the Objective, displayed in the GUI.
     */
    public CornerCountObjective(int points, int number, Card card, String description) {
        super(points, number, description);
        this.card = card;
    }

    // Getters and Setters

    /**
     * Getter Method for card
     * @return the Card that contains this Objective/Condition, used to determine who are the nearby Cards to check.
     */
    public Card getCard() {
        return card;
    }

    /**
     * Setter Method for card
     * @param card: the Card that contains this Objective/Condition, used to determine who are the nearby Cards to check.
     */
    public void setCard(Card card) {
        this.card = card;
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
        //TODO: Implement (check if Corners of nearby Cards are covered)
        return 0;
    }
}
