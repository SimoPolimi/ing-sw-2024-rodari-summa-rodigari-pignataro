package it.polimi.ingsw.gc42.network.messages.PlayerMessages;

import it.polimi.ingsw.gc42.network.messages.MessageType;

/**
 * Implementation of a Socket Message used to send Chat Messages
 */
public class SendMessageMessage extends PlayerMessage {
    // Attributes
    private final String message;

    // Constructor Method
    /**
     * Constructor Method
     * @param type the Message's type of content
     * @param gameID the Game's gameID
     * @param playerID the Player's playerID
     * @param message the Message's text to send
     */
    public SendMessageMessage(MessageType type, int gameID, int playerID, String message) {
        super(type, gameID, playerID);
        this.message = message;
    }

    // Getters and Setters

    /**
     * Getter Method for message
     * @return the Message's text
     */
    public String getMessage() {
        return message;
    }
}
