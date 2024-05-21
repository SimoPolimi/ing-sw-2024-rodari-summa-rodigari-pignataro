package it.polimi.ingsw.gc42.network.messages;

public class HandCardMessage extends PlayerMessage {
    private final int slot;

    HandCardMessage(MessageType type, int gameID, int playerID, int slot) {
        super(type, gameID, playerID);
        this.slot = slot;
    }

    public int getSlot() {
        return slot;
    }
}
