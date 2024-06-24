package it.polimi.ingsw.gc42.model.classes.cards;

import java.util.ArrayList;
import java.util.stream.Collectors;

import it.polimi.ingsw.gc42.model.classes.game.Game;
import it.polimi.ingsw.gc42.model.classes.game.PlayField;
import it.polimi.ingsw.gc42.model.classes.game.Player;
import it.polimi.ingsw.gc42.model.classes.game.Token;
import it.polimi.ingsw.gc42.model.exceptions.IllegalPlacementException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ItemCountObjectiveTest {

    // check that the result is zero when the condition is never met
    @Test
    void checkZero() {
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

    // test to check the getter and that the points are correct
    @Test
    void checkPoints() {
        Game g = new Game();
        PlayField field = new PlayField();

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
                try {
                    field.addCard((PlayableCard) s,0,0);
                } catch (IllegalPlacementException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        ArrayList<PlayableCard> deck = g.getResourcePlayingDeck().getDeck().getCopy().stream()
                .map(PlayableCard.class::cast).collect(Collectors.toCollection(ArrayList::new));
        int i = 0;
        for (PlayableCard c : deck) {
            // place six plant cards on the back side
            // so that there is always one resource showing
            if (c.getKingdom().equals(KingdomResource.PLANT)) {
                c.flip();
                i++;
                try {
                    field.addCard(c,i,0);
                } catch (IllegalPlacementException e) {
                    throw new RuntimeException(e);
                }
            }
            if (i > 5) break;
        }

        ItemCountObjective o = (ItemCountObjective) obj.getObjective();
        assertEquals(KingdomResource.PLANT, o.getItem());
        // earn 2 points every set of 3 items. On the field there are 7 visible items
        assertEquals(4, obj.getObjective().calculatePoints(field.getPlayedCards()));
    }
}