package it.polimi.ingsw.gc42.network.messages;

/**
 * Implementation of a Socket Message used to send the playerID info
 */
public class PlayerMessage extends GameMessage {
    private final int playerID;

    /**
     * Constructor Method
     * @param type the Message's type of content
     * @param gameID the Game's gameID
     * @param playerID the Player's playerID
     */
    public PlayerMessage(MessageType type, int gameID, int playerID) {
        super(type, gameID);
        this.playerID = playerID;
    }

    /**
     * Getter Method for playerID
     * @return the Player's playerID
     */
    public int getPlayerID() {
        return playerID;
    }
}