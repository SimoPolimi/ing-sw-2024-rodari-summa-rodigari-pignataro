package it.polimi.ingsw.gc42.model.classes.game;

import it.polimi.ingsw.gc42.model.classes.Deck;
import it.polimi.ingsw.gc42.model.classes.PlayingDeck;

import it.polimi.ingsw.gc42.model.classes.cards.CardType;
import it.polimi.ingsw.gc42.model.interfaces.*;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Game implements Observable {
    private PlayingDeck resourcePlayingDeck;
    private PlayingDeck goldPlayingDeck;
    private PlayingDeck objectivePlayingDeck;
    private Deck starterDeck;
    private boolean isResourceDeckEmpty;
    private boolean isGoldDeckEmpty;
    private boolean playerHasReachedTwentyPoints;
    private int playerTurn;
    private final Chat chat = new Chat();

    private final ArrayList<Player> players = new ArrayList<>();
    private final ArrayList<Listener> listeners = new ArrayList<>();

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
        resourcePlayingDeck.getDeck().setListener(new EmptyDeckListener() {
            @Override
            public void onEvent() {
                setResourceDeckEmpty(true);
                checkEndGame();
            }
        });
        goldPlayingDeck.getDeck().setListener(new EmptyDeckListener() {
            @Override
            public void onEvent() {
                setGoldDeckEmpty(true);
                checkEndGame();
            }
        });
        this.playerTurn = 1;
    }

    public void startGame() {

    }

    // TODO: remove
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
    public void addPlayer(Player player) {
        player.setListener(new PlayerListener() {
            @Override
            public void onEvent() {
                setPlayerHasReachedTwentyPoints(true);
                checkEndGame();
            }
        });
        player.setListener(new PointsListener() {
            @Override
            public void onEvent() {
                notifyListeners("Points have changed");
                setFirstPlayer();
            }
        });
        notifyListeners("Points have changed");
        players.add(player);
        setFirstPlayer();
    }

    /**
     * Remove the Player from the game
     *
     * @param player the player to be removed from the game
     * @return true if the Player is removed and false otherwise
     */
    public boolean kickPlayer(Player player) {
        return players.remove(player);
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

    public Player getPlayer(int index) {
        return players.get(index - 1);
    }

    // TODO: for grabCard
    public Player getPlayer(Token token) {
        for (Player p : players) {
            if (p.getToken().equals(token)) {
                return p;
            }
        }
        return null;
    }

    public boolean isPlayerHasReachedTwentyPoints() {
        return playerHasReachedTwentyPoints;
    }

    public void setPlayerHasReachedTwentyPoints(boolean playerHasReachedTwentyPoints) {
        this.playerHasReachedTwentyPoints = playerHasReachedTwentyPoints;
    }

    public int getPlayerTurn() {
        return playerTurn;
    }

    public void setPlayerTurn(int i) throws IllegalArgumentException {
        if (i == players.size()) {
            i = 1;
        }
        if (i >= 1 && i < players.size()) {
            this.playerTurn = i - 1;
        } else throw new IllegalArgumentException("This player doesn't exist");
    }

    public Player getCurrentPlayer() {
        return getPlayer(getPlayerTurn());
    }

    public int getNumberOfPlayers() {
        return players.size();
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
        this.playerTurn = 1;
    }

    public Chat getChat() {
        return chat;
    }

    private void setFirstPlayer() {
        if (!players.isEmpty()) {
            int max = 0;
            for (Player player : players) {
                if (player.getPoints() > players.get(max).getPoints()) {
                    max = players.indexOf(player);
                }
            }
            for (int i = 0; i < players.size(); i++) {
                if (i == max) {
                    players.get(i).setFirst(true);
                } else {
                    players.get(i).setFirst(false);
                }
            }
        }
    }

    @Override
    public void setListener(Listener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(Listener listener) {
        listeners.remove(listener);
    }

    @Override
    public void notifyListeners(String context) {
        for (Listener l: listeners) {
            l.onEvent();
        }
    }
}