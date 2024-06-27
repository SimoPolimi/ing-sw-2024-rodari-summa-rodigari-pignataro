package it.polimi.ingsw.gc42.model.classes.game;

import it.polimi.ingsw.gc42.controller.GameStatus;
import it.polimi.ingsw.gc42.model.exceptions.IllegalActionException;
import it.polimi.ingsw.gc42.model.exceptions.IllegalPlacementException;
import it.polimi.ingsw.gc42.model.exceptions.PlacementConditionNotMetException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.gc42.model.classes.cards.*;


import java.util.ArrayList;
import java.util.NoSuchElementException;


class PlayerTest {

    @Test
    void generalTesting(){
        // given
        Game game = new Game();
        Player player = new Player(null, true, 0, null, null);
        game.addPlayer(player);
        String nickname = "pippo";
        Token token = Token.BLUE;
        int points = 20;
        boolean isFirst = true;
        ObjectiveCard objectiveCard = new ObjectiveCard(0, null, null, null);
        GameStatus status = GameStatus.READY;
        StarterCard starterCard = (StarterCard) game.getStarterDeck().draw();

        // given
        player.setNickname(nickname);
        player.setToken(token);
        player.setPoints(points);
        player.setFirst(isFirst);

        player.setSecretObjective(objectiveCard);

        player.setStatus(status);
        player.setTemporaryStarterCard(starterCard);


        // then
        assertEquals(player.getNickname(), nickname);
        assertEquals(player.getToken(), token);
        assertEquals(player.getPoints(), points);
        assert(player.isFirst());
        assertEquals(player.getSecretObjective(), objectiveCard);
        assertEquals(player.getStatus(), status);
        assertEquals(player.getTemporaryStarterCard(), starterCard);
    }

    @Test
    void drawSecretObjectives() {
        // given
        Game game = new Game();
        Player player = new Player(null, true, 0, null, null);
        game.addPlayer(player);

        // when
        player.drawSecretObjectives(game.getObjectivePlayingDeck());

        // then
        assertEquals(player.getTemporaryObjectiveCards().size(), 2);

    }

    @Test
    void drawStartingHand() {
        // given
        // new game because it initializes the decks
        Game game = new Game();
        Player player = new Player(null, true, 0, null, null);
        game.addPlayer(player);

        // when
        player.drawStartingHand(game.getResourcePlayingDeck(), game.getGoldPlayingDeck());

        // then
        for (int i = 0; i < 3; i++) {
            assertNotNull(player.getHandCard(i));
        }

        int resourceCount = 0;
        int goldCardCount = 0;
        for (int i = 0; i < 3; i++) {
            if (player.getHandCard(i) instanceof ResourceCard) {
                resourceCount++;
            } else if (player.getHandCard(i) instanceof GoldCard) {
                goldCardCount++;
            }
        }
        assertEquals(resourceCount, 2);
        assertEquals(goldCardCount, 1);
    }

    @Test
    void playCard() {
        // given
        // new game because it initializes the decks
        Game game = new Game();
        Player player = new Player(null, true, 0, null, null);
        game.addPlayer(player);
        player.drawStartingHand(game.getResourcePlayingDeck(), game.getGoldPlayingDeck());
        player.drawTemporaryStarterCard(game.getStarterDeck());
        player.setStarterCard();

        PlayableCard playedCard = player.getHandCard(0);
        // when
        try {
            player.playCard(1, 1, 0);
        } catch (IllegalPlacementException | PlacementConditionNotMetException | IllegalActionException e) {
            e.printStackTrace();
        }

        // then
        assertFalse(player.getPlayField().getPlayedCards().isEmpty());
        assertTrue(player.getPlayField().getPlayedCards().contains(playedCard));
        // size = 2
        for (int i = 0; i < 2; i++) {
            assertNotNull(player.getHandCard(i));
        }
        assertNull(player.getHandCard(2));
        // card not anymore in hand
        for (int i = 0; i < 3; i++) {
            assertNotEquals(player.getHandCard(i), playedCard);
        }
    }

    @Test
    void drawCard() {
        // given
        // new game because it initializes the decks
        Game game = new Game();
        Player player = new Player(null, true, 0, null, null);
        game.addPlayer(player);
        Card topCard = null;
        try {
            player.drawCard(game.getResourcePlayingDeck());
            player.drawCard(game.getResourcePlayingDeck());
            topCard = game.getResourcePlayingDeck().getDeck().getTopCard();
            // when
            player.drawCard(game.getResourcePlayingDeck());
        }catch (IllegalActionException e){
            e.printStackTrace();
        }


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
        assertNotEquals(game.getResourcePlayingDeck().getDeck().getTopCard(), topCard);
    }

    @Test
    void cannotDrawCard(){
        // given
        // new game because it initializes the decks
        Game game = new Game();
        Player player = new Player(null, true, 0, null, null);
        game.addPlayer(player);
        Card topCard;
        try {
            player.drawCard(game.getResourcePlayingDeck());
            player.drawCard(game.getResourcePlayingDeck());
            player.drawCard(game.getResourcePlayingDeck());
        }catch (IllegalActionException e){
            e.printStackTrace();
        }

        topCard = game.getResourcePlayingDeck().getDeck().getTopCard();
        // when and then
        assertThrowsExactly(IllegalActionException.class, () -> player.drawCard(game.getResourcePlayingDeck()));
        assertEquals(game.getResourcePlayingDeck().getDeck().getTopCard(), topCard);

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
        assertFalse(containsTheCard);
    }


    @Test
    void drawEmptyDeck() {
        // given
        // new game because it initializes the decks
        Game game = new Game();

        while (game.getResourcePlayingDeck().getDeck().getNumberOfCards() > 0) {
            game.getResourcePlayingDeck().getDeck().draw();
        }
        // then

        // Deck is empty
        assertEquals(game.getResourcePlayingDeck().getDeck().getNumberOfCards(), 0);
        assertThrowsExactly(NoSuchElementException.class, () -> game.getResourcePlayingDeck().getDeck().draw());
    }


    @Test
    void grabWhenFullHand() {
        Game game = new Game();
        Player player = new Player("");

        assertThrowsExactly(IllegalActionException.class, () -> {
            // force player more cards than the hand's max size
            for (int i = 0; i < 5; i++) {
                player.grabCard(game.getResourcePlayingDeck(), 1);
            }
        });
    }

    @Test
    void getHandCard() {
        // given
        Game game = new Game();
        Player player = new Player("");
        // when
        Card card1 = game.getResourcePlayingDeck().getDeck().getTopCard();
        try{
            player.drawCard(game.getResourcePlayingDeck());
        }catch (IllegalActionException e){
            e.printStackTrace();
        }

        // then
        assertThrowsExactly(IllegalArgumentException.class, () -> player.getHandCard(9));
        assertNull(player.getHandCard(1));
        assertEquals(card1, player.getHandCard(0));
    }

    @Test
    void playCard_GoldCard_ConditionNotMet() {
        Game game = new Game();
        Player player = new Player(Token.BLUE);
        GoldCard goldCard = null;
        ArrayList<Card> deckCopy = game.getGoldPlayingDeck().getDeck().getCopy();
        ArrayList<Card> starterDeckCopy = game.getStarterDeck().getCopy();

        StarterCard starterCard = null;
        for (Card card : starterDeckCopy) {
            if (card.getId() == 82) {
                starterCard = (StarterCard) card;
            }
        }

        for (Card card : deckCopy) {
            if (card.getId() == 41) {
                goldCard = (GoldCard) card;
            }
        }
        if (null == goldCard) {
            Card card = game.getGoldPlayingDeck().getSlot(1);
            if (card.getId() == 41) {
                goldCard = (GoldCard) card;
            } else {
                card = game.getGoldPlayingDeck().getSlot(2);
                if (card.getId() == 41) {
                    goldCard = (GoldCard) card;
                }
            }
        }

        starterCard.flip();
        player.setTemporaryStarterCard(starterCard);
        player.setStarterCard();

        try {
            player.drawCard(game.getResourcePlayingDeck());
            player.drawCard(game.getResourcePlayingDeck());
            player.drawCard(game.getResourcePlayingDeck());
        } catch (IllegalActionException e) {
            e.printStackTrace();
        }

        player.setHandCard(0, goldCard);
        assertThrowsExactly(PlacementConditionNotMetException.class, () -> player.playCard(1, 1, 0));
    }

    @Test
    void playCard_sizeLessThan3_notStarter() {
        Game game = new Game();
        Player player = new Player("");

        try {
            // grab only 2 cards
            player.grabCard(game.getResourcePlayingDeck(), 1);
            player.grabCard(game.getResourcePlayingDeck(), 1);
        } catch (IllegalActionException e) {
            throw new RuntimeException(e);
        }

        assertThrowsExactly(IllegalActionException.class, () -> player.playCard(1, 0, 0));
    }

    @Test
    void playCard_illegalPlacement() {
        Game game = new Game();
        Player player = new Player("a");

        player.setHandCard(0, (PlayableCard) game.getStarterDeck().draw());
        assertThrowsExactly(IllegalPlacementException.class, () -> player.playCard( 1, 1, 3));
    }
}