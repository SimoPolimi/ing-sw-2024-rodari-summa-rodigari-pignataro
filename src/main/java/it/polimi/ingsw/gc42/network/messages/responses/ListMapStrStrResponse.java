package it.polimi.ingsw.gc42.network.messages.responses;

import it.polimi.ingsw.gc42.network.messages.Message;
import it.polimi.ingsw.gc42.network.messages.MessageType;

import java.util.ArrayList;
import java.util.HashMap;

public class ListMapStrStrResponse extends Message {
    private final ArrayList<HashMap<String, String>> response;

    public ListMapStrStrResponse(MessageType type, ArrayList<HashMap<String, String>> response) {
        super(type);
        this.response = response;
    }

    public ArrayList<HashMap<String, String>> getResponse() {return response;}
}
