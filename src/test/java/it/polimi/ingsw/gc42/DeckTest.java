package it.polimi.ingsw.gc42;

import it.polimi.ingsw.gc42.model.classes.Deck;
import it.polimi.ingsw.gc42.model.classes.cards.Card;
import it.polimi.ingsw.gc42.model.classes.game.Game;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class DeckTest {

    @Test
    void testResourceDeckIsEmpty() {
        // given
        Game g = new Game();
        // when
        int num = g.getResourcePlayingDeck().getDeck().getNumberOfCards();
        for (int i = 0; i < num; i++) {
            g.getResourcePlayingDeck().getDeck().draw();
        }
        // then
        assertTrue(g.isResourceDeckEmpty());
    }

    @Test
    void testGoldDeckIsEmpty() {
        // given
        Game g = new Game();
        // when
        int num = g.getGoldPlayingDeck().getDeck().getNumberOfCards();
        for (int i = 0; i < num; i++) {
            g.getGoldPlayingDeck().getDeck().draw();
        }
        // then
        assertTrue(g.isGoldDeckEmpty());
    }

    @Test
    void testCardDraw() {
        // given
        Game g = new Game();
        // when
        // the test is not dependent on which PlayingDeck is used as they all use the same methods from Deck
        ArrayList<Card> oldDeck = g.getResourcePlayingDeck().getDeck().getCopy();
        Card oldTop = g.getResourcePlayingDeck().getDeck().getTopCard();
        Card drawnCard = g.getResourcePlayingDeck().getDeck().draw();
        // then
        // assert that the drawn card was the first in the deck
        // and that the second one is now the first
        assertEquals(drawnCard, oldTop);
        assertEquals(g.getResourcePlayingDeck().getDeck().getTopCard(), oldDeck.get(1));

    }
}