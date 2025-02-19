package it.polimi.ingsw.gc42.controller;

import it.polimi.ingsw.gc42.model.classes.cards.*;
import it.polimi.ingsw.gc42.model.classes.game.ChatMessage;
import it.polimi.ingsw.gc42.model.classes.game.Game;
import it.polimi.ingsw.gc42.model.classes.game.Player;
import it.polimi.ingsw.gc42.model.classes.game.Token;
import it.polimi.ingsw.gc42.model.exceptions.IllegalActionException;
import it.polimi.ingsw.gc42.model.exceptions.IllegalPlacementException;
import it.polimi.ingsw.gc42.model.exceptions.PlacementConditionNotMetException;
import it.polimi.ingsw.gc42.model.interfaces.*;
import it.polimi.ingsw.gc42.network.interfaces.RemoteViewController;
import it.polimi.ingsw.gc42.view.Interfaces.DeckViewListener;
import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
        Player player = new Player("bot1", true, 0, null, null);
        // Other player to test the change of turn
        Player player2 = new Player("bot2", false, 0, null, null);
        controller.addPlayer(player);
        controller.addPlayer(player2);
        Card topCard;

        // This way only player (bot1) draws
        try{
            player.drawCard(controller.getGame().getResourcePlayingDeck());
            player.drawCard(controller.getGame().getResourcePlayingDeck());
        }catch (IllegalActionException e){
            e.printStackTrace();
        }
        topCard = controller.getGame().getResourcePlayingDeck().getDeck().getTopCard();
        // when
        controller.getGame().setPlayerTurn(1);

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
        controller.getGame().setPlayerTurn(1);

        // when
        controller.playCard(controller.getGame().getIndexOfPlayer("bot"), 1, 0, 1);

        // then
        assertEquals(player.getPlayField().getLastPlayedCard(), cardToBePlayed);
    }

    // Last turn testing
    @Test
    void dontGrabCardIfSlotsAreEmpty_LastTurn() throws RemoteException {
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

        controller.getGame().setPlayerTurn(1);
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
        Player player = new Player("bot1", true, 0, null, null);
        Player player2 = new Player("bot2", false, 0, null, null);
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
        Player player = new Player("bot1", true, 0, null, null);
        Player player2 = new Player("bot2", false, 0, null, null);
        controller.addPlayer(player);
        controller.addPlayer(player2);

        // when
        controller.drawSecretObjectives();

        // then
        // Every player drew their secret ObjectiveCards to choose from
        for(int i=0; i<controller.getGame().getNumberOfPlayers(); i++){
            assertEquals(controller.getPlayer(i+1).getTemporaryObjectiveCards().size(), 2);
        }
    }

    @Test
    void beginStarterCardChoosing() throws RemoteException {
        // given
        GameController controller = new GameController("test");
        Player player = new Player("bot1", true, 0, null, null);
        Player player2 = new Player("bot2", false, 0, null, null);
        controller.addPlayer(player);
        controller.addPlayer(player2);

        // when
        controller.beginStarterCardChoosing();

        // then
        // Every Player drew their StarterCard
        for(int i=0; i<controller.getGame().getNumberOfPlayers(); i++){
            assertNotNull(controller.getPlayer(i+1).getTemporaryStarterCard());
        }
    }

    @Test
    void grabCard() throws RemoteException {
        // given
        GameController controller = new GameController("test");
        Player player = new Player("bot1", true, 0, null, null);
        // Other player to test the change of turn
        Player player2 = new Player("bot2", false, 0, null, null);
        controller.addPlayer(player);
        controller.addPlayer(player2);

        controller.getGame().setPlayerTurn(1);


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
        Player player = new Player("bot");
        controller.getGame().addPlayer(player);
        controller.getGame().setPlayerTurn(1);


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
        Player player = new Player("bot");
        controller.getGame().addPlayer(player);
        controller.getGame().setPlayerTurn(1);

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
        player.setSecretObjective((ObjectiveCard) controller.getGame().getObjectivePlayingDeck().getDeck().draw());
        controller.getGame().addPlayer(player);


        // when
        // LAST_TURN
        controller.getGame().setPlayerTurn(1);

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

        // when
        // No such Card grabbed (caught)
        controller.grabCard(player, controller.getGame().getResourcePlayingDeck(), 1);

        // then
        // No Card Grabbed
        assertEquals(player.getHandSize(), 2);
        assertEquals(controller.getGame().getResourcePlayingDeck().getDeck().getNumberOfCards(), 0);
        assertNull(controller.getGame().getResourcePlayingDeck().getSlot(1));
    }

    @Test
    void beginTokenChoosing() throws RemoteException {
        GameController controller = new GameController("test");
        assertDoesNotThrow(()->controller.beginTokenChoosing());
    }

    @Test
    void listenerTest() throws RemoteException, IllegalActionException {
        // given
        GameController controller = new GameController("test");
        Player player = new Player("bot1", true, 0, null, null);
        controller.addPlayer(player);
        // New view empty just to test listeners
        controller.addView(new RemoteViewController() {
            @Override
            public int getOwner() throws RemoteException {
                return 1;
            }

            @Override
            public void showSecretObjectivesSelectionDialog() throws RemoteException {}
            @Override
            public void showStarterCardSelectionDialog() throws RemoteException {}
            @Override
            public void showTokenSelectionDialog() throws RemoteException {}
            @Override
            public void askToDrawOrGrab(int playerID) throws RemoteException {}
            @Override
            public void notifyGameIsStarting() throws RemoteException {}
            @Override
            public void notifyDeckChanged(CardType type) throws RemoteException {}
            @Override
            public void notifySlotCardChanged(CardType type, int slot) throws RemoteException {}
            @Override
            public void notifyPlayersPointsChanged(Token token, int newPoints) throws RemoteException {}
            @Override
            public void notifyNumberOfPlayersChanged() throws RemoteException {}
            @Override
            public void notifyPlayersTokenChanged(int playerID) throws RemoteException {}
            @Override
            public void notifyPlayersPlayAreaChanged(int playerID) throws RemoteException {}
            @Override
            public void notifyPlayersHandChanged(int playerID) throws RemoteException {}
            @Override
            public void notifyHandCardWasFlipped(int playedID, int cardID) throws RemoteException {}
            @Override
            public void notifyPlayersObjectiveChanged(int playerID) throws RemoteException {}
            @Override
            public void notifyCommonObjectivesChanged() throws RemoteException {}
            @Override
            public void notifyTurnChanged() throws RemoteException {}
            @Override
            public void getReady(int numberOfPlayers) throws RemoteException {}
            @Override
            public void notifyLastTurn() throws RemoteException {}
            @Override
            public void notifyEndGame(ArrayList<HashMap<String, String>> points) throws RemoteException {}
            @Override
            public void notifyNewMessage(ChatMessage message) throws RemoteException {}
        });
        // Map because even if it's final, its elements can still be modified
        final Map<String, Boolean> isListenerNotified = new HashMap<>();
        String[] listenerName = {"resourceDeckNotified", "goldDeckNotified", "resourceSlot1Notified", "resourceSlot2Notified", "goldSlot1Notified", "goldSlot2Notified", "commonObjectivesNotified", "chatNotified", "playerHandNotified", "playerSecretObjectiveNotified", "playerTokenNotified", "playerPointsNotified", "playerPlayAreaNotified"};
        for(String listener: listenerName){
            isListenerNotified.put(listener, false);
        }

        controller.getGame().getResourcePlayingDeck().getDeck().setListener(new DeckViewListener() {
            @Override
            public void onEvent() {
                isListenerNotified.put("resourceDeckNotified", true);
            }
        });

        controller.getGame().getGoldPlayingDeck().getDeck().setListener(new DeckViewListener() {
            @Override
            public void onEvent() {
                isListenerNotified.put("goldDeckNotified", true);
            }
        });

        controller.getGame().setListener(new ResourceSlot1Listener() {
            @Override
            public void onEvent() {
                isListenerNotified.put("resourceSlot1Notified", true);
            }
        });

        controller.getGame().setListener(new ResourceSlot2Listener() {
            @Override
            public void onEvent() {
                isListenerNotified.put("resourceSlot2Notified", true);
            }
        });

        controller.getGame().setListener(new GoldSlot1Listener() {
            @Override
            public void onEvent() {
                isListenerNotified.put("goldSlot1Notified", true);
            }
        });

        controller.getGame().setListener(new GoldSlot2Listener() {
            @Override
            public void onEvent() {
                isListenerNotified.put("goldSlot2Notified", true);
            }
        });

        controller.getGame().setListener(new CommonObjectivesListener() {
            @Override
            public void onEvent() {
                isListenerNotified.put("commonObjectivesNotified", true);
            }
        });

        controller.getGame().getChat().setListener(new Listener() {
            @Override
            public void onEvent() {
                isListenerNotified.put("chatNotified", true);
            }
        });

        player.setListener(new HandListener() {
            @Override
            public void onEvent() {
                isListenerNotified.put("playerHandNotified", true);
            }
        });

        player.setListener(new SecretObjectiveListener() {
            @Override
            public void onEvent() {
                isListenerNotified.put("playerSecretObjectiveNotified", true);
            }
        });

        player.setListener(new TokenListener() {
            @Override
            public void onEvent() {
                isListenerNotified.put("playerTokenNotified", true);
            }
        });

        player.setListener(new PointsListener() {
            @Override
            public void onEvent() {
                isListenerNotified.put("playerPointsNotified", true);
            }
        });

        player.getPlayField().setListener(new PlayAreaListener() {
            @Override
            public void onEvent() {
                isListenerNotified.put("playerPlayAreaNotified", true);
            }
        });

        // when
        controller.getGame().getResourcePlayingDeck().getDeck().draw();
        controller.getGame().getGoldPlayingDeck().getDeck().draw();
        controller.getGame().putDown(controller.getGame().getResourcePlayingDeck(), 1);
        controller.getGame().putDown(controller.getGame().getResourcePlayingDeck(), 2);
        controller.getGame().putDown(controller.getGame().getGoldPlayingDeck(), 1);
        controller.getGame().putDown(controller.getGame().getGoldPlayingDeck(), 2);
        controller.getGame().getChat().sendMessage(new ChatMessage("text", player.getNickname()));
        player.drawCard(controller.getGame().getResourcePlayingDeck());
        player.setSecretObjective((ObjectiveCard) controller.getGame().getObjectivePlayingDeck().getDeck().draw());
        player.setToken(Token.BLUE);
        player.setPoints(10);
        player.drawTemporaryStarterCard(controller.getGame().getStarterDeck());
        player.setStarterCard();

        assertTrue(isListenerNotified.get("resourceDeckNotified"));
        assertTrue(isListenerNotified.get("goldDeckNotified"));
        assertTrue(isListenerNotified.get("resourceSlot1Notified"));
        assertTrue(isListenerNotified.get("resourceSlot2Notified"));
        assertTrue(isListenerNotified.get("goldSlot1Notified"));
        assertTrue(isListenerNotified.get("goldSlot2Notified"));
        // This one is notified when the game is created aka when the controller is created
        //assertTrue(isListenerNotified.get("commonObjectivesNotified"));
        assertTrue(isListenerNotified.get("chatNotified"));
        assertTrue(isListenerNotified.get("playerHandNotified"));
        assertTrue(isListenerNotified.get("playerSecretObjectiveNotified"));
        assertTrue(isListenerNotified.get("playerTokenNotified"));
        assertTrue(isListenerNotified.get("playerPointsNotified"));
        assertTrue(isListenerNotified.get("playerPlayAreaNotified"));
    }

    @Test
    void testGameEndedBecauseOfEmptyDecks() throws RemoteException {
        // Tests if the Game correctly ends in a situation where both Decks become empty
        GameController controller = new GameController("Test");

        controller.addPlayer(new Player("Bot1"));
        controller.addPlayer(new Player("Bot2"));
        controller.addPlayer(new Player("Bot3"));
        controller.addPlayer(new Player("Bot4"));

        controller.getPlayer(1).setToken(Token.BLUE);
        controller.getPlayer(2).setToken(Token.RED);
        controller.getPlayer(3).setToken(Token.GREEN);
        controller.getPlayer(4).setToken(Token.YELLOW);

        controller.setCurrentStatus(GameStatus.PLAYING);
        for (int i = 1; i < 5; i++) {
            Player p = controller.getPlayer(i);
            p.drawSecretObjectives(controller.getGame().getObjectivePlayingDeck());
            p.setSecretObjective(controller.getPlayer(i).getTemporaryObjectiveCards().getFirst());
            p.drawTemporaryStarterCard(controller.getGame().getStarterDeck());
            p.getTemporaryStarterCard().flip();
            p.setTemporaryStarterCard(p.getTemporaryStarterCard());
            p.drawStartingHand(controller.getGame().getResourcePlayingDeck(), controller.getGame().getGoldPlayingDeck());
            p.setStatus(GameStatus.PLAYING);
        }

        // Emptying the Decks
        while (!controller.getGame().isResourceDeckEmpty()) {
            controller.getGame().getResourcePlayingDeck().getDeck().draw();
        }
        while (!controller.getGame().isGoldDeckEmpty()) {
            controller.getGame().getGoldPlayingDeck().getDeck().draw();
        }

        // Game should now be in Semi Last Turn
        assertEquals(GameStatus.SEMI_LAST_TURN, controller.getCurrentStatus());

        controller.nextTurn();
        controller.nextTurn();
        controller.nextTurn();
        controller.nextTurn();

        // Game should now be in Last Turn
        assertEquals(GameStatus.LAST_TURN, controller.getCurrentStatus());

        controller.nextTurn();
        controller.nextTurn();
        controller.nextTurn();
        controller.nextTurn();

        // Game should now be ended
        assertEquals(GameStatus.END_GAME, controller.getCurrentStatus());

    }

    @Test
    void testGameEndedBecauseOfPlayerReaching20Points() throws RemoteException {
        // Tests if the Game correctly ends in a situation where a Player reaches 20 Points
        GameController controller = new GameController("Test");

        controller.addPlayer(new Player("Bot1"));
        controller.addPlayer(new Player("Bot2"));
        controller.addPlayer(new Player("Bot3"));
        controller.addPlayer(new Player("Bot4"));

        controller.getPlayer(1).setToken(Token.BLUE);
        controller.getPlayer(2).setToken(Token.RED);
        controller.getPlayer(3).setToken(Token.GREEN);
        controller.getPlayer(4).setToken(Token.YELLOW);

        controller.setCurrentStatus(GameStatus.PLAYING);
        for (int i = 1; i < 5; i++) {
            Player p = controller.getPlayer(i);
            p.drawSecretObjectives(controller.getGame().getObjectivePlayingDeck());
            p.setSecretObjective(controller.getPlayer(i).getTemporaryObjectiveCards().getFirst());
            p.drawTemporaryStarterCard(controller.getGame().getStarterDeck());
            p.getTemporaryStarterCard().flip();
            p.setTemporaryStarterCard(p.getTemporaryStarterCard());
            p.drawStartingHand(controller.getGame().getResourcePlayingDeck(), controller.getGame().getGoldPlayingDeck());
            p.setStatus(GameStatus.READY_TO_PLAY);
        }

        // Player reaches 20 Points
        controller.getPlayer(1).setPoints(20);

        // Game should now be in Semi Last Turn
        assertEquals(GameStatus.SEMI_LAST_TURN, controller.getCurrentStatus());

        controller.nextTurn();
        controller.nextTurn();
        controller.nextTurn();
        controller.nextTurn();

        // Game should now be in Last Turn
        assertEquals(GameStatus.LAST_TURN, controller.getCurrentStatus());

        controller.nextTurn();
        controller.nextTurn();
        controller.nextTurn();
        controller.nextTurn();

        // Game should now be ended
        assertEquals(GameStatus.END_GAME, controller.getCurrentStatus());
    }

    @Test
    void disconnectPlayer() throws RemoteException {
        GameController controller = new GameController("Test");
        controller.addPlayer(new Player("Bot1"));
        controller.addPlayer(new Player("Bot2"));
        controller.addPlayer(new Player("Bot3"));
        controller.addPlayer(new Player("Bot4"));

        controller.getPlayer(1).setToken(Token.BLUE);
        controller.getPlayer(2).setToken(Token.RED);
        controller.getPlayer(3).setToken(Token.GREEN);
        controller.getPlayer(4).setToken(Token.YELLOW);

        controller.setCurrentStatus(GameStatus.PLAYING);
        for (int i = 1; i < 5; i++) {
            Player p = controller.getPlayer(i);
            p.drawSecretObjectives(controller.getGame().getObjectivePlayingDeck());
            p.setSecretObjective(controller.getPlayer(i).getTemporaryObjectiveCards().getFirst());
            p.drawTemporaryStarterCard(controller.getGame().getStarterDeck());
            p.getTemporaryStarterCard().flip();
            p.setTemporaryStarterCard(p.getTemporaryStarterCard());
            p.drawStartingHand(controller.getGame().getResourcePlayingDeck(), controller.getGame().getGoldPlayingDeck());
            p.setStatus(GameStatus.READY_TO_PLAY);
        }

        controller.getGame().setPlayerTurn(1);

        // Player 1 disconnects
        controller.disconnectPlayer(1);

        // Player 1 should be marked as disconnected
        assertTrue(controller.getPlayer(1).isDisconnected());

        // Turn should now be on Player 2
        assertEquals(2, controller.getGame().getPlayerTurn());

        // Player 1 should be ignored by Turn System
        controller.nextTurn();
        controller.nextTurn();
        controller.nextTurn();
        assertEquals(2, controller.getGame().getPlayerTurn());

        // Player 1 should be counted in the number of disconnected players
        assertEquals(1, controller.getNumberOfDisconnectedPlayers());
    }

    @Test
    void rejoin() throws RemoteException {
        GameController controller = new GameController("Test");
        controller.addPlayer(new Player("Bot1"));
        controller.addPlayer(new Player("Bot2"));
        controller.addPlayer(new Player("Bot3"));
        controller.addPlayer(new Player("Bot4"));

        controller.getPlayer(1).setToken(Token.BLUE);
        controller.getPlayer(2).setToken(Token.RED);
        controller.getPlayer(3).setToken(Token.GREEN);
        controller.getPlayer(4).setToken(Token.YELLOW);

        controller.setCurrentStatus(GameStatus.PLAYING);
        for (int i = 1; i < 5; i++) {
            Player p = controller.getPlayer(i);
            p.drawSecretObjectives(controller.getGame().getObjectivePlayingDeck());
            p.setSecretObjective(controller.getPlayer(i).getTemporaryObjectiveCards().getFirst());
            p.drawTemporaryStarterCard(controller.getGame().getStarterDeck());
            p.getTemporaryStarterCard().flip();
            p.setTemporaryStarterCard(p.getTemporaryStarterCard());
            p.drawStartingHand(controller.getGame().getResourcePlayingDeck(), controller.getGame().getGoldPlayingDeck());
            p.setStatus(GameStatus.READY_TO_PLAY);
        }

        controller.getGame().setPlayerTurn(1);

        // Player 1 disconnects
        controller.disconnectPlayer(1);

        // Player 1 should be marked as disconnected
        assertTrue(controller.getPlayer(1).isDisconnected());

        // Turn should now be on Player 2
        assertEquals(2, controller.getGame().getPlayerTurn());

        // Player 1 should be ignored by Turn System
        controller.nextTurn();
        controller.nextTurn();
        controller.nextTurn();
        assertEquals(2, controller.getGame().getPlayerTurn());

        // Player 1 should be counted in the number of disconnected players
        assertEquals(1, controller.getNumberOfDisconnectedPlayers());

        // Player 1 re-joins the Game
        controller.rejoinGame(1);

        // Player 1 should not be marked as disconnected anymore
        assertFalse(controller.getPlayer(1).isDisconnected());

        // Turn still should now be on Player 2
        assertEquals(2, controller.getGame().getPlayerTurn());

        // Player 1 should not be ignored by Turn System anymore
        controller.nextTurn();
        controller.nextTurn();
        controller.nextTurn();
        assertEquals(1, controller.getGame().getPlayerTurn());
    }

    @Test
    void rejoinWithoutChoices() throws RemoteException {
        GameController controller = new GameController("Test");
        controller.addPlayer(new Player("Bot1"));
        controller.addPlayer(new Player("Bot2"));
        controller.addPlayer(new Player("Bot3"));
        controller.addPlayer(new Player("Bot4"));

        // Player 1 disconnects
        controller.disconnectPlayer(1);

        // Other Players make their choices
        controller.getPlayer(2).setToken(Token.RED);
        controller.getPlayer(3).setToken(Token.GREEN);
        controller.getPlayer(4).setToken(Token.YELLOW);

        controller.setCurrentStatus(GameStatus.READY_TO_DRAW_STARTING_HAND);
        controller.setStarted(true);
        for (int i = 2; i < 5; i++) {
            Player p = controller.getPlayer(i);
            p.drawSecretObjectives(controller.getGame().getObjectivePlayingDeck());
            p.setSecretObjective(controller.getPlayer(i).getTemporaryObjectiveCards().getFirst());
            p.drawTemporaryStarterCard(controller.getGame().getStarterDeck());
            p.getTemporaryStarterCard().flip();
            p.setTemporaryStarterCard(p.getTemporaryStarterCard());
            p.setStatus(GameStatus.READY_TO_DRAW_STARTING_HAND);
        }
        //controller.getPlayer(1).setStatus(GameStatus.PLAYING);

        controller.getGame().setPlayerTurn(2);

        // Player 1 re-joins the Game
        controller.rejoinGame(1);

        // The Server should have made Player's choices in his behalf
        assertEquals(Token.BLUE, controller.getPlayer(1).getToken());
        assertNotNull(controller.getPlayer(1).getSecretObjective());
        assertEquals(1, controller.getPlayer(1).getPlayField().getPlayedCards().size());
        assertEquals(3, controller.getPlayer(1).getHandSize());
    }

    @Test
    void statusTest() throws RemoteException {
        // given
        GameController controller = new GameController("test");
        Player player = new Player("bot1", true, 0, Token.BLUE, (ObjectiveCard) controller.getGame().getObjectivePlayingDeck().getDeck().draw());
        // New view empty just to test listeners
        controller.addView(new RemoteViewController() {
            @Override
            public int getOwner() throws RemoteException {
                return 1;
            }

            @Override
            public void showSecretObjectivesSelectionDialog() throws RemoteException {}
            @Override
            public void showStarterCardSelectionDialog() throws RemoteException {}
            @Override
            public void showTokenSelectionDialog() throws RemoteException {}
            @Override
            public void askToDrawOrGrab(int playerID) throws RemoteException {}
            @Override
            public void notifyGameIsStarting() throws RemoteException {}
            @Override
            public void notifyDeckChanged(CardType type) throws RemoteException {}
            @Override
            public void notifySlotCardChanged(CardType type, int slot) throws RemoteException {}
            @Override
            public void notifyPlayersPointsChanged(Token token, int newPoints) throws RemoteException {}
            @Override
            public void notifyNumberOfPlayersChanged() throws RemoteException {}
            @Override
            public void notifyPlayersTokenChanged(int playerID) throws RemoteException {}
            @Override
            public void notifyPlayersPlayAreaChanged(int playerID) throws RemoteException {}
            @Override
            public void notifyPlayersHandChanged(int playerID) throws RemoteException {}
            @Override
            public void notifyHandCardWasFlipped(int playedID, int cardID) throws RemoteException {}
            @Override
            public void notifyPlayersObjectiveChanged(int playerID) throws RemoteException {}
            @Override
            public void notifyCommonObjectivesChanged() throws RemoteException {}
            @Override
            public void notifyTurnChanged() throws RemoteException {}
            @Override
            public void getReady(int numberOfPlayers) throws RemoteException {}
            @Override
            public void notifyLastTurn() throws RemoteException {}
            @Override
            public void notifyEndGame(ArrayList<HashMap<String, String>> points) throws RemoteException {}
            @Override
            public void notifyNewMessage(ChatMessage message) throws RemoteException {}
        });

        // nextStatus test
        controller.setCurrentStatus(GameStatus.WAITING_FOR_SERVER);
        assertEquals(controller.getCurrentStatus(), GameStatus.WAITING_FOR_SERVER);

        controller.addPlayer(player);
        player.setStatus(GameStatus.WAITING_FOR_SERVER);
        assertEquals(controller.getCurrentStatus(), GameStatus.READY);

        player.setStatus(GameStatus.READY);
        assertEquals(controller.getCurrentStatus(), GameStatus.READY_TO_CHOOSE_SECRET_OBJECTIVE);

        player.setStatus(GameStatus.READY_TO_CHOOSE_SECRET_OBJECTIVE);
        assertEquals(controller.getCurrentStatus(), GameStatus.READY_TO_CHOOSE_STARTER_CARD);

        player.setStatus(GameStatus.READY_TO_CHOOSE_STARTER_CARD);
        assertEquals(controller.getCurrentStatus(), GameStatus.READY_TO_DRAW_STARTING_HAND);

        player.setStatus(GameStatus.READY_TO_DRAW_STARTING_HAND);
        assertEquals(controller.getCurrentStatus(), GameStatus.PLAYING);

        player.setStatus(GameStatus.PLAYING);
        assertEquals(controller.getCurrentStatus(), GameStatus.PLAYING);

        // Semi_last_turn, Player is first -> Last_turn
        controller.setCurrentStatus(GameStatus.SEMI_LAST_TURN);
        controller.nextTurn();
        assertEquals(controller.getCurrentStatus(), GameStatus.LAST_TURN);

        // Last_turn, Player is First -> Counting points and End_game
        controller.nextTurn();
        assertEquals(controller.getCurrentStatus(), GameStatus.END_GAME);
    }
}