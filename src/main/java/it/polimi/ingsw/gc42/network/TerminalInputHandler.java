package it.polimi.ingsw.gc42.network;

import it.polimi.ingsw.gc42.model.interfaces.Listener;
import it.polimi.ingsw.gc42.model.interfaces.Observable;

import java.util.ArrayList;
import java.util.Scanner;

public class TerminalInputHandler implements Runnable, TerminalObservable {

    TerminalListener listener;
    private final Scanner scanner ;
    private boolean isRunning = true;

    public TerminalInputHandler(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public void run() {
        while (isRunning) {
            if (scanner.hasNext()) {
                notifyListeners(scanner.nextLine());
            }
        }
    }

    public void stop() {
        isRunning = false;
    }


    @Override
    public void listen(TerminalListener listener) {
        // Only one Thread can listen at any given time
        this.listener = listener;
    }

    @Override
    public void unlisten(TerminalListener listener) {
        this.listener = null;
    }

    @Override
    public void notifyListeners(String input) {
        if (null != listener) {
            listener.onEvent(input);
        }
    }
}
