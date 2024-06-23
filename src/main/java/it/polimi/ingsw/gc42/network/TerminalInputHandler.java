package it.polimi.ingsw.gc42.network;

import java.util.Scanner;

/**
 * This Class handles the User's inputs in Terminal during the TUI's execution.
 * All the Inputs are read and the current Listener is notified.
 * Only one Listener at a time is allowed: adding another one will overwrite the other.
 */
public class TerminalInputHandler implements Runnable, TerminalObservable {
    // Attributes
    TerminalListener listener;
    private final Scanner scanner ;
    private boolean isRunning = true;

    /**
     * Constructor Method
     * @param scanner the input reader
     */
    public TerminalInputHandler(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * Starts the Class
     */
    @Override
    public void run() {
        while (isRunning) {
            if (scanner.hasNext()) {
                notifyListeners(scanner.nextLine());
            }
        }
    }

    /**
     * Stops the Class
     */
    public void stop() {
        isRunning = false;
    }


    /**
     * Adds a Listener to read the next input
     * @param listener the code to execute once an input is registered
     */
    @Override
    public void listen(TerminalListener listener) {
        // Only one Thread can listen at any given time
        this.listener = listener;
    }

    /**
     * Removes the Listener and stops listening
     * @param listener the Listener to remove
     */
    @Override
    public void unlisten(TerminalListener listener) {
        this.listener = null;
    }

    /**
     * Notifies the Listener that the user wrote something, and executes its code
     * @param input the User's input
     */
    @Override
    public void notifyListeners(String input) {
        if (null != listener) {
            listener.onEvent(input);
        }
    }
}
