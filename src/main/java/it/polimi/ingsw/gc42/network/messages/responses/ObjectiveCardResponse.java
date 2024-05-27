package it.polimi.ingsw.gc42.network.messages.responses;

import it.polimi.ingsw.gc42.model.classes.cards.ObjectiveCard;
import it.polimi.ingsw.gc42.network.messages.Message;
import it.polimi.ingsw.gc42.network.messages.MessageType;

public class ObjectiveCardResponse extends Message {
    private final ObjectiveCard response;


    public ObjectiveCardResponse(MessageType type, ObjectiveCard response) {
        super(type);
        this.response = response;
    }

    public ObjectiveCard getResponse() {
        return response;
    }
}
