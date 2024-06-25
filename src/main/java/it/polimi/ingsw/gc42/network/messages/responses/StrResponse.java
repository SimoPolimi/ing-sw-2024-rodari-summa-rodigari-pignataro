package it.polimi.ingsw.gc42.network.messages.responses;

import com.google.gson.annotations.Expose;
import it.polimi.ingsw.gc42.network.messages.Message;
import it.polimi.ingsw.gc42.network.messages.MessageType;

/**
 * Implementation of a Socket Message used to send a String
 */
public class StrResponse extends Message {
    private final String response;

    /**
     * Constructor Method
     * @param type the Message's type of content
     * @param response the String to send
     */
    public StrResponse(MessageType type,  String response) {
        super(type);
        this.response = response;
    }

    /**
     * Getter Method for response
     * @return the String
     */
    public String getResponse() {return response;}
}
