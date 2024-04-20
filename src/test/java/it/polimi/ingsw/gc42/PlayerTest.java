package it.polimi.ingsw.gc42;

import it.polimi.ingsw.gc42.model.classes.Deck;
import it.polimi.ingsw.gc42.model.exceptions.IllegalActionException;
import it.polimi.ingsw.gc42.model.exceptions.IllegalPlacementException;
import it.polimi.ingsw.gc42.model.exceptions.PlacementConditionNotMetException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.gc42.model.classes.cards.*;
import it.polimi.ingsw.gc42.model.classes.game.*;
import org.junit.jupiter.api.function.Executable;


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
        player.setStarterCard((StarterCard) game.getStarterDeck().draw());

        PlayableCard playedCard = player.getHandCard(0);
        // when
        try {
            player.playCard(playedCard, 1, 0);
        } catch (IllegalPlacementException | PlacementConditionNotMetException e) {
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
        Player player = new Player(null, true, 0, null, null, game);
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
        // TODO: Redo using assertThrows
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

    // TODO: remove comment
    /*@Test
    void grabCard() {
        // given
        Game game = new Game();
        Player player = new Player("");

        Card slotCard = game.getResourcePlayingDeck().getSlot(1);
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
        assertNotEquals(game.getResourcePlayingDeck().getSlot(1), slotCard);
        assertNotEquals(game.getResourcePlayingDeck().getSlot(2), slotCard);
    }

    @Test
    void getHandCard() {
        // given
        Game game = new Game();
        Player player = new Player("");
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
    }*/

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
        player.setStarterCard(starterCard);

        GoldCard finalGoldCard = goldCard;
        assertThrowsExactly(PlacementConditionNotMetException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                player.playCard(finalGoldCard, 1, 0);
            }
        });
    }
}