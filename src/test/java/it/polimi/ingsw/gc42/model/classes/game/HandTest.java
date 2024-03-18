package it.polimi.ingsw.gc42.model.classes.game;

import it.polimi.ingsw.gc42.model.classes.cards.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class HandTest {

    @Test
    void drawStartingHand() {
        // given
        // new game because it initializes the decks
        Game game = new Game();
        Hand hand = new Hand();
        Player player = new Player(true, 0, null, null, hand, null, game);

        // when
        player.getHand().drawStartingHand(game);

        // then
        assertEquals(player.getHand().getCards().size(), 3);

        int resourceCount = 0;
        int goldCardCount = 0;
        for (Card card : player.getHand().getCards()) {
            if (card instanceof ResourceCard) {
                resourceCount++;
            } else if (card instanceof GoldCard) {
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
        Hand hand = new Hand();
        PlayField playField = new PlayField((StarterCard) game.getStarterDeck().draw(), new ArrayList<Card>());
        Player player = new Player(true, 0, null, null, hand, playField, game);
        player.getHand().drawStartingHand(game);

        Card playedCard = player.getHand().getCards().get(0);
        // when
        player.getHand().playCard(playedCard, 1, 0, player.getPlayField());

        // then
        assertTrue(player.getPlayField().getPlayedCards().size() > 0);
        assertTrue(player.getPlayField().getPlayedCards().contains(playedCard));
        assertEquals(player.getHand().getCards().size(), 2);
        assertFalse(player.getHand().getCards().contains(playedCard));
    }

    @Test
    void drawCard() {
    }

    @Test
    void grabCard() {
    }
}