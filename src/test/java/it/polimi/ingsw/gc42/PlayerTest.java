package it.polimi.ingsw.gc42;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.gc42.model.classes.cards.*;
import it.polimi.ingsw.gc42.model.classes.game.*;


import java.util.ArrayList;
import java.util.NoSuchElementException;


class PlayerTest {

    @Test
    void drawStarterCard() {
    }

    @Test
    void drawSecretObjectives() {
    }

    @Test
    void drawStartingHand() {
        // given
        // new game because it initializes the decks
        Game game = new Game();
        Player player = new Player(null, true, 0, null, null, game);

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
        PlayField playField = new PlayField((StarterCard) game.getStarterDeck().draw());
        Player player = new Player(null, true, 0, null, null, game);
        player.drawStartingHand(game.getResourcePlayingDeck(), game.getGoldPlayingDeck());

        PlayableCard playedCard = player.getHandCard(0);
        // when
        player.playCard(playedCard, 1, 0);

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
        Player player = new Player(null, true, 0, null, null, game);

        player.drawCard(game.getResourcePlayingDeck());
        player.drawCard(game.getResourcePlayingDeck());
        Card topCard = game.getResourcePlayingDeck().getDeck().getTopCard();
        // when
        player.drawCard(game.getResourcePlayingDeck());

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
    void drawEmptyDeck() {
        // given
        // new game because it initializes the decks
        Game game = new Game();
        Player player = new Player(null, true, 0, null, null, game);

        int DeckSize = game.getResourcePlayingDeck().getDeck().getNumberOfCards();
        while (game.getResourcePlayingDeck().getDeck().getNumberOfCards() > 0) {
            game.getResourcePlayingDeck().getDeck().draw();
        }
        // when
        boolean deckWasEmpty = false;
        try {
            game.getResourcePlayingDeck().getDeck().draw();
        } catch (NoSuchElementException e) {
            deckWasEmpty = true;
        }
        // then
      
        // Deck is empty
        assertEquals(game.getResourcePlayingDeck().getDeck().getNumberOfCards(), 0);
        //assertThrowsExactly(NoSuchElementException.class, () -> game.getResourcePlayingDeck().getDeck().draw());
        assertTrue(deckWasEmpty);
    }

    @Test
    void grabCard() {
        // given
        Game game = new Game();
        Player player = new Player(null);

        Card slotCard = game.getResourcePlayingDeck().getCard(1);
        // when
        player.grabCard(game.getResourcePlayingDeck(), 1);

        // then
        boolean containsTheCard = false;
        for (int i = 0; i < player.getHandSize(); i++) {
            if (player.getHandCard(i).equals(slotCard)) {
                containsTheCard = true;
            }
        }
        assertTrue(containsTheCard);
        assertNotEquals(game.getResourcePlayingDeck().getCard(1), slotCard);
        assertNotEquals(game.getResourcePlayingDeck().getCard(2), slotCard);
    }

    @Test
    void getHandCard() {
        // given
        Game game = new Game();
        Player player = new Player(null);
        // when
        Card card1 = game.getResourcePlayingDeck().getDeck().getTopCard();
        player.drawCard(game.getResourcePlayingDeck());
        boolean caught = false;
        try {
            player.getHandCard(9);
        } catch (IllegalArgumentException e) {
            caught = true;
        }
        // then
        assertTrue(caught);
        assertNull(player.getHandCard(1));
        assertEquals(card1, player.getHandCard(0));
    }
}