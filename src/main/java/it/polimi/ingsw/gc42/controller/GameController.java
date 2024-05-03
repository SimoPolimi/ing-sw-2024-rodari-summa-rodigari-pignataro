package it.polimi.ingsw.gc42.controller;

import it.polimi.ingsw.gc42.model.classes.PlayingDeck;
import it.polimi.ingsw.gc42.model.classes.cards.CardType;
import it.polimi.ingsw.gc42.model.exceptions.IllegalActionException;
import it.polimi.ingsw.gc42.model.exceptions.IllegalPlacementException;
import it.polimi.ingsw.gc42.model.exceptions.PlacementConditionNotMetException;
import it.polimi.ingsw.gc42.model.interfaces.*;
import it.polimi.ingsw.gc42.network.RemoteViewController;
import it.polimi.ingsw.gc42.view.Interfaces.DeckViewListener;
import it.polimi.ingsw.gc42.view.Interfaces.ViewController;
import it.polimi.ingsw.gc42.model.classes.cards.Card;
import it.polimi.ingsw.gc42.model.classes.game.Game;
import it.polimi.ingsw.gc42.model.classes.game.Player;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class GameController implements Serializable, Observable {
    private final Game game;
    private final ArrayList<RemoteViewController> views = new ArrayList<>();
    private GameStatus currentStatus;
    private String name;

    private final ArrayList<Listener> listeners = new ArrayList<>();

    public Game getGame() {
        return game;
    }

    public Player getPlayer(int index) {
        return game.getPlayer(index);
    }

    public GameStatus getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(GameStatus currentStatus) {
        this.currentStatus = currentStatus;
    }

    public String getName() throws RemoteException {
        return name;
    }

    public void setName(String name) throws RemoteException {
        this.name = name;
    }

    public GameController(String name) throws RemoteException{
        this.name = name;
        this.game = new Game();
        currentStatus = GameStatus.NOT_IN_GAME;
        // Check last turn for drawing and playing mechanics
        game.setListener(new LastTurnListener() {
            @Override
            public void onEvent() {
                currentStatus = GameStatus.LAST_TURN;
            }
        });
        game.getResourcePlayingDeck().getDeck().setListener(new DeckViewListener() {
            @Override
            public void onEvent() {
                for (RemoteViewController view : views) {
                    try {
                        view.notifyDeckChanged(CardType.RESOURCECARD);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        game.getGoldPlayingDeck().getDeck().setListener(new DeckViewListener() {
            @Override
            public void onEvent() {
                for (RemoteViewController view : views) {
                    try {
                        view.notifyDeckChanged(CardType.GOLDCARD);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        game.setListener(new ResourceSlot1Listener() {
            @Override
            public void onEvent() {
                for (RemoteViewController view : views) {
                    try {
                        view.notifySlotCardChanged(CardType.RESOURCECARD, 1);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        game.setListener(new ResourceSlot2Listener() {
            @Override
            public void onEvent() {
                for (RemoteViewController view : views) {
                    try {
                        view.notifySlotCardChanged(CardType.RESOURCECARD, 2);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        game.setListener(new GoldSlot1Listener() {
            @Override
            public void onEvent() {
                for (RemoteViewController view : views) {
                    try {
                        view.notifySlotCardChanged(CardType.GOLDCARD, 1);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        game.setListener(new GoldSlot2Listener() {
            @Override
            public void onEvent() {
                for (RemoteViewController view : views) {
                    try {
                        view.notifySlotCardChanged(CardType.GOLDCARD, 2);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        game.setListener(new CommonObjectivesListener() {
            @Override
            public void onEvent() {
                for (RemoteViewController view : views) {
                    try {
                        view.notifyCommonObjectivesChanged();
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

    public void startGame() {
        setCurrentStatus(GameStatus.READY);
    }

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
                    try {
                        view.notifyPlayersHandChanged(game.getIndexOfPlayer(player.getNickname()));
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        player.setListener(new SecretObjectiveListener() {
            @Override
            public void onEvent() {
                for (RemoteViewController view : views) {
                    try {
                        view.notifyPlayersObjectiveChanged(game.getIndexOfPlayer(player.getNickname()));
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        player.setListener(new TokenListener() {
            @Override
            public void onEvent() {
                for (RemoteViewController view : views) {
                    try {
                        view.notifyPlayersTokenChanged(game.getIndexOfPlayer(player.getNickname()));
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        player.setListener(new PointsListener() {
            @Override
            public void onEvent() {
                for (RemoteViewController view : views) {
                    try {
                        view.notifyPlayersPointsChanged();
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        player.getPlayField().setListener(new PlayAreaListener() {
            @Override
            public void onEvent() {
                for (RemoteViewController view: views) {
                    try {
                        view.notifyPlayersPlayAreaChanged(game.getIndexOfPlayer(player.getNickname()));
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        game.addPlayer(player);
        notifyListeners("Number of Players changed");
    }

    public boolean kickPlayer(Player player) {
        return game.kickPlayer(player);
    }

    public void nextTurn() {
        int turn = game.getPlayerTurn();
        if (turn == game.getNumberOfPlayers()) {
            turn = 1;
        } else {
            turn++;
        }
        game.setPlayerTurn(turn);
        for (RemoteViewController view: views) {
            try {
                view.notifyTurnChanged();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
        /*try {
            game.getPlayer(game.getPlayerTurn()).setStatus(GameStatus.NOT_MY_TURN);
            if(game.getPlayerTurn() >= game.getNumberOfPlayers()){
                game.setPlayerTurn(1);
                game.getPlayer(1).setStatus(GameStatus.MY_TURN);
            }else{
                game.getPlayer(game.getPlayerTurn()+1).setStatus(GameStatus.MY_TURN);
                game.setPlayerTurn(game.getPlayerTurn() + 1);
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }*/
    }

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
                        if (null != view.getOwner() && view.getOwner().getNickname().equals(player.getNickname())) {
                            view.askToDrawOrGrab();
                        }
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
                    // TODO: implement dialog
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
                view.getReady();
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
                for (int i = 0; i < game.getNumberOfPlayers(); i++) {
                    game.getPlayer(i+1).setStatus(GameStatus.NOT_MY_TURN);
                }
                nextTurn();
            case COUNTING_POINTS:
                break;
            case END_GAME:
                break;
            default:
                break;
        }
    }

    //TODO: remove after test
    public String test(int a) throws RemoteException{
        System.out.println("Metodo chiamato dal client");
        return "Sono il GameController";
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