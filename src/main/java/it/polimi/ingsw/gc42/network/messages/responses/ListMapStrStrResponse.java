package it.polimi.ingsw.gc42.network.messages.responses;

import it.polimi.ingsw.gc42.network.messages.Message;
import it.polimi.ingsw.gc42.network.messages.MessageType;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Implementation of a Socket Message used to send an ArrayList of HashMaps of Strings
 */
public class ListMapStrStrResponse extends Message {
    private final ArrayList<HashMap<String, String>> response;

    /**
     * Constructor Method
     * @param type the Message's type of content
     * @param response the data to send
     */
    public ListMapStrStrResponse(MessageType type, ArrayList<HashMap<String, String>> response) {
        super(type);
        this.response = response;
    }

    /**
     * Getter Method for response
     * @return the data
     */
    public ArrayList<HashMap<String, String>> getResponse() {return response;}
}
