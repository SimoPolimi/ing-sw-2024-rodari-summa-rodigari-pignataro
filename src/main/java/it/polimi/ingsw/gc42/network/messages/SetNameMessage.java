package it.polimi.ingsw.gc42.network.messages;

import com.google.gson.annotations.Expose;

public class SetNameMessage extends GameMessage{
    @Expose
    private String name;

    public SetNameMessage(MessageType type, int gameID, String name) {
        super(type, gameID);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString(){
        return super.toString() + ", " + name;
    }
}
