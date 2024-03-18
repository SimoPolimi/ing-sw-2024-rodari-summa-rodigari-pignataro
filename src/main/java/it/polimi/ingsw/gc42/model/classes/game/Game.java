package it.polimi.ingsw.gc42.model.classes.game;

import it.polimi.ingsw.gc42.model.classes.Deck;
import it.polimi.ingsw.gc42.model.classes.PlayingDeck;

import it.polimi.ingsw.gc42.model.classes.cards.CardType;
import it.polimi.ingsw.gc42.model.interfaces.Listener;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Game {
    private PlayingDeck resourcePlayingDeck;
    private PlayingDeck goldPlayingDeck;
    private PlayingDeck objectivePlayingDeck;
    private Deck starterDeck;
    private boolean isResourceDeckEmpty;
    private boolean isGoldDeckEmpty;
    private boolean playerHasReachedTwentyPoints;

    private ArrayList<Player> players = new ArrayList<>();

    // Constructor only used internally
    public Game() {
        this.isResourceDeckEmpty = false;
        this.isGoldDeckEmpty = false;
        this.playerHasReachedTwentyPoints = false;
        try {
            initPlayingDecks();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        resourcePlayingDeck.getDeck().setListener(new Listener() {
            @Override
            public void onEvent() {
                setResourceDeckEmpty(true);
                checkEndGame();
            }
        });
        goldPlayingDeck.getDeck().setListener(new Listener() {
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

    public boolean isPlayerHasReachedTwentyPoints() {
        return playerHasReachedTwentyPoints;
    }

    public void setPlayerHasReachedTwentyPoints(boolean playerHasReachedTwentyPoints) {
        this.playerHasReachedTwentyPoints = playerHasReachedTwentyPoints;
    }


    /**
     * Initializer Method for all PlayingDecks
     * Creates and fills the 4 Decks, one for each type of Card.
     * Fills 3 PlayingDecks: one for Resource Cards, one for Gold Cards and one for Objective Cards.
     * Every PlayingDeck contains a Deck and two cards placed in Slot1 and Slot2, showing their Front Side.
     * Those 2 Cards are already drawn and positioned into their Slots.
     * Starter Cards are contained in StarterDeck, they don't have a PlayingDeck.
     */
    public void initPlayingDecks() throws FileNotFoundException {
        Deck resourceCardDeck = Deck.initDeck(CardType.RESOURCECARD);
        Deck goldCardDeck = Deck.initDeck(CardType.GOLDCARD);
        Deck objectiveCardDeck = Deck.initDeck(CardType.OBJECTIVECARD);
        this.starterDeck = Deck.initDeck(CardType.STARTERCARD);
        this.resourcePlayingDeck = new PlayingDeck(resourceCardDeck.draw(), resourceCardDeck.draw(), resourceCardDeck);
        this.goldPlayingDeck = new PlayingDeck(goldCardDeck.draw(), goldCardDeck.draw(), goldCardDeck);
        this.objectivePlayingDeck = new PlayingDeck(objectiveCardDeck.draw(), objectiveCardDeck.draw(), objectiveCardDeck);
    }
}