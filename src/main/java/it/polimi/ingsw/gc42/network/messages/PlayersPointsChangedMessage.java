package it.polimi.ingsw.gc42.network.messages;

import it.polimi.ingsw.gc42.model.classes.game.Token;

/**
 * Implementation of a Socket Message used to notify Client that one Player's points have changed
 */
public class PlayersPointsChangedMessage extends Message {
    private final Token token;
    private final int newPoints;

    /**
     * Constructor Method
     * @param type the Message's type of content
     * @param token the Player's Token
     * @param newPoints the Player's updated Points
     */
    public PlayersPointsChangedMessage(MessageType type, Token token, int newPoints) {
        super(type);
        this.token = token;
        this.newPoints = newPoints;
    }

    /**
     * Getter Method for token
     * @return the Player's Token
     */
    public Token getToken() {
        return token;
    }

    /**
     * Getter Method for newPoints
     * @return the Player's updated Points
     */
    public int getNewPoints() {
        return newPoints;
    }
}
