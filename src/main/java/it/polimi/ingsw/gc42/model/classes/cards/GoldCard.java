package it.polimi.ingsw.gc42.model.classes.cards;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Implementation of a Gold Card for Model
 */
public class GoldCard extends PlayableCard{
    // Attributes
    private final HashMap<Item, Integer> costs = new HashMap<>();
    private Objective objective;

    // Constructor Methods
    /**
     * Constructor Method with the x and y coordinates
     *
     * @param frontSide           : Side shown on the front of the Card
     * @param backSide            : Side shown on the back of the Card
     * @param isFrontFacing       : true if the frontSide is shown, false otherwise
     * @param id                  : unique identifier for the specific Gold Card
     * @param x                   : horizontal coordinate for the Card's position on the table
     * @param y                   : vertical coordinate for the Card's position on the table
     * @param permanentResources  : ArrayList of Items containing the Permanent Resource(s) shown on the back side of the Card
     * @param plantKingdomPoints  : Plant Type points necessary to place the Card on the table (0 if not necessary)
     * @param animalKingdomPoints : Animal Type points necessary to place the Card on the table (0 if not necessary)
     * @param fungiKingdomPoints  : Fungi Type points necessary to place the Card on the table (0 if not necessary)
     * @param insectKingdomPoints : Insect Type points necessary to place the Card on the table (0 if not necessary)
     * @param objective           : the Condition used to calculate how many points the player earns.
     * @param earnedPoints        : points the card gives once it's placed (0 if it doesn't give points)
     * @param frontImage          : a String containing the Description of the Objective, displayed in the GUI.
     * @param backImage           : a String containing the Description of the Objective, displayed in the GUI.
     * @param kingdom             : the kingdom the card belongs to
     */
    public GoldCard(CardSide frontSide, CardSide backSide, boolean isFrontFacing, int id, int x, int y,
                    ArrayList<Item> permanentResources, int plantKingdomPoints,
                    int animalKingdomPoints, int fungiKingdomPoints, int insectKingdomPoints, Objective objective,
                    int earnedPoints, String frontImage, String backImage, KingdomResource kingdom) {
        super(frontSide, backSide, isFrontFacing, permanentResources, earnedPoints, id, x, y, frontImage, backImage, kingdom);
        initMap();
        setCost(KingdomResource.FUNGI, fungiKingdomPoints);
        setCost(KingdomResource.PLANT, plantKingdomPoints);
        setCost(KingdomResource.ANIMAL, animalKingdomPoints);
        setCost(KingdomResource.INSECT, insectKingdomPoints);
        this.objective = objective;
    }

    /**
     * Constructor Method with the x and y coordinates
     *
     * @param frontSide           : Side shown on the front of the Card
     * @param backSide            : Side shown on the back of the Card
     * @param isFrontFacing       : true if the frontSide is shown, false otherwise
     * @param id                  : unique identifier for the specific Gold Card
     * @param permanentResources  : ArrayList of Items containing the Permanent Resource(s) shown on the back side of the Card
     * @param plantKingdomPoints  : Plant Type points necessary to place the Card on the table (0 if not necessary)
     * @param animalKingdomPoints : Animal Type points necessary to place the Card on the table (0 if not necessary)
     * @param fungiKingdomPoints  : Fungi Type points necessary to place the Card on the table (0 if not necessary)
     * @param insectKingdomPoints : Insect Type points necessary to place the Card on the table (0 if not necessary)
     * @param objective           : the Condition used to calculate how many points the player earns.
     * @param earnedPoints        : points the card gives once it's placed (0 if it doesn't give points)
     * @param frontImage          : a String containing the Description of the Objective, displayed in the GUI.
     * @param backImage           : a String containing the Description of the Objective, displayed in the GUI.
     * @param kingdom             : the kingdom the card belongs to
     */
    public GoldCard(CardSide frontSide, CardSide backSide, boolean isFrontFacing, int id,
                    ArrayList<Item> permanentResources, int plantKingdomPoints, int animalKingdomPoints,
                    int fungiKingdomPoints, int insectKingdomPoints, Objective objective, int earnedPoints,
                    String frontImage, String backImage, KingdomResource kingdom) {
        super(frontSide, backSide, isFrontFacing, permanentResources,earnedPoints, id, frontImage, backImage, kingdom);
        initMap();
        setCost(KingdomResource.FUNGI, fungiKingdomPoints);
        setCost(KingdomResource.PLANT, plantKingdomPoints);
        setCost(KingdomResource.ANIMAL, animalKingdomPoints);
        setCost(KingdomResource.INSECT, insectKingdomPoints);
        this.objective = objective;
    }

    // Getter and Setter

    /**
     * Setter Method for costs.
     * @param item: the Item to change the cost of.
     * @param cost: the int value to set as cost.
     */
    public void setCost(Item item, int cost) {
        costs.replace(item, cost);
    }

    /**
     * Getter Method for costs.
     * @param item: the Item to get the cost of.
     * @return the cost of that KingdomResource as and int value.
     */
    public int getCost(Item item) {
        return costs.get(item);
    }


    /**
     * Getter Method for objective
     * @return a reference to the Objective used for the earned points calculation (null if none)
     */
    public Objective getObjective() {
        return objective;
    }

    /**
     * Setter Method for objective
     * @param objective: reference to the Objective used for the earned points calculation (null if none)
     */
    public void setObjective(Objective objective) {
        this.objective = objective;
    }

    // Methods
    private void initMap() {
        costs.put(KingdomResource.FUNGI, 0);
        costs.put(KingdomResource.ANIMAL, 0);
        costs.put(KingdomResource.PLANT, 0);
        costs.put(KingdomResource.INSECT, 0);
        costs.put(Resource.FEATHER, 0);
        costs.put(Resource.POTION, 0);
        costs.put(Resource.SCROLL, 0);
    }

    @Override
    public boolean canBePlaced(ArrayList<PlayableCard> playArea) {
        if(isFrontFacing()) {
            HashMap<Item, Integer> playedItems = new HashMap<>();
            playedItems.put(KingdomResource.FUNGI, 0);
            playedItems.put(KingdomResource.ANIMAL, 0);
            playedItems.put(KingdomResource.PLANT, 0);
            playedItems.put(KingdomResource.INSECT, 0);
            playedItems.put(Resource.FEATHER, 0);
            playedItems.put(Resource.POTION, 0);
            playedItems.put(Resource.SCROLL, 0);
            for (PlayableCard card : playArea) {
                if (card instanceof StarterCard && card.isFrontFacing()) {
                    for (Item item : card.getPermanentResources()) {
                        if (null != item) {
                            increaseValue(playedItems, item);
                        }
                    }
                } else if (!card.isFrontFacing() && !(card instanceof StarterCard)) {
                    for (Item item : card.getPermanentResources()) {
                        if (null != item) {
                            increaseValue(playedItems, item);
                        }
                    }
                }
                if (null != card.getShowingSide().getTopLeftCorner() && !card.getShowingSide().getTopLeftCorner().isCovered()
                        && null != card.getShowingSide().getTopLeftCorner().getItem()) {
                    increaseValue(playedItems, card.getShowingSide().getTopLeftCorner().getItem());
                }
                if (null != card.getShowingSide().getTopRightCorner() && !card.getShowingSide().getTopRightCorner().isCovered()
                        && null != card.getShowingSide().getTopRightCorner().getItem()) {
                    increaseValue(playedItems, card.getShowingSide().getTopRightCorner().getItem());
                }
                if (null != card.getShowingSide().getBottomLeftCorner() && !card.getShowingSide().getBottomLeftCorner().isCovered()
                        && null != card.getShowingSide().getBottomLeftCorner().getItem()) {
                    increaseValue(playedItems, card.getShowingSide().getBottomLeftCorner().getItem());
                }
                if (null != card.getShowingSide().getBottomRightCorner() && !card.getShowingSide().getBottomRightCorner().isCovered()
                        && null != card.getShowingSide().getBottomRightCorner().getItem()) {
                    increaseValue(playedItems, card.getShowingSide().getBottomRightCorner().getItem());
                }
            }
            boolean canBePlaced = true;
            for (Item item : costs.keySet()) {
                if (playedItems.get(item) < costs.get(item)) {
                    canBePlaced = false;
                }
            }
            return canBePlaced;
        } else return true;
    }

    private void increaseValue(HashMap<Item, Integer> map, Item item) {
        int value = map.get(item);
        map.replace(item, value + 1);
    }
}


