package it.polimi.ingsw.gc42.network.messages;

public class PlayCardMessage extends PlayerMessage {
    private final int handCard;
    private final int x;
    private final int y;

    public PlayCardMessage(MessageType type, int gameID, int playerID, int handCard, int x, int y) {
        super(type, gameID, playerID);
        this.handCard = handCard;
        this.y = y;
        this.x = x;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getHandCard() {
        return handCard;
    }
}
