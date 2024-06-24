package it.polimi.ingsw.gc42.controller;

import it.polimi.ingsw.gc42.model.classes.cards.*;
import it.polimi.ingsw.gc42.model.classes.game.ChatMessage;
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
    void generalTesting() throws RemoteException {
        // given
        GameController controller = new GameController("test");
        Player player = new Player("bot1", true, 0, null, null);
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
        //TODO: check if ok

        // nextStatus coverage
        controller.setCurrentStatus(GameStatus.WAITING_FOR_SERVER);
        controller.addPlayer(player);
        player.setStatus(GameStatus.WAITING_FOR_SERVER);
        player.setStatus(GameStatus.READY);
        player.setStatus(GameStatus.READY_TO_CHOOSE_TOKEN);
        player.setToken(Token.BLUE);
        player.setStatus(GameStatus.READY_TO_CHOOSE_SECRET_OBJECTIVE);
        player.setSecretObjective((ObjectiveCard) controller.getGame().getObjectivePlayingDeck().getDeck().draw());
        player.setStatus(GameStatus.READY_TO_CHOOSE_STARTER_CARD);
        player.setStatus(GameStatus.READY_TO_DRAW_STARTING_HAND);
        player.setStatus(GameStatus.PLAYING);
        player.setStatus(GameStatus.END_GAME);

    }

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
        // TODO: add more things to check?
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
        // TODO: add more things to check?
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
        // TODO: calling other methods?
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
}