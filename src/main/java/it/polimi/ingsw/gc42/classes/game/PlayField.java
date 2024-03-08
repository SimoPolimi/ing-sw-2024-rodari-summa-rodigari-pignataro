package it.polimi.ingsw.gc42.classes.game;

import it.polimi.ingsw.gc42.classes.cards.Card;
import it.polimi.ingsw.gc42.classes.cards.StarterCard;

import java.util.ArrayList;

public class PlayField {

    public StarterCard getStarterCard() {
        return starterCard;
    }

    public void setStarterCard(StarterCard starterCard) {
        this.starterCard = starterCard;
    }

    public ArrayList<Card> getPlayedCards() {
        return playedCards;
    }

    public void setPlayedCards(ArrayList<Card> playedCards) {
        this.playedCards = playedCards;
    }

    public int getCountFeathers() {
        return countFeathers;
    }

    public void setCountFeathers(int countFeathers) {
        this.countFeathers = countFeathers;
    }

    public int getCountScrolls() {
        return countScrolls;
    }

    public void setCountScrolls(int countScrolls) {
        this.countScrolls = countScrolls;
    }

    public int getCountPotions() {
        return countPotions;
    }

    public void setCountPotions(int countPotions) {
        this.countPotions = countPotions;
    }

    public int getCountPlantResources() {
        return countPlantResources;
    }

    public void setCountPlantResources(int countPlantResources) {
        this.countPlantResources = countPlantResources;
    }

    public int getCountAnimalResources() {
        return countAnimalResources;
    }

    public void setCountAnimalResources(int countAnimalResources) {
        this.countAnimalResources = countAnimalResources;
    }

    public int getCountFungiResources() {
        return countFungiResources;
    }

    public void setCountFungiResources(int countFungiResources) {
        this.countFungiResources = countFungiResources;
    }

    public int getCountInsectResources() {
        return countInsectResources;
    }

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
