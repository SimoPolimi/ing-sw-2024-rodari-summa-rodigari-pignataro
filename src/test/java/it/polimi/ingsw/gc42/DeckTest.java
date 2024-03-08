package it.polimi.ingsw.gc42;

import it.polimi.ingsw.gc42.classes.Deck;
import it.polimi.ingsw.gc42.classes.cards.*;
import it.polimi.ingsw.gc42.classes.game.*;

import java.util.ArrayList;

import it.polimi.ingsw.gc42.interfaces.Listener;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeckTest {

    /* To run these tests some parts of PlayingDeck need to be commented.
    Since the actual Cards are not implemented yet, any attempt to generate them and/or draw from their Deck results in a crash.
    However, with those parts commented, both tests passed without issues.
     */

    @Test
    void testResourceDeckIsEmpty(){
        // given
        Game g = new Game();
        ResourceCard c = new ResourceCard(new CardSide(null, null, null, null),
                new CardSide(null, null, null, null), true,
                1,0, 0, Resource.FEATHER, 5);
        ArrayList<Card> a = new ArrayList<>();
        a.add(c);
        Deck d = new Deck(a, a.size(), CardType.RESOURCECARD);
        g.getPlayingDeck().setResourceCardDeck(d);
        g.getPlayingDeck().getResourceCardDeck().setListener(new Listener() {
            @Override
            public void onEvent() {
                g.setResourceDeckEmpty(true);
            }
        });
        // when
        Card c2 = g.getPlayingDeck().getResourceCardDeck().draw();
        // then
        assertTrue(g.isResourceDeckEmpty());
    }

    @Test
    void testGoldDeckIsEmpty(){
        // given
        Game g = new Game();
        GoldCard c = new GoldCard(new CardSide(null, null, null, null),
                new CardSide(null, null, null, null), true,
                1,0, 0, 0, 0, 0, 0, null, 0);
        ArrayList<Card> a = new ArrayList<>();
        a.add(c);
        Deck d = new Deck(a, a.size(), CardType.GOLDCARD);
        g.getPlayingDeck().setGoldCardDeck(d);
        g.getPlayingDeck().getGoldCardDeck().setListener(new Listener() {
            @Override
            public void onEvent() {
                g.setGoldDeckEmpty(true);
            }
        });
        // when
        Card c2 = g.getPlayingDeck().getGoldCardDeck().draw();
        // then
        assertTrue(g.isGoldDeckEmpty());
    }
}