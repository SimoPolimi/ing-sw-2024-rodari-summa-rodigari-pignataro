package it.polimi.ingsw.gc42.network.messages;

import com.google.gson.annotations.Expose;

public class NumberMessage extends GameMessage {
    @Expose
    private int number;

    public NumberMessage(MessageType type, int gameID, int number) {
        super(type, gameID);
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String toString() {
        return super.toString() + ", " + number;
    }
}
