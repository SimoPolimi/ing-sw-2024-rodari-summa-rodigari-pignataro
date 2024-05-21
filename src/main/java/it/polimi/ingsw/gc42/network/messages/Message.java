package it.polimi.ingsw.gc42.network.messages;

import java.io.Serializable;

public class Message implements Serializable {
    private final MessageType type;

    public Message(MessageType type) {
        this.type = type;
    }

    public MessageType getType() {
        return type;
    }
}