package it.polimi.ingsw.gc42.network.messages.responses;

import it.polimi.ingsw.gc42.network.messages.MessageType;
import it.polimi.ingsw.gc42.network.messages.PlayerMessage;

public class SendMessageMessage extends PlayerMessage {
    // Attributes
    private final String message;

    // Constructor Method
    public SendMessageMessage(MessageType type, int gameID, int playerID, String message) {
        super(type, gameID, playerID);
        this.message = message;
    }

    // Getters and Setters
    public String getMessage() {
        return message;
    }
}
