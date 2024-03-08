package it.polimi.ingsw.gc42.classes;

import it.polimi.ingsw.gc42.classes.cards.GoldCard;
import it.polimi.ingsw.gc42.classes.cards.Objective;
import it.polimi.ingsw.gc42.classes.cards.ResourceCard;

public class PlayingDecks {
    public ResourceCard getResourceCardOne() {
        return resourceCardOne;
    }

    public void setResourceCardOne(ResourceCard resourceCardOne) {
        this.resourceCardOne = resourceCardOne;
    }

    public ResourceCard getResourceCardTwo() {
        return resourceCardTwo;
    }

    public void setResourceCardTwo(ResourceCard resourceCardTwo) {
        this.resourceCardTwo = resourceCardTwo;
    }

    public GoldCard getGoldCardOne() {
        return goldCardOne;
    }

    public void setGoldCardOne(GoldCard goldCardOne) {
        this.goldCardOne = goldCardOne;
    }

    public GoldCard getGoldCardTwo() {
        return goldCardTwo;
    }

    public void setGoldCardTwo(GoldCard goldCardTwo) {
        this.goldCardTwo = goldCardTwo;
    }

    public Objective getCommonObjectiveOne() {
        return commonObjectiveOne;
    }

    public void setCommonObjectiveOne(Objective commonObjectiveOne) {
        this.commonObjectiveOne = commonObjectiveOne;
    }

    public Objective getCommonObjectiveTwo() {
        return commonObjectiveTwo;
    }

    public void setCommonObjectiveTwo(Objective commonObjectiveTwo) {
        this.commonObjectiveTwo = commonObjectiveTwo;
    }

    private ResourceCard resourceCardOne;
    private ResourceCard resourceCardTwo;
    private GoldCard goldCardOne;
    private GoldCard goldCardTwo;
    private Objective commonObjectiveOne;
    private Objective commonObjectiveTwo;

    public PlayingDecks(ResourceCard resourceCardOne, ResourceCard resourceCardTwo, GoldCard goldCardOne, GoldCard goldCardTwo, Objective commonObjectiveOne, Objective commonObjectiveTwo) {
        this.resourceCardOne = resourceCardOne;
        this.resourceCardTwo = resourceCardTwo;
        this.goldCardOne = goldCardOne;
        this.goldCardTwo = goldCardTwo;
        this.commonObjectiveOne = commonObjectiveOne;
        this.commonObjectiveTwo = commonObjectiveTwo;
    }


    public ResourceCard getResourceCard(int number){
        if(number==1){
            return resourceCardOne;
        }else{
            return resourceCardTwo;
        }
    }

    public GoldCard getGoldCard(int number){
        if(number==1){
            return goldCardOne;
        }else{
            return goldCardTwo;
        }
    }

    // TODO add last 2 methods (draw)



}
