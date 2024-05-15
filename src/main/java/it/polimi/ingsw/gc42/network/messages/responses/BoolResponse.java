package it.polimi.ingsw.gc42.network.messages.responses;

import it.polimi.ingsw.gc42.network.messages.Message;
import it.polimi.ingsw.gc42.network.messages.MessageType;

public class BoolResponse extends Message {
    private boolean response;

    public BoolResponse(MessageType type, boolean response) {
        super(type);
        this.response = response;
    }

    public boolean getResponse() {return response;}
}
