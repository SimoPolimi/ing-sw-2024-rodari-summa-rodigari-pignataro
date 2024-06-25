package it.polimi.ingsw.gc42.network.messages;

import it.polimi.ingsw.gc42.model.classes.cards.CardType;

/**
 * Implementation of a Socket Message used to request one Deck's Cards
 */
public class GetDeckMessage extends GameMessage {
    private final CardType cardType;

    /**
     * Constructor Method
     * @param type the Message's type of content
     * @param gameID the Game's gameID
     * @param cardType the Deck's CardType
     */
    public GetDeckMessage(MessageType type, int gameID, CardType cardType) {
        super(type, gameID);
        this.cardType = cardType;
    }

    /**
     * Getter Method for cardType
     * @return the Deck's CardType
     */
    public CardType getCardType() {
        return cardType;
    }
}
