package it.polimi.ingsw.gc42.network.messages;

import java.io.Serializable;

public class Message implements Serializable {
    private final MessageType type;

    public Message(final MessageType type) {
        this.type = type;
    }
    @Override
    public String toString() {
        return type.toString() + ": ";
    }

    public MessageType getType() {
        return type;
    }
}