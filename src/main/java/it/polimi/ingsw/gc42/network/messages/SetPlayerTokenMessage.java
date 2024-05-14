package it.polimi.ingsw.gc42.network.messages;

import com.google.gson.annotations.Expose;
import it.polimi.ingsw.gc42.model.classes.game.Token;

public class SetPlayerTokenMessage extends PlayerMessage{
    @Expose
    private Token token;

    public SetPlayerTokenMessage(MessageType type, int gameID, int playerID, Token token) {
        super(type, gameID, playerID);
        this.token = token;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return super.toString() + ", " + token;
    }
}
