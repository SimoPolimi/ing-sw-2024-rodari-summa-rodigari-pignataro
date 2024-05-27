package it.polimi.ingsw.gc42.network.messages.responses;

import it.polimi.ingsw.gc42.model.classes.cards.Card;
import it.polimi.ingsw.gc42.network.messages.Message;
import it.polimi.ingsw.gc42.network.messages.MessageType;

import java.util.ArrayList;

public class DeckResponse extends Message {
    private final ArrayList<Card> cards;


    public DeckResponse(MessageType type, ArrayList<Card> cards) {
        super(type);
        this.cards = cards;
    }

    public ArrayList<Card> getResponse() {
        return cards;
    }
}
