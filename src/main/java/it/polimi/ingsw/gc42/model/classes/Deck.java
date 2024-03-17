package it.polimi.ingsw.gc42.model.classes;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import it.polimi.ingsw.gc42.model.classes.cards.*;
import it.polimi.ingsw.gc42.model.interfaces.Listener;
import it.polimi.ingsw.gc42.model.interfaces.Observable;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

/**
 * Implementation of Deck for Model.
 * A Deck is a group of Cards, all belonging the same Type.
 */
public class Deck implements Observable {
    // Attributes
    private ArrayList<Listener> listeners = new ArrayList<>();
    private List<Card> cards;
    private int counter;
    private CardType cardType;

    // Constructor

    /**
     * Constructor Method
     *
     * @param cards:    ArrayList containing the Card that make up the Deck
     * @param counter:  number of Cards contained inside the Deck
     * @param cardType: Type of the Cards contained inside the Deck
     */
    public Deck(ArrayList<Card> cards, int counter, CardType cardType) {
        this.cards = cards;
        this.counter = counter;
        this.cardType = cardType;
    }

    // Getter and Setter

    /**
     * Getter Method for cards
     *
     * @return cards: ArrayList of Cards
     */
    public List<Card> getCards() {
        return cards;
    }

    /**
     * Setter Method for cards
     *
     * @param cards: ArrayList of Cards
     */
    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    /**
     * Getter Method for counter
     *
     * @return counter: number of Cards remaining inside the Deck
     */
    public int getCounter() {
        return counter;
    }

    /**
     * Setter Method for counter
     *
     * @param counter: number of Cards remaining inside the Deck
     */
    public void setCounter(int counter) {
        this.counter = counter;
    }

    /**
     * Getter Method for cardType
     *
     * @return the type of Cards the Deck is made of
     */
    public CardType getCardType() {
        return cardType;
    }

    /**
     * Setter Method for cardType
     *
     * @param cardType: the type of Cards the Deck is made of
     */
    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }

    // Methods


    public static Deck initDeck(CardType type) throws FileNotFoundException {
        //TODO: Fully Implement
        ArrayList<Card> cards = new ArrayList<>();
        int num;
        List<JsonElement> list;
        JsonObject object = JsonParser.parseReader(new JsonReader(new FileReader("src/main/resources/data.json"))).getAsJsonObject();
        switch (type) {
            case RESOURCECARD:
                num = object.get("Game").getAsJsonObject().get("ResourceCardsNumber").getAsInt();
                list = object.get("ResourceCards").getAsJsonObject().get("list").getAsJsonArray().asList();
                for (int i = 0; i < num; i++) {
                    int id = list.get(i).getAsJsonObject().get("Id").getAsInt();
                    String frontImage = list.get(i).getAsJsonObject().get("FrontImage").getAsJsonPrimitive().getAsString();
                    String backImage = list.get(i).getAsJsonObject().get("BackImage").getAsString();
                    KingdomResource kingdom = getKingdom(list.get(i).getAsJsonObject().get("Kingdom").getAsString());
                    int points = list.get(i).getAsJsonObject().get("Points").getAsInt();
                    String upperLeftFront = list.get(i).getAsJsonObject().getAsJsonObject("FrontSide").get("UpperLeftCorner").getAsJsonPrimitive().getAsString();
                    String upperRightFront = list.get(i).getAsJsonObject().getAsJsonObject("FrontSide").get("UpperRightCorner").getAsJsonPrimitive().getAsString();
                    String bottomLeftFront = list.get(i).getAsJsonObject().getAsJsonObject("FrontSide").get("BottomLeftCorner").getAsJsonPrimitive().getAsString();
                    String bottomRightFront = list.get(i).getAsJsonObject().getAsJsonObject("FrontSide").get("BottomRightCorner").getAsJsonPrimitive().getAsString();
                    String upperLeftBack = list.get(i).getAsJsonObject().getAsJsonObject("BackSide").get("UpperLeftCorner").getAsJsonPrimitive().getAsString();
                    String upperRightBack = list.get(i).getAsJsonObject().getAsJsonObject("BackSide").get("UpperRightCorner").getAsJsonPrimitive().getAsString();
                    String bottomLeftBack = list.get(i).getAsJsonObject().getAsJsonObject("BackSide").get("BottomLeftCorner").getAsJsonPrimitive().getAsString();
                    String bottomRightBack = list.get(i).getAsJsonObject().getAsJsonObject("BackSide").get("BottomRightCorner").getAsJsonPrimitive().getAsString();
                    cards.add(new ResourceCard(new CardSide(getCorner(upperLeftFront), getCorner(upperRightFront), getCorner(bottomLeftFront), getCorner(bottomRightFront)),
                            new CardSide(getCorner(upperLeftBack), getCorner(upperRightBack), getCorner(bottomLeftBack), getCorner(bottomRightBack)),
                            true, id, kingdom, points, frontImage, backImage));
                }
                break;
            case GOLDCARD:
                num = object.get("Game").getAsJsonObject().get("GoldCardsNumber").getAsInt();
                list = object.get("GoldCards").getAsJsonObject().get("list").getAsJsonArray().asList();
                for (int i = 0; i < num; i++) {
                    int id = list.get(i).getAsJsonObject().get("Id").getAsInt();
                    String frontImage = list.get(i).getAsJsonObject().get("FrontImage").getAsJsonPrimitive().getAsString();
                    String backImage = list.get(i).getAsJsonObject().get("BackImage").getAsString();
                    KingdomResource kingdom = getKingdom(list.get(i).getAsJsonObject().get("Kingdom").getAsJsonPrimitive().getAsString());
                    String condition = list.get(i).getAsJsonObject().get("Condition").getAsJsonPrimitive().getAsString();
                    int points = list.get(i).getAsJsonObject().get("Points").getAsInt();
                    int fungiCost = list.get(i).getAsJsonObject().get("FungiCost").getAsInt();
                    int plantCost = list.get(i).getAsJsonObject().get("PlantCost").getAsInt();
                    int animalCost = list.get(i).getAsJsonObject().get("AnimalCost").getAsInt();
                    int insectCost = list.get(i).getAsJsonObject().get("InsectCost").getAsInt();
                    String upperLeftFront = list.get(i).getAsJsonObject().getAsJsonObject("FrontSide").get("UpperLeftCorner").getAsJsonPrimitive().getAsString();
                    String upperRightFront = list.get(i).getAsJsonObject().getAsJsonObject("FrontSide").get("UpperRightCorner").getAsJsonPrimitive().getAsString();
                    String bottomLeftFront = list.get(i).getAsJsonObject().getAsJsonObject("FrontSide").get("BottomLeftCorner").getAsJsonPrimitive().getAsString();
                    String bottomRightFront = list.get(i).getAsJsonObject().getAsJsonObject("FrontSide").get("BottomRightCorner").getAsJsonPrimitive().getAsString();
                    String upperLeftBack = list.get(i).getAsJsonObject().getAsJsonObject("BackSide").get("UpperLeftCorner").getAsJsonPrimitive().getAsString();
                    String upperRightBack = list.get(i).getAsJsonObject().getAsJsonObject("BackSide").get("UpperRightCorner").getAsJsonPrimitive().getAsString();
                    String bottomLeftBack = list.get(i).getAsJsonObject().getAsJsonObject("BackSide").get("BottomLeftCorner").getAsJsonPrimitive().getAsString();
                    String bottomRightBack = list.get(i).getAsJsonObject().getAsJsonObject("BackSide").get("BottomRightCorner").getAsJsonPrimitive().getAsString();
                    cards.add(new GoldCard(new CardSide(getCorner(upperLeftFront), getCorner(upperRightFront), getCorner(bottomLeftFront), getCorner(bottomRightFront)),
                            new CardSide(getCorner(upperLeftBack), getCorner(upperRightBack), getCorner(bottomLeftBack), getCorner(bottomRightBack)),
                            true, id, kingdom, plantCost, animalCost, fungiCost, insectCost, getObjective(points, condition, true, null),
                            points, frontImage, backImage));
                }
                break;
            case STARTERCARD:
                num = object.get("Game").getAsJsonObject().get("StarterCardsNumber").getAsInt();
                list = object.get("StarterCards").getAsJsonObject().get("list").getAsJsonArray().asList();
                for (int i = 0; i < num; i++) {
                    int id = list.get(i).getAsJsonObject().get("Id").getAsInt();
                    String frontImage = list.get(i).getAsJsonObject().get("FrontImage").getAsJsonPrimitive().getAsString();
                    String backImage = list.get(i).getAsJsonObject().get("BackImage").getAsString();
                    int permanentResourceNumber = list.get(i).getAsJsonObject().get("FrontSide").getAsJsonObject().get("PermanentResourceNumber").getAsInt();
                    KingdomResource res1 = getKingdom(list.get(i).getAsJsonObject().get("FrontSide").getAsJsonObject().get("PermanentResource").getAsJsonArray().asList().get(0).getAsJsonPrimitive().getAsString());
                    KingdomResource res2;
                    KingdomResource res3;
                    //TODO: Remove and make better
                    if (permanentResourceNumber > 1) {
                        res2 = getKingdom(list.get(i).getAsJsonObject().get("FrontSide").getAsJsonObject().get("PermanentResource").getAsJsonArray().asList().get(1).getAsJsonPrimitive().getAsString());
                        if (permanentResourceNumber == 3) {
                            res3 = getKingdom(list.get(i).getAsJsonObject().get("FrontSide").getAsJsonObject().get("PermanentResource").getAsJsonArray().asList().get(2).getAsJsonPrimitive().getAsString());
                        } else {
                            res3 = null;
                        }
                    } else {
                        res2 = null;
                        res3 = null;
                    }
                    String upperLeftFront = list.get(i).getAsJsonObject().getAsJsonObject("FrontSide").get("UpperLeftCorner").getAsJsonPrimitive().getAsString();
                    String upperRightFront = list.get(i).getAsJsonObject().getAsJsonObject("FrontSide").get("UpperRightCorner").getAsJsonPrimitive().getAsString();
                    String bottomLeftFront = list.get(i).getAsJsonObject().getAsJsonObject("FrontSide").get("BottomLeftCorner").getAsJsonPrimitive().getAsString();
                    String bottomRightFront = list.get(i).getAsJsonObject().getAsJsonObject("FrontSide").get("BottomRightCorner").getAsJsonPrimitive().getAsString();
                    String upperLeftBack = list.get(i).getAsJsonObject().getAsJsonObject("BackSide").get("UpperLeftCorner").getAsJsonPrimitive().getAsString();
                    String upperRightBack = list.get(i).getAsJsonObject().getAsJsonObject("BackSide").get("UpperRightCorner").getAsJsonPrimitive().getAsString();
                    String bottomLeftBack = list.get(i).getAsJsonObject().getAsJsonObject("BackSide").get("BottomLeftCorner").getAsJsonPrimitive().getAsString();
                    String bottomRightBack = list.get(i).getAsJsonObject().getAsJsonObject("BackSide").get("BottomRightCorner").getAsJsonPrimitive().getAsString();
                    cards.add(new StarterCard(new CardSide(getCorner(upperLeftFront), getCorner(upperRightFront), getCorner(bottomLeftFront), getCorner(bottomRightFront)),
                            new CardSide(getCorner(upperLeftBack), getCorner(upperRightBack), getCorner(bottomLeftBack), getCorner(bottomRightBack)),
                            true, id, res1, res2, res3, frontImage, backImage));
                }
                break;
            case OBJECTIVECARD:
                num = object.get("Game").getAsJsonObject().get("ObjectiveCardsNumber").getAsInt();
                list = object.get("ObjectiveCards").getAsJsonObject().get("list").getAsJsonArray().asList();
                for (int i = 0; i < num; i++) {
                    int id = list.get(i).getAsJsonObject().get("Id").getAsInt();
                    String frontImage = list.get(i).getAsJsonObject().get("FrontImage").getAsJsonPrimitive().getAsString();
                    String backImage = list.get(i).getAsJsonObject().get("BackImage").getAsString();
                    int points = list.get(i).getAsJsonObject().get("Points").getAsInt();
                    String condition = list.get(i).getAsJsonObject().get("Condition").getAsJsonPrimitive().getAsString();
                    String name = list.get(i).getAsJsonObject().get("Name").getAsJsonPrimitive().getAsString();
                    String primaryType = list.get(i).getAsJsonObject().get("PrimaryKingdom").getAsJsonPrimitive().getAsString();
                    String direction = list.get(i).getAsJsonObject().get("Direction").getAsJsonPrimitive().getAsString();
                    String description = list.get(i).getAsJsonObject().get("Description").getAsJsonPrimitive().getAsString();
                    boolean isLeftToRight = true;
                    if (direction.equals("topLeftToBottomRight")) {
                        isLeftToRight = true;
                    } else {
                        isLeftToRight = false;
                    }
                    Objective objective = getObjective(points, condition, isLeftToRight, description);
                    cards.add(new ObjectiveCard(null, null, true, id, points, objective, frontImage, backImage));
                }
        }
        Deck deck = new Deck(cards, cards.size(), type);
        deck.shuffle();
        return deck;
    }

    private static Corner getCorner(String string) {
        switch (string) {
            case "Fungi":
                return new KingdomCorner(KingdomResource.FUNGI);
            case "Plant":
                return new KingdomCorner(KingdomResource.PLANT);
            case "Animal":
                return new KingdomCorner(KingdomResource.ANIMAL);
            case "Insect":
                return new KingdomCorner(KingdomResource.INSECT);
            case "Potion":
                return new ResourceCorner(Resource.POTION);
            case "Feather":
                return new ResourceCorner(Resource.FEATHER);
            case "Scroll":
                return new ResourceCorner(Resource.SCROLL);
            case "EmptyCorner":
                return new EmptyCorner();
            default:
                return null;
        }
    }

    private static KingdomResource getKingdom(String string) {
        switch (string) {
            case "Fungi":
                return KingdomResource.FUNGI;
            case "Plant":
                return KingdomResource.PLANT;
            case "Animal":
                return KingdomResource.ANIMAL;
            case "Insect":
                return KingdomResource.INSECT;
            default:
                return null;
        }
    }

    private static Objective getObjective(int points, String condition, boolean isLeftToRight, String description) {
        switch (condition) {
            case "forEachScroll":
                return new ItemCountObjective(points, 1, Resource.SCROLL, description);
            case "forEachPotion":
                return new ItemCountObjective(points, 1, Resource.POTION, description);
            case "forEachFeather":
                return new ItemCountObjective(points, 1, Resource.FEATHER, description);
            case "forEachCorner":
                return new CornerCountObjective(points, 1, null, description);
            case "diagonalPlacingRed!":
                return new DiagonalPlacementObjective(points, KingdomResource.FUNGI, isLeftToRight, description);
            case "diagonalPlacingGreen!":
                return new DiagonalPlacementObjective(points, KingdomResource.PLANT, isLeftToRight, description);
            case "diagonalPlacingBlue":
                return new DiagonalPlacementObjective(points, KingdomResource.ANIMAL, isLeftToRight, description);
            case "diagonalPlacingPurple":
                return new DiagonalPlacementObjective(points, KingdomResource.INSECT, isLeftToRight, description);
            case "LShapedPlacingRed":
                return new LShapedPlacementObjective(points, KingdomResource.FUNGI, KingdomResource.PLANT, CornerPosition.BOTTOM_RIGHT, description);
            case "LShapedPlacingGreen":
                return new LShapedPlacementObjective(points, KingdomResource.PLANT, KingdomResource.INSECT, CornerPosition.BOTTOM_LEFT, description);
            case "LShapedPlacingBlue":
                return new LShapedPlacementObjective(points, KingdomResource.ANIMAL, KingdomResource.FUNGI, CornerPosition.BOTTOM_RIGHT, description);
            case "LShapedPlacingPurple":
                return new LShapedPlacementObjective(points, KingdomResource.INSECT, KingdomResource.ANIMAL, CornerPosition.TOP_LEFT, description);
            case "forEach3KingdomResourcesFungi":
                return new KingdomCountObjective(points, 3, KingdomResource.FUNGI, description);
            case "forEach3KingdomResourcesPlant":
                return new KingdomCountObjective(points, 3, KingdomResource.PLANT, description);
            case "forEach3KingdomResourcesAnimal":
                return new KingdomCountObjective(points, 3, KingdomResource.ANIMAL, description);
            case "forEach3KingdomResourcesInsect":
                return new KingdomCountObjective(points, 3, KingdomResource.INSECT, description);
            case "forEach3DifferentItems":
                return new SetItemCountObjective(points, 3, description);
            case "forEach2Scrolls":
                return new ItemCountObjective(points, 2, Resource.SCROLL, description);
            case "forEach2Potions":
                return new ItemCountObjective(points, 2, Resource.POTION, description);
            case "forEach2Feathers":
                return new ItemCountObjective(points, 2, Resource.FEATHER, description);
            default:
                return null;
        }
    }

    /**
     * Shuffles in random order the Cards inside the Deck
     */
    public void shuffle() {
        Collections.shuffle(cards);
    }

    /**
     * Draws/Extracts a Card from the Deck.
     * The drawn Card is the one at the TOP of the Deck.
     *
     * @return Card
     * @throws NoSuchElementException if there are 0 Cards left inside the Deck
     */
    public Card draw() throws NoSuchElementException {
        try {
            Card card = cards.getFirst();
            cards.removeFirst();
            counter--;
            if (cards.size() == 0) {
                notifyListeners();
            }
            return card;
        } catch (NoSuchElementException e) {
            //TODO: Remove after handling
            e.printStackTrace();
        }
        return null;
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
    public void notifyListeners() {
        for (Listener d : listeners) {
            d.onEvent();
        }
    }
}