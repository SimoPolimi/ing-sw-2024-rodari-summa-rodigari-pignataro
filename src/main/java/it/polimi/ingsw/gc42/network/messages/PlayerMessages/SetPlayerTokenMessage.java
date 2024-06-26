package it.polimi.ingsw.gc42.network.messages.PlayerMessages;

import it.polimi.ingsw.gc42.model.classes.game.Token;
import it.polimi.ingsw.gc42.network.messages.MessageType;

/**
 * Implementation of a Socket Message used to set a Player's Token
 */
public class SetPlayerTokenMessage extends PlayerMessage{
    private final Token token;

    /**
     * Constructor Method
     * @param type the Message's type of content
     * @param gameID the Game's gameID
     * @param playerID the Player's playerID
     * @param token the Token to set
     */
    public SetPlayerTokenMessage(MessageType type, int gameID, int playerID, Token token) {
        super(type, gameID, playerID);
        this.token = token;
    }

    /**
     * Getter Method for token
     * @return the Token to set
     */
    public Token getToken() {
        return token;
    }
}
