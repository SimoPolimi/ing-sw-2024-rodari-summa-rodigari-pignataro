package it.polimi.ingsw.gc42.network.messages.responses;

import it.polimi.ingsw.gc42.model.classes.cards.Card;
import it.polimi.ingsw.gc42.network.messages.Message;
import it.polimi.ingsw.gc42.network.messages.MessageType;

import java.util.ArrayList;

/**
 * Implementation of a Socket Message used to send a Deck
 */
public class DeckResponse extends Message {
    private final ArrayList<Card> cards;

    /**
     * Constructor Method
     * @param type the Message's type of content
     * @param cards the Deck to send
     */
    public DeckResponse(MessageType type, ArrayList<Card> cards) {
        super(type);
        this.cards = cards;
    }

    /**
     * Getter Method for response
     * @return the Deck
     */
    public ArrayList<Card> getResponse() {
        return cards;
    }
}
