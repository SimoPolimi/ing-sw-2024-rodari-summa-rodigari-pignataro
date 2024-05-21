package it.polimi.ingsw.gc42.network.messages;

import it.polimi.ingsw.gc42.model.classes.cards.CardType;

public class SlotCardMessage extends Message {
    private final CardType cardType;
    private final int slot;

    public SlotCardMessage(MessageType type, CardType cardType, int slot) {
        super(type);
        this.cardType = cardType;
        this.slot = slot;
    }

    public CardType getCardType() {
        return cardType;
    }

    public int getSlot() {
        return slot;
    }
}