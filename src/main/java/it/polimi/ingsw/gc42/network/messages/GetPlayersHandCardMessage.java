package it.polimi.ingsw.gc42.network.messages;

import com.google.gson.annotations.Expose;

public class GetPlayersHandCardMessage extends PlayerMessage{
    @Expose
    private int cardID;

    public GetPlayersHandCardMessage(MessageType type, int gameID, int playerID, int cardID) {
        super(type, gameID, playerID);
        this.cardID = cardID;
    }

    public int getCardID() {
        return cardID;
    }

    public void setCardID(int cardID) {
        this.cardID = cardID;
    }

    @Override
    public String toString(){
        return super.toString() + ", " + cardID;
    }
}
