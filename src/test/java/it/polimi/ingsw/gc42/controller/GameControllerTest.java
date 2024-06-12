package it.polimi.ingsw.gc42.controller;

import it.polimi.ingsw.gc42.model.classes.cards.Card;
import it.polimi.ingsw.gc42.model.classes.cards.CardSide;
import it.polimi.ingsw.gc42.model.classes.cards.GoldCard;
import it.polimi.ingsw.gc42.model.classes.cards.ResourceCard;
import it.polimi.ingsw.gc42.model.classes.game.Game;
import it.polimi.ingsw.gc42.model.classes.game.Player;
import it.polimi.ingsw.gc42.model.classes.game.Token;
import it.polimi.ingsw.gc42.model.exceptions.IllegalActionException;
import it.polimi.ingsw.gc42.model.exceptions.IllegalPlacementException;
import it.polimi.ingsw.gc42.model.exceptions.PlacementConditionNotMetException;
import it.polimi.ingsw.gc42.model.interfaces.Listener;
import it.polimi.ingsw.gc42.network.ClientController;
import it.polimi.ingsw.gc42.view.Interfaces.ViewController;
import javafx.event.ActionEvent;
import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;

import static org.junit.jupiter.api.Assertions.*;

class GameControllerTest {

    @Test
    void nameIsSet() throws RemoteException {
        GameController controller = new GameController("test");
        assertEquals(controller.getName(), "test");

        controller.setName("changed");
        assertEquals(controller.getName(), "changed");
    }

    @Test
    void drawCard() throws RemoteException {
        // given
        GameController controller = new GameController("test");
        Player player = new Player("bot1", true, 0, null, null, controller.getGame());
        // Other player to test the change of turn
        Player player2 = new Player("bot2", false, 0, null, null, controller.getGame());
        controller.addPlayer(player);
        controller.addPlayer(player2);
        Card topCard = null;

        // This way only player (bot1) draws
        try{
            player.drawCard(controller.getGame().getResourcePlayingDeck());
            player.drawCard(controller.getGame().getResourcePlayingDeck());
        }catch (IllegalActionException e){
            e.printStackTrace();
        }
        topCard = controller.getGame().getResourcePlayingDeck().getDeck().getTopCard();
        // when
        controller.drawCard(controller.getGame().getCurrentPlayer(), controller.getGame().getResourcePlayingDeck());

        // then
        // Player has 3 Cards
        for (int i = 0; i < 3; i++) {
            assertNotNull(player.getHandCard(i));
        }
        // Card in Hand
        boolean containsTheCard = false;
        for (int i = 0; i < player.getHandSize(); i++) {
            if (player.getHandCard(i).equals(topCard)) {
                containsTheCard = true;
            }
        }
        assertTrue(containsTheCard);

        // TopCard is drawn
        assertNotEquals(controller.getGame().getResourcePlayingDeck().getDeck().getTopCard(), topCard);

        // nextTurn
        assertEquals(controller.getGame().getCurrentPlayer(),player2);
    }

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
        assertEquals(controller.getPlayer(controller.getGame().getNumberOfPlayers()).getNickname(), "bot");
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

    @Test
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
    void drawStartingHand() throws RemoteException {
        // given
        GameController controller = new GameController("test");
        Player player = new Player("bot1", true, 0, null, null, controller.getGame());
        Player player2 = new Player("bot2", false, 0, null, null, controller.getGame());
        controller.addPlayer(player);
        controller.addPlayer(player2);

        // when
        controller.drawStartingHand();

        // then
        for(int i=0; i<controller.getGame().getNumberOfPlayers(); i++){
            for (int j = 0; j < 3; j++) {
                assertNotNull(controller.getPlayer(i+1).getHandCard(j));
            }
            int resourceCount = 0;
            int goldCardCount = 0;
            for (int j = 0; j < 3; j++) {
                if (player.getHandCard(j) instanceof ResourceCard) {
                    resourceCount++;
                } else if (player.getHandCard(j) instanceof GoldCard) {
                    goldCardCount++;
                }
            }
            assertEquals(resourceCount, 2);
            assertEquals(goldCardCount, 1);
        }
    }

    @Test
    void drawSecretObjectives() throws RemoteException {
        // given
        GameController controller = new GameController("test");
        Player player = new Player("bot1", true, 0, null, null, controller.getGame());
        Player player2 = new Player("bot2", false, 0, null, null, controller.getGame());
        controller.addPlayer(player);
        controller.addPlayer(player2);

        // when
        controller.drawSecretObjectives();

        // then
        // TODO: add more things to check?
        for(int i=0; i<controller.getGame().getNumberOfPlayers(); i++){
            assertEquals(controller.getPlayer(i+1).getTemporaryObjectiveCards().size(), 2);
        }
    }

    @Test
    void beginStarterCardChoosing() throws RemoteException {
        // given
        GameController controller = new GameController("test");
        Player player = new Player("bot1", true, 0, null, null, controller.getGame());
        Player player2 = new Player("bot2", false, 0, null, null, controller.getGame());
        controller.addPlayer(player);
        controller.addPlayer(player2);

        // when
        controller.beginStarterCardChoosing();

        // then
        // TODO: add more things to check?
        for(int i=0; i<controller.getGame().getNumberOfPlayers(); i++){
            assertNotNull(controller.getPlayer(i+1).getTemporaryStarterCard());
        }
    }

    @Test
    void grabCard() throws RemoteException {
        // given
        GameController controller = new GameController("test");
        Player player = new Player("bot1", true, 0, null, null, controller.getGame());
        // Other player to test the change of turn
        Player player2 = new Player("bot2", false, 0, null, null, controller.getGame());
        controller.addPlayer(player);
        controller.addPlayer(player2);

        // This way only player (bot1) draws
        try{
            player.drawCard(controller.getGame().getResourcePlayingDeck());
            player.drawCard(controller.getGame().getResourcePlayingDeck());
        }catch (IllegalActionException e){
            e.printStackTrace();
        }
        Card slotCard = controller.getGame().getResourcePlayingDeck().getSlot(1);

        // when
        controller.grabCard(controller.getGame().getCurrentPlayer(), controller.getGame().getResourcePlayingDeck(), 1);

        // then
        boolean containsTheCard = false;
        for (int i = 0; i < player.getHandSize(); i++) {
            if (player.getHandCard(i).equals(slotCard)) {
                containsTheCard = true;
            }
        }
        assertTrue(containsTheCard);
        assertNotEquals(controller.getGame().getResourcePlayingDeck().getSlot(1), slotCard);
        assertNotEquals(controller.getGame().getResourcePlayingDeck().getSlot(2), slotCard);

        // nextTurn
        assertEquals(controller.getGame().getCurrentPlayer(),player2);
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

    @Test
    void beginTokenChoosing() throws RemoteException {
        GameController controller = new GameController("test");
        assertDoesNotThrow(()->controller.beginTokenChoosing());
    }
}