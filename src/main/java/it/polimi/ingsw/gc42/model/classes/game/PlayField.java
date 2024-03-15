package it.polimi.ingsw.gc42.model.classes.game;

import it.polimi.ingsw.gc42.model.classes.cards.Card;
import it.polimi.ingsw.gc42.model.classes.cards.KingdomResource;
import it.polimi.ingsw.gc42.model.classes.cards.Resource;
import it.polimi.ingsw.gc42.model.classes.cards.StarterCard;
import it.polimi.ingsw.gc42.model.exceptions.RemovingFromZeroException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;

/**
 * Implement PlayField
 */
public class PlayField {
    // Attributes
    private StarterCard starterCard;
    private ArrayList<Card> playedCards;

    private final HashMap<String, Integer> counter;

    // Constructor Method

    /**
     * Constructor Method for PlayField.
     * The HashMap to store the player's Resources/Items is initialized in here (all 0s).
     * It doesn't need to be passed as argument!
     * @param starterCard: the player's initial Card from the StarterDeck
     * @param playedCards: ArrayList that stores the player's played Cards
     */
    public PlayField(StarterCard starterCard, ArrayList<Card> playedCards) {
        this.starterCard = starterCard;
        this.playedCards = playedCards;
        //TODO: Make numMappings dynamic after implementing reading from JSON
        counter = HashMap.newHashMap(7);
        initMap();
    }

    // Getters and Setters

    /**
     * Getter Method for StarterCard
     * @return the Starter Card
     */
    public StarterCard getStarterCard() {
        return starterCard;
    }

    /**
     * Setter Method for StarterCard
     * @param starterCard: the player's initial Card
     */
    public void setStarterCard(StarterCard starterCard) {
        this.starterCard = starterCard;
    }

    /**
     * Getter method for playedCards
     * @return the ArrayList that stores the plauer's played Cards
     */
    public ArrayList<Card> getPlayedCards() {
        return playedCards;
    }

    /**
     * Setter Method for plauedCards
     * @param playedCards: the ArrayList that stores the player's played Cards
     */
    public void setPlayedCards(ArrayList<Card> playedCards) {
        this.playedCards = playedCards;
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
        counter.put("FEATHER", 0);
        counter.put("SCROLLS", 0);
        counter.put("POTION", 0);
        counter.put("PLANT", 0);
        counter.put("ANIMAL", 0);
        counter.put("FUNGI", 0);
        counter.put("INSECT", 0);
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
}
