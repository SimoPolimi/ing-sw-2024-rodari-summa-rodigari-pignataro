package it.polimi.ingsw.gc42.model.classes.game;

import it.polimi.ingsw.gc42.model.classes.cards.*;
import it.polimi.ingsw.gc42.model.exceptions.IllegalPlacementException;
import it.polimi.ingsw.gc42.model.interfaces.Listener;
import it.polimi.ingsw.gc42.model.interfaces.Observable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Implement PlayField
 */
public class PlayField implements Observable, Serializable {
    // Attributes
    private final ArrayList<PlayableCard> playedCards = new ArrayList<>();
    private final HashMap<Item, Integer> counter = HashMap.newHashMap(7);
    private final ArrayList<Listener> listeners = new ArrayList<>();

    // Constructor Method

    /**
     * Constructor Method for PlayField.
     * The HashMap to store the player's Resources/Items is initialized in here (all 0s).
     * It doesn't need to be passed as an argument!
     * @param starterCard: the player's initial Card from the StarterDeck
     */
    public PlayField(StarterCard starterCard) {
        initMap();
        try {
            addCard(starterCard, 0, 0);
        } catch (IllegalPlacementException e) {
            e.printStackTrace();
        }
    }

    public PlayField() {
        initMap();
    }

    // Getters and Setters

    /**
     * Getter method for playedCards
     * @return the ArrayList that stores the Player's played Cards
     */
    public ArrayList<PlayableCard> getPlayedCards() {
        return new ArrayList<>(playedCards);
    }

    /**
     * Getter Method for the number of a specific KingdomResource inside the HashMap
     * @param item: one of the Items
     * @return the number of KingdomResources the player has played and which are visible on the table
     */
    public int getNumberOf(Item item) {
        return counter.get(item);
    }

    /**
     * Setter Method for the number of a specific KingdomResource inside the HashMap.
     * ONLY USED INTERNALLY
     * @param item: one of the Items
     * @param num: the number to save for that specific KingdomResource
     */
    private void setNumberOf(Item item, int num) {
        counter.replace(item, num);
    }

    // Methods

    /**
     * Initializer Method for the HashMap.
     * ONLY USED INTERNALLY.
     * The HashMap is used to store an integer value for each KingdomResource and Resource available in the game.
     * The value stored in it corresponds to the number of said KingdomResource/Resource played throughout
     * the game by the player, and which are still visible at the moment.
     * This number gets increased for every new KingdomResource/Resource the player adds, and decreased
     * for each that gets covered by another Card.
     * When this method is called, a new <Key, Value> tuple is created for any of the KingdomResources and Resources
     * available in the game, and all of them are initialized with a Value of 0.
     */
    private void initMap() {
        counter.put(Resource.FEATHER, 0);
        counter.put(Resource.SCROLL, 0);
        counter.put(Resource.POTION, 0);
        counter.put(KingdomResource.ANIMAL, 0);
        counter.put(KingdomResource.PLANT, 0);
        counter.put(KingdomResource.FUNGI, 0);
        counter.put(KingdomResource.INSECT, 0);
    }

    /**
     * Increases the number of this Item by 1.
     * @param item: the Item to be incremented.
     */
    public void add(Item item) {
        setNumberOf(item, getNumberOf(item) + 1);
    }

    /**
     * Decreases the number of this Item by 1, if present inside the Map
     * @param item: the Item to be decremented.
     */
    public void remove(Item item) {
        if (null != item) {
            int num = getNumberOf(item);
            if (num > 0) {
                setNumberOf(item, num - 1);
            }
        }
    }

    /**
     * Checks if the specified PlayableCard can be played into the PlayField in position (x,y) and adds it to the PlayField in that those coordinates
     * @param card the PlayableCard to be added
     * @param x the horizontal coordinate of the PlayableCard on the PlayField
     * @param y the vertical coordinate of the PlayableCard on the PlayField
     * @throws IllegalPlacementException if the PlayCard can't be played in those coordinates
     */
    public void addCard(PlayableCard card, int x, int y) throws IllegalPlacementException {
        boolean isValid = false;
        for (Coordinates coordinates : getAvailablePlacements()) {
            if (coordinates.getX() == x && coordinates.getY() == y) {
                isValid = true;
            }
        }
        if (isValid) {
            card.setX(x);
            card.setY(y);
            playedCards.add(card);

            // Updates the PlayField's Inventory
            if (null != card.getShowingSide().topLeftCorner()
                    && null != card.getShowingSide().topLeftCorner().getItem()
                    && !card.getShowingSide().topLeftCorner().isCovered()) {
                add(card.getShowingSide().topLeftCorner().getItem());
            }
            if (null != card.getShowingSide().topRightCorner()
                    && null != card.getShowingSide().topRightCorner().getItem()
                    && !card.getShowingSide().topRightCorner().isCovered()) {
                add(card.getShowingSide().topRightCorner().getItem());
            }
            if (null != card.getShowingSide().bottomLeftCorner()
                    && null != card.getShowingSide().bottomLeftCorner().getItem()
                    && !card.getShowingSide().bottomLeftCorner().isCovered()) {
                add(card.getShowingSide().bottomLeftCorner().getItem());
            }
            if (null != card.getShowingSide().bottomRightCorner()
                    && null != card.getShowingSide().bottomRightCorner().getItem()
                    && !card.getShowingSide().bottomRightCorner().isCovered()) {
                add(card.getShowingSide().bottomRightCorner().getItem());
            }
            if ((card instanceof StarterCard && card.isFrontFacing()) ||
                    ((card instanceof GoldCard || card instanceof ResourceCard) && !card.isFrontFacing())) {
                ArrayList<Item> permanentItems = card.getPermanentResources();
                for (Item item : permanentItems) {
                    add(item);
                }
            }

            coverNearbyCorners(x, y);
            notifyListeners("PlayArea Updated");
        } else throw new IllegalPlacementException();
    }

    /**
     * Covers the Corners that a PlayableCard placed on the PlayField in position (x,y) would cover
     * @param x the horizontal coordinate of the PlayableCard on the PlayField
     * @param y the vertical coordinate of the PlayableCard on the PlayField
     */
    private void coverNearbyCorners(int x, int y) {
        for (PlayableCard card : playedCards) {
            if (card.getX() == x + 1 && card.getY() == y && null != card.getShowingSide().bottomLeftCorner()) {
                card.getShowingSide().bottomLeftCorner().setCovered(true);
                if (null != card.getShowingSide().bottomLeftCorner()) {
                    remove(card.getShowingSide().bottomLeftCorner().getItem());
                }
            } else if (card.getX() == x && card.getY() == y + 1 && null != card.getShowingSide().bottomRightCorner()) {
                card.getShowingSide().bottomRightCorner().setCovered(true);
                if (null != card.getShowingSide().bottomRightCorner()) {
                    remove(card.getShowingSide().bottomRightCorner().getItem());
                }
            } else if (card.getX() == x - 1 && card.getY() == y && null != card.getShowingSide().topRightCorner()) {
                card.getShowingSide().topRightCorner().setCovered(true);
                if (null != card.getShowingSide().topRightCorner()) {
                    remove(card.getShowingSide().topRightCorner().getItem());
                }
            } else if (card.getX() == x && card.getY() == y - 1 && null != card.getShowingSide().topLeftCorner()) {
                card.getShowingSide().topLeftCorner().setCovered(true);
                if (null != card.getShowingSide().topLeftCorner()) {
                    remove(card.getShowingSide().topLeftCorner().getItem());
                }
            }
        }
    }

    /**
     * Gets the list of coordinates in which a PlayableCard can be played
     * @return the list of coordinates in which a PlayableCard can be played
     */
    public ArrayList<Coordinates> getAvailablePlacements() {
        ArrayList<Coordinates> placements = new ArrayList<>();
        ArrayList<Coordinates> illegalPlacements = new ArrayList<>();
        for (PlayableCard card : playedCards) {
            if (null != card.getShowingSide().topLeftCorner()
                    && !isThereACardIn(card.getX(), card.getY()+1)) {
                placements.add(new Coordinates(card.getX(), card.getY() + 1));
            } else {
                illegalPlacements.add(new Coordinates(card.getX(), card.getY()+1));
            }
            if (null != card.getShowingSide().topRightCorner()
                    && !isThereACardIn(card.getX()+1, card.getY())) {
                placements.add(new Coordinates(card.getX()+1, card.getY()));
            } else {
                illegalPlacements.add(new Coordinates(card.getX()+1, card.getY()));
            }
            if (null != card.getShowingSide().bottomLeftCorner()
                    && !isThereACardIn(card.getX()-1, card.getY())) {
                placements.add(new Coordinates(card.getX()-1, card.getY()));
            } else {
                illegalPlacements.add(new Coordinates(card.getX()-1, card.getY()));
            }
            if (null != card.getShowingSide().bottomRightCorner()
                    && !isThereACardIn(card.getX(), card.getY()-1)) {
                placements.add(new Coordinates(card.getX(), card.getY()-1));
            } else {
                illegalPlacements.add(new Coordinates(card.getX(), card.getY()-1));
            }
        }
        if (placements.isEmpty()) {
            placements.add(new Coordinates(0, 0));
        }
        return removeIllegalPlacements(removeDuplicatePlacements(placements), illegalPlacements);
    }

    // TODO: check param javadoc
    /**
     * Removes the duplicates from the specified ArrayList
     * @param placements the placements ArrayList
     * @return the List of placements without duplicates
     */
    private ArrayList<Coordinates> removeDuplicatePlacements(ArrayList<Coordinates> placements) {
        for (int i = 0; i < placements.size(); i++) {
            for (int j = i+1; j < placements.size(); j++) {
                if (placements.get(j).getX() == placements.get(i).getX()
                        && placements.get(j).getY() == placements.get(i).getY()) {
                    placements.remove(j);
                    i = 0;
                    j = 1;
                }
            }
        }
        return placements;
    }

    // TODO: check param javadoc
    /**
     * Removes the Coordinates where a PlayableCard can't be played from the specified ArrayList
     * @param placements the placements ArrayList
     * @return the placements ArrayList without illegal placements
     */
    private ArrayList<Coordinates> removeIllegalPlacements(ArrayList<Coordinates> placements, ArrayList<Coordinates> illegalPlacements) {
        for (int i = 0; i < placements.size(); i++) {
            for (int j = 0; j < illegalPlacements.size(); j++) {
                if (placements.get(i).getX() == illegalPlacements.get(j).getX() && placements.get(i).getY() == illegalPlacements.get(j).getY()) {
                    placements.remove(placements.get(i));
                    i = 0;
                    j = 1;
                }
            }
        }
        return placements;
    }

    /**
     * Checks if there is a PlayableCard in position (x,y)
     * @param x the horizontal coordinate of the PlayableCard on the PlayField
     * @param y the vertical coordinate of the PlayableCard on the PlayField
     * @return {@code true} if there is a PlayableCard in position (x,y)
     */
    private boolean isThereACardIn(int x, int y) {
        for (PlayableCard card : playedCards) {
            if (card.getX() == x && card.getY() == y) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the last played PlayableCard
     * @return the last played PlayableCard
     */
    public PlayableCard getLastPlayedCard() {
        return playedCards.getLast();
    }

    @Override
    public void setListener(Listener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(Listener listener) {
        listeners.remove(listener);
    }

    @Override
    public void notifyListeners(String context) {
        for (Listener l : listeners) {
            l.onEvent();
        }
    }
}
