package it.polimi.ingsw.gc42.network.messages;

public class SetPlayerSecretObjectiveMessage extends PlayerMessage{
    private final int pickedCard;

    public SetPlayerSecretObjectiveMessage(MessageType type, int gameID, int playerID, int pickedCard) {
        super(type, gameID, playerID);
        this.pickedCard = pickedCard;
    }

    public int getPickedCard() {
        return pickedCard;
    }
}
