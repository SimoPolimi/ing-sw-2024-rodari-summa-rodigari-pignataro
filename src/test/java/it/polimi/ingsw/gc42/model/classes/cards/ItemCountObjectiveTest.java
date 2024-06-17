package it.polimi.ingsw.gc42.model.classes.cards;

import java.util.ArrayList;
import it.polimi.ingsw.gc42.model.exceptions.IllegalActionException;
import it.polimi.ingsw.gc42.model.classes.game.Game;
import it.polimi.ingsw.gc42.model.classes.game.Player;
import it.polimi.ingsw.gc42.model.classes.game.Token;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ItemCountObjectiveTest {

    @Test
    void check() {
        Game g = new Game();
        Player p = new Player(Token.BLUE);

        ArrayList<Card> countObjectives = g.getObjectivePlayingDeck().getDeck().getCopy();
        ObjectiveCard obj = null;

        if (g.getObjectivePlayingDeck().getSlot(1).getId() == 96) {
            obj = (ObjectiveCard) g.getObjectivePlayingDeck().getSlot(1);
        } else if (g.getObjectivePlayingDeck().getSlot(2).getId() == 96) {
            obj = (ObjectiveCard) g.getObjectivePlayingDeck().getSlot(2);
        } else {
        for (Card c : countObjectives) {
            if (c.getId() == 96) {
                obj = (ObjectiveCard) c;
                break;
            }
        }
        }

        ArrayList<Card> starters = g.getStarterDeck().getCopy();
        for (Card s : starters) {
            if (s.getId() == 84) {
                s.flip();
                p.setTemporaryStarterCard((StarterCard) s);
                p.setStarterCard();
            }
        }

        assertEquals(0, obj.getObjective().calculatePoints(p.getPlayField().getPlayedCards()));
    }
}