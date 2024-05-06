package it.polimi.ingsw.gc42.network.messages;

import it.polimi.ingsw.gc42.model.classes.cards.CardType;

public class DrawCardMessage extends PlayerMessage{
    private CardType cardType;

    public DrawCardMessage(MessageType type, int gameID, int playerID, CardType cardType) {
        super(type, gameID, playerID);
        this.cardType = cardType;
    }

    public CardType getCardType() {
        return cardType;
    }

    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }

    @Override
    public String toString() {
        return super.toString() + ", " + cardType;
    }
}
