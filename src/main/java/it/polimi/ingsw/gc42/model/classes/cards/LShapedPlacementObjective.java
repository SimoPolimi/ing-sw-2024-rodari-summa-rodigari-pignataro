package it.polimi.ingsw.gc42.model.classes.cards;

import java.util.ArrayList;

/**
 * Model implementation of a specific type of Condition/Objective, that requires the Cards to be placed
 * in a shape, consisting of two Cards (of the same Type) on top of each other and another one (of a different Type)
 * attached on a Corner, inside the Play Area.
 */
public class LShapedPlacementObjective extends PlacementObjective {
    // Attributes
    private KingdomResource secondaryType;
    private LPlacementOffset positionCornerCard;

    // Constructor Method
    /**
     * Constructor Method
     * @param points: the number of points the Condition gives every time it's satisfied
     * @param primaryType: the KingdomResource indicating the color of the two identical Cards required for the Objective.
     * @param secondaryType: the KingdomResource indicating the color of the third Card required for the Objective.
     * @param positionCornerCard: the CornerPosition indicating where the third Card is positioned.
     * @param description: a String containing the Description of the Objective, displayed in the GUI.
     */
    public LShapedPlacementObjective(int points, KingdomResource primaryType, KingdomResource secondaryType,
                                     LPlacementOffset positionCornerCard, String name, String description) {
        super(points,name, description, primaryType);
        this.secondaryType = secondaryType;
        this.positionCornerCard = positionCornerCard;
    }

    // Getters and Setters

    /**
     * Getter Method for secondaryType.
     * @return the KingdomResource indicating the color of the third Card required for the Objective.
     */
    public KingdomResource getSecondaryType() {
        return secondaryType;
    }

    /**
     * Setter Method for secondaryType.
     * @param secondaryType: the KingdomResource indicating the color of the third Card required for the Objective.
     */
    public void setSecondaryType(KingdomResource secondaryType) {
        this.secondaryType = secondaryType;
    }

    /**
     * Getter Method for positionedCornerCard.
     * @return the CornerPosition indicating where the third Card is positioned.
     */
    public LPlacementOffset getPositionCornerCard() {
        return positionCornerCard;
    }

    /**
     * Setter Method for positionedCornerCard.
     * @param positionCornerCard: the CornerPosition indicating where the third Card is positioned.
     */
    public void setPositionCornerCard(LPlacementOffset positionCornerCard) {
        this.positionCornerCard = positionCornerCard;
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
        int count = 0;
        ArrayList<ArrayList<PlayableCard>> allInlineCouples = new ArrayList<>();
        for (PlayableCard baseCard : playArea) {
            if (baseCard.getKingdom() != null && !baseCard.getKingdom().equals(getPrimaryType())) continue;

            ArrayList<PlayableCard> inlineCouple = new ArrayList<>();
            inlineCouple.add(baseCard);
            boolean validCorner = false;
            boolean validInline = false;
            for (PlayableCard c : playArea) {
                if (c.getX() == baseCard.getX() + positionCornerCard.getXOffset() && c.getY() == baseCard.getY() + positionCornerCard.getYOffset() && c.getKingdom().equals(secondaryType)) {
                    validCorner = true;
                    break;
                }
            }
            for (PlayableCard c : playArea) {
                if (c.getX() == baseCard.getX() + positionCornerCard.getInlineOffset() && c.getY() == baseCard.getY() + positionCornerCard.getInlineOffset() && c.getKingdom().equals(secondaryType)) {
                    validInline = true;
                    inlineCouple.add(c);
                    break;
                }
            }
            if (validCorner && validInline) {
                count++;
                allInlineCouples.add(inlineCouple);
            }
        }
        int overlaps = countOverlap(allInlineCouples);
        if (count != 0 && overlaps != 0) {
            return count / Math.ceilDiv(overlaps, 2);
        } else return 0;
    }

    /**
     * This method counts how many times an overlap between two found
     * patterns happens
     * @param list: A list containing ArrayLists of PlayableCard. The internal Arraylists are meant to
     *              contain the two inline cards of a pattern. If two lists have the same card there is an overlap
     * @return the number of overlaps counted
     */
    private int countOverlap(ArrayList<ArrayList<PlayableCard>> list) {
        int overlaps = 0;
        for (int i = 0; i < list.size(); i++) {
            ArrayList<PlayableCard> primaryList = list.get(i);
            for (int j = i + 1; j < list.size(); j++) {
                ArrayList<PlayableCard> secondaryList = list.get(j);
                for (PlayableCard c : primaryList) {
                    if (secondaryList.contains(c)) {
                        overlaps++;
                        break;
                    }
                }
            }
        }
        return overlaps;
    }
}
