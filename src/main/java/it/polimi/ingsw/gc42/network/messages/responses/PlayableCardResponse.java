package it.polimi.ingsw.gc42.network.messages.responses;

import it.polimi.ingsw.gc42.model.classes.cards.PlayableCard;
import it.polimi.ingsw.gc42.network.messages.Message;
import it.polimi.ingsw.gc42.network.messages.MessageType;

public class PlayableCardResponse extends Message {
    private final PlayableCard response;
    private final int x;
    private final int y;
    private final boolean isFrontFacing;
    
    public PlayableCardResponse(MessageType type, PlayableCard response, int x, int y, boolean isFrontFacing) {
        super(type);
        this.response = response;
        this.x = x;
        this.y = y;
        this.isFrontFacing = isFrontFacing;
    }
    
    public PlayableCard getResponse() {return response;}

    public int getX() {return x;}

    public int getY() {return y;}

    public boolean isFrontFacing() {
        return isFrontFacing;
    }
}
