package it.polimi.ingsw.gc42.network.messages.responses;

import it.polimi.ingsw.gc42.controller.GameStatus;
import it.polimi.ingsw.gc42.network.messages.Message;
import it.polimi.ingsw.gc42.network.messages.MessageType;

public class GameStatusResponse extends Message {
    private final GameStatus response;

    public GameStatusResponse(MessageType type, GameStatus response) {
        super(type);
        this.response = response;
    }

    public GameStatus getResponse() {return response;}
}
