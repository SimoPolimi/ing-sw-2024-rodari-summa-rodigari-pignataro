package it.polimi.ingsw.gc42.network.messages;

import it.polimi.ingsw.gc42.model.classes.cards.CardType;

public class GetSlotCardTextureMessage extends GetDeckTexturesMessage {
    private final int slot;

    public GetSlotCardTextureMessage(MessageType type, int gameID, CardType cardType, int slot) {
        super(type, gameID, cardType);
        this.slot = slot;
    }

    public int getSlot() {
        return slot;
    }
}
