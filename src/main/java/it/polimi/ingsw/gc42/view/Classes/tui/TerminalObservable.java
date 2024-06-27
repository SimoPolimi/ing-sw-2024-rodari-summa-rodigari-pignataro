package it.polimi.ingsw.gc42.view.Classes.tui;

public interface TerminalObservable {
    void listen(TerminalListener listener);
    void unlisten(TerminalListener listener);
    void notifyListeners(String input);
}
