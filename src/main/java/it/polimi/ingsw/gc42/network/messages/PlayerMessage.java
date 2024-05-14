package it.polimi.ingsw.gc42.network.messages;

import com.google.gson.annotations.Expose;

public class PlayerMessage extends GameMessage{
    @Expose
    private int playerID;

    public PlayerMessage(MessageType type, int gameID, int playerID) {
        super(type, gameID);
        this.playerID = playerID;
    }

    public int getPlayerID() {
        return playerID;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    public String toString(){
        return super.toString() + ", " + playerID;
    }
}
