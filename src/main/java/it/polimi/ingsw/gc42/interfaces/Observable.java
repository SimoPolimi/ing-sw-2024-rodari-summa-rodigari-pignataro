package it.polimi.ingsw.gc42.interfaces;

import it.polimi.ingsw.gc42.exceptions.NoSuchDeckTypeException;

import java.util.EventListener;

public interface Observable {
    void register(EventListener listener);
    void unregister(EventListener listener);
    void eventHappens() throws NoSuchDeckTypeException;
}