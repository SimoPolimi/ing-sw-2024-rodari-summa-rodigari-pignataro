package it.polimi.ingsw.gc42.model.classes;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import it.polimi.ingsw.gc42.model.classes.cards.*;
import it.polimi.ingsw.gc42.model.interfaces.EmptyDeckListener;
import it.polimi.ingsw.gc42.model.interfaces.Listener;
import it.polimi.ingsw.gc42.model.interfaces.Observable;
import it.polimi.ingsw.gc42.view.Interfaces.DeckViewListener;

import java.io.*;
import java.util.*;

/**
 * Implementation of Deck for Model.
 * A Deck is a group of Cards, all belonging the same Type.
 */
public class Deck implements Observable, Serializable {
    // Attributes
    private final ArrayList<Listener> listeners = new ArrayList<>();
    private final ArrayList<Card> cards = new ArrayList<>();
    private final CardType cardType;

    // Constructor

    /**
     * Constructor Method
     *
     * @param cardType: Type of the Cards contained inside the Deck
     */
    public Deck(CardType cardType) {
        this.cardType = cardType;
    }

    // Getter and Setter

    /**
     * Getter Method for counter
     *
     * @return the number of Cards remaining inside the Deck
     */
    public int getNumberOfCards() {
        return cards.size();
    }

    /**
     * Getter Method for cardType
     *
     * @return the type of Cards the Deck is made of
     */
    public CardType getCardType() {
        return cardType;
    }

    // Methods


    /**
     * Initializer Method.
     * Creates and Initialized a Deck of the CardType specified as a parameter.
     * The Deck is initialized with all the Cards of that CardType, all already initialized based
     * on the parameters read from the data.json file.
     *
     * @param type: the CardType that will be contained inside the Deck.
     * @return the initialized Deck, already shuffled.
     */
    public static Deck initDeck(CardType type) {
        Deck deck = new Deck(type);
        int num;
        List<JsonElement> list;
        InputStream inputStream = Objects.requireNonNull(Deck.class.getResourceAsStream("/data.json"));
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        JsonObject object = JsonParser.parseReader(new JsonReader(inputStreamReader)).getAsJsonObject();
        switch (type) {
            case RESOURCECARD:
                num = object.get("Game").getAsJsonObject().get("ResourceCardsNumber").getAsInt();
                list = object.get("ResourceCards").getAsJsonObject().get("list").getAsJsonArray().asList();
                for (int i = 0; i < num; i++) {
                    int id = list.get(i).getAsJsonObject().get("Id").getAsInt();
                    String frontImage = list.get(i).getAsJsonObject().get("FrontImage").getAsJsonPrimitive().getAsString();
                    String backImage = list.get(i).getAsJsonObject().get("BackImage").getAsString();
                    Item item = getKingdom(list.get(i).getAsJsonObject().get("Kingdom").getAsString());
                    int points = list.get(i).getAsJsonObject().get("Points").getAsInt();
                    String upperLeftFront = list.get(i).getAsJsonObject().getAsJsonObject("FrontSide").get("UpperLeftCorner").getAsJsonPrimitive().getAsString();
                    String upperRightFront = list.get(i).getAsJsonObject().getAsJsonObject("FrontSide").get("UpperRightCorner").getAsJsonPrimitive().getAsString();
                    String bottomLeftFront = list.get(i).getAsJsonObject().getAsJsonObject("FrontSide").get("BottomLeftCorner").getAsJsonPrimitive().getAsString();
                    String bottomRightFront = list.get(i).getAsJsonObject().getAsJsonObject("FrontSide").get("BottomRightCorner").getAsJsonPrimitive().getAsString();
                    String upperLeftBack = list.get(i).getAsJsonObject().getAsJsonObject("BackSide").get("UpperLeftCorner").getAsJsonPrimitive().getAsString();
                    String upperRightBack = list.get(i).getAsJsonObject().getAsJsonObject("BackSide").get("UpperRightCorner").getAsJsonPrimitive().getAsString();
                    String bottomLeftBack = list.get(i).getAsJsonObject().getAsJsonObject("BackSide").get("BottomLeftCorner").getAsJsonPrimitive().getAsString();
                    String bottomRightBack = list.get(i).getAsJsonObject().getAsJsonObject("BackSide").get("BottomRightCorner").getAsJsonPrimitive().getAsString();
                    KingdomResource kingdom = (KingdomResource) getKingdom(list.get(i).getAsJsonObject().get("Kingdom").getAsString());
                    ArrayList<Item> permRes = new ArrayList<>();
                    permRes.add(item);
                    deck.cards.add(new ResourceCard(new CardSide(getCorner(upperLeftFront), getCorner(upperRightFront), getCorner(bottomLeftFront), getCorner(bottomRightFront)),
                            new CardSide(getCorner(upperLeftBack), getCorner(upperRightBack), getCorner(bottomLeftBack), getCorner(bottomRightBack)),
                            true, id, permRes, points, "/cards/"+frontImage, "/cards/"+backImage, kingdom));
                }
                break;
            case GOLDCARD:
                num = object.get("Game").getAsJsonObject().get("GoldCardsNumber").getAsInt();
                list = object.get("GoldCards").getAsJsonObject().get("list").getAsJsonArray().asList();
                for (int i = 0; i < num; i++) {
                    int id = list.get(i).getAsJsonObject().get("Id").getAsInt();
                    String frontImage = list.get(i).getAsJsonObject().get("FrontImage").getAsJsonPrimitive().getAsString();
                    String backImage = list.get(i).getAsJsonObject().get("BackImage").getAsString();
                    Item item = getKingdom(list.get(i).getAsJsonObject().get("Kingdom").getAsJsonPrimitive().getAsString());
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
                    KingdomResource kingdom = (KingdomResource) getKingdom(list.get(i).getAsJsonObject().get("Kingdom").getAsString());
                    ArrayList<Item> permRes = new ArrayList<>();
                    permRes.add(item);
                    Objective objective = getObjective(points, condition, true, null, null);
                    if (null != objective) {
                        points = 0;
                    }
                    deck.cards.add(new GoldCard(new CardSide(getCorner(upperLeftFront),
                            getCorner(upperRightFront), getCorner(bottomLeftFront),
                            getCorner(bottomRightFront)),
                            new CardSide(getCorner(upperLeftBack),
                                    getCorner(upperRightBack), getCorner(bottomLeftBack),
                                    getCorner(bottomRightBack)),
                            true, id, permRes,
                            plantCost, animalCost, fungiCost,
                            insectCost, objective, points, "/cards/"+frontImage, "/cards/"+backImage, kingdom));
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
                    Item res1 = getKingdom(list.get(i).getAsJsonObject().get("FrontSide").getAsJsonObject().get("PermanentResource").getAsJsonArray().asList().getFirst().getAsJsonPrimitive().getAsString());
                    Item res2;
                    Item res3;
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
                    ArrayList<Item> resList = new ArrayList<>();
                    if (null != res1) {
                        resList.add(res1);
                    }
                    if (null != res2) {
                        resList.add(res2);
                    }
                    if (null != res3) {
                        resList.add(res3);
                    }
                    deck.cards.add(new StarterCard(new CardSide(getCorner(upperLeftFront), getCorner(upperRightFront), getCorner(bottomLeftFront), getCorner(bottomRightFront)),
                            new CardSide(getCorner(upperLeftBack), getCorner(upperRightBack), getCorner(bottomLeftBack), getCorner(bottomRightBack)),
                            true, id, resList, 0, "/cards/"+frontImage, "/cards/"+backImage));
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
                    String direction = list.get(i).getAsJsonObject().get("Direction").getAsJsonPrimitive().getAsString();
                    String description = list.get(i).getAsJsonObject().get("Description").getAsJsonPrimitive().getAsString();
                    boolean isLeftToRight;
                    isLeftToRight = direction.equals("topLeftToBottomRight");
                    Objective objective = getObjective(points, condition, isLeftToRight, name, description);
                    deck.cards.add(new ObjectiveCard(id, objective, "/cards/"+frontImage, "/cards/"+backImage));
                }
        }
        deck.shuffle();
        return deck;
    }

    /**
     * Static Method used by initDeck() to initialize the Corners of the Cards.
     *
     * @param string: the name of the Corner read from the file.
     * @return the appropriate Corner object, null if there is no Corner.
     */
    private static Corner getCorner(String string) {
        return switch (string) {
            case "Fungi" -> new Corner(KingdomResource.FUNGI);
            case "Plant" -> new Corner(KingdomResource.PLANT);
            case "Animal" -> new Corner(KingdomResource.ANIMAL);
            case "Insect" -> new Corner(KingdomResource.INSECT);
            case "Potion" -> new Corner(Resource.POTION);
            case "Feather" -> new Corner(Resource.FEATHER);
            case "Scroll" -> new Corner(Resource.SCROLL);
            case "EmptyCorner" -> new Corner();
            default -> null;
        };
    }

    /**
     * Static Method used by initDeck() to get the appropriate KingdomResource for the Card.
     *
     * @param string: the KingdomResource read from the file.
     * @return the appropriate KingdomResource.
     */
    private static Item getKingdom(String string) {
        return switch (string) {
            case "Fungi" -> KingdomResource.FUNGI;
            case "Plant" -> KingdomResource.PLANT;
            case "Animal" -> KingdomResource.ANIMAL;
            case "Insect" -> KingdomResource.INSECT;
            default -> null;
        };
    }

    /**
     * Static Method used by initDeck() to get the appropriate Objective, already initialized.
     *
     * @param points:        number of points the Objective gives every time its condition is satisfied.
     * @param condition:     the string read from the file, containing the name of the Condition to satisfy.
     * @param isLeftToRight: a boolean indicating the direction, used for DiagonalPlacingObjectives.
     * @param description:   a String containing the Objective Description, read from the file.
     * @return the appropriate Objective initialized with the data passed as parameters.
     */
    private static Objective getObjective(int points, String condition, boolean isLeftToRight, String name, String description) {
        return switch (condition) {
            case "forEachScroll" -> new ItemCountObjective(points, 1, Resource.SCROLL, name, description);
            case "forEachPotion" -> new ItemCountObjective(points, 1, Resource.POTION, name, description);
            case "forEachFeather" -> new ItemCountObjective(points, 1, Resource.FEATHER, name, description);
            case "forEachCorner" -> new CornerCountObjective(points, 1, name, description);
            case "diagonalPlacingRed" ->
                    new DiagonalPlacementObjective(points, KingdomResource.FUNGI, isLeftToRight, name, description);
            case "diagonalPlacingGreen" ->
                    new DiagonalPlacementObjective(points, KingdomResource.PLANT, isLeftToRight, name, description);
            case "diagonalPlacingBlue" ->
                    new DiagonalPlacementObjective(points, KingdomResource.ANIMAL, isLeftToRight, name, description);
            case "diagonalPlacingPurple" ->
                    new DiagonalPlacementObjective(points, KingdomResource.INSECT, isLeftToRight, name, description);
            case "LShapedPlacingRed" ->
                    new LShapedPlacementObjective(points, KingdomResource.FUNGI, KingdomResource.PLANT, LPlacementOffset.BOTTOM_RIGHT, name, description);
            case "LShapedPlacingGreen" ->
                    new LShapedPlacementObjective(points, KingdomResource.PLANT, KingdomResource.INSECT, LPlacementOffset.BOTTOM_LEFT, name, description);
            case "LShapedPlacingBlue" ->
                    new LShapedPlacementObjective(points, KingdomResource.ANIMAL, KingdomResource.FUNGI, LPlacementOffset.BOTTOM_RIGHT, name, description);
            case "LShapedPlacingPurple" ->
                    new LShapedPlacementObjective(points, KingdomResource.INSECT, KingdomResource.ANIMAL, LPlacementOffset.TOP_LEFT, name, description);
            case "forEach3KingdomResourcesFungi" ->
                    new ItemCountObjective(points, 3, KingdomResource.FUNGI, name, description);
            case "forEach3KingdomResourcesPlant" ->
                    new ItemCountObjective(points, 3, KingdomResource.PLANT, name, description);
            case "forEach3KingdomResourcesAnimal" ->
                    new ItemCountObjective(points, 3, KingdomResource.ANIMAL, name, description);
            case "forEach3KingdomResourcesInsect" ->
                    new ItemCountObjective(points, 3, KingdomResource.INSECT, name, description);
            case "forEach3DifferentItems" -> new SetItemCountObjective(points, 3, name, description);
            case "forEach2Scrolls" -> new ItemCountObjective(points, 2, Resource.SCROLL, name, description);
            case "forEach2Potions" -> new ItemCountObjective(points, 2, Resource.POTION, name, description);
            case "forEach2Feathers" -> new ItemCountObjective(points, 2, Resource.FEATHER, name, description);
            default -> null;
        };
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
     * Once drawn, the Card is also removed from the ArrayList.
     * If it's the last Card of the Deck, all its Listeners are notified that the Deck is empty.
     *
     * @return the drawn Card.
     * @throws NoSuchElementException if there are 0 Cards left inside the Deck
     */
    public Card draw() throws NoSuchElementException {
        Card card = cards.getFirst();
        cards.removeFirst();
        if (cards.isEmpty()) {
            notifyListeners("Empty Deck");
        }
        notifyListeners("Deck has changed");
        return card;
    }

    /**
     * Getter Method for the first Card inside the Deck.
     * IT DOES NOT REMOVE THE CARD FROM THE DECK.
     * Use draw() to get the Card AND remove it.
     *
     * @return the first Card inside the Deck.
     */
    public Card getTopCard() {
        return cards.getFirst();
    }

    public ArrayList<Card> getCopy() {
        return new ArrayList<>(cards);
    }

    /**
     * Method from the Observable interface.
     * Adds listener to the ArrayList of Listener objects who are subscribed to the Deck.
     *
     * @param listener: object containing the lambda function to execute once the Deck is emptied.
     */
    @Override
    public void setListener(Listener listener) {
        listeners.add(listener);
    }

    /**
     * Method from the Observable interface.
     * Removes listener from the ArrayList of Listener objects, so that it doesn't get notified of its status anymore.
     *
     * @param listener: object containing the lambda function to execute once the Deck is emptied
     */
    @Override
    public void removeListener(Listener listener) {
        listeners.remove(listener);
    }

    /**
     * Method from the Observable interface.
     * For each Listener inside the ArrayList, it calls the onEvent() method that runs the lambda contained in them.
     */
    @Override
    public void notifyListeners(String context) {
        switch (context) {
            case "Empty Deck" -> {
                for (Listener l : listeners) {
                    if (l instanceof EmptyDeckListener) {
                        l.onEvent();
                    }
                }
            }
            case "Deck has changed" -> {
                for (Listener l: listeners) {
                    if (l instanceof DeckViewListener) {
                        l.onEvent();
                    }
                }
            }
        }
    }
}