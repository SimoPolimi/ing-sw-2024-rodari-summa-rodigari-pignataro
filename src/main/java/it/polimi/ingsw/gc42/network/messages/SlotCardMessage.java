package it.polimi.ingsw.gc42.network.messages;

import com.google.gson.annotations.Expose;
import it.polimi.ingsw.gc42.model.classes.cards.CardType;

public class SlotCardMessage extends Message {
    @Expose
    private CardType cardType;
    @Expose
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