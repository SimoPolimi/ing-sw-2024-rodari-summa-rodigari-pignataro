package it.polimi.ingsw.gc42.interfaces;

public interface DeckObservable {
    void register(DeckListener listener);
    void eventHappens();
}
