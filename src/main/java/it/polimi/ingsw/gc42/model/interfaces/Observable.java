package it.polimi.ingsw.gc42.model.interfaces;

import java.io.Serializable;

public interface Observable extends Serializable {
    /**
     * Adds a Listener to the List of Listeners that will be notified
     * @param listener the Listener to add in the List
     */
    void setListener(Listener listener);
    /**
     * Removes an existing Listener to the List.
     * @param listener the Listener to remove from the List
     */
    void removeListener(Listener listener);
    /**
     * Notifies all the appropriate Listener inside the List
     * @param context a string specifying which event has been triggered
     */
    void notifyListeners(String context);
}