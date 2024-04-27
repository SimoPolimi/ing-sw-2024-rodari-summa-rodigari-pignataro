package it.polimi.ingsw.gc42.model.classes.cards;

import it.polimi.ingsw.gc42.model.interfaces.Listener;
import it.polimi.ingsw.gc42.model.interfaces.Observable;
import javafx.scene.image.Image;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Implementation of Card for Model.
 * A Card contains all the information necessary to create, represent and operate a Card.
 * It's an Observable object, meaning that other objects can add a Listener to a Card to be notified
 * when the Card gets flipped.
 */
public class Card implements Observable, Serializable {
    // Attributes
    private final ArrayList<Listener> listeners;
    protected boolean isFrontFacing;
    private int id;
    private String frontImage;
    private String backImage;

    // Constructor Method

    /**
     * Constructor Method
     *
     * @param isFrontFacing: true if the Card is showing the front Side, false if it is showing the back Side
     * @param id:            unique identifier for the specific Card
     * @param frontImage:    String containing the name (+ extension) of the Image resource to display on the GUI
     * @param backImage:     String containing the name (+ extension) of the Image resource to display on the GUI
     */
    public Card(boolean isFrontFacing, int id, String frontImage, String backImage) {
        this.isFrontFacing = isFrontFacing;
        this.id = id;
        this.frontImage = "/cards/" + frontImage;
        this.backImage = "/cards/" + backImage;
        listeners = new ArrayList<>();
    }

    // Getter and Setter Methods
        /**
     * Getter Method for isFrontFacing
     *
     * @return isFrontFacing: true if the Card is showing its Front Side, false otherwise
     */
    public boolean isFrontFacing() {
        return isFrontFacing;
    }

    /**
     * Setter Method for isFrontFacing
     * Allows to change the Card's showing Side
     * Private Method: DO NOT CALL. Use flip()
     *
     * @param frontFacing: true if the Card is showing its Front Side, false otherwise
     */
    private void setFrontFacing(boolean frontFacing) {
        isFrontFacing = frontFacing;
    }

    /**
     * Getter Method for id
     *
     * @return id: unique identifier for the specific Card
     */
    public int getId() {
        return id;
    }

    /**
     * Setter Method for id
     *
     * @param id: unique identifier for the specific Card
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Getter Method for the FrontImage
     *
     * @return Image resource to display on the GUI
     */
    public String getFrontImage() {
        return frontImage;
    }

    /**
     * Setter Method for frontImage
     *
     * @param frontImage: the String containing the name (+ extension) of the Image resource to display on the GUI
     */
    public void setFrontImage(String frontImage) {
        this.frontImage = frontImage;
    }

    /**
     * Getter Method for backImage
     *
     * @return the Image resource to display on the GUI
     */
    public String getBackImage() {
        return backImage;
    }

    /**
     * Setter Method for backImage
     *
     * @param backImage: the String containing the name (+ extension) of the Image resource to display on the GUI
     */
    public void setBackImage(String backImage) {
        this.backImage = backImage;
    }

    /**
     * Getter Method for frontImage and backImage.
     * @return the appropriate Image resource to display on the GUI, based on which Side is shown.
     */
    public String getShowingImage() {
        if (isFrontFacing()) {
            return frontImage;
        } else {
            return backImage;
        }
    }

    // Methods

    /**
     * Flips the Card, changing which Side it is showing.
     * Inverts the value inside isFrontFacing.
     */
    public void flip() {
        setFrontFacing(!isFrontFacing());
        notifyListeners("Card Flipped");
    }

    /**
     * Method from the Observable interface.
     * Adds listener to the ArrayList of Listener objects who are subscribed to the Card.
     *
     * @param listener: object containing the lambda function to execute once the Card gets flipped.
     */
    @Override
    public void setListener(Listener listener) {
        listeners.add(listener);

    }

    /**
     * Method from the Observable interface.
     * Removes listener from the ArrayList of Listener objects, so that it doesn't get notified of its status anymore.
     *
     * @param listener: object containing the lambda function to execute once the Card gets flipped.
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
        for (Listener l : listeners) {
            l.onEvent();
        }
    }
}