package it.polimi.ingsw.gc42.network.messages;

public class GetPlayersHandCardMessage extends PlayerMessage{
    private final int cardID;

    public GetPlayersHandCardMessage(MessageType type, int gameID, int playerID, int cardID) {
        super(type, gameID, playerID);
        this.cardID = cardID;
    }

    public int getCardID() {
        return cardID;
    }
}
