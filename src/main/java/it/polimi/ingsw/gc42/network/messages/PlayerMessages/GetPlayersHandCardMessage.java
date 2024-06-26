package it.polimi.ingsw.gc42.network.messages.PlayerMessages;

import it.polimi.ingsw.gc42.network.messages.MessageType;

/**
 * Implementation of a Socket Message used to request a Player's Hand Card
 */
public class GetPlayersHandCardMessage extends PlayerMessage{
    private final int cardID;

    /**
     * Constructor Method
     * @param type the Message's type of content
     * @param gameID the Game's gameID
     * @param playerID the Player's playerID
     * @param cardID the index of the Card inside the Player's Hand
     */
    public GetPlayersHandCardMessage(MessageType type, int gameID, int playerID, int cardID) {
        super(type, gameID, playerID);
        this.cardID = cardID;
    }

    /**
     * Getter Method for cardID
     * @return the index of the Card inside the Player's Hand
     */
    public int getCardID() {
        return cardID;
    }
}
