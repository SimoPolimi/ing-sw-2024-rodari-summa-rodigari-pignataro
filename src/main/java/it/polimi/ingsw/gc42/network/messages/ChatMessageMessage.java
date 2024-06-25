package it.polimi.ingsw.gc42.network.messages;

import it.polimi.ingsw.gc42.model.classes.game.ChatMessage;

/**
 * Implementation of a Socket Message used to send a ChatMessage to the Client (notify)
 */
public class ChatMessageMessage extends Message {
    // Attributes
    private final ChatMessage response;

    /**
     * Constructor Method
     * @param type the Message's type of content
     * @param response the ChatMessage to send
     */
    // Constructor Method
    public ChatMessageMessage(MessageType type, ChatMessage response) {
        super(type);
        this.response = response;
    }

    // Getters and Setters

    /**
     * Getter Method for the Content
     * @return the ChatMessage
     */
    public ChatMessage getResponse() {
        return response;
    }
}
