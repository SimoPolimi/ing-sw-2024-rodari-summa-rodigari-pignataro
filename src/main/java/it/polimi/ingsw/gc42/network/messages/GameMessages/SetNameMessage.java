package it.polimi.ingsw.gc42.network.messages.GameMessages;

import it.polimi.ingsw.gc42.network.messages.MessageType;

/**
 * Implementation of a Socket Message used to set a Game's Name
 */
public class SetNameMessage extends GameMessage{
    private final String name;

    /**
     * Constructor Method
     * @param type the Message's type of content
     * @param gameID the Game's gameID
     * @param name the new Name to set
     */
    public SetNameMessage(MessageType type, int gameID, String name) {
        super(type, gameID);
        this.name = name;
    }

    /**
     * Getter Method for name
     * @return the new Name to set
     */
    public String getName() {
        return name;
    }
}
