package it.polimi.ingsw.gc42.network.messages;

import it.polimi.ingsw.gc42.model.classes.game.ChatMessage;

/**
 * Implementation of a Socket Message used to send a ChatMessage to the Client (notify)
 */
public class NotifyNewMessageMessage extends Message {
    // Attributes
    private final ChatMessage message;

    /**
     * Constructor Method
     * @param type the Message's type of content
     * @param message the ChatMessage to send
     */
    // Constructor Method
    public NotifyNewMessageMessage(MessageType type, ChatMessage message) {
        super(type);
        this.message = message;
    }

    // Getters and Setters

    /**
     * Getter Method for the Content
     * @return the ChatMessage
     */
    public ChatMessage getMessage() {
        return message;
    }
}
