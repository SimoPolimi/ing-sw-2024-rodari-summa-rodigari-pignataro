package it.polimi.ingsw.gc42.network.messages;

import it.polimi.ingsw.gc42.model.classes.cards.CardType;

/**
 * Implementation of a Socket Message used to draw a Card
 */
public class DrawCardMessage extends PlayerMessage{
    private CardType cardType;

    /**
     * Constructor Method
     * @param type the Message's type of content
     * @param gameID the Game's gameID
     * @param playerID the Player's playerID
     * @param cardType the Deck's CardType
     */
    public DrawCardMessage(MessageType type, int gameID, int playerID, CardType cardType) {
        super(type, gameID, playerID);
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
