package it.polimi.ingsw.gc42.network.messages.responses;

import it.polimi.ingsw.gc42.model.classes.cards.PlayableCard;
import it.polimi.ingsw.gc42.network.messages.Message;
import it.polimi.ingsw.gc42.network.messages.MessageType;

import java.util.ArrayList;

public class PlayableCardListResponse extends Message {
    private final ArrayList<PlayableCard> response;


    public PlayableCardListResponse(MessageType type, ArrayList<PlayableCard> response) {
        super(type);
        this.response = response;
    }

    public ArrayList<PlayableCard> getResponse() {
        return response;
    }
}
