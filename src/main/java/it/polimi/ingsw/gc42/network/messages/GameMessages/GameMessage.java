package it.polimi.ingsw.gc42.network.messages.GameMessages;

import it.polimi.ingsw.gc42.network.messages.Message;
import it.polimi.ingsw.gc42.network.messages.MessageType;

/**
 * Implementation of a Socket Message used to send the gameID's info
 */
public class GameMessage extends Message {
    private int gameID;

    /**
     * Constructor Method
     * @param type the Message's type of content
     * @param gameID the Game's gameID
     */
    public GameMessage(MessageType type, int gameID) {
        super(type);
        this.gameID = gameID;
    }

    /**
     * Getter Method for gameID
     * @return the Game's gameID
     */
    public int getGameID() {
        return gameID;
    }

    /**
     * Setter Method for gameID
     * @param gameID  the Game's gameID
     */
    public void setGameID(int gameID) {
        this.gameID = gameID;
    }
}