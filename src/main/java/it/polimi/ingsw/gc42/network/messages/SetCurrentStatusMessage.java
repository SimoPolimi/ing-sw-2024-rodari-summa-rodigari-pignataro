package it.polimi.ingsw.gc42.network.messages;

import com.google.gson.annotations.Expose;
import it.polimi.ingsw.gc42.controller.GameStatus;

public class SetCurrentStatusMessage extends GameMessage{
    @Expose
    private GameStatus status;

    public SetCurrentStatusMessage(MessageType type, int gameID, GameStatus status) {
        super(type, gameID);
        this.status = status;
    }

    public GameStatus getStatus() {
        return status;
    }

    public void setStatus(GameStatus status) {
        this.status = status;
    }

    @Override
    public String toString(){
        return super.toString() + ", " + status;
    }
}
