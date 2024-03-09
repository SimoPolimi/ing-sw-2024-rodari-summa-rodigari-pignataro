package it.polimi.ingsw.gc42.classes;

import it.polimi.ingsw.gc42.classes.cards.*;

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

    public Deck getResourceCardDeck() {
        return ResourceCardDeck;
    }

    public void setResourceCardDeck(Deck resourceCardDeck) {
        ResourceCardDeck = resourceCardDeck;
    }

    public Deck getGoldCardDeck() {
        return GoldCardDeck;
    }

    public void setGoldCardDeck(Deck goldCardDeck) {
        GoldCardDeck = goldCardDeck;
    }

    public Deck getStarterCardDeck() {
        return StarterCardDeck;
    }

    public void setStarterCardDeck(Deck starterCardDeck) {
        StarterCardDeck = starterCardDeck;
    }

    public Deck getObjectiveDeck() {
        return ObjectiveDeck;
    }

    public void setObjectiveDeck(Deck objectiveDeck) {
        ObjectiveDeck = objectiveDeck;
    }

    private ResourceCard resourceCardOne;
    private ResourceCard resourceCardTwo;
    private GoldCard goldCardOne;
    private GoldCard goldCardTwo;
    private Objective commonObjectiveOne;
    private Objective commonObjectiveTwo;
    private Deck ResourceCardDeck;
    private Deck GoldCardDeck;

    // Two decks for StarterCard and ObjectiveCard so that they can be shuffled when the Game starts without needing to write code for it
    private Deck StarterCardDeck;
    private Deck ObjectiveDeck;

    public static PlayingDecks initPlayingDeck() {
        Deck resourceCardDeck = Deck.initDeck(CardType.RESOURCECARD);
        Deck goldCardDeck = Deck.initDeck(CardType.GOLDCARD);
        Deck objectiveCardDeck = Deck.initDeck(CardType.OBJECTIVECARD);
        Deck starterCardDeck = Deck.initDeck(CardType.STARTERCARD);
        return new PlayingDecks((ResourceCard) resourceCardDeck.draw(), (ResourceCard) resourceCardDeck.draw(),
                (GoldCard) goldCardDeck.draw(), (GoldCard) goldCardDeck.draw(),
                ((ObjectiveCard) objectiveCardDeck.draw()).getObjective(), ((ObjectiveCard) objectiveCardDeck.draw()).getObjective(),
                resourceCardDeck, goldCardDeck, starterCardDeck, objectiveCardDeck);
    }

    // Constructor Method only used internally
    public PlayingDecks(ResourceCard resourceCardOne, ResourceCard resourceCardTwo, GoldCard goldCardOne, GoldCard goldCardTwo,
                        Objective commonObjectiveOne, Objective commonObjectiveTwo, Deck resourceCardDeck, Deck goldCardDeck,
                        Deck starterCardDeck, Deck objectiveDeck) {
        this.resourceCardOne = resourceCardOne;
        this.resourceCardTwo = resourceCardTwo;
        this.goldCardOne = goldCardOne;
        this.goldCardTwo = goldCardTwo;
        this.commonObjectiveOne = commonObjectiveOne;
        this.commonObjectiveTwo = commonObjectiveTwo;
        ResourceCardDeck = resourceCardDeck;
        GoldCardDeck = goldCardDeck;
        StarterCardDeck = starterCardDeck;
        ObjectiveDeck = objectiveDeck;
    }

    /**
     * Get the ResourceCard placed in the specified slot
     *
     * @param number is the slot of the Card to be picked
     * @return the chosen ResourceCard
     */
    public ResourceCard getResourceCard(int number) {
        switch (number) {
            case 1:
                return resourceCardOne;
            case 2:
                return resourceCardTwo;
            default:
                //TODO errore
                return null;
        }
    }

    /**
     * Get the GoldCard placed in the specified slot
     *
     * @param number is the slot of the Card to be picked
     * @return the chosen GoldCard
     */
    public GoldCard getGoldCard(int number) {
        switch (number) {
            case 1:
                return goldCardOne;
            case 2:
                return goldCardTwo;
            default:
                //TODO errore
                return null;
        }
    }

}
