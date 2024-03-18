package it.polimi.ingsw.gc42;

import it.polimi.ingsw.gc42.model.classes.Deck;
import it.polimi.ingsw.gc42.model.classes.PlayingDeck;

import java.util.ArrayList;

import it.polimi.ingsw.gc42.model.interfaces.Listener;
import it.polimi.ingsw.gc42.model.classes.cards.*;
import it.polimi.ingsw.gc42.model.classes.game.Game;
import it.polimi.ingsw.gc42.model.classes.game.Hand;
import it.polimi.ingsw.gc42.model.classes.game.Player;
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