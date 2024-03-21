package it.polimi.ingsw.gc42.controller;

import it.polimi.ingsw.gc42.model.classes.cards.Card;
import it.polimi.ingsw.gc42.model.classes.game.Game;
import it.polimi.ingsw.gc42.model.classes.game.Player;

public class GameController {
    private final Game game;

    public Game getGame() {
        return game;
    }

    public GameController() {
        this.game = new Game();
    }

    public void startGame() {

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

    public void playCard(Card card, int x, int y) {
        Player player = game.getCurrentPlayer();
        player.playCard(card, x, y);
    }
}