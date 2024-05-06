package it.polimi.ingsw.gc42.network.messages;

public class GetPlayerMessage extends GameMessage{
    private int playerID;

    public GetPlayerMessage(MessageType type, int gameID, int playerID) {
        super(type, gameID);
        this.playerID = playerID;
    }

    public int getPlayerID() {
        return playerID;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    @Override
    public String toString() {
        return super.toString() + ", " + playerID;
    }
}
