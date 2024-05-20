package it.polimi.ingsw.gc42.network.messages.responses;

import it.polimi.ingsw.gc42.model.classes.cards.Coordinates;
import it.polimi.ingsw.gc42.model.classes.cards.PlayableCard;
import it.polimi.ingsw.gc42.network.messages.Message;
import it.polimi.ingsw.gc42.network.messages.MessageType;

public class PlayableCardResponse extends Message {
    private PlayableCard response;
    private int x;
    private int y;
    
    public PlayableCardResponse(MessageType type, PlayableCard response, int x, int y) {
        super(type);
        this.response = response;
        this.x = x;
        this.y = y;
    }
    
    public PlayableCard getResponse() {return response;}

    public int getX() {return x;}
    public int getY() {return y;}
}
