package it.polimi.ingsw.gc42.network.messages.responses;

import it.polimi.ingsw.gc42.model.classes.game.Token;
import it.polimi.ingsw.gc42.network.messages.Message;
import it.polimi.ingsw.gc42.network.messages.MessageType;

/**
 * Implementation of a Socket Message used to send a Token
 */
public class TokenResponse extends Message {
    private final Token response;

    /**
     * Constructor Method
     * @param type the Message's type of content
     * @param response the Token to send
     */
    public TokenResponse(MessageType type, Token response) {
        super(type);
        this.response = response;
    }

    /**
     * Getter Method for response
     * @return the Token
     */
    public Token getResponse() {return response;}
}
