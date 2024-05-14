package it.polimi.ingsw.gc42.network.messages;

import com.google.gson.annotations.Expose;

public class NumberMessage extends Message {
    @Expose
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
