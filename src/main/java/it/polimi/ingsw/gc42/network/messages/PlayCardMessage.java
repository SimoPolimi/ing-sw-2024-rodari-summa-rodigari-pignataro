package it.polimi.ingsw.gc42.network.messages;

/**
 * Implementation of a Socket Message used to play a Card
 */
public class PlayCardMessage extends PlayerMessage {
    private final int handCard;
    private final int x;
    private final int y;

    /**
     * Constructor Method
     * @param type the Message's type of content
     * @param gameID the Game's gameID
     * @param playerID the Player's playerID
     * @param handCard the index of the Card inside the Player's Hand
     * @param x the x coordinate where the Card will be played
     * @param y the y coordinate where the Card will be played
     */
    public PlayCardMessage(MessageType type, int gameID, int playerID, int handCard, int x, int y) {
        super(type, gameID, playerID);
        this.handCard = handCard;
        this.y = y;
        this.x = x;
    }

    /**
     * Getter Method for x
     * @return the x coordinate where the Card will be played
     */
    public int getX() {
        return x;
    }

    /**
     * Getter Method for y
     * @return the y coordinate where the Card will be played
     */
    public int getY() {
        return y;
    }

    /**
     * Getter Method for cardID
     * @return the index of the Card inside the Player's Hand
     */
    public int getHandCard() {
        return handCard;
    }
}
