package it.polimi.ingsw.gc42.network.messages.responses;

import it.polimi.ingsw.gc42.model.classes.cards.Card;
import it.polimi.ingsw.gc42.network.messages.Message;
import it.polimi.ingsw.gc42.network.messages.MessageType;

public class CardResponse extends Message {
    private final Card response;
    private final boolean isFrontFacing;

    public CardResponse(MessageType type, Card response, boolean isFrontFacing) {
        super(type);
        this.response = response;
        this.isFrontFacing = isFrontFacing;
    }

    public Card getResponse() {return response;}

    public boolean isFrontFacing() {
        return isFrontFacing;
    }
}
