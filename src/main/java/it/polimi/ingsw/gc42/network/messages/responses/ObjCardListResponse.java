package it.polimi.ingsw.gc42.network.messages.responses;

import it.polimi.ingsw.gc42.model.classes.cards.ObjectiveCard;
import it.polimi.ingsw.gc42.network.messages.Message;
import it.polimi.ingsw.gc42.network.messages.MessageType;

import java.util.ArrayList;

public class ObjCardListResponse extends Message {
    private final ArrayList<ObjectiveCard> response;


    public ObjCardListResponse(MessageType type, ArrayList<ObjectiveCard> response) {
        super(type);
        this.response = response;
    }

    public ArrayList<ObjectiveCard> getResponse() {
        return response;
    }
}
