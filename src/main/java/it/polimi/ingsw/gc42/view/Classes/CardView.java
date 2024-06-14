package it.polimi.ingsw.gc42.view.Classes;

import javafx.scene.image.Image;

import java.util.Objects;

/**
 * Implementation of a Card for the GUI.
 * A CardView is a container for a Card's textures, containing its Front and Back.
 */
public class CardView {
    private boolean isFrontFacing;
    private Image front;
    private Image back;
    private boolean oddRotation = true;

    /**
     * Constructor Method with Images.
     * All CardViews are created facing their Front Side by default.
     * @param front: Image containing the texture for the Front Side
     * @param back: Image containing the texture for the Back Side
     */
    public CardView(Image front, Image back) {
        this.isFrontFacing = true;
        this.front = front;
        this.back = back;
    }

    /**
     * Constructor Method with Strings.
     * All CardViews are created facing their Front Side by default.
     * @param frontName: String containing the texture URL path for the Front Side Image
     * @param backName: String containing the texture URL path for the Back Side Image
     */
    public CardView(String frontName, String backName) {
        this.isFrontFacing = true;
        this.front = new Image(Objects.requireNonNull(getClass().getResourceAsStream(frontName)));
        this.back = new Image(Objects.requireNonNull(getClass().getResourceAsStream(backName)));
    }

    /**
     * Getter Method for oddRotation.
     * This is used to specify the amount of times a CardView has been flipped, because the animation changes
     * based on that value.
     * @return a boolean value indicating whether it's an odd or even rotation (true for odd, false for even).
     */
    public boolean isOddRotation() {
        return oddRotation;
    }

    /**
     * Setter Method for oddRotation.
     * This is used to specify the amount of times a CardView has been flipped, because the animation changes
     * based on that value.
     * This needs to be called to invert the value every time a CardView is animated (flip).
     * @param oddRotation: a boolean value indicating whether the next rotation is an odd or even rotation (true for odd, false for even).
     */
    public void setOddRotation(boolean oddRotation) {
        this.oddRotation = oddRotation;
    }

    /**
     * Getter Method for isFrontFacing.
     * @return a boolean value indicating which Side the CardView is showing: true for Front, false for Back.
     */
    public boolean isFrontFacing() {
        return isFrontFacing;
    }

    /**
     * Setter Method for isFrontFacing.
     * @param frontFacing: a boolean value indicating which Side the CardView is showing: true for Front, false for Back.
     */
    public void setFrontFacing(boolean frontFacing) {
        isFrontFacing = frontFacing;
    }

    /**
     * Getter Method for the Front Image.
     * @return an Image containing the Front Image texture.
     */
    public Image getFront() {
        return front;
    }

    /**
     * Setter Method for the Front Image.
     * @param front: an Image containing the Front Image Texture.
     */
    public void setFront(Image front) {
        this.front = front;
    }

    /**
     * Getter Method for the Back Image.
     * @return an Image containing the Back Image texture.
     */
    public Image getBack() {
        return back;
    }

    /**
     * Setter Method for the Back Image.
     * @param back: an Image containing the Back Image Texture.
     */
    public void setBack(Image back) {
        this.back = back;
    }

    /**
     * Setter Method for isFrontFacing.
     * Inverts the current value of isFrontFacing.
     */
    public void flip() {
        setFrontFacing(!isFrontFacing);
    }
}

