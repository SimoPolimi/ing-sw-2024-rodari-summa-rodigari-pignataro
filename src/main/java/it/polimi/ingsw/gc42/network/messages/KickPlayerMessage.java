package it.polimi.ingsw.gc42.network.messages;

import it.polimi.ingsw.gc42.model.classes.game.Player;

public class KickPlayerMessage extends GameMessage{
    private final Player player;

    public KickPlayerMessage(MessageType type, int gameID, Player player) {
        super(type, gameID);
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }
}
