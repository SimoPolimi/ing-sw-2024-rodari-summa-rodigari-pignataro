package it.polimi.ingsw.gc42.network.messages;

import com.google.gson.annotations.Expose;
import it.polimi.ingsw.gc42.model.classes.cards.CardType;

public class GrabCardMessage extends PlayerMessage {
    @Expose
    private CardType type;
    @Expose
    private int slot;

    public GrabCardMessage(MessageType type, int gameID, int playerID, CardType cardType, int slot) {
        super(type, gameID, playerID);
        this.type = cardType;
        this.slot = slot;
    }

    public CardType getCardType() {
        return type;
    }

    public void setType(CardType type) {
        this.type = type;
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    @Override
    public String toString() {
        return super.toString() + type + ", " + slot;
    }
}
