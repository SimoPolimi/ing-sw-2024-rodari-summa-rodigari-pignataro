package it.polimi.ingsw.gc42.network.messages;

/**
 * Implementation of a Socket Message used to request a Player
 */
public class GetPlayerMessage extends GameMessage{
    private int playerID;

    /**
     * Constructor Method
     * @param type the Message's type of content
     * @param gameID the Game's gameID
     * @param playerID the Player's playerID
     */
    public GetPlayerMessage(MessageType type, int gameID, int playerID) {
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

    /**
     * Setter Method for playerID
     * @param playerID the Player's playerID
     */
    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }
}
