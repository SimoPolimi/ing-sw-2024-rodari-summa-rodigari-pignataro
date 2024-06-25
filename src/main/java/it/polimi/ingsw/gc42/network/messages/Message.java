package it.polimi.ingsw.gc42.network.messages;

import java.io.Serializable;

/**
 * Implementation of a Socket Message
 */
public class Message implements Serializable {
    private final MessageType type;

    /**
     * Constructor Method
     * @param type the Message's type of content
     */
    public Message(MessageType type) {
        this.type = type;
    }

    /**
     * Getter Method for the type
     * @return a MessageType indicating what is the content of this Message
     */
    public MessageType getType() {
        return type;
    }
}