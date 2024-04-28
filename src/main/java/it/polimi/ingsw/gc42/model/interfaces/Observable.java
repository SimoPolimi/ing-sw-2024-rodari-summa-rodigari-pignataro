package it.polimi.ingsw.gc42.model.interfaces;

import java.io.Serializable;

public interface Observable extends Serializable {

    void setListener(Listener listener);
    void removeListener(Listener listener);
    void notifyListeners(String context);
}