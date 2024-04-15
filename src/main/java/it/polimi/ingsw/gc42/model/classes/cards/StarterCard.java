package it.polimi.ingsw.gc42.model.classes.cards;

import java.util.ArrayList;

/**
 * Implement an extension of PlayableCard. It's one card type
 */
public class StarterCard extends PlayableCard{
    // Attributes
    private final ArrayList<Item> permanentResources = new ArrayList<>();
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
    public StarterCard(CardSide frontSide, CardSide backSide, boolean isFrontFacing, int id, int x, int y, ArrayList<Item> list, String frontImage, String backImage) {
        super(frontSide, backSide, isFrontFacing, id, x, y, frontImage, backImage);
        for (Item item: list){
            addPermanentResource(item);
        }
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
    public StarterCard(CardSide frontSide, CardSide backSide, boolean isFrontFacing, int id, ArrayList<Item> list, String frontImage, String backImage) {
        super(frontSide, backSide, isFrontFacing, id, frontImage, backImage);
        for (Item item: list) {
            addPermanentResource(item);
        }
    }

    /**
     * Implement method getter for permanent resource
     * @param index: item position in the array list
     * @return permanentResource in the index position
     * @throws IllegalArgumentException: exception called if the index is invalid
     */
    //Getter and Setter
    public Item getPermanentResource(int index) throws IllegalArgumentException{
        if (index >= 0 && index < permanentResources.size()) {
            return permanentResources.get(index);
        }else{
            throw new IllegalArgumentException();
        }
    }

    /**
     * Implement method setter for permanent resource
     * @param item: is a new item to add at the permanentResources
     */
    public void addPermanentResource(Item item){
        permanentResources.add(item);
    }
}
