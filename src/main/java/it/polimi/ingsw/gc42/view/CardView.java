package it.polimi.ingsw.gc42.view;

import javafx.scene.image.Image;

import java.util.Objects;

public class CardView {
    private boolean isFrontFacing;
    private Image front;
    private Image back;
    private boolean oddRotation = true;

    public CardView(Image front, Image back) {
        this.isFrontFacing = true;
        this.front = front;
        this.back = back;
    }

    public CardView(String frontName, String backName) {
        this.isFrontFacing = true;
        this.front = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/card/" + frontName)));
        this.back = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/card/" + backName)));
    }

    public boolean isOddRotation() {
        return oddRotation;
    }

    public void setOddRotation(boolean oddRotation) {
        this.oddRotation = oddRotation;
    }

    public boolean isFrontFacing() {
        return isFrontFacing;
    }

    public void setFrontFacing(boolean frontFacing) {
        isFrontFacing = frontFacing;
    }

    public Image getFront() {
        return front;
    }

    public void setFront(Image front) {
        this.front = front;
    }

    public Image getBack() {
        return back;
    }

    public void setBack(Image back) {
        this.back = back;
    }

    public void flip() {
        setFrontFacing(!isFrontFacing);
    }
}

