package it.polimi.ingsw.gc42.model.classes.game;

import it.polimi.ingsw.gc42.controller.GameStatus;
import it.polimi.ingsw.gc42.model.classes.Deck;
import it.polimi.ingsw.gc42.model.classes.PlayingDeck;

import it.polimi.ingsw.gc42.model.classes.cards.CardType;
import it.polimi.ingsw.gc42.model.classes.cards.ObjectiveCard;
import it.polimi.ingsw.gc42.model.interfaces.*;
import it.polimi.ingsw.gc42.network.interfaces.PlayersNumberListener;

import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Game implements Observable, Serializable {
    private PlayingDeck resourcePlayingDeck;
    private PlayingDeck goldPlayingDeck;
    private PlayingDeck objectivePlayingDeck;
    private Deck starterDeck;
    private boolean isResourceDeckEmpty;
    private boolean isGoldDeckEmpty;
    private boolean playerHasReachedTwentyPoints;
                                                                                            //************************************
    private int playerTurn = 0;                                                                 // PlayerTurn goes from 1 to 4
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

    // TODO: remove?
    public void endGame() {
        notifyListeners("End Game");
    }

    private void checkEndGame() {
        if ((isResourceDeckEmpty && isGoldDeckEmpty) || playerHasReachedTwentyPoints) {
            notifyListeners("Semi Last Turn");
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
                if (!playerHasReachedTwentyPoints) {
                    setPlayerHasReachedTwentyPoints(true);
                    checkEndGame();
                }
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

    /**
     * Getter method for the resourcePlayingDeck
     * @return the resourcePlayingDeck
     */
    public PlayingDeck getResourcePlayingDeck() {
        return resourcePlayingDeck;
    }

    /**
     * Setter method for the resourcePlayingDeck
     * @param resourcePlayingDeck the resourcePlayingDeck
     */
    public void setResourcePlayingDeck(PlayingDeck resourcePlayingDeck) {
        this.resourcePlayingDeck = resourcePlayingDeck;
    }

    /**
     * Getter method for the goldPlayingDeck
     * @return the goldPlayingDeck
     */
    public PlayingDeck getGoldPlayingDeck() {
        return goldPlayingDeck;
    }

    /**
     * Setter method for the goldPlayingDeck
     * @param goldPlayingDeck the goldPlayingDeck
     */
    public void setGoldPlayingDeck(PlayingDeck goldPlayingDeck) {
        this.goldPlayingDeck = goldPlayingDeck;
    }

    /**
     * Getter method for the objectivePlayingDeck
     * @return the objectivePlayingDeck
     */
    public PlayingDeck getObjectivePlayingDeck() {
        return objectivePlayingDeck;
    }

    /**
     * Setter method for the objectivePlayingDeck
     * @param objectivePlayingDeck the objectivePlayingDeck
     */
    public void setObjectivePlayingDeck(PlayingDeck objectivePlayingDeck) {
        this.objectivePlayingDeck = objectivePlayingDeck;
    }

    /**
     * Getter method for the starterDeck
     * @return the starterDeck
     */
    public Deck getStarterDeck() {
        return starterDeck;
    }

    /**
     * Setter method for the starterDeck
     * @param starterDeck the starterDeck
     */
    public void setStarterDeck(Deck starterDeck) {
        this.starterDeck = starterDeck;
    }

    /**
     * Checks if the ResourceDeck is empty
     * @return {@code true} if the ResourceDeck is empty
     */
    public boolean isResourceDeckEmpty() {
        return isResourceDeckEmpty;
    }

    /**
     * Setter Method for the boolean that indicates if the ResourceDeck is empty
     * @param resourceDeckEmpty {@code true} if the ResourceDeck is empty, {@code false} if not
     */
    public void setResourceDeckEmpty(boolean resourceDeckEmpty) {
        isResourceDeckEmpty = resourceDeckEmpty;
    }

    /**
     * Checks if the GoldDeck is empty
     * @return {@code true} if the GoldDeck is empty
     */
    public boolean isGoldDeckEmpty() {
        return isGoldDeckEmpty;
    }

    /**
     * Setter Method for the boolean that indicates if the GoldDeck is empty
     * @param goldDeckEmpty {@code true} if the GoldDeck is empty, {@code false} if not
     */
    public void setGoldDeckEmpty(boolean goldDeckEmpty) {
        isGoldDeckEmpty = goldDeckEmpty;
    }

    /**
     * Gets the Player at the specified index (from 1 to 4)
     * @param index the index of the player (from 1 to 4)
     * @return the Player at the specified index
     */
    public Player getPlayer(int index) {
        return players.get(index - 1);
    }

    /**
     * Checks if a Player has reached 20 points
     * @return {@code true} a Player has reached 20 points
     */
    public boolean isPlayerHasReachedTwentyPoints() {
        return playerHasReachedTwentyPoints;
    }

    /**
     * Setter Method for the boolean that indicates if a Player has reached 20 points
     * @param playerHasReachedTwentyPoints {@code true} if a Player has reached 20 points, {@code false} if not
     */
    public void setPlayerHasReachedTwentyPoints(boolean playerHasReachedTwentyPoints) {
        this.playerHasReachedTwentyPoints = playerHasReachedTwentyPoints;
    }

    //TODO: check index

    /**
     * Gets the index of the Player whose turn it is
     * @return the index of the Player whose turn it is
     */
    public int getPlayerTurn() {
        return playerTurn;
    }

    /**
     * Sets the index of the Player whose turn it is and sets his status to MY_TURN.
     * Also sets the statuses of other Players to NOT_MY_TURN
     * @param i the index of the Player whose turn it is
     * @throws IllegalArgumentException if the Player at that index doesn't exist
     */
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

    /**
     * Gets the Player whose turn it is
     * @return the Player whose turn it is
     */
    public Player getCurrentPlayer() {
        return getPlayer(getPlayerTurn());
    }

    /**
     * Gets the number of Players in the Game
     * @return the number of Players in the Game
     */
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
        setStarterDeck(Deck.initDeck(CardType.STARTERCARD));
        setResourcePlayingDeck(new PlayingDeck(resourceCardDeck.draw(), resourceCardDeck.draw(), resourceCardDeck));
        setGoldPlayingDeck(new PlayingDeck(goldCardDeck.draw(), goldCardDeck.draw(), goldCardDeck));
        setObjectivePlayingDeck(new PlayingDeck(objectiveCardDeck.draw(), objectiveCardDeck.draw(), objectiveCardDeck));

        this.playerTurn = 1;
    }

    /**
     * Getter Method for the Chat
     * @return the Chat
     */
    public Chat getChat() {
        return chat;
    }

    // TODO: what???

    /**
     * Sets who is the winning Player, based on points (visual purposes)
     */
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
            } else throw new IllegalArgumentException("There is no such Deck in this Game");
        } else throw new IllegalArgumentException("There is no such slot in this PlayingDeck");
    }

    /**
     * Gets the index of the Player with the specified nickname
     * @param nickName the nickname of the Player to be returned
     * @return the Player with the specified nickname
     */
    public int getIndexOfPlayer(String nickName) {
        int index = -1;
        for (Player player : players) {
            if (player.getNickname().equals(nickName)) {
                index = players.indexOf(player);
            }
        }
        return index + 1;
    }

    public ArrayList<HashMap<String, String>> countPoints(){
        ArrayList<HashMap<String, String>> points = new ArrayList<>();
        for (Player player : players) {
            HashMap<String, String> playerPoints = new HashMap<>();
            playerPoints.put("Nickname", player.getNickname());
            switch (player.getToken()) {
                case RED -> playerPoints.put("Token", "RED");
                case BLUE -> playerPoints.put("Token", "BLUE");
                case GREEN -> playerPoints.put("Token", "GREEN");
                case YELLOW -> playerPoints.put("Token", "YELLOW");
            }
            int secretObjectivePoints = player.getSecretObjective().getObjective().calculatePoints(player.getPlayField().getPlayedCards());
            int commonObjective1Points = ((ObjectiveCard)objectivePlayingDeck.getSlot(1)).getObjective().calculatePoints(player.getPlayField().getPlayedCards());
            int commonObjective2Points = ((ObjectiveCard)objectivePlayingDeck.getSlot(2)).getObjective().calculatePoints(player.getPlayField().getPlayedCards());
            playerPoints.put("InitialPoints", String.valueOf(player.getPoints()));
            playerPoints.put("SecretObjectivePoints", String.valueOf(secretObjectivePoints));
            playerPoints.put("CommonObjective1Points", String.valueOf(commonObjective1Points));
            playerPoints.put("CommonObjective2Points", String.valueOf(commonObjective2Points));
            playerPoints.put("TotalPoints", String.valueOf(player.getPoints() + secretObjectivePoints + commonObjective1Points + commonObjective2Points));
            playerPoints.put("IsWinner", String.valueOf(false));
            points.add(playerPoints);

            // Updated the Player's Points
            player.setPoints(player.getPoints() + secretObjectivePoints + commonObjective1Points + commonObjective2Points);
        }

        int winnerID = 0;
        int max = 0;
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getPoints() > max) {
                winnerID = i;
                max = players.get(i).getPoints();
            }
        }
        points.get(winnerID).replace("IsWinner", String.valueOf(true));

        return points;
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
            case "Semi Last Turn" ->{
                for (Listener l: listeners) {
                    if(l instanceof SemiLastTurnListener){
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