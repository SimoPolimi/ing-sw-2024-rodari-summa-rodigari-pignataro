package it.polimi.ingsw.gc42.network.messages.responses;

import com.google.gson.annotations.Expose;
import it.polimi.ingsw.gc42.network.messages.Message;
import it.polimi.ingsw.gc42.network.messages.MessageType;

public class IntResponse extends Message {
    private final int response;

    public IntResponse(MessageType type, int response) {
        super(type);
        this.response = response;
    }

    public int getResponse() {return response;}
}
