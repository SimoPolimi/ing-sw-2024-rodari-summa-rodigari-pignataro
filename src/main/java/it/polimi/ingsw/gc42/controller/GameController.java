package it.polimi.ingsw.gc42.controller;

import it.polimi.ingsw.gc42.view.Interfaces.ViewController;
import it.polimi.ingsw.gc42.model.classes.cards.Card;
import it.polimi.ingsw.gc42.model.classes.cards.PlayableCard;
import it.polimi.ingsw.gc42.model.classes.game.Game;
import it.polimi.ingsw.gc42.model.classes.game.Player;
import it.polimi.ingsw.gc42.model.interfaces.StatusListener;

import java.util.ArrayList;

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
        game.setPlayerTurn(game.getPlayerTurn()+1);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public void playCard(PlayableCard card, int x, int y) {
        Player player = game.getCurrentPlayer();
        player.playCard(card, x, y);
        //TODO: Make the player choose what to draw from
        player.drawCard(game.getResourcePlayingDeck());
    }

    public void flipCard(Card card) {
        card.flip();
    }

    public void drawStartingHand() {
        for (int i = 1; i <= game.getNumberOfPlayers(); i++) {
            game.getPlayer(i).drawStartingHand(game.getResourcePlayingDeck(), game.getGoldPlayingDeck());
        }
    }

    public void drawSecretObjectives() {
        for (ViewController view: views) {
            view.showSecretObjectivesSelectionDialog();
        }
    }

    public void beginStarterCardChoosing() {
        for (ViewController view: views) {
            view.showStarterCardSelectionDialog();
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
            case READY_TO_CHOOSE_SECRET_OBJECTIVE:
                drawSecretObjectives();
                currentStatus = GameStatus.READY_TO_CHOOSE_STARTER_CARD;
                break;
            case READY_TO_CHOOSE_STARTER_CARD:
                beginStarterCardChoosing();
                currentStatus = GameStatus.READY_TO_DRAW_STARTING_HAND;
                break;
            case READY_TO_DRAW_STARTING_HAND:
                drawStartingHand();
                currentStatus = GameStatus.PLAYING;
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