package it.polimi.ingsw.gc42.controller;

import it.polimi.ingsw.gc42.model.classes.PlayingDeck;
import it.polimi.ingsw.gc42.model.classes.cards.CardType;
import it.polimi.ingsw.gc42.model.exceptions.IllegalActionException;
import it.polimi.ingsw.gc42.model.exceptions.IllegalPlacementException;
import it.polimi.ingsw.gc42.model.exceptions.PlacementConditionNotMetException;
import it.polimi.ingsw.gc42.view.Interfaces.ViewController;
import it.polimi.ingsw.gc42.model.classes.cards.Card;
import it.polimi.ingsw.gc42.model.classes.cards.PlayableCard;
import it.polimi.ingsw.gc42.model.classes.game.Game;
import it.polimi.ingsw.gc42.model.classes.game.Player;
import it.polimi.ingsw.gc42.model.interfaces.StatusListener;

import java.util.ArrayList;
import java.util.NoSuchElementException;

public class GameController {
    private final Game game;
    private final ArrayList<ViewController> views = new ArrayList<>();
    private GameStatus currentStatus;

    public Game getGame() {
        return game;
    }

    public GameStatus getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(GameStatus currentStatus) {
        this.currentStatus = currentStatus;
    }

    public GameController() {
        this.game = new Game();
        currentStatus = GameStatus.NOT_IN_GAME;
    }

    public void startGame() {

    }

    public void addPlayer(Player player) {
        player.setListener(new StatusListener() {
            @Override
            public void onEvent() {
                checkIfGameCanContinue();
            }
        });
        game.addPlayer(player);
    }

    public boolean kickPlayer(Player player) {
        return game.kickPlayer(player);
    }

    public void nextTurn() {
        try {
            game.getPlayer(game.getPlayerTurn()).setStatus(GameStatus.NOT_MY_TURN);
            if(game.getPlayerTurn() >= game.getNumberOfPlayers()){
                game.setPlayerTurn(1);
                game.getPlayer(1).setStatus(GameStatus.MY_TURN);
                System.out.println(game.getPlayerTurn());
            }else{
                game.getPlayer(game.getPlayerTurn()+1).setStatus(GameStatus.MY_TURN);
                game.setPlayerTurn(game.getPlayerTurn() + 1);
                System.out.println(game.getPlayerTurn());
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public void playCard(PlayableCard card, int x, int y) {
        Player player = game.getCurrentPlayer();
        try {
            player.playCard(card, x, y);
            for (ViewController view : views) {
                if (view.getOwner().equals(player)) {
                    view.askToDrawOrGrab();
                }
            }
        } catch (IllegalPlacementException | PlacementConditionNotMetException e) {
            // TODO: Handle exception
            e.printStackTrace();
        }

    }

    public void flipCard(Card card) {
        card.flip();
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
            player.drawCard(playingDeck);
            // End turn!!!!!!!!!!!!
            nextTurn();
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
            player.grabCard(playingDeck, slot);
            game.putDown(playingDeck, slot);
            // End turn!!!!!!!!!!!!
            nextTurn();
        } catch (IllegalActionException e) {
            e.printStackTrace();
        }
    }

    public void drawSecretObjectives() {
        for (ViewController view : views) {
            view.getOwner().drawSecretObjectives(game.getObjectivePlayingDeck());
            view.showSecretObjectivesSelectionDialog();
        }
    }

    public void beginStarterCardChoosing() {
        for (ViewController view : views) {
            view.showStarterCardSelectionDialog();
        }
    }

    public void beginTokenChoosing() {
        for (ViewController view : views) {
            view.showTokenSelectionDialog();
        }
    }

    public void addView(ViewController view) {
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
                startGame();
                break;
            case READY:
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
            case COUNTING_POINTS:
                break;
            case END_GAME:
                break;
            default:
                break;
        }
    }
}