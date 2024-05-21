package it.polimi.ingsw.gc42.network.messages;

public class SetNameMessage extends GameMessage{
    private final String name;

    public SetNameMessage(MessageType type, int gameID, String name) {
        super(type, gameID);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
