package it.polimi.ingsw.gc42.network.messages;

import it.polimi.ingsw.gc42.model.classes.game.Player;

public class AddPlayerMessage extends GameMessage{
    private Player player;

    public AddPlayerMessage(MessageType type, int gameID, Player player) {
        super(type, gameID);
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    @Override
    public String toString(){
        return super.toString() + ", " + player.getNickname();
    }
}
