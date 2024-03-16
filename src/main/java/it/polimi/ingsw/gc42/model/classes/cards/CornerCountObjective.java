package it.polimi.ingsw.gc42.model.classes.cards;

import java.util.ArrayList;

public class CornerCountObjective extends CountObjective{
    // Attributes
    private Card card;

    // Constructor Method
    public CornerCountObjective(int points, int number, Card card) {
        super(points, number);
        this.card = card;
    }

    @Override
    protected int check(ArrayList<Card> playArea) {
        //TODO: Implement (check if Corners of nearby Cards are covered)
        return 0;
    }
}
