package it.polimi.ingsw.gc42.network.messages;

import it.polimi.ingsw.gc42.model.classes.game.ChatMessage;

public class ChatMessageMessage extends Message {
    // Attributes
    private final ChatMessage response;

    // Constructor Method
    public ChatMessageMessage(MessageType type, ChatMessage response) {
        super(type);
        this.response = response;
    }

    // Getters and Setters
    public ChatMessage getResponse() {
        return response;
    }
}
