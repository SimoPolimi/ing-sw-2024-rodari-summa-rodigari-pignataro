package it.polimi.ingsw.gc42.network.messages;

public class NumberMessage extends Message {
    private int number;

    public NumberMessage(MessageType type, int number) {
        super(type);
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
