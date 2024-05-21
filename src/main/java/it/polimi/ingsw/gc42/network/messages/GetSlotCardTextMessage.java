package it.polimi.ingsw.gc42.network.messages;

import it.polimi.ingsw.gc42.model.classes.cards.CardType;

public class GetSlotCardTextMessage extends Message{
    private final CardType type;
    private final int slot;

    public GetSlotCardTextMessage(MessageType type, CardType cardType, int slot) {
        super(type);
        this.type = cardType;
        this.slot = slot;
    }

    public CardType getCardType() {
        return type;
    }

    public int getSlot() {
        return slot;
    }
}
