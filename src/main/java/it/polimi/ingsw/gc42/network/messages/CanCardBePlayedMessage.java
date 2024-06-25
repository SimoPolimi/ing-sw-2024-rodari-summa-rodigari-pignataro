package it.polimi.ingsw.gc42.network.messages;

/**
 * Implementation of a Socket Message used to ask if a Card can be played
 * or if its cost requirements are not satisfied
 */
public class CanCardBePlayedMessage extends PlayerMessage{
    private int cardID;

    /**
     * Constructor Method
     * @param type the Message's type of content
     * @param gameID the Game's gameID
     * @param playerID the Player's playerID
     * @param cardID the index of the Card inside the Player's Hand
     */
    public CanCardBePlayedMessage(MessageType type, int gameID, int playerID, int cardID) {
        super(type, gameID, playerID);
        this.cardID = cardID;
    }

    /**
     * Geter Method for content
     * @return the index of the Card inside the Player's Hand
     */
    public int getCardID() {
        return cardID;
    }

    /**
     * Setter Method for the content
     * @param cardID the index of the Card inside the Player's Hand
     */
    public void setCardID(int cardID) {
        this.cardID = cardID;
    }
}
