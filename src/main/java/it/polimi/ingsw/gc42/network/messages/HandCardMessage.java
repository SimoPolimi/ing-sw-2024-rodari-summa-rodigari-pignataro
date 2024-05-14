package it.polimi.ingsw.gc42.network.messages;

import com.google.gson.annotations.Expose;
import it.polimi.ingsw.gc42.model.classes.cards.CardType;
public class HandCardMessage extends PlayerMessage {
    @Expose
    private int slot;

    HandCardMessage(MessageType type, int gameID, int playerID, int slot) {
        super(type, gameID, playerID);
        this.slot = slot;
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }
}
