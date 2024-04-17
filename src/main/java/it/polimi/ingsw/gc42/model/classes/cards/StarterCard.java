package it.polimi.ingsw.gc42.model.classes.cards;

import java.util.ArrayList;

/**
 * Implement an extension of PlayableCard. It's one card type
 */
public class StarterCard extends PlayableCard{
    // Attributes

    /**
     * Constructor method for StarterCard
     * @param frontSide: card's front side
     * @param backSide: card's back side
     * @param isFrontFacing: a boolean that means if the selected face is front or back
     * @param id: card identification
     * @param x: card coordinate x
     * @param y card coordinate y
     * @param frontImage: image of the front face
     * @param backImage: image of the back face
     */
    // Constructor Method
    public StarterCard(CardSide frontSide, CardSide backSide, boolean isFrontFacing, int id, int x, int y,
                       ArrayList<Item> permanentResources, int earnedPoints, String frontImage, String backImage) {
        super(frontSide, backSide, isFrontFacing, permanentResources, earnedPoints, id, x, y, frontImage, backImage);

    }
    /**
     * Constructor method for StarterCard
     * @param frontSide: card's front side
     * @param backSide: card's back side
     * @param isFrontFacing: a boolean that means if the selected face is front or back
     * @param id: card identification
     * @param frontImage: image of the front face
     * @param backImage: image of the back face
     */
    public StarterCard(CardSide frontSide, CardSide backSide, boolean isFrontFacing, int id,
                       ArrayList<Item> permanentResources, int earnedPoints, String frontImage, String backImage) {
        super(frontSide, backSide, isFrontFacing, permanentResources, earnedPoints, id, frontImage, backImage);
    }

    @Override
    public boolean canBePlaced(ArrayList<PlayableCard> playedCards) {
        return true;
    }

    // Getter and Setter

}
