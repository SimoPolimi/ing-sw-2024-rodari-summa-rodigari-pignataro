package it.polimi.ingsw.gc42;

import it.polimi.ingsw.gc42.model.classes.PlayingDeck;
import it.polimi.ingsw.gc42.model.classes.game.Game;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PlayingDeckTest {

    @Test // Test for slot2 not null after putDown()
    void testPutDown() {
        Game g = new Game();
        PlayingDeck pd = g.getResourcePlayingDeck();
        pd.putDown(2);

        assertNotNull(pd.getSlot2());
    }
}
