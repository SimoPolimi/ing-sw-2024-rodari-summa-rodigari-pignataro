package it.polimi.ingsw.gc42;

import it.polimi.ingsw.gc42.model.classes.game.Game;
import it.polimi.ingsw.gc42.model.classes.game.Player;
import it.polimi.ingsw.gc42.model.classes.cards.*;
import it.polimi.ingsw.gc42.model.classes.game.Token;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class PlayingDeckTest {

    @Test
    void testIsPutDownAfterGrabCard() {
        // given
        Game game = new Game();
        Player player = new Player(Token.BLUE);

        // when
        player.grabCard(game.getResourcePlayingDeck(), 1);

        // then
        assertNotNull(game.getResourcePlayingDeck().getCard(1));
        assertNotEquals(player.getHandCard(0), game.getResourcePlayingDeck().getCard(1));

    }

    @Test
    void testCannotGrabCard() {
        // given
        Game game = new Game();
        Player player = new Player(Token.BLUE);

        // when
        // Deck is empty
        int DeckSize = game.getResourcePlayingDeck().getDeck().getNumberOfCards();
        while (game.getResourcePlayingDeck().getDeck().getNumberOfCards() > 0) {
            game.getResourcePlayingDeck().getDeck().draw();
        }
        player.grabCard(game.getResourcePlayingDeck(), 1);

        // Grab from empty slot
        Card test = game.getResourcePlayingDeck().grabCard(1);

        // then
        // No such Card grabbed
        assertNull(test);
        // Deck is empty
        assertEquals(game.getResourcePlayingDeck().getDeck().getNumberOfCards(), 0);
        assertNull(game.getResourcePlayingDeck().getCard(1));
    }
}
