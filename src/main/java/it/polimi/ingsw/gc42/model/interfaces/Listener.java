package it.polimi.ingsw.gc42.model.interfaces;

import java.io.Serializable;
import java.util.EventListener;

public interface Listener extends EventListener, Serializable {

    void onEvent();
}
