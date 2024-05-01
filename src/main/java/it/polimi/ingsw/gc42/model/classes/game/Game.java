package it.polimi.ingsw.gc42.model.classes.game;

import it.polimi.ingsw.gc42.controller.GameStatus;
import it.polimi.ingsw.gc42.model.classes.Deck;
import it.polimi.ingsw.gc42.model.classes.PlayingDeck;

import it.polimi.ingsw.gc42.model.classes.cards.CardType;
import it.polimi.ingsw.gc42.model.interfaces.*;
import it.polimi.ingsw.gc42.network.PlayersNumberListener;
import it.polimi.ingsw.gc42.network.RemoteViewController;

import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;

public class Game implements Observable, Serializable {
    private PlayingDeck resourcePlayingDeck;
    private PlayingDeck goldPlayingDeck;
    private PlayingDeck objectivePlayingDeck;
    private Deck starterDeck;
    private boolean isResourceDeckEmpty;
    private boolean isGoldDeckEmpty;
    private boolean playerHasReachedTwentyPoints;
                                                                                            //************************************
    private int playerTurn;                                                                 // PlayerTurn goes from 1 to 4
                                                                                            //*************************************
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
            notifyListeners("Common Objectives");
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
            notifyListeners("End game");
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
        notifyListeners("Players Changed");
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
        if (i >= 1 && i <= players.size()) {
            this.playerTurn = i;
        } else throw new IllegalArgumentException("This player doesn't exist");
        for (int j = 1; j <= players.size(); j++) {
            if (j == i) {
                players.get(j-1).setStatus(GameStatus.MY_TURN);
            } else {
                players.get(j-1).setStatus(GameStatus.NOT_MY_TURN);
            }
        }
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

    /**
     * Puts a Card from the top of the Deck of the PlayingDeck in the specified slot.
     * If the Deck is empty, puts a Card from the top of the Deck of the other PlayingDeck in the specified slot.
     * @param playingDeck: the PlayingDeck associated to the slot where the Card will be put
     * @param slot: the slot where the Card will be put
     */
    public void putDown(PlayingDeck playingDeck,int slot){
        if (slot > 0 && slot < 3) {
            // Fill
            // Resource
            if (playingDeck.getDeck().getCardType().equals(CardType.RESOURCECARD)) {
                if (playingDeck.getDeck().getNumberOfCards() > 0) {
                    // Grab from ResourcePlayingDeck
                    playingDeck.setSlot(resourcePlayingDeck.getDeck().draw(), slot);
                } else {
                    if (goldPlayingDeck.getDeck().getNumberOfCards() > 0) {
                        // Grab from GoldPlayingDeck
                        playingDeck.setSlot(goldPlayingDeck.getDeck().draw(), slot);
                    } else {
                        // Both Decks are empty
                        playingDeck.setSlot(null, slot);
                    }
                }
                if (slot == 1) {
                    notifyListeners("Resource 1");
                } else if (slot == 2) {
                    notifyListeners("Resource 2");
                }
            } else if (playingDeck.getDeck().getCardType().equals(CardType.GOLDCARD)) {
                // Gold
                if (playingDeck.getDeck().getNumberOfCards() > 0) {
                    // Grab from GoldPlayingDeck
                    playingDeck.setSlot(goldPlayingDeck.getDeck().draw(), slot);
                } else {
                    if (resourcePlayingDeck.getDeck().getNumberOfCards() > 0) {
                        // Grab from ResourcePlayingDeck
                        playingDeck.setSlot(resourcePlayingDeck.getDeck().draw(), slot);
                    } else {
                        // Both Decks are empty
                        playingDeck.setSlot(null, slot);
                    }
                }
                if (slot == 1) {
                    notifyListeners("Gold 1");
                } else if (slot == 2) {
                    notifyListeners("Gold 2");
                }
                // TODO: exceptions... move to Game?
            } else throw new IllegalArgumentException("There is no such Deck in this Game");
        } else throw new IllegalArgumentException("There is no such slot in this PlayingDeck");
    }

    public int getIndexOfPlayer(String nickName) {
        int index = -1;
        for (Player player : players) {
            if (player.getNickname().equals(nickName)) {
                index = players.indexOf(player);
            }
        }
        return index + 1;
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
        switch (context) {
            case "Resource 1" -> {
                for (Listener l: listeners) {
                    if (l instanceof ResourceSlot1Listener) {
                        l.onEvent();
                    }
                }
            }
            case "Resource 2" -> {
                for (Listener l: listeners) {
                    if (l instanceof ResourceSlot2Listener) {
                        l.onEvent();
                    }
                }
            }
            case "Gold 1" -> {
                for (Listener l: listeners) {
                    if (l instanceof GoldSlot1Listener) {
                        l.onEvent();
                    }
                }
            }
            case "Gold 2" -> {
                for (Listener l: listeners) {
                    if (l instanceof GoldSlot2Listener) {
                        l.onEvent();
                    }
                }
            }
            case "Common Objectives" -> {
                for (Listener l: listeners) {
                    if (l instanceof CommonObjectivesListener) {
                        l.onEvent();
                    }
                }
            }
            case "Points have changed" -> {
                for (Listener l: listeners) {
                    if (l instanceof StatusListener) {
                        l.onEvent();
                    }
                }
            }
            // TODO: check uses
            case "End game" ->{
                for (Listener l: listeners) {
                    if(l instanceof LastTurnListener){
                        l.onEvent();
                    }
                }
            }
            case "Players Changed" -> {
                for (Listener l: listeners) {
                    if (l instanceof PlayersNumberListener) {
                        l.onEvent();
                    }
                }
            }
        }
    }
}