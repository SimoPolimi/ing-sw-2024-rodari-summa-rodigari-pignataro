package it.polimi.ingsw.gc42.network.messages;

public class KickPlayerMessage extends GameMessage{
    private int playerId;

    public KickPlayerMessage(MessageType type, int gameID, int playerId) {
        super(type, gameID);
        this.playerId = playerId;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    @Override
    public String toString() {
        return super.toString() + ", " + playerId;
    }
}
