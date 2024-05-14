package it.polimi.ingsw.gc42.network.messages;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class Message implements Serializable {
    @Expose
    private MessageType type;

    public Message(MessageType type) {
        this.type = type;
    }
    @Override
    public String toString() {
        return type.toString() + ": ";
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }
}