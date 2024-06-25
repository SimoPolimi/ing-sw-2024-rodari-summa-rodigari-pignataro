package it.polimi.ingsw.gc42.network.messages.responses;

import it.polimi.ingsw.gc42.network.messages.Message;
import it.polimi.ingsw.gc42.network.messages.MessageType;

/**
 * Implementation of a Socket Message used to send a boolean value
 */
public class BoolResponse extends Message {
    private final boolean response;

    /**
     * Constructor Method
     * @param type the Message's type of content
     * @param response the boolean value
     */
    public BoolResponse(MessageType type, boolean response) {
        super(type);
        this.response = response;
    }

    /**
     * Getter Method for response
     * @return the boolean value
     */
    public boolean getResponse() {return response;}
}
