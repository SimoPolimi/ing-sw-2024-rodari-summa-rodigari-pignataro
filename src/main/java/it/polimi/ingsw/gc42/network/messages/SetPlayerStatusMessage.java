package it.polimi.ingsw.gc42.network.messages;

import it.polimi.ingsw.gc42.controller.GameStatus;

public class SetPlayerStatusMessage extends PlayerMessage{
    private final GameStatus status;

    public SetPlayerStatusMessage(MessageType type, int gameID, int playerID, GameStatus status) {
        super(type, gameID, playerID);
        this.status = status;
    }

    public GameStatus getStatus() {
        return status;
    }

}
