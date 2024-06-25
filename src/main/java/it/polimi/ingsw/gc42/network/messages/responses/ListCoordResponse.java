package it.polimi.ingsw.gc42.network.messages.responses;

import it.polimi.ingsw.gc42.model.classes.cards.Coordinates;
import it.polimi.ingsw.gc42.network.messages.Message;
import it.polimi.ingsw.gc42.network.messages.MessageType;

import java.util.ArrayList;

/**
 * Implementation of a Socket Message used to send an ArrayList of Coordinates
 */
public class ListCoordResponse extends Message {
    private final ArrayList<Coordinates> response;

    /**
     * Constructor Method
     * @param type the Message's type of content
     * @param response the List of Coordinates
     */
    public ListCoordResponse(MessageType type, ArrayList<Coordinates> response) {
        super(type);
        this.response = response;
    }

    /**
     * Getter Method for response
     * @return the List of Coordinates
     */
    public ArrayList<Coordinates> getResponse() {return response;}
}
