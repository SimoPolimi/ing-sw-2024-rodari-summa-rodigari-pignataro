package it.polimi.ingsw.gc42.network.messages.responses;

import it.polimi.ingsw.gc42.model.classes.cards.ObjectiveCard;
import it.polimi.ingsw.gc42.network.messages.Message;
import it.polimi.ingsw.gc42.network.messages.MessageType;

import java.util.ArrayList;

/**
 * Implementation of a Socket Message used to send an ArrayList of ObjectiveCards
 */
public class ObjCardListResponse extends Message {
    private final ArrayList<ObjectiveCard> response;

    /**
     * Constructor Method
     * @param type the Message's type of content
     * @param response the list of Objective Cards
     */
    public ObjCardListResponse(MessageType type, ArrayList<ObjectiveCard> response) {
        super(type);
        this.response = response;
    }

    /**
     * Getter Method for response
     * @return the list of Objective Cards
     */
    public ArrayList<ObjectiveCard> getResponse() {
        return response;
    }
}
