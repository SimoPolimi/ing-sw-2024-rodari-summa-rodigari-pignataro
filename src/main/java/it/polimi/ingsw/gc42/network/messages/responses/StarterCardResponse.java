package it.polimi.ingsw.gc42.network.messages.responses;

import it.polimi.ingsw.gc42.model.classes.cards.StarterCard;
import it.polimi.ingsw.gc42.network.messages.Message;
import it.polimi.ingsw.gc42.network.messages.MessageType;

/**
 * Implementation of a Socket Message used to send a Starter Card
 */
public class StarterCardResponse extends Message {
    private final StarterCard response;

    /**
     * Constructor Method
     * @param type the Message's type of content
     * @param response the StarterCard to send
     */
    public StarterCardResponse(MessageType type, StarterCard response) {
        super(type);
        this.response = response;
    }

    /**
     * Getter Method for response
     * @return the StarterCard
     */
    public StarterCard getResponse() {
        return response;
    }
}
