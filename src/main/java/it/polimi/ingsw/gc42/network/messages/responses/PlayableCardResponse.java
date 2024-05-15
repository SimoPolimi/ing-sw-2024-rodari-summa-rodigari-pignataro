package it.polimi.ingsw.gc42.network.messages.responses;

import it.polimi.ingsw.gc42.model.classes.cards.PlayableCard;
import it.polimi.ingsw.gc42.network.messages.Message;
import it.polimi.ingsw.gc42.network.messages.MessageType;

public class PlayableCardResponse extends Message {
    private PlayableCard response;
    
    public PlayableCardResponse(MessageType type, PlayableCard response) {
        super(type);
        this.response = response;
    }
    
    public PlayableCard getResponse() {return response;}
}
