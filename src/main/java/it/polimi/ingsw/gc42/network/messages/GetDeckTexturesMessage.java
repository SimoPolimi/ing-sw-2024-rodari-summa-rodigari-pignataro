package it.polimi.ingsw.gc42.network.messages;

import it.polimi.ingsw.gc42.model.classes.cards.CardType;

public class GetDeckTexturesMessage extends GameMessage {
    private CardType cardType;

    public GetDeckTexturesMessage(MessageType type, int gameID, CardType cardType) {
        super(type, gameID);
        this.cardType = cardType;
    }

    public CardType getCardType() {
        return cardType;
    }
}
