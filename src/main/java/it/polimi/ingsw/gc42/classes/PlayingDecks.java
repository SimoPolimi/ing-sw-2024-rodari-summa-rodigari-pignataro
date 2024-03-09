package it.polimi.ingsw.gc42.classes;

import it.polimi.ingsw.gc42.classes.cards.*;

/**
 * Implementation of the playing decks
 */
public class PlayingDecks {
    /**
     * Getter method for resourceCardOne
     * @return resourceCardOne
     */
    public ResourceCard getResourceCardOne() {
        return resourceCardOne;
    }

    /**
     * Setter method for resourceCard One
     * @param resourceCardOne:the first resource card in the field
     */
    public void setResourceCardOne(ResourceCard resourceCardOne) {
        this.resourceCardOne = resourceCardOne;
    }

    /**
     * Getter method for resourceCardTwo
     * @return resourceCardTwo
     */
    public ResourceCard getResourceCardTwo() {
        return resourceCardTwo;
    }

    /**
     * Setter method for resourceCardTwo
     * @param resourceCardTwo:the second resource card in the field
     */
    public void setResourceCardTwo(ResourceCard resourceCardTwo) {
        this.resourceCardTwo = resourceCardTwo;
    }

    /**
     * Getter method for goldCardOne
     * @return goldCardOne
     */
    public GoldCard getGoldCardOne() {
        return goldCardOne;
    }

    /**
     * Setter method for goldCardOne
     * @param goldCardOne: the first gold card on the field
     */
    public void setGoldCardOne(GoldCard goldCardOne) {
        this.goldCardOne = goldCardOne;
    }

    /**
     * Getter method for goldCardTwo
     * @return goldCardTwo
     */
    public GoldCard getGoldCardTwo() {
        return goldCardTwo;
    }

    /**
     * Setter method for goldCardTwo
     * @param goldCardTwo:the second gold card on the field
     */
    public void setGoldCardTwo(GoldCard goldCardTwo) {
        this.goldCardTwo = goldCardTwo;
    }

    /**
     * Getter method for commonObjectiveOne
     * @return commonObjectiveOne
     */
    public Objective getCommonObjectiveOne() {
        return commonObjectiveOne;
    }

    /**
     * Setter method for commonObjectiveOne
     * @param commonObjectiveOne:is the first objective in common for this game
     */
    public void setCommonObjectiveOne(Objective commonObjectiveOne) {
        this.commonObjectiveOne = commonObjectiveOne;
    }

    /**
     * Getter method for commonObjectiveTwo
     * @return commonObjectiveTwo
     */
    public Objective getCommonObjectiveTwo() {
        return commonObjectiveTwo;
    }

    /**
     * Setter method for commonObjectiveTwo
     * @param commonObjectiveTwo:is the second objective in common for this game
     */
    public void setCommonObjectiveTwo(Objective commonObjectiveTwo) {
        this.commonObjectiveTwo = commonObjectiveTwo;
    }

    /**
     * Getter method for ResourceCardDeck
     * @return ResourceCardDeck
     */
    public Deck getResourceCardDeck() {
        return ResourceCardDeck;
    }

    /**
     * Setter method for ResourceCardDeck
     * @param resourceCardDeck:resource card deck, one of the four types of deck
     */
    public void setResourceCardDeck(Deck resourceCardDeck) {
        ResourceCardDeck = resourceCardDeck;
    }

    /**
     * Getter method for GoldCardDeck
     * @return GoldCardDeck
     */
    public Deck getGoldCardDeck() {
        return GoldCardDeck;
    }

    /**
     * Setter method for goldCardDeck
     * @param goldCardDeck:gold card deck, one of the four types of deck
     */
    public void setGoldCardDeck(Deck goldCardDeck) {
        GoldCardDeck = goldCardDeck;
    }

    /**
     * Getter method for StarterCardDeck
     * @return StarterCardDeck
     */
    public Deck getStarterCardDeck() {
        return StarterCardDeck;
    }

    /**
     * Setter method for starterCardDeck
     * @param starterCardDeck:starter card deck, one of the four types of deck
     */
    public void setStarterCardDeck(Deck starterCardDeck) {
        StarterCardDeck = starterCardDeck;
    }

    /**
     * Getter method for ObjectiveCardDeck
     * @return ObjectiveCardDeck
     */
    public Deck getObjectiveDeck() {
        return ObjectiveDeck;
    }

    /**
     * Setter method for objectiveCardDeck
     * @param objectiveDeck:objective card deck, one of the four types of deck
     */
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

    /**
     * Initialization four decks
     * @return Create and return playing deck
     */
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
    /**
     * Class constructor
     * @param resourceCardOne:the first resource card on the field that you can take
     * @param resourceCardTwo:the second resource card on the field that you can take
     * @param goldCardOne:the first gold card on the field that you can take
     * @param goldCardTwo:the second gold card on the field that you can take
     * @param commonObjectiveOne:the first objective card on the field
     * @param commonObjectiveTwo:the second objective card on the field
     * @param resourceCardDeck:resource card deck
     * @param goldCardDeck:gold card deck
     * @param starterCardDeck:starter card deck(this deck will not be available through the game)
     * @param objectiveDeck:objective card deck
     */
    // Constructor Method only used internally
    // We need for the test, but Private is better
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
