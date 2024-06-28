package it.polimi.ingsw.gc42.model.classes.cards;

import it.polimi.ingsw.gc42.model.classes.Deck;
import it.polimi.ingsw.gc42.model.classes.game.Game;
import it.polimi.ingsw.gc42.model.classes.game.PlayField;
import it.polimi.ingsw.gc42.model.exceptions.IllegalPlacementException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class DiagonalPlacementObjectiveTest {

    @Test
    void check() {
        Game g = new Game();
        PlayField field = new PlayField();

        ArrayList<Card> countObjectives = g.getObjectivePlayingDeck().getDeck().getCopy();
        ObjectiveCard obj = null;

        if (g.getObjectivePlayingDeck().getSlot(1).getId() == 87) {
            obj = (ObjectiveCard) g.getObjectivePlayingDeck().getSlot(1);
        } else if (g.getObjectivePlayingDeck().getSlot(2).getId() == 87) {
            obj = (ObjectiveCard) g.getObjectivePlayingDeck().getSlot(2);
        } else {
            for (Card c : countObjectives) {
                if (c.getId() == 87) {
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
            if (c.getKingdom().equals(KingdomResource.FUNGI)) {
                i++;
                c.flip();
                try {
                    field.addCard(c, i, 0);
                } catch (IllegalPlacementException e) {
                    throw new RuntimeException(e);
                }
            }
            if (i > 7) break;
        }

        // Earn 3 points every matching pattern, excluding the ones containing at least one card included in another pattern
        // On the field there are 2 patterns, but one card is included in both, so only one pattern is counted
        assertEquals(4, obj.getObjective().calculatePoints(field.getPlayedCards()));
    }

    @Test
    void checkProgressiveBuildingPattern() {
        ArrayList<Card> resource = Deck.initDeck(CardType.RESOURCECARD).getCopy();
        ArrayList<Card> objective = Deck.initDeck(CardType.OBJECTIVECARD).getCopy();
        StarterCard starter = (StarterCard) Deck.initDeck(CardType.STARTERCARD).draw();
        ArrayList<PlayableCard> playfield = new ArrayList<>();

        ObjectiveCard target = null;
        for (int i = 0; i < objective.size(); i++) {
            if (objective.get(i).getId() == 87) {
                target = (ObjectiveCard) objective.get(i);
            }
        }

        starter.flip();
        starter.setX(0);
        starter.setY(0);
        playfield.add(starter);

        PlayableCard c = null;
        boolean exit = false;
        for (int i = 0; i < resource.size() && !exit; i++) {
            if (((PlayableCard) resource.get(i)).getKingdom().equals(KingdomResource.FUNGI)) {
                c = (PlayableCard) resource.get(i);
                resource.remove(c);
                exit = true;
            }
        }

        c.flip();
        c.setX(1);
        c.setY(0);
        playfield.add(c);

        exit = false;
        for (int i = 0; i < resource.size() && !exit; i++) {
            if (((PlayableCard) resource.get(i)).getKingdom().equals(KingdomResource.FUNGI)) {
                c = (PlayableCard) resource.get(i);
                resource.remove(c);
                exit = true;
            }
        }
        c.flip();
        c.setX(-1);
        c.setY(0);
        playfield.add(c);

        assertEquals(0, target.getObjective().check(playfield));

        exit = false;
        for (int i = 0; i < resource.size() && !exit; i++) {
            if (((PlayableCard) resource.get(i)).getKingdom().equals(KingdomResource.FUNGI)) {
                c = (PlayableCard) resource.get(i);
                resource.remove(c);
                exit = true;
            }
        }
        c.flip();
        c.setX(2);
        c.setY(0);
        playfield.add(c);

        assertEquals(0, target.getObjective().check(playfield));

        exit = false;
        for (int i = 0; i < resource.size() && !exit; i++) {
            if (((PlayableCard) resource.get(i)).getKingdom().equals(KingdomResource.FUNGI)) {
                c = (PlayableCard) resource.get(i);
                resource.remove(c);
                exit = true;
            }
        }
        c.flip();
        c.setX(3);
        c.setY(0);
        playfield.add(c);

        assertEquals(1, target.getObjective().check(playfield));

        exit = false;
        for (int i = 0; i < resource.size() && !exit; i++) {
            if (((PlayableCard) resource.get(i)).getKingdom().equals(KingdomResource.FUNGI)) {
                c = (PlayableCard) resource.get(i);
                resource.remove(c);
                exit = true;
            }
        }
        c.flip();
        c.setX(4);
        c.setY(0);
        playfield.add(c);

        assertEquals(1, target.getObjective().check(playfield));

        c.flip();
        c.setX(5);
        c.setY(0);
        playfield.add(c);

        assertEquals(1, target.getObjective().check(playfield));

        c.flip();
        c.setX(6);
        c.setY(0);
        playfield.add(c);

        assertEquals(2, target.getObjective().check(playfield));
    }

    @Test
    void checkProgressiveBuildingPattern2() {
        ArrayList<Card> resource = Deck.initDeck(CardType.RESOURCECARD).getCopy();
        ArrayList<Card> objective = Deck.initDeck(CardType.OBJECTIVECARD).getCopy();
        StarterCard starter = (StarterCard) Deck.initDeck(CardType.STARTERCARD).draw();
        ArrayList<PlayableCard> playfield = new ArrayList<>();

        ObjectiveCard target = null;
        for (int i = 0; i < objective.size(); i++) {
            if (objective.get(i).getId() == 88) {
                target = (ObjectiveCard) objective.get(i);
            }
        }

        starter.flip();
        starter.setX(0);
        starter.setY(0);
        playfield.add(starter);

        PlayableCard c = null;
        boolean exit = false;
        for (int i = 0; i < resource.size() && !exit; i++) {
            if (((PlayableCard) resource.get(i)).getKingdom().equals(KingdomResource.PLANT)) {
                c = (PlayableCard) resource.get(i);
                resource.remove(c);
                exit = true;
            }
        }

        c.flip();
        c.setX(0);
        c.setY(1);
        playfield.add(c);

        exit = false;
        for (int i = 0; i < resource.size() && !exit; i++) {
            if (((PlayableCard) resource.get(i)).getKingdom().equals(KingdomResource.PLANT)) {
                c = (PlayableCard) resource.get(i);
                resource.remove(c);
                exit = true;
            }
        }
        c.flip();
        c.setX(0);
        c.setY(-1);
        playfield.add(c);

        assertEquals(0, target.getObjective().check(playfield));

        exit = false;
        for (int i = 0; i < resource.size() && !exit; i++) {
            if (((PlayableCard) resource.get(i)).getKingdom().equals(KingdomResource.PLANT)) {
                c = (PlayableCard) resource.get(i);
                resource.remove(c);
                exit = true;
            }
        }
        c.flip();
        c.setX(0);
        c.setY(2);
        playfield.add(c);

        assertEquals(0, target.getObjective().check(playfield));

        exit = false;
        for (int i = 0; i < resource.size() && !exit; i++) {
            if (((PlayableCard) resource.get(i)).getKingdom().equals(KingdomResource.PLANT)) {
                c = (PlayableCard) resource.get(i);
                resource.remove(c);
                exit = true;
            }
        }
        c.flip();
        c.setX(0);
        c.setY(3);
        playfield.add(c);

        assertEquals(1, target.getObjective().check(playfield));

        exit = false;
        for (int i = 0; i < resource.size() && !exit; i++) {
            if (((PlayableCard) resource.get(i)).getKingdom().equals(KingdomResource.PLANT)) {
                c = (PlayableCard) resource.get(i);
                resource.remove(c);
                exit = true;
            }
        }
        c.flip();
        c.setX(0);
        c.setY(4);
        playfield.add(c);

        assertEquals(1, target.getObjective().check(playfield));

        exit = false;
        for (int i = 0; i < resource.size() && !exit; i++) {
            if (((PlayableCard) resource.get(i)).getKingdom().equals(KingdomResource.PLANT)) {
                c = (PlayableCard) resource.get(i);
                resource.remove(c);
                exit = true;
            }
        }

        c.flip();
        c.setX(0);
        c.setY(5);
        playfield.add(c);

        assertEquals(1, target.getObjective().check(playfield));

        exit = false;
        for (int i = 0; i < resource.size() && !exit; i++) {
            if (((PlayableCard) resource.get(i)).getKingdom().equals(KingdomResource.PLANT)) {
                c = (PlayableCard) resource.get(i);
                resource.remove(c);
                exit = true;
            }
        }

        c.flip();
        c.setX(0);
        c.setY(6);
        playfield.add(c);

        assertEquals(2, target.getObjective().check(playfield));
    }
}