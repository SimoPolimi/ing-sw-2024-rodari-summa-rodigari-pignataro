package it.polimi.ingsw.gc42.network.messages;

public class PlayCardMessage extends Message {
    private int handCard;
    private int x;
    private int y;

    public PlayCardMessage(MessageType type, int handCard, int y, int x) {
        super(type);
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
        return handCard + ", " + x + ", " + y;
    }
}
