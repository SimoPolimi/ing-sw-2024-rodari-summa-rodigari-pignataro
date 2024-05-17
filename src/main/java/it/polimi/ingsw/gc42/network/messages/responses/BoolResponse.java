package it.polimi.ingsw.gc42.network.messages.responses;

import com.google.gson.annotations.Expose;
import it.polimi.ingsw.gc42.network.messages.Message;
import it.polimi.ingsw.gc42.network.messages.MessageType;

public class BoolResponse extends Message {
    @Expose
    private boolean response;

    public BoolResponse(MessageType type, boolean response) {
        super(type);
        this.response = response;
    }

    public boolean getResponse() {return response;}
}
