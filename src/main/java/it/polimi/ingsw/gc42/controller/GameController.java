package it.polimi.ingsw.gc42.controller;

import it.polimi.ingsw.gc42.model.classes.PlayingDeck;
import it.polimi.ingsw.gc42.model.classes.cards.CardType;
import it.polimi.ingsw.gc42.model.classes.game.ChatMessage;
import it.polimi.ingsw.gc42.model.exceptions.IllegalActionException;
import it.polimi.ingsw.gc42.model.exceptions.IllegalPlacementException;
import it.polimi.ingsw.gc42.model.exceptions.PlacementConditionNotMetException;
import it.polimi.ingsw.gc42.model.interfaces.*;
import it.polimi.ingsw.gc42.network.interfaces.RemoteViewController;
import it.polimi.ingsw.gc42.view.Interfaces.DeckViewListener;
import it.polimi.ingsw.gc42.view.Interfaces.ViewController;
import it.polimi.ingsw.gc42.model.classes.game.Game;
import it.polimi.ingsw.gc42.model.classes.game.Player;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

public class GameController implements Serializable, Observable {
    private final Game game;
    private final ArrayList<RemoteViewController> views = new ArrayList<>();
    private GameStatus currentStatus;
    private String name;

    private final ArrayList<Listener> listeners = new ArrayList<>();

    /**
     * Getter Method for the Game
     * @return the Game
     */
    public Game getGame() {
        return game;
    }

    /**
     * Getter Method for the Game
     * @return the Game
     */
    public Player getPlayer(int index) {
        return game.getPlayer(index);
    }

    /**
     * Getter Method for the currentStatus
     * @return the currentStatus
     */
    public GameStatus getCurrentStatus() {
        return currentStatus;
    }

    /**
     * Setter Method for the currentStatus
     * @@param the currentStatus
     */
    public void setCurrentStatus(GameStatus currentStatus) {
        this.currentStatus = currentStatus;
    }

    /**
     * Getter Method for the GameController's name
     * @return the GameController's name
     * @throws RemoteException if a communication error occurs during the remote operation
     */
    public String getName() throws RemoteException {
        return name;
    }

    /**
     * Setter Method for the GameController's name
     * @param name the GameController's name
     * @throws RemoteException if a communication error occurs during the remote operation
     */
    public void setName(String name) throws RemoteException {
        this.name = name;
    }

    /**
     * Constructor Method for the GameController
     * @param name the GameController's Name
     * @throws RemoteException if a communication error occurs during the remote operation
     */
    public GameController(String name) throws RemoteException{
        this.name = name;
        this.game = new Game();
        currentStatus = GameStatus.NOT_IN_GAME;
        game.setListener(new LastTurnListener() {
            @Override
            public void onEvent() {
                currentStatus = GameStatus.LAST_TURN;
            }
        });
        // Check last turn for drawing and playing mechanics
        game.setListener(new SemiLastTurnListener() {
            @Override
            public void onEvent() {
                currentStatus = GameStatus.SEMI_LAST_TURN;
            }
        });
        game.getResourcePlayingDeck().getDeck().setListener(new DeckViewListener() {
            @Override
            public void onEvent() {
                for (RemoteViewController view : views) {
                    Thread thread = new Thread(() -> {
                        try {
                            view.notifyDeckChanged(CardType.RESOURCECARD);
                        } catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    thread.start();
                }
            }
        });
        game.getGoldPlayingDeck().getDeck().setListener(new DeckViewListener() {
            @Override
            public void onEvent() {
                for (RemoteViewController view : views) {
                    Thread thread = new Thread(() -> {
                    try {
                        view.notifyDeckChanged(CardType.GOLDCARD);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                    });
                    thread.start();
                }
            }
        });
        game.setListener(new ResourceSlot1Listener() {
            @Override
            public void onEvent() {
                for (RemoteViewController view : views) {
                    Thread thread = new Thread(() -> {
                        try {
                            view.notifySlotCardChanged(CardType.RESOURCECARD, 1);
                        } catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    thread.start();
                }
            }
        });
        game.setListener(new ResourceSlot2Listener() {
            @Override
            public void onEvent() {
                for (RemoteViewController view : views) {
                    Thread thread = new Thread(() -> {
                    try {
                        view.notifySlotCardChanged(CardType.RESOURCECARD, 2);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                    });
                    thread.start();
                }
            }
        });
        game.setListener(new GoldSlot1Listener() {
            @Override
            public void onEvent() {
                for (RemoteViewController view : views) {
                    Thread thread = new Thread(() -> {
                        try {
                            view.notifySlotCardChanged(CardType.GOLDCARD, 1);
                        } catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    thread.start();
                }
            }
        });
        game.setListener(new GoldSlot2Listener() {
            @Override
            public void onEvent() {
                for (RemoteViewController view : views) {
                    Thread thread = new Thread(() -> {
                    try {
                        view.notifySlotCardChanged(CardType.GOLDCARD, 2);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                    });
                    thread.start();
                }
            }
        });
        game.setListener(new CommonObjectivesListener() {
            @Override
            public void onEvent() {
                for (RemoteViewController view : views) {
                    Thread thread = new Thread(() -> {
                        try {
                            view.notifyCommonObjectivesChanged();
                        } catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    thread.start();
                }
            }
        });
        game.getChat().setListener(new Listener() {
            @Override
            public void onEvent() {
                for (RemoteViewController view: views) {
                    Thread thread = new Thread(() -> {
                        try {
                            ChatMessage message = game.getChat().getLastChatMessage();
                            view.notifyNewMessage(message);
                        } catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    thread.start();
                }
            }
        });
    }

    //TODO: add
    /**
     * Sets the currentStatus to READY
     */
    public void startGame() {
        setCurrentStatus(GameStatus.READY);
    }

    /**
     * Adds the specified Player to the Game and sets his listeners
     * @param player the Player to be added to the Game
     */
    public void addPlayer(Player player) {
        player.setListener(new StatusListener() {
            @Override
            public void onEvent() {
                checkIfGameCanContinue();
            }
        });
        player.setListener(new HandListener() {
            @Override
            public void onEvent() {
                for (RemoteViewController view : views) {
                    Thread thread = new Thread(() -> {
                        try {
                            view.notifyPlayersHandChanged(game.getIndexOfPlayer(player.getNickname()));
                        } catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    thread.start();
                }
            }
        });
        player.setListener(new SecretObjectiveListener() {
            @Override
            public void onEvent() {
                for (RemoteViewController view : views) {
                    Thread thread = new Thread(() -> {
                        try {
                            view.notifyPlayersObjectiveChanged(game.getIndexOfPlayer(player.getNickname()));
                        } catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    thread.start();
                }
            }
        });
        player.setListener(new TokenListener() {
            @Override
            public void onEvent() {
                for (RemoteViewController view : views) {
                    Thread thread = new Thread(() -> {
                        try {
                            view.notifyPlayersTokenChanged(game.getIndexOfPlayer(player.getNickname()));
                        } catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    thread.start();
                }
            }
        });
        player.setListener(new PointsListener() {
            @Override
            public void onEvent() {
                for (RemoteViewController view : views) {
                    Thread thread = new Thread(() -> {
                        try {
                            view.notifyPlayersPointsChanged(player.getToken(), player.getPoints());
                        } catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    thread.start();
                }
            }
        });
        player.getPlayField().setListener(new PlayAreaListener() {
            @Override
            public void onEvent() {
                for (RemoteViewController view: views) {
                    Thread thread = new Thread(() -> {
                        try {
                            view.notifyPlayersPlayAreaChanged(game.getIndexOfPlayer(player.getNickname()));
                        } catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    thread.start();
                }
            }
        });
        game.addPlayer(player);
        notifyListeners("Number of Players changed");
    }

    /**
     * Remove the specified Player from the Game
     * @param player the Player to be removed
     * @return {@code true} if the Player has been removed correctly
     */
    public boolean kickPlayer(Player player) {
        return game.kickPlayer(player);
    }

    /**
     * Passes the turn to the next Player.
     * Sets the status of the Game corresponding to the Game progress
     * Sets the status of the Game to "SEMI_LAST_TURN" if the Players have to play their penultimate turns.
     * Sets the status of the Game to "LAST_TURN" if the Players have to play their last turns.
     * Sets the status of the Game to "COUNTING_POINTS" to count the points of every Player.
     */
    public void nextTurn() {
        int turn = game.getPlayerTurn();
        if(getPlayer(turn).isFirst()) {
            if(currentStatus.equals(GameStatus.SEMI_LAST_TURN)){
                setCurrentStatus(GameStatus.LAST_TURN);
                game.getChat().sendMessage(new ChatMessage("This is the last turn, good luck!", "Server"));
                for (RemoteViewController view: views) {
                    Thread thread = new Thread(() -> {
                        try {
                            view.notifyLastTurn();
                        } catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    thread.start();
                }
            }else if (currentStatus.equals(GameStatus.LAST_TURN)){
                game.getChat().sendMessage(new ChatMessage("Game's over, let's see who is the winner!", "Server"));
                setCurrentStatus(GameStatus.COUNTING_POINTS);
                nextStatus();
            }
        }

        if (turn == game.getNumberOfPlayers()) {
            turn = 1;
        } else {
            turn++;
        }
        game.setPlayerTurn(turn);
        for (RemoteViewController view: views) {
            Thread thread = new Thread(() -> {
                try {
                    view.notifyTurnChanged();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            });
            thread.start();
        }
    }

    /**
     * The specified Player plays the Card at the specified index in the specified coordinates.
     * Then asks the Player to draw or grab a card from a Deck/Slot if there is still a card in a PlayingDeck slot
     * @param playerID the index of the Player who plays the Card
     * @param handCard the index of the Card to be Played
     * @param x the horizontal coordinate of the Card on the PlayField
     * @param y the vertical coordinate of the Card on the PlayField
     */
    public void playCard(int playerID, int handCard, int x, int y) {
        Player player = game.getPlayer(playerID);
        // TODO: test drawing in GameStatus.LAST_TURN
        try {
            if(player.equals(game.getCurrentPlayer())) {
                player.playCard(handCard, x, y);

                // Don's ask to grab or draw if there are no cards to be picked
                if(null != game.getResourcePlayingDeck().getSlot(1)
                        || null != game.getResourcePlayingDeck().getSlot(2)
                        || null != game.getGoldPlayingDeck().getSlot(1)
                        || null != game.getGoldPlayingDeck().getSlot(2)
                        || !game.isResourceDeckEmpty() || !game.isGoldDeckEmpty()){
                    for (RemoteViewController view : views) {
                        view.askToDrawOrGrab(playerID);
                    }
                } else {
                    nextTurn();
                }

            }else throw new IllegalActionException();
        } catch (IllegalPlacementException | PlacementConditionNotMetException | IllegalActionException e) {
            // TODO: Handle exception
            e.printStackTrace();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Flips the specified Card of the specified Player
     * @param playerID the index of Player to flip the Card from
     * @param cardID the index of the Card to flip
     */
    public void flipCard(int playerID, int cardID) {
        game.getPlayer(playerID).getHandCard(cardID).flip();
        for (RemoteViewController view: views) {
            try {
                view.notifyHandCardWasFlipped(playerID,cardID);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Each Player in the Game draws their StartingHand
     */
    public void drawStartingHand() {
        for (int i = 1; i <= game.getNumberOfPlayers(); i++) {
            game.getPlayer(i).drawStartingHand(game.getResourcePlayingDeck(), game.getGoldPlayingDeck());
        }
    }

    /**
     * calls Player.drawCard(playingDeck).
     * Then turn passes to the next Player
     * @param player: the player who draw the Card
     * @param playingDeck: the deck from where the Card is drawn
     */
    public void drawCard(Player player, PlayingDeck playingDeck){
        try {
            if(player.getNickname().equals((game.getCurrentPlayer().getNickname()))) {
                try {
                    player.drawCard(playingDeck);
                }catch (IllegalArgumentException e){
                    // Last turn and zero Cards in the decks... don't draw
                }
                nextTurn();
            } else throw new IllegalActionException();
        } catch (IllegalActionException e) {
            e.printStackTrace();
        }

    }

    /**
     * Grabs a Card from the specified slot and Puts it in the Player's hand, then puts a Card from the top of the Deck of the PlayingDeck in the now empty slot (calls player.grabCard(playingDeck, slot).
     * If the PlayingDeck is empty, puts in the slot a Card from the top of the other PlayingDeck (calls game.putDown(playingDeck, slot).
     * Then turn passes to the next Player
     * @param player:      the player who grabs the Card
     * @param playingDeck: the PlayingDeck associated to the Slots where the Player grabs the Card from
     * @param slot:        an int value to identify the slot to grab the Card from.
     */
    public void grabCard(Player player, PlayingDeck playingDeck, int slot) {
        try {
            if(player.equals(game.getCurrentPlayer())) {
                try{
                    player.grabCard(playingDeck, slot);
                    game.putDown(playingDeck, slot);
                }catch (IllegalArgumentException e){
                    // TODO: implement dialog
                    // Last turn and zero Cards on the table... don't grab
                }
                nextTurn();
            } else throw new IllegalActionException();
        } catch (IllegalActionException e) {
            e.printStackTrace();
        }
    }

    /**
     * Each Player draws two secret ObjectiveCard to choose from
     */
    public void drawSecretObjectives() {
        for (int i = 1; i <= game.getNumberOfPlayers(); i++) {
            game.getPlayer(i).drawSecretObjectives(game.getObjectivePlayingDeck());
        }
        for (RemoteViewController view : views) {
            try {
                view.showSecretObjectivesSelectionDialog();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Each Player draws the StarterCard and chooses the side of the StarterCard to play
     */
    public void beginStarterCardChoosing() {
        for (int i = 1; i <= game.getNumberOfPlayers(); i++) {
            game.getPlayer(i).drawTemporaryStarterCard(game.getStarterDeck());
        }
        for (RemoteViewController view : views) {
            try {
                view.showStarterCardSelectionDialog();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Starts token selection
     */
    public void beginTokenChoosing() {
        for (RemoteViewController view : views) {
            try {
                view.showTokenSelectionDialog();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void addView(RemoteViewController view) {
        views.add(view);
    }

    public void removeView(ViewController view) {
        views.remove(view);
    }

    /**
     * Checks if all the player are in the same Status to go to the next Status
     */
    private void checkIfGameCanContinue() {
        int readyPlayers = 0;
        for (int i = 1; i <= game.getNumberOfPlayers(); i++) {
            if (game.getPlayer(i).getStatus() == currentStatus) {
                readyPlayers++;
            }
        }
        if (readyPlayers == game.getNumberOfPlayers()) {
            nextStatus();
        }
    }

    private void setupViews() {
        for (RemoteViewController view: views) {
            try {
                view.getReady(getGame().getNumberOfPlayers());
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Changes the status of the Game
     */
    private void nextStatus() {
        switch (currentStatus) {
            case NOT_IN_GAME:
                break;
            case CONNECTING:
                break;
            case WAITING_FOR_SERVER:
                currentStatus = GameStatus.READY;
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                setupViews();
                break;
            case READY:
                currentStatus = GameStatus.READY_TO_CHOOSE_TOKEN;
                for (int i = 1; i <= game.getNumberOfPlayers(); i++) {
                    game.getPlayer(i).setStatus(GameStatus.READY_TO_CHOOSE_TOKEN);
                }
                break;
            case READY_TO_CHOOSE_TOKEN:
                currentStatus = GameStatus.READY_TO_CHOOSE_SECRET_OBJECTIVE;
                beginTokenChoosing();
                break;
            case READY_TO_CHOOSE_SECRET_OBJECTIVE:
                currentStatus = GameStatus.READY_TO_CHOOSE_STARTER_CARD;
                drawSecretObjectives();
                break;
            case READY_TO_CHOOSE_STARTER_CARD:
                currentStatus = GameStatus.READY_TO_DRAW_STARTING_HAND;
                beginStarterCardChoosing();
                break;
            case READY_TO_DRAW_STARTING_HAND:
                currentStatus = GameStatus.PLAYING;
                drawStartingHand();
                break;
            case PLAYING:
                game.getChat().sendMessage(new ChatMessage("Game is Starting", "Server"));
                for (int i = 0; i < game.getNumberOfPlayers(); i++) {
                    game.getPlayer(i+1).setStatus(GameStatus.NOT_MY_TURN);
                }
                nextTurn();
                break;
            case COUNTING_POINTS:
                ArrayList<HashMap<String, String>> points = game.countPoints();
                for (RemoteViewController view : views) {
                    try {
                        view.notifyEndGame(points);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                }
                currentStatus = GameStatus.END_GAME;
            case END_GAME:
                break;
            default:
                break;
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
        switch (context) {
            case "Number of Players changed" -> {
                for (RemoteViewController v: views) {
                    try {
                        v.notifyNumberOfPlayersChanged();
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }
}