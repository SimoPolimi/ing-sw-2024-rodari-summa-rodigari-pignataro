package it.polimi.ingsw.gc42.network.messages.responses;

import it.polimi.ingsw.gc42.network.messages.Message;
import it.polimi.ingsw.gc42.network.messages.MessageType;

import java.util.ArrayList;

public class ListStrResponse extends Message {
    private final ArrayList<String> response;

    public ListStrResponse(MessageType type, ArrayList<String> response) {
        super(type);
        this.response = response;
    }

    public ArrayList<String> getResponse() {return response;}
}
