package it.polimi.ingsw.gc42.network.messages;

import it.polimi.ingsw.gc42.model.classes.game.Token;

public class SetPlayerSecretObjectiveMessage extends PlayerMessage{
    private int pickedCard;

    public SetPlayerSecretObjectiveMessage(MessageType type, int gameID, int playerID, int pickedCard) {
        super(type, gameID, playerID);
        this.pickedCard = pickedCard;
    }

    public int getToken() {
        return pickedCard;
    }

    public void setToken(int token) {
        this.pickedCard = token;
    }

    public String toString(){
        return super.toString() + ", " + pickedCard;
    }
}
