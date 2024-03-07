package it.polimi.ingsw.gc42.classes;

import it.polimi.ingsw.gc42.interfaces.CardObservable;
import javafx.beans.InvalidationListener;
import javafx.geometry.Side;

import java.util.ArrayList;
import java.util.List;

public class Card implements CardObservable {
    // Attributes
    private Side frontSide;
    private Side backSide;
    private boolean isFrontFacing;
    private int id;
    private int x;
    private int y;
    // TODO: Add ViewCard data type
    //private List<> views = new ArrayList<>;

    // Constructor Method

    protected Card(Side frontSide, Side backSide, boolean isFrontFacing, int id, int x, int y) {
        this.frontSide = frontSide;
        this.backSide = backSide;
        this.isFrontFacing = isFrontFacing;
        this.id = id;
        this.x = x;
        this.y = y;
    }

    // Getter and Setter Methods
    public Side getFrontSide() {
        return frontSide;
    }

    public void setFrontSide(Side frontSide) {
        this.frontSide = frontSide;
    }

    public Side getBackSide() {
        return backSide;
    }

    public void setBackSide(Side backSide) {
        this.backSide = backSide;
    }

    public boolean isFrontFacing() {
        return isFrontFacing;
    }

    private void setFrontFacing(boolean frontFacing) {
        isFrontFacing = frontFacing;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

// Methods

    public void flip() {
        setFrontFacing(!isFrontFacing());
    }

    //TODO: Implement View Card data type
    @Override
    public void addListener(InvalidationListener invalidationListener) {
        //this.views.add(invalidationListener);
    }

    @Override
    public void removeListener(InvalidationListener invalidationListener) {
        //this.views.add(invalidationListener);
    }
}