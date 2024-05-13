package it.polimi.ingsw.gc42.network.messages;

import it.polimi.ingsw.gc42.model.classes.cards.CardType;

public class SlotCardMessage extends Message {
    private CardType cardType;
    private int slot;

    SlotCardMessage(MessageType type, CardType cardType, int slot) {
        super(type);
        this.cardType = cardType;
        this.slot = slot;
    }

    public CardType getCardType() {
        return cardType;
    }

    public void setCardType(CardType type) {
        this.cardType = type;
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }
}