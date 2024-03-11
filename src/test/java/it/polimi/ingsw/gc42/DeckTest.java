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

    /* To run these tests some parts of PlayingDeck need to be commented.
    Since the actual Cards are not implemented yet, any attempt to generate them and/or draw from their Deck results in a crash.
    However, with those parts commented, both tests passed without issues.
     */

    @Test
    void testResourceDeckIsEmpty() {
        // given
        Game g = new Game();
        ResourceCard c = new ResourceCard(new CardSide(null, null, null, null),
                new CardSide(null, null, null, null), true,
                1, 0, 0, KingdomResource.ANIMAL, 5);
        ArrayList<Card> a = new ArrayList<>();
        a.add(c);
        Deck d = new Deck(a, a.size(), CardType.RESOURCECARD);
        g.getResourcePlayingDeck().setDeck(d);
        g.getResourcePlayingDeck().getDeck().setListener(new Listener() {
            @Override
            public void onEvent() {
                g.setResourceDeckEmpty(true);
            }
        });
        // when
        Card c2 = g.getResourcePlayingDeck().getDeck().draw();
        // then
        assertTrue(g.isResourceDeckEmpty());
    }

    @Test
    void testGoldDeckIsEmpty() {
        // given
        Game g = new Game();
        GoldCard c = new GoldCard(new CardSide(null, null, null, null),
                new CardSide(null, null, null, null), true,
                1, 0, 0, 0, 0, 0, 0, null, 0);
        ArrayList<Card> a = new ArrayList<>();
        a.add(c);
        Deck d = new Deck(a, a.size(), CardType.GOLDCARD);
        g.getGoldPlayingDeck().setDeck(d);
        g.getGoldPlayingDeck().getDeck().setListener(new Listener() {
            @Override
            public void onEvent() {
                g.setGoldDeckEmpty(true);
            }
        });
        // when
        Card c2 = g.getGoldPlayingDeck().getDeck().draw();
        // then
        assertTrue(g.isGoldDeckEmpty());
    }

    @Test
    void testIsPutDown() {
        //TODO remake with initPlayingDecks

        // given
        // decks
        Game game = new Game();
        ResourceCard resourceCard1 = new ResourceCard(null, null, false, 1, 0, 0, null, 0);
        ResourceCard resourceCard2 = new ResourceCard(null, null, false, 2, 0, 0, null, 0);
        GoldCard goldCard1 = new GoldCard(null, null, false, 3, 0, 0, 0, 0, 0, 0, null, 0);
        GoldCard goldCard2 = new GoldCard(null, null, false, 4, 0, 0, 0, 0, 0, 0, null, 0);
        ResourceCard resourceCardInDeck = new ResourceCard(new CardSide(null, null, null, null),
                new CardSide(null, null, null, null), true,
                1, 0, 0, KingdomResource.PLANT, 5);
        ArrayList<Card> a1 = new ArrayList<>();
        a1.add(resourceCardInDeck);
        Deck resourceDeck = new Deck(a1, a1.size(), CardType.RESOURCECARD);
        GoldCard goldCardInDeck = new GoldCard(new CardSide(null, null, null, null),
                new CardSide(null, null, null, null), true,
                1, 0, 0, 0, 0, 0, 0, null, 0);
        ArrayList<Card> a2 = new ArrayList<>();
        a2.add(goldCardInDeck);
        Deck goldDeck = new Deck(a2, a2.size(), CardType.GOLDCARD);
        PlayingDeck goldPlayingDeck = new PlayingDeck(goldCard1, goldCard2, goldDeck);

        game.setGoldPlayingDeck(goldPlayingDeck);

        // Player
        Hand hand = new Hand(null);
        Player player = new Player(true, 0, null, null, hand);

        // meh
        game.addPlayer(player);

        //TODO condition if the Deck is empty and cannot put down another Card

        // when and then
        // draw Resourcecard1
        player.getHand().grabCard(goldPlayingDeck, 1);
        assertNotNull(goldPlayingDeck.getSlot1());
        assertNotEquals(player.getHand().getCards().get(0), goldPlayingDeck.getSlot1());


    }
}