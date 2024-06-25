package it.polimi.ingsw.gc42.network.messages.responses;

import it.polimi.ingsw.gc42.model.classes.cards.ObjectiveCard;
import it.polimi.ingsw.gc42.network.messages.Message;
import it.polimi.ingsw.gc42.network.messages.MessageType;

/**
 * Implementation of a Socket Message used to send an Objective Card
 */
public class ObjectiveCardResponse extends Message {
    private final ObjectiveCard response;

    /**
     * Constructor Method
     * @param type the Message's type of content
     * @param response the ObjectiveCard to send
     */
    public ObjectiveCardResponse(MessageType type, ObjectiveCard response) {
        super(type);
        this.response = response;
    }

    /**
     * Getter Method for response
     * @return the Objective Card
     */
    public ObjectiveCard getResponse() {
        return response;
    }
}
