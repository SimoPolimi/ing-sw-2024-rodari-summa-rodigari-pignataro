package it.polimi.ingsw.gc42.network.messages;

import com.google.gson.annotations.Expose;

public class PlayCardMessage extends PlayerMessage {
    @Expose
    private int handCard;
    @Expose
    private int x;
    @Expose
    private int y;

    public PlayCardMessage(MessageType type, int gameID, int playerID, int handCard, int x, int y) {
        super(type, gameID, playerID);
        this.handCard = handCard;
        this.y = y;
        this.x = x;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getHandCard() {
        return handCard;
    }

    public void setHandCard(int handCard) {
        this.handCard = handCard;
    }

    @Override
    public String toString() {
        return super.toString() + ", " + handCard + ", " + x + ", " + y;
    }
}
