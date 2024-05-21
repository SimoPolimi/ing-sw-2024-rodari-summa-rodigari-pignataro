package it.polimi.ingsw.gc42.network.messages;

import it.polimi.ingsw.gc42.network.ClientState;

public class ClientStateMessage extends Message {
    private final ClientState state;

    public ClientStateMessage(MessageType type, ClientState state) {
        super(type);
        this.state = state;
    }

    public ClientState getState() {
        return state;
    }
}
