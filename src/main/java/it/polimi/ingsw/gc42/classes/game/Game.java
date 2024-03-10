package it.polimi.ingsw.gc42.classes.game;

import it.polimi.ingsw.gc42.classes.Deck;
import it.polimi.ingsw.gc42.classes.PlayingDecks;

import it.polimi.ingsw.gc42.interfaces.Listener;

import java.util.ArrayList;

public class Game {
    private PlayingDeck resourcePlayingDeck;
    private PlayingDeck goldPlayingDeck;
    private PlayingDeck objectivePlayingDeck;
    private Deck starterDeck;
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

    public void startGame() {
    }

    public void endGame() {

    }

    private void checkEndGame() {
        if ((isResourceDeckEmpty && isGoldDeckEmpty) || playerHasReachedTwentyPoints) {
            endGame();
        }
    }

    /**
     * Add the Player from the game
     *
     * @param player the player to be added from the game
     */
    //TODO make it boolean
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

    /**
     * Remove the Player from the game
     *
     * @param player the player to be removed from the game
     * @return true if the Player is removed and false otherwise
     */
    public boolean kickPlayer(Player player) {
        return false;
    }

    /**
     * Changes player's turn
     */
    public void nextTurn() {
    }

    public PlayingDeck getResourcePlayingDeck() {
        return resourcePlayingDeck;
    }

    public void setResourcePlayingDeck(PlayingDeck resourcePlayingDeck) {
        this.resourcePlayingDeck = resourcePlayingDeck;
    }

    public PlayingDeck getGoldPlayingDeck() {
        return goldPlayingDeck;
    }

    public void setGoldPlayingDeck(PlayingDeck goldPlayingDeck) {
        this.goldPlayingDeck = goldPlayingDeck;
    }

    public PlayingDeck getObjectivePlayingDeck() {
        return objectivePlayingDeck;
    }

    public void setObjectivePlayingDeck(PlayingDeck objectivePlayingDeck) {
        this.objectivePlayingDeck = objectivePlayingDeck;
    }

    public Deck getStarterDeck() {
        return starterDeck;
    }

    public void setStarterDeck(Deck starterDeck) {
        this.starterDeck = starterDeck;
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