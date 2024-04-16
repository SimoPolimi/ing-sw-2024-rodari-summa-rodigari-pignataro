package it.polimi.ingsw.gc42.model.classes.cards;

import it.polimi.ingsw.gc42.model.classes.game.Game;
import it.polimi.ingsw.gc42.model.classes.game.Player;
import it.polimi.ingsw.gc42.model.classes.game.Token;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CornerCountObjectiveTest {

    @Test
    void check() {
        Game g = new Game();
        Player p = new Player(Token.BLUE);
        StarterCard s = (StarterCard) g.getStarterDeck().draw();
        GoldCard gold = null;

        for(Card c : g.getGoldPlayingDeck().getDeck().getCopy()) {
            if(c.getId() == 46) {
                gold = (GoldCard) c;
            }
        }
        s.flip();
        p.setStarterCard(s);
        try {
            PlayableCard c = (PlayableCard) g.getResourcePlayingDeck().getDeck().draw();
            c.flip();
            p.playCard(c, 1, 0);
            c = (PlayableCard) g.getResourcePlayingDeck().getDeck().draw();
            c.flip();
            p.playCard(c, 0, 1);
            p.playCard( gold, 1, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(4, gold.getObjective().calculatePoints(p.getPlayField().getPlayedCards()));
    }
}