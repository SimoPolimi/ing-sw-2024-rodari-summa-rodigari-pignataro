package it.polimi.ingsw.gc42.network.messages;

import it.polimi.ingsw.gc42.controller.GameStatus;

public class SetPlayerStatusMessage extends PlayerMessage{
    private GameStatus status;

    public SetPlayerStatusMessage(MessageType type, int gameID, int playerID, GameStatus status) {
        super(type, gameID, playerID);
        this.status = status;
    }

    public GameStatus getStatus() {
        return status;
    }

    public void setStatus(GameStatus status) {
        this.status = status;
    }

    public String toString(){
        return super.toString() + ", " + status;
    }
}
