package it.polimi.ingsw.gc42.network.messages;

import it.polimi.ingsw.gc42.model.classes.game.Token;

public class SetPlayerTokenMessage extends PlayerMessage{
    private final Token token;

    public SetPlayerTokenMessage(MessageType type, int gameID, int playerID, Token token) {
        super(type, gameID, playerID);
        this.token = token;
    }

    public Token getToken() {
        return token;
    }
}
