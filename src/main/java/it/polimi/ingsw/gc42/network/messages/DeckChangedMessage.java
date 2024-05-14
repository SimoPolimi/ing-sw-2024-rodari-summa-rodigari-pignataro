package it.polimi.ingsw.gc42.network.messages;

import com.google.gson.annotations.Expose;
import it.polimi.ingsw.gc42.model.classes.cards.CardType;

public class DeckChangedMessage extends Message{
    @Expose
    private CardType cardType;

    public DeckChangedMessage(MessageType type, CardType cardType) {
        super(type);
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
