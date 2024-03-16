package it.polimi.ingsw.gc42.model.classes.cards;

public class StarterCard extends Card{
    // Attributes
    private Resource permanentResourceOne;
    private Resource permanentResourceTwo;

    // Constructor Method
    public StarterCard(CardSide frontSide, CardSide backSide, boolean isFrontFacing, int id, int x, int y, Resource permanentResourceOne, Resource permanentResourceTwo,
                       String frontImage, String backImage) {
        super(frontSide, backSide, isFrontFacing, id, x, y, frontImage, backImage);
        this.permanentResourceOne = permanentResourceOne;
        this.permanentResourceTwo = permanentResourceTwo;
    }

    public StarterCard(CardSide frontSide, CardSide backSide, boolean isFrontFacing, int id, Resource permanentResourceOne, Resource permanentResourceTwo,
                       String frontImage, String backImage) {
        super(frontSide, backSide, isFrontFacing, id, frontImage, backImage);
        this.permanentResourceOne = permanentResourceOne;
        this.permanentResourceTwo = permanentResourceTwo;
    }

    // Getter and Setter
    public Resource getPermanentResourceOne() {
        return permanentResourceOne;
    }

    public void setPermanentResourceOne(Resource permanentResourceOne) {
        this.permanentResourceOne = permanentResourceOne;
    }

    public Resource getPermanentResourceTwo() {
        return permanentResourceTwo;
    }

    public void setPermanentResourceTwo(Resource permanentResourceTwo) {
        this.permanentResourceTwo = permanentResourceTwo;
    }
}
