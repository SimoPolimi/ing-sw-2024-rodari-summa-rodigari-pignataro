package it.polimi.ingsw.gc42.network.messages.responses;

import it.polimi.ingsw.gc42.controller.GameStatus;
import it.polimi.ingsw.gc42.network.messages.Message;
import it.polimi.ingsw.gc42.network.messages.MessageType;

/**
 * Implementation of a Socket Message used to send a GameStatus
 */
public class GameStatusResponse extends Message {
    private final GameStatus response;

    /**
     * Constructor Method
     * @param type the Message's type of content
     * @param response the GameStatus to send
     */
    public GameStatusResponse(MessageType type, GameStatus response) {
        super(type);
        this.response = response;
    }

    /**
     * Getter Method for response
     * @return the GameStatus
     */
    public GameStatus getResponse() {return response;}
}
