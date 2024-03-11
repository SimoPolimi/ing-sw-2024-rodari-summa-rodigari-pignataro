package it.polimi.ingsw.gc42.model.classes.game;

import it.polimi.ingsw.gc42.model.classes.cards.Card;
import it.polimi.ingsw.gc42.model.classes.cards.StarterCard;

import java.util.ArrayList;

/**
 * Implement PlayField
 */
public class PlayField {
    /**
     * Getter method for starterCard
     * @return startedCard
     */
    public StarterCard getStarterCard() {
        return starterCard;
    }

    /**
     * Setter method for starterCard
     * @param starterCard:it's the first card in player field
     */
    public void setStarterCard(StarterCard starterCard) {
        this.starterCard = starterCard;
    }

    /**
     * Getter method for playedCards
     * @return playedCards
     */
    public ArrayList<Card> getPlayedCards() {
        return playedCards;
    }

    /**
     * Setter method for playedCards
     * @param playedCards: all the cards in the player field
     */
    public void setPlayedCards(ArrayList<Card> playedCards) {
        this.playedCards = playedCards;
    }

    /**
     * Getter method for countFeathers
     * @return countFeathers
     */
    public int getCountFeathers() {
        return countFeathers;
    }

    /**
     * Setter method for countFeathers
     * @param countFeathers:it's the count of feathers owned
     */
    public void setCountFeathers(int countFeathers) {
        this.countFeathers = countFeathers;
    }

    /**
     * Getter method for countScrolls
     * @return countScrolls
     */
    public int getCountScrolls() {
        return countScrolls;
    }

    /**
     * Setter method for countScrolls
     * @param countScrolls:it's the count of scrolls owned
     */
    public void setCountScrolls(int countScrolls) {
        this.countScrolls = countScrolls;
    }

    /**
     * Getter method for countPotions
     * @return countPotions
     */
    public int getCountPotions() {
        return countPotions;
    }

    /**
     * Setter method for countPotions
     * @param countPotions:it's the count of potions owned
     */
    public void setCountPotions(int countPotions) {
        this.countPotions = countPotions;
    }

    /**
     * Getter for countPlantResources
     * @return countPlantResources
     */
    public int getCountPlantResources() {
        return countPlantResources;
    }

    /**
     * Setter for countPlantResources
     * @param countPlantResources:it's the count of plant resources owned
     */
    public void setCountPlantResources(int countPlantResources) {
        this.countPlantResources = countPlantResources;
    }

    /**
     * Getter method for countAnimalResources
     * @return countAnimalResources
     */
    public int getCountAnimalResources() {
        return countAnimalResources;
    }

    /**
     * Setter for countPlantResources
     * @param countAnimalResources:it's the count of animal resources owned
     */
    public void setCountAnimalResources(int countAnimalResources) {
        this.countAnimalResources = countAnimalResources;
    }

    /**
     * Getter method for countFungiResources
     * @return countFungiResources
     */
    public int getCountFungiResources() {
        return countFungiResources;
    }

    /**
     * Setter for countFungiResources
     * @param countFungiResources:it's the count of fungi resources owned
     */
    public void setCountFungiResources(int countFungiResources) {
        this.countFungiResources = countFungiResources;
    }

    /**
     * Getter method for countInsectResources
     * @return countInsectResources
     */
    public int getCountInsectResources() {
        return countInsectResources;
    }

    /**
     * Setter for countInsectResources
     * @param countInsectResources:it's the count of insect resources owned
     */
    public void setCountInsectResources(int countInsectResources) {
        this.countInsectResources = countInsectResources;
    }

    private StarterCard starterCard;
    private ArrayList<Card> playedCards;
    private int countFeathers;
    private int countScrolls;
    private int countPotions;
    private int countPlantResources;
    private int countAnimalResources;
    private int countFungiResources;
    private int countInsectResources;

}
