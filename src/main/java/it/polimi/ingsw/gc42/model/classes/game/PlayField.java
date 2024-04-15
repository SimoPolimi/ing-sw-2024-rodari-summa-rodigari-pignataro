package it.polimi.ingsw.gc42.model.classes.game;

import it.polimi.ingsw.gc42.model.classes.cards.*;
import it.polimi.ingsw.gc42.model.exceptions.IllegalPlacementException;
import it.polimi.ingsw.gc42.model.exceptions.RemovingFromZeroException;
import it.polimi.ingsw.gc42.model.interfaces.Listener;
import it.polimi.ingsw.gc42.model.interfaces.Observable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;

/**
 * Implement PlayField
 */
public class PlayField implements Observable {
    // Attributes
    private final ArrayList<PlayableCard> playedCards = new ArrayList<>();
    private final HashMap<String, Integer> counter = HashMap.newHashMap(7);
    private final ArrayList<Listener> listeners = new ArrayList<>();

    // Constructor Method

    /**
     * Constructor Method for PlayField.
     * The HashMap to store the player's Resources/Items is initialized in here (all 0s).
     * It doesn't need to be passed as argument!
     * @param starterCard: the player's initial Card from the StarterDeck
     */
    public PlayField(StarterCard starterCard) {
        try {
            addCard(starterCard, 0, 0);
        } catch (IllegalPlacementException e) {
            e.printStackTrace();
        }
        initMap();
    }

    public PlayField() {
        initMap();
    }

    // Getters and Setters

    /**
     * Getter method for playedCards
     * @return the ArrayList that stores the plauer's played Cards
     */
    public ArrayList<PlayableCard> getPlayedCards() {
        return playedCards;
    }

    /**
     * Getter Method for the number of a specific KingdomResource inside the HashMap
     * @param kingdom: one of the Kingdom Types
     * @return the number of KingdomResources the player has played and which are visible on the table
     */
    public int getNumberOf(KingdomResource kingdom) {
        return counter.get(kingdom.toString());
    }

    /**
     * Setter Method for the number of a specific KingdomResource inside the HashMap.
     * ONLY USED INTERNALLY
     * @param kingdom: one of the Kingdom Types
     * @param num: the number to save for that specific KingdomResource
     */
    private void setNumberOf(KingdomResource kingdom, int num) {
        counter.replace(kingdom.toString(), num);
    }

    /**
     * Getter Method for the number of a specific Resource or item inside the HashMap
     * @param resource: one of the Resource Types
     * @return the number of Resources the player has played and which are visible on the table.
     */
    public int getNumberOf(Resource resource) {
        return counter.get(resource.toString());
    }

    /**
     * Setter Method for the number of a specific Resource inside the HashMap
     * ONLY USED INTERNALLY
     * @param resource: one of the Resource Types
     * @param num: the number to save for that specific Resource
     */
    private void setNumberOf(Resource resource, int num) {
        counter.replace(resource.toString(), num);
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
        //TODO: Make dynamic after implementing reading from JSON
        counter.put(Resource.FEATHER.toString(), 0);
        counter.put(Resource.SCROLL.toString(), 0);
        counter.put(Resource.POTION.toString(), 0);
        counter.put(KingdomResource.ANIMAL.toString(), 0);
        counter.put(KingdomResource.PLANT.toString(), 0);
        counter.put(KingdomResource.FUNGI.toString(), 0);
        counter.put(KingdomResource.INSECT.toString(), 0);
    }

    /**
     * Increases the number of this KingdomResource by 1.
     * @param kingdom: the KingdomResource to be incremented.
     */
    public void add(KingdomResource kingdom) {
        setNumberOf(kingdom, getNumberOf(kingdom) + 1);
    }

    /**
     * Decreases the number of this KingdomResource by 1.
     * @param kingdom: the KingdomResource to be decremented.
     * @throws RemovingFromZeroException if the number is already 0.
     */
    public void remove(KingdomResource kingdom) throws RemovingFromZeroException {
        int num = getNumberOf(kingdom);
        if (num > 0) {
            setNumberOf(kingdom, num - 1);
        } else throw new RemovingFromZeroException();
    }

    /**
     * Increases the number of this Resource by 1.
     * @param resource: the Resource to be incremented.
     */
    public void add(Resource resource) {
        setNumberOf(resource, getNumberOf(resource) + 1);
    }

    /**
     * Decreases the number of this Resource by 1.
     * @param resource: the Resource to be decremented.
     * @throws RemovingFromZeroException if the number is already 0.
     */
    public void remove(Resource resource) throws RemovingFromZeroException {
        int num = getNumberOf(resource);
        if (num > 0) {
            setNumberOf(resource, num - 1);
        } else throw new RemovingFromZeroException();
    }

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
            coverNearbyCorners(x, y);
            notifyListeners("PlayArea Updated");
        } else throw new IllegalPlacementException();
    }

    private void coverNearbyCorners(int x, int y) {
        for (PlayableCard card : playedCards) {
            if (card.getX() == x + 1 && card.getY() == y && null != card.getShowingSide().getBottomLeftCorner()) {
                card.getShowingSide().getBottomLeftCorner().setCovered(true);
            } else if (card.getX() == x && card.getY() == y + 1 && null != card.getShowingSide().getBottomRightCorner()) {
                card.getShowingSide().getBottomRightCorner().setCovered(true);
            } else if (card.getX() == x - 1 && card.getY() == y && null != card.getShowingSide().getTopRightCorner()) {
                card.getShowingSide().getTopRightCorner().setCovered(true);
            } else if (card.getX() == x && card.getY() == y - 1 && null != card.getShowingSide().getTopLeftCorner()) {
                card.getShowingSide().getTopLeftCorner().setCovered(true);
            }
        }
    }

    public ArrayList<Coordinates> getAvailablePlacements() {
        ArrayList<Coordinates> placements = new ArrayList<>();
        ArrayList<Coordinates> illegalPlacementes = new ArrayList<>();
        for (PlayableCard card : playedCards) {
            if (null != card.getShowingSide().getTopLeftCorner()
                    && !isThereACardIn(card.getX(), card.getY()+1)) {
                placements.add(new Coordinates(card.getX(), card.getY() + 1));
            } else {
                illegalPlacementes.add(new Coordinates(card.getX(), card.getY()+1));
            }
            if (null != card.getShowingSide().getTopRightCorner()
                    && !isThereACardIn(card.getX()+1, card.getY())) {
                placements.add(new Coordinates(card.getX()+1, card.getY()));
            } else {
                illegalPlacementes.add(new Coordinates(card.getX()+1, card.getY()));
            }
            if (null != card.getShowingSide().getBottomLeftCorner()
                    && !isThereACardIn(card.getX()-1, card.getY())) {
                placements.add(new Coordinates(card.getX()-1, card.getY()));
            } else {
                illegalPlacementes.add(new Coordinates(card.getX()-1, card.getY()));
            }
            if (null != card.getShowingSide().getBottomRightCorner()
                    && !isThereACardIn(card.getX(), card.getY()-1)) {
                placements.add(new Coordinates(card.getX(), card.getY()-1));
            } else {
                illegalPlacementes.add(new Coordinates(card.getX(), card.getY()-1));
            }
        }
        if (placements.isEmpty()) {
            placements.add(new Coordinates(0, 0));
        }
        return removeIllegalPlacements(removeDuplicatePlacements(placements), illegalPlacementes);
    }

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

    private boolean isThereACardIn(int x, int y) {
        for (PlayableCard card : playedCards) {
            if (card.getX() == x && card.getY() == y) {
                return true;
            }
        }
        return false;
    }

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
