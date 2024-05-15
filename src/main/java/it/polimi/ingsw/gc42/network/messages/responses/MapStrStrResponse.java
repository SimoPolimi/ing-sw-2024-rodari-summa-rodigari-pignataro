package it.polimi.ingsw.gc42.network.messages.responses;

import it.polimi.ingsw.gc42.network.messages.Message;
import it.polimi.ingsw.gc42.network.messages.MessageType;

import java.util.HashMap;

public class MapStrStrResponse extends Message {
    private HashMap<String,String> response;

    public MapStrStrResponse(MessageType type, HashMap<String,String> response) {
        super(type);
        this.response = response;
    }

    public HashMap<String,String> getResponse() {return response;}
}
