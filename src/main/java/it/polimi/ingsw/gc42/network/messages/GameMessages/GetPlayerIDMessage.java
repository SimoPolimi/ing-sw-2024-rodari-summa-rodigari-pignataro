package it.polimi.ingsw.gc42.network.messages.GameMessages;

import it.polimi.ingsw.gc42.network.messages.MessageType;

/**
 * Implementation of a Socket Message used to request one Player's playerID
 */
public class GetPlayerIDMessage extends GameMessage{
    private String name;

    /**
     * Constructor Method
     * @param type the Message's type of content
     * @param gameID the Game's gameID
     * @param name the Player's Nickname
     */
    public GetPlayerIDMessage(MessageType type, int gameID, String name) {
        super(type, gameID);
        this.name = name;
    }

    /**
     * Getter Method for name
     * @return the Player's Nickname
     */
    public String getName() {
        return name;
    }

    /**
     * Setter Method for name
     * @param name the Player's Nickname
     */
    public void setName(String name) {
        this.name = name;
    }
}
