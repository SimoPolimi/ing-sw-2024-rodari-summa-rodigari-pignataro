package it.polimi.ingsw.gc42.network.messages.responses;

import it.polimi.ingsw.gc42.network.messages.Message;
import it.polimi.ingsw.gc42.network.messages.MessageType;

/**
 * Implementation of a Socket Message used to send an int value
 */
public class IntResponse extends Message {
    private final int response;

    /**
     * Constructor Method
     * @param type the Message's type of content
     * @param response the int value to send
     */
    public IntResponse(MessageType type, int response) {
        super(type);
        this.response = response;
    }

    /**
     * Getter Method for response
     * @return the int value
     */
    public int getResponse() {return response;}
}
