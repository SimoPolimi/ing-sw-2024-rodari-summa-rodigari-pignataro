package it.polimi.ingsw.gc42.network.messages;

import com.google.gson.annotations.Expose;
import it.polimi.ingsw.gc42.model.classes.game.Player;

public class StringMessage extends Message{
    @Expose
    private String string;

    public StringMessage(MessageType type, String string) {
        super(type);
        this.string = string;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    @Override
    public String toString() {
        return super.toString() + ", " + string;
    }
}
