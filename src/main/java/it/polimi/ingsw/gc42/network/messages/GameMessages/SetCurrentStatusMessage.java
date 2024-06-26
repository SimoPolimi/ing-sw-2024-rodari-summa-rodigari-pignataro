package it.polimi.ingsw.gc42.network.messages.GameMessages;

import it.polimi.ingsw.gc42.controller.GameStatus;
import it.polimi.ingsw.gc42.network.messages.MessageType;

/**
 * Implementation of a Socket Message used to set a Game's GameStatus
 */
public class SetCurrentStatusMessage extends GameMessage{
    private final GameStatus status;

    /**
     * Constructor Method
     * @param type the Message's type of content
     * @param gameID the Game's gameID
     * @param status the GameStatus to set
     */
    public SetCurrentStatusMessage(MessageType type, int gameID, GameStatus status) {
        super(type, gameID);
        this.status = status;
    }

    /**
     * Getter Method for status
     * @return the GameStatus to set
     */
    public GameStatus getStatus() {
        return status;
    }
}
