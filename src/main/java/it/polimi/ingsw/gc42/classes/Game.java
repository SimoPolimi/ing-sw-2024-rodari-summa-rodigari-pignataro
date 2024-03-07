package it.polimi.ingsw.gc42.classes;

import java.util.ArrayList;

public class Game {

    private boolean isResourceDeckEmpty;
    private boolean isGoldDeckEmpty;

    private ArrayList<Player> players;

    public Game() {
        this.isResourceDeckEmpty = false;
        this.isGoldDeckEmpty = false;
    }

    public void startGame(){

    }

    public void endGame(){

    }

    public boolean addPlayers(Player player) {
        return false;
    }

    public boolean kickPlayers(Player player) {
        return false;
    }
}
