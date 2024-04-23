package it.polimi.ingsw.gc42.controller;

import it.polimi.ingsw.gc42.model.classes.game.Game;
import it.polimi.ingsw.gc42.model.classes.game.Player;
import it.polimi.ingsw.gc42.model.classes.cards.*;
import it.polimi.ingsw.gc42.model.classes.game.Token;
import it.polimi.ingsw.gc42.model.exceptions.IllegalActionException;
import it.polimi.ingsw.gc42.model.exceptions.IllegalPlacementException;
import it.polimi.ingsw.gc42.model.exceptions.PlacementConditionNotMetException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.*;

class GameControllerTest {

    @Test
    void startGame() {
    }

    @Test
    void addPlayer() {
    }

    @Test
    void kickPlayer() {
    }

    @Test
    void playCard() {
    }

    @Test
    void flipCard() {
    }

    @Test
    void drawStartingHand() {
    }

    @Test
    void grabCard() {
    }

    @Test
    void testIsPutDownAfterGrabCard() {
        // given
        GameController controller = new GameController();
        Player player = new Player(Token.BLUE);
        controller.getGame().addPlayer(player);

        // when
        controller.grabCard(player, controller.getGame().getResourcePlayingDeck(), 1);

        // then
        assertNotNull(controller.getGame().getResourcePlayingDeck().getSlot(1));
        assertNotEquals(player.getHandCard(0), controller.getGame().getResourcePlayingDeck().getSlot(1));

    }

    @Test
    void testCannotGrabCardEmptySlot() {
        // given
        GameController controller = new GameController();
        Player player = new Player(Token.BLUE);
        controller.getGame().addPlayer(player);


        // when
        // LAST_TURN
        try{
            for (int i = 0; i < 2; i++) {
                player.drawCard(controller.getGame().getResourcePlayingDeck());
            }
        }catch (IllegalActionException e){
            e.printStackTrace();
        }
        // Deck is empty
        while (controller.getGame().getResourcePlayingDeck().getDeck().getNumberOfCards() > 0) {
            controller.getGame().getResourcePlayingDeck().getDeck().draw();
        }
        while (controller.getGame().getGoldPlayingDeck().getDeck().getNumberOfCards() > 0) {
            controller.getGame().getGoldPlayingDeck().getDeck().draw();
        }
        controller.grabCard(player, controller.getGame().getResourcePlayingDeck(), 1);

        try {
            player.playCard(player.getHandCard(player.getHandSize()-1),0,0);
        } catch (PlacementConditionNotMetException | IllegalPlacementException | IllegalActionException e){
            e.printStackTrace();
        }

        // then
        // No such Card grabbed
        controller.grabCard(player, controller.getGame().getResourcePlayingDeck(), 1);

        // Deck is empty
        assertEquals(player.getHandSize(), 2);
        assertEquals(controller.getGame().getResourcePlayingDeck().getDeck().getNumberOfCards(), 0);
        assertNull(controller.getGame().getResourcePlayingDeck().getSlot(1));
    }
}