package it.polimi.ingsw.gc42;

import it.polimi.ingsw.gc42.model.classes.game.Game;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeckTest {

    @Test
    void testResourceDeckIsEmpty() {
        // given
        Game g = new Game();
        // when
        int num = g.getResourcePlayingDeck().getDeck().getCounter();
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
        int num = g.getGoldPlayingDeck().getDeck().getCounter();
        for (int i = 0; i < num; i++) {
            g.getGoldPlayingDeck().getDeck().draw();
        }
        // then
        assertTrue(g.isGoldDeckEmpty());
    }
}