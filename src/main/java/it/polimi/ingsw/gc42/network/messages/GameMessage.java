package it.polimi.ingsw.gc42.network.messages;

public class GameMessage extends Message {
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
}