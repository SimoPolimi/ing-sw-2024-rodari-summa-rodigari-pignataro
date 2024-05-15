package it.polimi.ingsw.gc42.network.messages.responses;

import it.polimi.ingsw.gc42.model.classes.cards.Coordinates;
import it.polimi.ingsw.gc42.network.messages.Message;
import it.polimi.ingsw.gc42.network.messages.MessageType;

import java.util.ArrayList;

public class ListCoordResponse extends Message {
    private ArrayList<Coordinates> response;

    public ListCoordResponse(MessageType type, ArrayList<Coordinates> response) {
        super(type);
        this.response = response;
    }

    public ArrayList<Coordinates> getResponse() {return response;}
}
