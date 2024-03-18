package it.polimi.ingsw.gc42.model.classes.game;

import it.polimi.ingsw.gc42.model.classes.cards.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    @Test
    void drawStartingHand() {
        // given
        // new game because it initializes the decks
        Game game = new Game();
        Player player = new Player(true, 0, null, null, new ArrayList<Card>(), null, game);

        // when
        player.drawStartingHand(game);

        // then
        assertEquals(player.getHand().size(), 3);

        int resourceCount = 0;
        int goldCardCount = 0;
        for (Card card : player.getHand()) {
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
        PlayField playField = new PlayField((StarterCard) game.getStarterDeck().draw(), new ArrayList<Card>());
        Player player = new Player(true, 0, null, null, new ArrayList<Card>(), playField, game);
        player.drawStartingHand(game);

        Card playedCard = player.getHand().get(0);
        // when
        player.playCard(playedCard, 1, 0, player.getPlayField());

        // then
        assertFalse(player.getPlayField().getPlayedCards().isEmpty());
        assertTrue(player.getPlayField().getPlayedCards().contains(playedCard));
        assertEquals(player.getHand().size(), 2);
        assertFalse(player.getHand().contains(playedCard));
    }

    @Test
    void drawCard() {
        // given
        // new game because it initializes the decks
        Game game = new Game();
        Player player = new Player(true, 0, null, null, new ArrayList<Card>(), null, game);

        Card topCard = game.getResourcePlayingDeck().getDeck().getCards().getFirst();
        // when
        player.drawCard(game.getResourcePlayingDeck());

        // then
        assertFalse(player.getHand().isEmpty());
        assertTrue(player.getHand().contains(topCard));
    }

    @Test
    void grabCard() {
    }
}