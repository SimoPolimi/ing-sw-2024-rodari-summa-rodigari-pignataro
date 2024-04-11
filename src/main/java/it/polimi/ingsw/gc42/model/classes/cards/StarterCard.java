package it.polimi.ingsw.gc42.model.classes.cards;

public class StarterCard extends PlayableCard{
    // Attributes
    private KingdomResource permanentResourceOne;
    private KingdomResource permanentResourceTwo;
    private KingdomResource permanentResourceThree;

    // Constructor Method
    public StarterCard(CardSide frontSide, CardSide backSide, boolean isFrontFacing, int id, int x, int y, KingdomResource permanentResourceOne, KingdomResource permanentResourceTwo,
                       KingdomResource permanentResourceThree, String frontImage, String backImage) {
        super(frontSide, backSide, isFrontFacing, id, x, y, frontImage, backImage);
        this.permanentResourceOne = permanentResourceOne;
        this.permanentResourceTwo = permanentResourceTwo;
        this.permanentResourceThree = permanentResourceThree;
    }

    public StarterCard(CardSide frontSide, CardSide backSide, boolean isFrontFacing, int id, KingdomResource permanentResourceOne, KingdomResource permanentResourceTwo,
                       KingdomResource permanentResourceThree, String frontImage, String backImage) {
        super(frontSide, backSide, isFrontFacing, id, frontImage, backImage);
        this.permanentResourceOne = permanentResourceOne;
        this.permanentResourceTwo = permanentResourceTwo;
        this.permanentResourceThree = permanentResourceThree;
    }

    // Getter and Setter
    public KingdomResource getPermanentResourceOne() {
        return permanentResourceOne;
    }

    public void setPermanentResourceOne(KingdomResource permanentResourceOne) {
        this.permanentResourceOne = permanentResourceOne;
    }

    public KingdomResource getPermanentResourceTwo() {
        return permanentResourceTwo;
    }

    public void setPermanentResourceTwo(KingdomResource permanentResourceTwo) {
        this.permanentResourceTwo = permanentResourceTwo;
    }

    public KingdomResource getPermanentResourceThree() {
        return permanentResourceThree;
    }

    public void setPermanentResourceThree(KingdomResource permanentResourceThree) {
        this.permanentResourceThree = permanentResourceThree;
    }
}
