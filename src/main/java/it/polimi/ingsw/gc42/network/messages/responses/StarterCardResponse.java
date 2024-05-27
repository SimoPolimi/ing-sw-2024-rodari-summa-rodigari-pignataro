package it.polimi.ingsw.gc42.network.messages.responses;

import it.polimi.ingsw.gc42.model.classes.cards.StarterCard;
import it.polimi.ingsw.gc42.network.messages.Message;
import it.polimi.ingsw.gc42.network.messages.MessageType;

public class StarterCardResponse extends Message {
    private final StarterCard response;


    public StarterCardResponse(MessageType type, StarterCard response) {
        super(type);
        this.response = response;
    }

    public StarterCard getResponse() {
        return response;
    }
}
