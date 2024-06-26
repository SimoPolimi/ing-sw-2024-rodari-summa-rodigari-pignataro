package it.polimi.ingsw.gc42.network.messages.responses;

import it.polimi.ingsw.gc42.model.classes.cards.Card;
import it.polimi.ingsw.gc42.network.messages.Message;
import it.polimi.ingsw.gc42.network.messages.MessageType;

/**
 * Implementation of a Socket Message used to send a Card
 */
public class CardResponse extends Message {
    private final Card response;
    private final boolean isFrontFacing;

    /**
     * Constructor Method
     * @param type the Message's type of content
     * @param response the Card to send
     * @param isFrontFacing a boolean value indicating if the Card is front facing
     */
    public CardResponse(MessageType type, Card response, boolean isFrontFacing) {
        super(type);
        this.response = response;
        this.isFrontFacing = isFrontFacing;
    }

    /**
     * Getter Method for response
     * @return the Card
     */
    public Card getResponse() {return response;}

    /**
     * Getter Method for the boolean value indicating if the Card is front facing
     * @return isFrontFacing
     */
    public boolean isFrontFacing() {
        return isFrontFacing;
    }
}
