package it.polimi.ingsw.gc42.network.messages;

public class PlayerMessage extends GameMessage {
    private final int playerID;

    public PlayerMessage(MessageType type, int gameID, int playerID) {
        super(type, gameID);
        this.playerID = playerID;
    }

    public int getPlayerID() {
        return playerID;
    }
}