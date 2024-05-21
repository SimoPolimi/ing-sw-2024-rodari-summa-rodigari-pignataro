package it.polimi.ingsw.gc42.network.messages.responses;

import it.polimi.ingsw.gc42.model.classes.game.Token;
import it.polimi.ingsw.gc42.network.messages.Message;
import it.polimi.ingsw.gc42.network.messages.MessageType;

public class TokenResponse extends Message {
    private final Token response;
    public TokenResponse(MessageType type, Token response) {
        super(type);
        this.response = response;
    }

    public Token getResponse() {return response;}
}
