package it.polimi.ingsw.gc42.interfaces;

import java.util.EventListener;

public interface Observable {
    void register(EventListener listener);
    void eventHappens();
}
