package it.polimi.ingsw.gc42.network.messages;

import it.polimi.ingsw.gc42.model.classes.game.Token;

public class PlayersPointsChangedMessage extends Message {
    private final Token token;
    private final int newPoints;


    public PlayersPointsChangedMessage(MessageType type, Token token, int newPoints) {
        super(type);
        this.token = token;
        this.newPoints = newPoints;
    }

    public Token getToken() {
        return token;
    }

    public int getNewPoints() {
        return newPoints;
    }
}
