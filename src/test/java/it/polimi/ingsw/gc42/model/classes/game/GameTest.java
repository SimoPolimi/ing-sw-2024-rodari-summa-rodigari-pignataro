package it.polimi.ingsw.gc42.model.classes.game;

import it.polimi.ingsw.gc42.model.classes.Deck;
import it.polimi.ingsw.gc42.model.classes.cards.*;
import it.polimi.ingsw.gc42.model.exceptions.IllegalPlacementException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    @Test
    void countPoints() throws IllegalPlacementException {
        Game game = new Game();

        Player p1 = new Player("P1");
        Player p2 = new Player("P2");
        Player p3 = new Player("P3");
        Player p4 = new Player("P4");

        p1.setToken(Token.BLUE);
        p2.setToken(Token.RED);
        p3.setToken(Token.GREEN);
        p4.setToken(Token.YELLOW);

        game.addPlayer(p1);
        game.addPlayer(p2);
        game.addPlayer(p3);
        game.addPlayer(p4);

        ArrayList<Card> resource = Deck.initDeck(CardType.RESOURCECARD).getCopy();
        ArrayList<Card> objective = Deck.initDeck(CardType.OBJECTIVECARD).getCopy();
        ArrayList<Card> starter = Deck.initDeck(CardType.STARTERCARD).getCopy();

        ObjectiveCard target = null;
        for (Card card : objective) {
            if (card.getId() == 87) {
                p1.setSecretObjective((ObjectiveCard) card);
            } else if (card.getId() == 88) {
                p2.setSecretObjective((ObjectiveCard) card);
            } else if (card.getId() == 89) {
                p3.setSecretObjective((ObjectiveCard) card);
            } else if (card.getId() == 90) {
                p4.setSecretObjective((ObjectiveCard) card);
            }
        }

        Card c = starter.getFirst();
        starter.remove(c);
        c.flip();
        p1.getPlayField().addCard((PlayableCard) c, 0, 0);

        c = starter.getFirst();
        starter.remove(c);
        c.flip();
        p2.getPlayField().addCard((PlayableCard) c, 0, 0);

        c = starter.getFirst();
        starter.remove(c);
        c.flip();
        p3.getPlayField().addCard((PlayableCard) c, 0, 0);

        c = starter.getFirst();
        starter.remove(c);
        c.flip();
        p4.getPlayField().addCard((PlayableCard) c, 0, 0);

        ArrayList<HashMap<String, String>> points = game.countPoints();
        assertEquals(0, Integer.valueOf(points.get(0).get("InitialPoints")));
        assertEquals(0, Integer.valueOf(points.get(0).get("SecretObjectivePoints")));
        assertEquals(0, Integer.valueOf(points.get(0).get("CommonObjective1Points")));
        assertEquals(0, Integer.valueOf(points.get(0).get("CommonObjective2Points")));
        assertEquals(0, Integer.valueOf(points.get(1).get("InitialPoints")));
        assertEquals(0, Integer.valueOf(points.get(1).get("SecretObjectivePoints")));
        assertEquals(0, Integer.valueOf(points.get(1).get("CommonObjective1Points")));
        assertEquals(0, Integer.valueOf(points.get(1).get("CommonObjective2Points")));
        assertEquals(0, Integer.valueOf(points.get(2).get("InitialPoints")));
        assertEquals(0, Integer.valueOf(points.get(2).get("SecretObjectivePoints")));
        assertEquals(0, Integer.valueOf(points.get(2).get("CommonObjective1Points")));
        assertEquals(0, Integer.valueOf(points.get(2).get("CommonObjective2Points")));
        assertEquals(0, Integer.valueOf(points.get(3).get("InitialPoints")));
        assertEquals(0, Integer.valueOf(points.get(3).get("SecretObjectivePoints")));
        assertEquals(0, Integer.valueOf(points.get(3).get("CommonObjective1Points")));
        assertEquals(0, Integer.valueOf(points.get(3).get("CommonObjective2Points")));

        c = null;
        boolean exit = false;
        for (int i = 0; i < resource.size() && !exit; i++) {
            if (((PlayableCard) resource.get(i)).getKingdom().equals(KingdomResource.FUNGI)) {
                c = (PlayableCard) resource.get(i);
                resource.remove(c);
                exit = true;
            }
        }
        c.flip();
        p1.getPlayField().addCard((PlayableCard) c, 1, 0);

        exit = false;
        for (int i = 0; i < resource.size() && !exit; i++) {
            if (((PlayableCard) resource.get(i)).getKingdom().equals(KingdomResource.FUNGI)) {
                c = (PlayableCard) resource.get(i);
                resource.remove(c);
                exit = true;
            }
        }
        c.flip();
        p1.getPlayField().addCard((PlayableCard) c, 2, 0);

        exit = false;
        for (int i = 0; i < resource.size() && !exit; i++) {
            if (((PlayableCard) resource.get(i)).getKingdom().equals(KingdomResource.FUNGI)) {
                c = (PlayableCard) resource.get(i);
                resource.remove(c);
                exit = true;
            }
        }
        c.flip();
        p1.getPlayField().addCard((PlayableCard) c, 3, 0);

        exit = false;
        for (int i = 0; i < resource.size() && !exit; i++) {
            if (((PlayableCard) resource.get(i)).getKingdom().equals(KingdomResource.PLANT)) {
                c = (PlayableCard) resource.get(i);
                resource.remove(c);
                exit = true;
            }
        }
        c.flip();
        p2.getPlayField().addCard((PlayableCard) c, 0, 1);

        exit = false;
        for (int i = 0; i < resource.size() && !exit; i++) {
            if (((PlayableCard) resource.get(i)).getKingdom().equals(KingdomResource.PLANT)) {
                c = (PlayableCard) resource.get(i);
                resource.remove(c);
                exit = true;
            }
        }
        c.flip();
        p2.getPlayField().addCard((PlayableCard) c, 0, 2);

        exit = false;
        for (int i = 0; i < resource.size() && !exit; i++) {
            if (((PlayableCard) resource.get(i)).getKingdom().equals(KingdomResource.PLANT)) {
                c = (PlayableCard) resource.get(i);
                resource.remove(c);
                exit = true;
            }
        }
        c.flip();
        p2.getPlayField().addCard((PlayableCard) c, 0, 3);

        exit = false;
        for (int i = 0; i < resource.size() && !exit; i++) {
            if (((PlayableCard) resource.get(i)).getKingdom().equals(KingdomResource.ANIMAL)) {
                c = (PlayableCard) resource.get(i);
                resource.remove(c);
                exit = true;
            }
        }
        c.flip();
        p3.getPlayField().addCard((PlayableCard) c, 1, 0);

        exit = false;
        for (int i = 0; i < resource.size() && !exit; i++) {
            if (((PlayableCard) resource.get(i)).getKingdom().equals(KingdomResource.ANIMAL)) {
                c = (PlayableCard) resource.get(i);
                resource.remove(c);
                exit = true;
            }
        }
        c.flip();
        p3.getPlayField().addCard((PlayableCard) c, 2, 0);

        exit = false;
        for (int i = 0; i < resource.size() && !exit; i++) {
            if (((PlayableCard) resource.get(i)).getKingdom().equals(KingdomResource.ANIMAL)) {
                c = (PlayableCard) resource.get(i);
                resource.remove(c);
                exit = true;
            }
        }
        c.flip();
        p3.getPlayField().addCard((PlayableCard) c, 3, 0);

        exit = false;
        for (int i = 0; i < resource.size() && !exit; i++) {
            if (((PlayableCard) resource.get(i)).getKingdom().equals(KingdomResource.INSECT)) {
                c = (PlayableCard) resource.get(i);
                resource.remove(c);
                exit = true;
            }
        }
        c.flip();
        p4.getPlayField().addCard((PlayableCard) c, 0, 1);

        exit = false;
        for (int i = 0; i < resource.size() && !exit; i++) {
            if (((PlayableCard) resource.get(i)).getKingdom().equals(KingdomResource.INSECT)) {
                c = (PlayableCard) resource.get(i);
                resource.remove(c);
                exit = true;
            }
        }
        c.flip();
        p4.getPlayField().addCard((PlayableCard) c, 0, 2);

        exit = false;
        for (int i = 0; i < resource.size() && !exit; i++) {
            if (((PlayableCard) resource.get(i)).getKingdom().equals(KingdomResource.INSECT)) {
                c = (PlayableCard) resource.get(i);
                resource.remove(c);
                exit = true;
            }
        }
        c.flip();
        p4.getPlayField().addCard((PlayableCard) c, 0, 3);

        exit = false;
        for (int i = 0; i < resource.size() && !exit; i++) {
            if (((PlayableCard) resource.get(i)).getKingdom().equals(KingdomResource.PLANT)) {
                c = (PlayableCard) resource.get(i);
                resource.remove(c);
                exit = true;
            }
        }
        c.flip();
        p4.getPlayField().addCard((PlayableCard) c, 0, 4);

        exit = false;
        for (int i = 0; i < resource.size() && !exit; i++) {
            if (((PlayableCard) resource.get(i)).getKingdom().equals(KingdomResource.PLANT)) {
                c = (PlayableCard) resource.get(i);
                resource.remove(c);
                exit = true;
            }
        }
        c.flip();
        p4.getPlayField().addCard((PlayableCard) c, 0, 5);

        exit = false;
        for (int i = 0; i < resource.size() && !exit; i++) {
            if (((PlayableCard) resource.get(i)).getKingdom().equals(KingdomResource.PLANT)) {
                c = (PlayableCard) resource.get(i);
                resource.remove(c);
                exit = true;
            }
        }
        c.flip();
        p4.getPlayField().addCard((PlayableCard) c, 0, 6);

        points = game.countPoints();
        assertEquals(0, Integer.valueOf(points.get(0).get("InitialPoints")));
        assertEquals(2, Integer.valueOf(points.get(0).get("SecretObjectivePoints")));
        assertEquals(0, Integer.valueOf(points.get(1).get("InitialPoints")));
        assertEquals(2, Integer.valueOf(points.get(1).get("SecretObjectivePoints")));
        assertEquals(0, Integer.valueOf(points.get(2).get("InitialPoints")));
        assertEquals(2, Integer.valueOf(points.get(2).get("SecretObjectivePoints")));
        assertEquals(0, Integer.valueOf(points.get(3).get("InitialPoints")));
        assertEquals(2, Integer.valueOf(points.get(3).get("SecretObjectivePoints")));
    }
}