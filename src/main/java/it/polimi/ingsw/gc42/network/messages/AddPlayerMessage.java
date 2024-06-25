package it.polimi.ingsw.gc42.network.messages;

import com.google.gson.annotations.Expose;
import it.polimi.ingsw.gc42.model.classes.game.Player;

/**
 * Implementation of a Socket Message used to send a Player to add in a Game
 */
public class AddPlayerMessage extends GameMessage{
    @Expose
    private Player player;

    /**
     * Constructor Method
     * @param type the Message's type of content
     * @param gameID the Game's gameID
     * @param player the Player's playerID
     */
    public AddPlayerMessage(MessageType type, int gameID, Player player) {
        super(type, gameID);
        this.player = player;
    }

    /**
     * Getter Method for the content of this Message
     * @return the Player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Setter Method for the content of this Message
     * @param player the Player to send
     */
    public void setPlayer(Player player) {
        this.player = player;
    }
}
