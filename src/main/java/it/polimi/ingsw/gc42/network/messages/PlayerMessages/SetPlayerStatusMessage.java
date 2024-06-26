package it.polimi.ingsw.gc42.network.messages.PlayerMessages;

import it.polimi.ingsw.gc42.controller.GameStatus;
import it.polimi.ingsw.gc42.network.messages.MessageType;

/**
 * Implementation of a Socket Message used to set a Player's GameStatus
 */
public class SetPlayerStatusMessage extends PlayerMessage{
    private final GameStatus status;

    /**
     * Constructor Method
     * @param type the Message's type of content
     * @param gameID the Game's gameID
     * @param playerID the Player's playerID
     * @param status the new GameStatus to set
     */
    public SetPlayerStatusMessage(MessageType type, int gameID, int playerID, GameStatus status) {
        super(type, gameID, playerID);
        this.status = status;
    }

    /**
     * Getter Method for status
     * @return the new GameStatus to set
     */
    public GameStatus getStatus() {
        return status;
    }

}
