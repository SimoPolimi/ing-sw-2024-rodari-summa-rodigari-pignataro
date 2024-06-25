package it.polimi.ingsw.gc42.network.messages;

import it.polimi.ingsw.gc42.model.classes.cards.CardType;

/**
 * Implementation of a Socket Message used to notify Clients that a Deck has been updated
 */
public class DeckChangedMessage extends Message{
    private CardType cardType;

    /**
     * COnstructor Method
     * @param type the Message's type of Content
     * @param cardType the Deck's CardType
     */
    public DeckChangedMessage(MessageType type, CardType cardType) {
        super(type);
        this.cardType = cardType;
    }

    /**
     * Getter Method for cardType
     * @return the Deck's CardType
     */
    public CardType getCardType() {
        return cardType;
    }

    /**
     * Setter Method for cardType
     * @param cardType the Deck's CardType
     */
    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }
}
