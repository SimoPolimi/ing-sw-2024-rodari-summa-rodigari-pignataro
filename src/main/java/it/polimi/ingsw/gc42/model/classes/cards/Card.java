package it.polimi.ingsw.gc42.model.classes.cards;

import it.polimi.ingsw.gc42.model.interfaces.CardObservable;
import it.polimi.ingsw.gc42.model.interfaces.Listener;
import it.polimi.ingsw.gc42.model.interfaces.Observable;
import javafx.beans.InvalidationListener;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Implementation of Card for Model
 * Test
 */
public class Card implements Observable {
    // Attributes
    private ArrayList<Listener> listeners;
    private CardSide frontSide;
    private CardSide backSide;
    private boolean isFrontFacing;
    private int id;
    private int x;
    private int y;
    private Image frontImage;
    private Image backImage;
    // TODO: Add ViewCard data type
    //private List<> views = new ArrayList<>;

    // Constructor Method

    /**
     *Class Constructors
     * @param frontSide         group of Corners that make the front Side of the card
     * @param backSide          group of empty Corners that make the back Side of the card, plus other features like permanent resources
     * @param isFrontFacing     true if the Card is showing the front Side, false if it is showing the back Side
     * @param id                unique identifier for the specific Card
     * @param x                 horizontal coordinate for the Card's position on the table (null if not placed)
     * @param y                 vertical coordinate for the Card's position on the table (null if not placed)
     */
    public Card(CardSide frontSide, CardSide backSide, boolean isFrontFacing, int id, int x, int y, String frontImage, String backImage) {
        this.frontSide = frontSide;
        this.backSide = backSide;
        this.isFrontFacing = isFrontFacing;
        this.id = id;
        this.x = x;
        this.y = y;
        this.frontImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/cards/" + frontImage)));
        this.backImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/cards/" + backImage)));
        listeners = new ArrayList<Listener>();
    }

    /**
     * Class Constructor without the x and y parameters, to generate Cards without having to place them already
     * @param frontSide         group of Corners that make the front Side of the card
     * @param backSide          group of empty Corners that make the back Side of the card, plus other features like permanent resources
     * @param isFrontFacing     true if the Card is showing the front Side, false if it is showing the back Side
     * @param id                unique identifier for the specific Card
     */
    public Card(CardSide frontSide, CardSide backSide, boolean isFrontFacing, int id, String frontImage, String backImage) {
        this.frontSide = frontSide;
        this.backSide = backSide;
        this.isFrontFacing = isFrontFacing;
        this.id = id;
        this.x = 0;
        this.y = 0;
        this.frontImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/cards/" + frontImage)));
        this.backImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/cards/" + backImage)));
        listeners = new ArrayList<Listener>();
    }

    // Getter and Setter Methods

    public CardSide getShowingSide() {
        if (isFrontFacing) {
            return frontSide;
        } else {
            return backSide;
        }
    }

    /**
     * Getter Method for frontSide
     * @return frontSide: Side shown on the front of the Card
     */
    public CardSide getFrontSide() {
        return frontSide;
    }

    /**
     * Setter Method for frontSide
     * @param frontSide: group of Corners that make the front Side of the card
     */
    public void setFrontSide(CardSide frontSide) {
        this.frontSide = frontSide;
    }

    /**
     * Getter Method for backSide
     * @return backSide: Side shown on the back of the Card
     */
    public CardSide getBackSide() {
        return backSide;
    }

    /**
     * Setter Method for backSide
     * @param backSide: group of Corners that make the back Side of the card
     */
    public void setBackSide(CardSide backSide) {
        this.backSide = backSide;
    }

    /**
     * Getter Method for isFrontFacing
     * @return isFrontFacing: true if the Card is showing its Front Side, false otherwise
     */
    public boolean isFrontFacing() {
        return isFrontFacing;
    }

    /**
     * Setter Method for isFrontFacing
     * Allows to change the Card's showing Side
     * Private Method: DO NOT CALL. Use flip()
     * @param frontFacing: true if the Card is showing its Front Side, false otherwise
     */
    private void setFrontFacing(boolean frontFacing) {
        isFrontFacing = frontFacing;
    }

    /**
     * Getter Method for id
     * @return id: unique identifier for the specific Card
     */
    public int getId() {
        return id;
    }

    /**
     * Setter Method for id
     * @param id: unique identifier for the specific Card
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Getter method for x
     * @return x: horizontal coordinate for the Card's position on the table (null if not placed)
     */
    public int getX() {
        return x;
    }

    /**
     * Setter Method for x
     * @param x: horizontal coordinate for the Card's position on the table (null if not placed)
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Getter Method for y
     * @return y: vertical coordinate for the Card's position on the table (null if not placed)
     */
    public int getY() {
        return y;
    }

    /**
     * Setter Method for y
     * @param y: vertical coordinate for the Card's position on the table (null if not placed)
     */
    public void setY(int y) {
        this.y = y;
    }

    public Image getFrontImage() {
        return frontImage;
    }

    public void setFrontImage(Image frontImage) {
        this.frontImage = frontImage;
    }

    public Image getBackImage() {
        return backImage;
    }

    public void setBackImage(Image backImage) {
        this.backImage = backImage;
    }

    // Methods

    /**
     * Flips the Card, changing which Side it is showing.
     * Inverts the value inside isFrontFacing.
     */
    public void flip() {
        setFrontFacing(!isFrontFacing());
        notifyListeners();
    }

    //TODO: Add documentation

    //TODO: Implement View Card data type

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
        for (Listener l: listeners) {
            l.onEvent();
        }
    }
}