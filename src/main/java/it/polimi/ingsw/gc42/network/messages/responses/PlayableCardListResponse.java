package it.polimi.ingsw.gc42.network.messages.responses;

import it.polimi.ingsw.gc42.model.classes.cards.PlayableCard;
import it.polimi.ingsw.gc42.network.messages.Message;
import it.polimi.ingsw.gc42.network.messages.MessageType;

import java.util.ArrayList;

/**
 * Implementation of a Socket Message used to send a List of PlayableCards
 */
public class PlayableCardListResponse extends Message {
    private final ArrayList<PlayableCard> response;

    /**
     * Constructor Method
     * @param type the Message's type of content
     * @param response the data to send
     */
    public PlayableCardListResponse(MessageType type, ArrayList<PlayableCard> response) {
        super(type);
        this.response = response;
    }

    /**
     * Getter Method for response
     * @return the data
     */
    public ArrayList<PlayableCard> getResponse() {
        return response;
    }
}
