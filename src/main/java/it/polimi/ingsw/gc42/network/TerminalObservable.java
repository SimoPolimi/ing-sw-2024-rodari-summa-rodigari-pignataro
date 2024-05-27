package it.polimi.ingsw.gc42.network;

import it.polimi.ingsw.gc42.model.interfaces.Listener;

public interface TerminalObservable {
    void listen(TerminalListener listener);
    void unlisten(TerminalListener listener);
    void notifyListeners(String input);
}
