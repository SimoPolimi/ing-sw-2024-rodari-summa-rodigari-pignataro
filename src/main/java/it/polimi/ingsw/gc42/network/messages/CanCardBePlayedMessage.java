package it.polimi.ingsw.gc42.network.messages;

public class CanCardBePlayedMessage extends PlayerMessage{
    private int cardID;

    public CanCardBePlayedMessage(MessageType type, int gameID, int playerID, int cardID) {
        super(type, gameID, playerID);
        this.cardID = cardID;
    }

    public int getCardID() {
        return cardID;
    }

    public void setCardID(int cardID) {
        this.cardID = cardID;
    }
}
