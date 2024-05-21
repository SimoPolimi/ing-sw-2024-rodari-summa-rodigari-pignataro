package it.polimi.ingsw.gc42.network.messages;

public class NumberMessage extends GameMessage {
    private final int number;

    public NumberMessage(MessageType type, int gameID, int number) {
        super(type, gameID);
        this.number = number;
    }

    public int getNumber() {
        return number;
    }
}
