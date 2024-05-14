package it.polimi.ingsw.gc42.network.messages;

import com.google.gson.annotations.Expose;

public class GameMessage extends Message {
    @Expose
    private int gameID;

    public GameMessage(MessageType type, int gameID) {
        super(type);
        this.gameID = gameID;
    }

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    @Override
    public String toString() {
        return super.toString() + ", " + gameID;
    }
}