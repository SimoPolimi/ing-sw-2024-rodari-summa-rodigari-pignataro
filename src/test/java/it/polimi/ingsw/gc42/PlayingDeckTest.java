package it.polimi.ingsw.gc42;

import it.polimi.ingsw.gc42.model.classes.game.Game;
import it.polimi.ingsw.gc42.model.classes.game.Player;
import it.polimi.ingsw.gc42.model.classes.cards.*;
import it.polimi.ingsw.gc42.model.classes.game.Token;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class PlayingDeckTest {

    @Test
    void testIsPutDown() {
        // given
        Game game = new Game();
        Player player = new Player(Token.BLUE);

        //TODO condition if the Deck is empty and cannot put down another Card

        // when and then
        player.grabCard(game.getGoldPlayingDeck(), 1);
        assertNotNull(game.getGoldPlayingDeck().getCard(1));
        assertNotEquals(player.getHand().getFirst(), game.getGoldPlayingDeck().getCard(1));


    }
}
