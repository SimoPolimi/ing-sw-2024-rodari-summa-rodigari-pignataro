package it.polimi.ingsw.gc42.network.messages;

public class GetNameMessage extends GameMessage{
    private String name;

    public GetNameMessage(MessageType type, int gameID, String name) {
        super(type, gameID);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
