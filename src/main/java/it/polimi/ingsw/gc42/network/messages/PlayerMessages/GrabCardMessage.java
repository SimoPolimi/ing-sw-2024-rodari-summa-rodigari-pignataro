package it.polimi.ingsw.gc42.network.messages.PlayerMessages;

import it.polimi.ingsw.gc42.model.classes.cards.CardType;
import it.polimi.ingsw.gc42.network.messages.MessageType;

/**
 * Implementation of a Socket Message used to grab a Card
 */
public class GrabCardMessage extends PlayerMessage {
    private final CardType type;
    private final int slot;

    /**
     * Constructor Method
     * @param type the Message's type of content
     * @param gameID the Game's gameID
     * @param playerID the Player's playerID
     * @param cardType the Card's CardType
     * @param slot the index of the Card inside the PlayingDeck [1, 2]
     */
    public GrabCardMessage(MessageType type, int gameID, int playerID, CardType cardType, int slot) {
        super(type, gameID, playerID);
        this.type = cardType;
        this.slot = slot;
    }

    /**
     * Getter Method for cardType
     * @return the Card's CardType
     */
    public CardType getCardType() {
        return type;
    }

    /**
     * Getter Method for slot
     * @return the index of the Card inside the PlayingDeck [1, 2]
     */
    public int getSlot() {
        return slot;
    }
}
