package it.polimi.ingsw.gc42.controller;

import it.polimi.ingsw.gc42.model.classes.PlayingDeck;
import it.polimi.ingsw.gc42.model.classes.cards.CardType;
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
            game.setPlayerTurn(game.getPlayerTurn() + 1);
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
     * Returns the Card from one of the Slots AND removes it from there, drawing another Card to put on its place.
     *
     * @param player:      the player who grabs the Card
     * @param playingDeck: the PlayingDeck associated to the Slots where the Player grabs the Card from
     * @param slot:        an int value to identify the slot to grab the Card from.
     * @return the Card contained in that Slot.
     */
    public void grabCard(Player player, PlayingDeck playingDeck, int slot) {
        try {
            Card card = null;
            if (slot > 0 && slot < 3) {
                // Fill
                // Resource
                if (playingDeck.getDeck().getCardType().equals(CardType.RESOURCECARD)) {
                    card = game.getResourcePlayingDeck().getSlot(slot);
                    if (playingDeck.getDeck().getNumberOfCards() > 0) {
                        // Grab from ResourcePlayingDeck
                        playingDeck.setSlot(game.getResourcePlayingDeck().getDeck().draw(), slot);
                    } else {
                        if (game.getGoldPlayingDeck().getDeck().getNumberOfCards() > 0) {
                            // Grab from GoldPlayingDeck
                            playingDeck.setSlot(game.getGoldPlayingDeck().getDeck().draw(), slot);
                        } else {
                            // Both Decks are empty
                            playingDeck.setSlot(null, slot);
                        }
                    }
                } else if (playingDeck.getDeck().getCardType().equals(CardType.GOLDCARD)) {
                    // Gold
                    card = game.getGoldPlayingDeck().getSlot(slot);
                    if (playingDeck.getDeck().getNumberOfCards() > 0) {
                        // Grab from GoldPlayingDeck
                        playingDeck.setSlot(game.getGoldPlayingDeck().getDeck().draw(), slot);
                    } else {
                        if (game.getResourcePlayingDeck().getDeck().getNumberOfCards() > 0) {
                            // Grab from ResourcePlayingDeck
                            playingDeck.setSlot(game.getResourcePlayingDeck().getDeck().draw(), slot);
                        } else {
                            // Both Decks are empty
                            playingDeck.setSlot(null, slot);
                        }
                    }
                } else throw new IllegalArgumentException("There is no such Deck in this Game");
            } else throw new IllegalArgumentException("There is no such slot in this PlayingDeck");

            if (card != null) {
                // Add to Player's hand
                game.getPlayer(player.getToken()).addHandCard((PlayableCard) card);
                return;
            } else {
                throw new IllegalArgumentException("Empty slot");
            }
        } catch (NoSuchElementException e) {
            return;
        }
    }

    public void drawSecretObjectives() {
        for (ViewController view : views) {
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
                beginTokenChoosing();
                currentStatus = GameStatus.READY_TO_CHOOSE_SECRET_OBJECTIVE;
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