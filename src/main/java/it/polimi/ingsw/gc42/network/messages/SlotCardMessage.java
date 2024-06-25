package it.polimi.ingsw.gc42.network.messages;

import it.polimi.ingsw.gc42.model.classes.cards.CardType;

/**
 * Implementation of a Socket Message used to notify to Clients that a Slot Card has changed
 */
public class SlotCardMessage extends Message {
    private final CardType cardType;
    private final int slot;

    /**
     * Constructor Method
     * @param type the Message's type of content
     * @param cardType the Card's CardType
     * @param slot an int indicating which Slot Card is [1, 2]
     */
    public SlotCardMessage(MessageType type, CardType cardType, int slot) {
        super(type);
        this.cardType = cardType;
        this.slot = slot;
    }

    /**
     * Getter Method for cardType
     * @return the Card's CardType
     */
    public CardType getCardType() {
        return cardType;
    }

    /**
     * Getter Method for slot
     * @return an int indicating which Slot Card is [1, 2]
     */
    public int getSlot() {
        return slot;
    }
}