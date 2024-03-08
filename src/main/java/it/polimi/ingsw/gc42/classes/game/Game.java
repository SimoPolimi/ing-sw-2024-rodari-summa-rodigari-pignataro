package it.polimi.ingsw.gc42.classes.game;

import it.polimi.ingsw.gc42.classes.PlayingDecks;

import it.polimi.ingsw.gc42.interfaces.Listener;

import java.util.ArrayList;

public class Game {

    private boolean isResourceDeckEmpty;
    private boolean isGoldDeckEmpty;
    private PlayingDecks playingDeck;
    private boolean playerHasReachedTwentyPoints;

    private ArrayList<Player> players;

    // Constructor only used internally
    public Game() {
        this.isResourceDeckEmpty = false;
        this.isGoldDeckEmpty = false;
        this.playerHasReachedTwentyPoints = false;
        this.playingDeck = PlayingDecks.initPlayingDeck();
        this.playingDeck.getResourceCardDeck().setListener(new Listener() {
            @Override
            public void onEvent() {
                setResourceDeckEmpty(true);
                checkEndGame();
            }
        });
        this.playingDeck.getGoldCardDeck().setListener(new Listener() {
            @Override
            public void onEvent() {
                setGoldDeckEmpty(true);
                checkEndGame();
            }
        });
    }

    public void startGame(){
    }

    public void endGame(){

    }

    private void checkEndGame() {
        if ((isResourceDeckEmpty && isGoldDeckEmpty) || playerHasReachedTwentyPoints) {
            endGame();
        }
    }

    public void addPlayer(Player player) {
        player.setListener(new Listener() {
            @Override
            public void onEvent() {
                setPlayerHasReachedTwentyPoints(true);
                checkEndGame();
            }
        });
        players.add(player);
    }

    public boolean kickPlayer(Player player) {
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

    public PlayingDecks getPlayingDeck() {
        return playingDeck;
    }

    public void setPlayingDeck(PlayingDecks playingDeck) {
        this.playingDeck = playingDeck;
    }

    public boolean isPlayerHasReachedTwentyPoints() {
        return playerHasReachedTwentyPoints;
    }

    public void setPlayerHasReachedTwentyPoints(boolean playerHasReachedTwentyPoints) {
        this.playerHasReachedTwentyPoints = playerHasReachedTwentyPoints;
    }
}
