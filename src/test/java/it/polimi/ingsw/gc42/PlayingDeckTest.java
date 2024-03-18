package it.polimi.ingsw.gc42;

import it.polimi.ingsw.gc42.model.classes.PlayingDeck;
import it.polimi.ingsw.gc42.model.classes.game.Game;
import it.polimi.ingsw.gc42.model.classes.game.Hand;
import it.polimi.ingsw.gc42.model.classes.game.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PlayingDeckTest {

    @Test
    void testIsPutDown() {
        // given
        Game game = new Game();
        Hand hand = new Hand();
        Player player = new Player(true, 0, null, null, hand, null, game);

        //TODO condition if the Deck is empty and cannot put down another Card

        // when and then
        player.getHand().grabCard(game.getGoldPlayingDeck(), 1);
        assertNotNull(game.getGoldPlayingDeck().getSlot1());
        assertNotEquals(player.getHand().getCards().getFirst(), game.getGoldPlayingDeck().getSlot1());


    }
}
