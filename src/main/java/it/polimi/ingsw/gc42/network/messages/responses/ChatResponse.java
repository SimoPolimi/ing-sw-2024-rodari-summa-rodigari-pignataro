package it.polimi.ingsw.gc42.network.messages.responses;

import it.polimi.ingsw.gc42.model.classes.game.ChatMessage;
import it.polimi.ingsw.gc42.network.messages.Message;
import it.polimi.ingsw.gc42.network.messages.MessageType;

import java.util.ArrayList;

/**
 * Implementation of a Socket Message used to send the full Chat
 */
public class ChatResponse extends Message {
    // Attributes
    private final ArrayList<ChatMessage> response;

    // Constructor Method

    /**
     * Constructor Method
     * @param type the Message's type of content
     * @param messages the Chat to send
     */
    public ChatResponse(MessageType type, ArrayList<ChatMessage> messages) {
        super(type);
        this.response = messages;
    }

    // Getters and Setters

    /**
     * Getter Method for response
     * @return the Chat
     */
    public ArrayList<ChatMessage> getResponse() {
        return response;
    }
}
