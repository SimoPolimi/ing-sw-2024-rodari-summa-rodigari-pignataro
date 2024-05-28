package it.polimi.ingsw.gc42.controller;

import it.polimi.ingsw.gc42.model.classes.cards.Card;
import it.polimi.ingsw.gc42.model.classes.cards.CardSide;
import it.polimi.ingsw.gc42.model.classes.game.Player;
import it.polimi.ingsw.gc42.model.classes.game.Token;
import it.polimi.ingsw.gc42.model.exceptions.IllegalActionException;
import it.polimi.ingsw.gc42.model.exceptions.IllegalPlacementException;
import it.polimi.ingsw.gc42.model.exceptions.PlacementConditionNotMetException;
import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;

import static org.junit.jupiter.api.Assertions.*;

class GameControllerTest {

    @Test
    void startGame() throws RemoteException {
        GameController controller = new GameController("test");
        controller.startGame();

        assertEquals(controller.getCurrentStatus(), GameStatus.READY);
    }

    @Test
    void addPlayer() throws RemoteException {
        GameController controller = new GameController("test");
        Player player = new Player("bot");
        controller.addPlayer(player);

        assertEquals(controller.getPlayer(controller.getGame().getNumberOfPlayers()), player);
    }

    @Test
    void kickPlayer() throws RemoteException {
        GameController controller = new GameController("test");
        Player player = new Player("bot");
        controller.addPlayer(player);

        boolean kicked = controller.kickPlayer(player);

        assertEquals(controller.getGame().getNumberOfPlayers(), 0);
        assert(kicked);
    }

    @Test
    void playCard() throws RemoteException {
        // given
        GameController controller = new GameController("test");
        Player player = new Player("bot");
        controller.addPlayer(player);
        player.drawTemporaryStarterCard(controller.getGame().getStarterDeck());
        player.setStarterCard();
        player.drawStartingHand(controller.getGame().getResourcePlayingDeck(), controller.getGame().getGoldPlayingDeck());
        Card cardToBePlayed = player.getHandCard(0);

        // when
        controller.playCard(controller.getGame().getIndexOfPlayer("bot"), 1, 0, 1);

        // then
        assertEquals(player.getPlayField().getLastPlayedCard(), cardToBePlayed);
    }

    void dontGrabCardIfSlotsAreEmpty() throws RemoteException {
        // given
        GameController controller = new GameController("test");
        Player player = new Player("bot");
        controller.addPlayer(player);
        player.drawTemporaryStarterCard(controller.getGame().getStarterDeck());
        player.setStarterCard();
        player.drawStartingHand(controller.getGame().getResourcePlayingDeck(), controller.getGame().getGoldPlayingDeck());
        // Empty decks and empty slots
        while (controller.getGame().getResourcePlayingDeck().getDeck().getNumberOfCards() > 0) {
            controller.getGame().getResourcePlayingDeck().getDeck().draw();
        }
        while (controller.getGame().getGoldPlayingDeck().getDeck().getNumberOfCards() > 0) {
            controller.getGame().getGoldPlayingDeck().getDeck().draw();
        }
        controller.getGame().getResourcePlayingDeck().setSlot(null, 1);
        controller.getGame().getResourcePlayingDeck().setSlot(null, 2);
        controller.getGame().getGoldPlayingDeck().setSlot(null, 1);
        controller.getGame().getGoldPlayingDeck().setSlot(null, 2);

        // when
        controller.playCard(controller.getGame().getIndexOfPlayer("bot"), 1, 0, 1);

        // then
        assertEquals(player.getHandSize(), 2);
    }

    @Test
    void flipCard() throws RemoteException {
        // given
        GameController controller = new GameController("test");
        Player player = new Player("bot");
        controller.addPlayer(player);
        player.drawTemporaryStarterCard(controller.getGame().getStarterDeck());
        player.setStarterCard();
        player.drawStartingHand(controller.getGame().getResourcePlayingDeck(), controller.getGame().getGoldPlayingDeck());
        CardSide cardSide = player.getHandCard(0).getShowingSide();

        // when
        controller.flipCard(controller.getGame().getIndexOfPlayer("bot"), 0);

        // then
        assertNotEquals(player.getHandCard(0).getShowingSide(), cardSide);

    }

    @Test
    void drawStartingHand() {
    }

    @Test
    void grabCard() {
    }

    @Test
    void testIsPutDownResourceAfterGrabCard() {
        // given
        GameController controller;
        try {
            controller = new GameController("Test");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        Player player = new Player(Token.BLUE);
        controller.getGame().addPlayer(player);

        // when
        controller.grabCard(player, controller.getGame().getResourcePlayingDeck(), 1);

        // then
        assertNotNull(controller.getGame().getResourcePlayingDeck().getSlot(1));
        assertNotEquals(player.getHandCard(0), controller.getGame().getResourcePlayingDeck().getSlot(1));

    }

    @Test
    void testIsPutDownGoldAfterGrabCard() {
        // given
        GameController controller;
        try {
            controller = new GameController("Test");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        Player player = new Player(Token.BLUE);
        controller.getGame().addPlayer(player);

        // when
        controller.grabCard(player, controller.getGame().getGoldPlayingDeck(), 1);

        // then
        assertNotNull(controller.getGame().getGoldPlayingDeck().getSlot(1));
        assertNotEquals(player.getHandCard(0), controller.getGame().getGoldPlayingDeck().getSlot(1));
    }

    @Test
    void testCannotGrabCardEmptySlot() {
        // given
        GameController controller;
        try {
            controller = new GameController("Test");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
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
            player.playCard(player.getHandSize()-1,0,0);
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