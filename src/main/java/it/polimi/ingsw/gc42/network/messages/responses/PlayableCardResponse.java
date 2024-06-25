package it.polimi.ingsw.gc42.network.messages.responses;

import it.polimi.ingsw.gc42.model.classes.cards.PlayableCard;
import it.polimi.ingsw.gc42.network.messages.Message;
import it.polimi.ingsw.gc42.network.messages.MessageType;

/**
 * Implementation of a Socket Message used to send a PlayableCard
 */
public class PlayableCardResponse extends Message {
    private final PlayableCard response;
    private final int x;
    private final int y;
    private final boolean isFrontFacing;

    /**
     * Constructor Method
     * @param type the Message's type of content
     * @param response the PlayableCard to send
     * @param x the Card's x coordinate
     * @param y the Card's y coordinate
     * @param isFrontFacing the Card's isFrontFacing status
     */
    public PlayableCardResponse(MessageType type, PlayableCard response, int x, int y, boolean isFrontFacing) {
        super(type);
        this.response = response;
        this.x = x;
        this.y = y;
        this.isFrontFacing = isFrontFacing;
    }

    /**
     * Getter Method for response
     * @return the PlayableCard
     */
    public PlayableCard getResponse() {return response;}

    /**
     * Getter Method for x
     * @return the Card's x coordinate
     */
    public int getX() {return x;}

    /**
     * Getter Method for y
     * @return the Card's y coordinate
     */
    public int getY() {return y;}

    /**
     * Getter Method for isFrontFacing
     * @return the Card's isFrontFacing status
     */
    public boolean isFrontFacing() {
        return isFrontFacing;
    }
}
