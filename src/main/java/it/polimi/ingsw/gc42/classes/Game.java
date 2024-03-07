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

    public boolean isResourceDeckEmpty() {
        return isResourceDeckEmpty;
    }

    public void setResourceDeckEmpty(boolean resourceDeckEmpty) {
        isResourceDeckEmpty = resourceDeckEmpty;
    }

    public boolean isGoldDeckEmpty() {
        return isGoldDeckEmpty;
    }

    public void setGoldDeckEmpty(boolean goldDeckEmpty) {
        isGoldDeckEmpty = goldDeckEmpty;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }
}
