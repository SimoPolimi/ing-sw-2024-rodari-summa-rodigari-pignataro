package it.polimi.ingsw.gc42.network.messages;

import it.polimi.ingsw.gc42.model.classes.cards.CardType;

public class GrabCardMessage extends PlayerMessage {
    private final CardType type;
    private final int slot;

    public GrabCardMessage(MessageType type, int gameID, int playerID, CardType cardType, int slot) {
        super(type, gameID, playerID);
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
