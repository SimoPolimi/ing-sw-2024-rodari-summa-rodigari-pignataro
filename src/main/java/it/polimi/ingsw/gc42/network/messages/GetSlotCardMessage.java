package it.polimi.ingsw.gc42.network.messages;

import it.polimi.ingsw.gc42.model.classes.cards.CardType;

/**
 * Implementation of a Socket Message used to request a Slot Card
 */
public class GetSlotCardMessage extends GetDeckMessage {
    private final int slot;

    /**
     * Constructor Method
     * @param type the Message's type of content
     * @param gameID the Game's gameID
     * @param cardType the Card's CardType
     * @param slot the index of the Card inside the PlayingDeck [1, 2]
     */
    public GetSlotCardMessage(MessageType type, int gameID, CardType cardType, int slot) {
        super(type, gameID, cardType);
        this.slot = slot;
    }

    /**
     * Getter Method for slot
     * @return the index of the Card inside the PlayingDeck [1, 2]
     */
    public int getSlot() {
        return slot;
    }
}
