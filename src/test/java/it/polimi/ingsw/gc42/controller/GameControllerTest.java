package it.polimi.ingsw.gc42.controller;

import it.polimi.ingsw.gc42.model.classes.game.Game;
import it.polimi.ingsw.gc42.model.classes.game.Player;
import it.polimi.ingsw.gc42.model.classes.cards.*;
import it.polimi.ingsw.gc42.model.classes.game.Token;
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

        // when
        controller.grabCard(player, controller.getGame().getResourcePlayingDeck(), 1);

        // then
        assertNotNull(controller.getGame().getResourcePlayingDeck().getSlot(1));
        assertNotEquals(player.getHandCard(0), controller.getGame().getResourcePlayingDeck().getSlot(1));

    }

    @Test
    void testCannotGrabCard() {
        // given
        GameController controller = new GameController();
        Player player = new Player(Token.BLUE);

        // when
        // Deck is empty
        int DeckSize = controller.getGame().getResourcePlayingDeck().getDeck().getNumberOfCards();
        while (controller.getGame().getResourcePlayingDeck().getDeck().getNumberOfCards() > 0) {
            controller.getGame().getResourcePlayingDeck().getDeck().draw();
        }
        while (controller.getGame().getGoldPlayingDeck().getDeck().getNumberOfCards() > 0) {
            controller.getGame().getGoldPlayingDeck().getDeck().draw();
        }
        controller.grabCard(player, controller.getGame().getResourcePlayingDeck(), 1);

        // then
        // No such Card grabbed
        // Todo: test
        assertThrowsExactly(IllegalArgumentException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                controller.grabCard(player, controller.getGame().getResourcePlayingDeck(), 1);
            }
        });
        // Deck is empty
        assertEquals(controller.getGame().getResourcePlayingDeck().getDeck().getNumberOfCards(), 0);
        assertNull(controller.getGame().getResourcePlayingDeck().getSlot(1));
    }
}