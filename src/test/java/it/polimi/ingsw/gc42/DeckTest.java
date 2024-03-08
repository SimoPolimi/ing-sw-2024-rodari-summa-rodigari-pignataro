package it.polimi.ingsw.gc42;

import it.polimi.ingsw.gc42.classes.cards.*;
import it.polimi.ingsw.gc42.classes.game.*;
import it.polimi.ingsw.gc42.classes.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeckTest {

    @Test
    void testResourceDeckIsEmpty(){
        // given
        ResourceCard c = new ResourceCard(new CardSide(null, null, null, null),
                new CardSide(null, null, null, null), true, 1,
                0, 0, Resource.FEATHER, 5);
        ArrayList<Card> a = new ArrayList<Card>();
        a.add(c);
        Game g = new Game();
        Deck d = new Deck(a, 1, CardType.RESOURCECARD);
        d.register(g);

        // when
        d.draw();

        // then
        assertTrue(g.isResourceDeckEmpty());
    }

    @Test
    void testGoldDeckIsEmpty(){
        // given
        //TODO objective != null
        GoldCard c = new GoldCard(new CardSide(null, null, null, null),
                new CardSide(null, null, null, null), true, 1,
                0, 0, 2,1,0,0, Objective.TEST, 3);
        ArrayList<Card> a = new ArrayList<Card>();
        a.add(c);
        Game g = new Game();
        Deck d = new Deck(a, 1, CardType.GOLDCARD);
        d.register(g);

        // when
        d.draw();

        // then
        assertTrue(g.isGoldDeckEmpty());
    }
}