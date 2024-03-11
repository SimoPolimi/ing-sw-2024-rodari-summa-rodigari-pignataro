package it.polimi.ingsw.gc42.model.interfaces;

public interface Observable {

    void setListener(Listener listener);
    void removeListener(Listener listener);
    void notifyListeners();
}