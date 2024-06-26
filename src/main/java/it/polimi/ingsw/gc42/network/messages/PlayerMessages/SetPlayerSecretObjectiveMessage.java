package it.polimi.ingsw.gc42.network.messages.PlayerMessages;

import it.polimi.ingsw.gc42.network.messages.MessageType;

/**
 * Implementation of a Socket Message used to set a Player's Secret Objective
 */
public class SetPlayerSecretObjectiveMessage extends PlayerMessage{
    private final int pickedCard;

    /**
     * Constructor Method
     * @param type the Message's type of content
     * @param gameID the Game's gameID
     * @param playerID the Player's playerID
     * @param pickedCard the index of the Temporary Objective to set
     */
    public SetPlayerSecretObjectiveMessage(MessageType type, int gameID, int playerID, int pickedCard) {
        super(type, gameID, playerID);
        this.pickedCard = pickedCard;
    }

    /**
     * Getter Method for pickedCard
     * @return the index of the Temporary Objective to set
     */
    public int getPickedCard() {
        return pickedCard;
    }
}
