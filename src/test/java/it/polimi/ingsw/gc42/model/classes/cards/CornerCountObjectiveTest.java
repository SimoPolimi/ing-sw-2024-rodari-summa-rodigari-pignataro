package it.polimi.ingsw.gc42.model.classes.cards;

import it.polimi.ingsw.gc42.model.classes.game.Game;
import it.polimi.ingsw.gc42.model.classes.game.Player;
import it.polimi.ingsw.gc42.model.classes.game.Token;
import it.polimi.ingsw.gc42.model.exceptions.IllegalActionException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CornerCountObjectiveTest {

    @Test
    void check() {
        Game g = new Game();
        Player p = new Player(Token.BLUE);

        try {
            p.drawCard(g.getResourcePlayingDeck());
            p.drawCard(g.getResourcePlayingDeck());
            p.drawCard(g.getResourcePlayingDeck());
        } catch (IllegalActionException e) {
            e.printStackTrace();
        }

        ArrayList<Card> starters = g.getStarterDeck().getCopy();
        for (Card s: starters) {
            if (s.getId() == 84) {
                s.flip();
                p.setTemporaryStarterCard((StarterCard) s);
                p.setStarterCard();
            }
        }
        GoldCard gold = null;
        Card c1 = null;
        Card c2 = null;

        for(Card c : g.getGoldPlayingDeck().getDeck().getCopy()) {
            if(c.getId() == 46) {
                gold = (GoldCard) c;
            }
        }

        if (null == gold) {
            Card c = g.getGoldPlayingDeck().getSlot(1);
            if (c.getId() == 46) {
                gold = (GoldCard) c;
            } else {
                c = g.getGoldPlayingDeck().getSlot(2);
                if (c.getId() == 46) {
                    gold = (GoldCard) c;
                }
            }
        }
        for (Card c: g.getResourcePlayingDeck().getDeck().getCopy()) {
            if(c.getId() == 36) {
                c1 = c;
            } else if(c.getId() == 10) {
                c2 = c;
            }
        }
        if (null == c1 || null == c2) {
            Card c = g.getResourcePlayingDeck().getSlot(1);
            if (c.getId() == 36) {
                c1 = c;
            } else if(c.getId() == 10) {
                c2 = c;
            }
        } else {
            Card c = g.getResourcePlayingDeck().getSlot(2);
            if (c.getId() == 36) {
                c1 = c;
            } else if(c.getId() == 10) {
                c2 = c;
            }
        }
        try {
            p.setHandCard(0, (PlayableCard) c1);
            p.setHandCard(1, (PlayableCard) c2);
            p.setHandCard(2, gold);
            p.playCard(1, 1, 0);
            p.drawCard(g.getResourcePlayingDeck());
            p.playCard(1,  0, 1);
            p.drawCard(g.getResourcePlayingDeck());
            p.playCard(1, 1, 1);
            p.drawCard(g.getResourcePlayingDeck());

        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(4, gold.getObjective().calculatePoints(p.getPlayField().getPlayedCards()));
    }
}