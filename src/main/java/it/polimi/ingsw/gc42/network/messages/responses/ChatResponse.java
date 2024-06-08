package it.polimi.ingsw.gc42.network.messages.responses;

import it.polimi.ingsw.gc42.model.classes.game.ChatMessage;
import it.polimi.ingsw.gc42.network.messages.Message;
import it.polimi.ingsw.gc42.network.messages.MessageType;

import java.util.ArrayList;

public class ChatResponse extends Message {
    // Attributes
    private final ArrayList<ChatMessage> response;

    // Constructor Method
    public ChatResponse(MessageType type, ArrayList<ChatMessage> messages) {
        super(type);
        this.response = messages;
    }

    // Getters and Setters
    public ArrayList<ChatMessage> getResponse() {
        return response;
    }
}
