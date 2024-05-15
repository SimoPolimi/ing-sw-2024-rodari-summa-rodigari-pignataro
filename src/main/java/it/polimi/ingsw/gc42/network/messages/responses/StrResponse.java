package it.polimi.ingsw.gc42.network.messages.responses;

import it.polimi.ingsw.gc42.network.messages.Message;
import it.polimi.ingsw.gc42.network.messages.MessageType;

public class StrResponse extends Message {
    private String response;

    public StrResponse(MessageType type,  String response) {
        super(type);
        this.response = response;
    }

    public String getResponse() {return response;}
}
