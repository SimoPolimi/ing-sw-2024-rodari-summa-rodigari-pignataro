package it.polimi.ingsw.gc42.interfaces;

public interface Observable {
    void setListener(Listener listener);
    void removeListener(Listener listener);
    void notifyListeners();
}