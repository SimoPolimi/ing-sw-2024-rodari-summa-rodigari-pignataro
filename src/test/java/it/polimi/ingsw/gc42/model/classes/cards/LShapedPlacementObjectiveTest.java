package it.polimi.ingsw.gc42.model.classes.cards;

import it.polimi.ingsw.gc42.model.classes.game.Game;
import it.polimi.ingsw.gc42.model.classes.game.PlayField;
import it.polimi.ingsw.gc42.model.classes.game.Player;
import it.polimi.ingsw.gc42.model.classes.game.Token;
import it.polimi.ingsw.gc42.model.exceptions.IllegalActionException;
import it.polimi.ingsw.gc42.model.exceptions.IllegalPlacementException;
import it.polimi.ingsw.gc42.model.exceptions.PlacementConditionNotMetException;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.util.ArrayList;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class LShapedPlacementObjectiveTest {

    @Test
    void check() {
        Game g = new Game();
        PlayField field = new PlayField();

        ArrayList<Card> countObjectives = g.getObjectivePlayingDeck().getDeck().getCopy();
        ObjectiveCard obj = null;

        if (g.getObjectivePlayingDeck().getSlot(1).getId() == 91) {
            obj = (ObjectiveCard) g.getObjectivePlayingDeck().getSlot(1);
        } else if (g.getObjectivePlayingDeck().getSlot(2).getId() == 91) {
            obj = (ObjectiveCard) g.getObjectivePlayingDeck().getSlot(2);
        } else {
            for (Card c : countObjectives) {
                if (c.getId() == 91) {
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

        ArrayList<PlayableCard> animal = new ArrayList<>();
        ArrayList<PlayableCard> plant = new ArrayList<>();
        ArrayList<PlayableCard> insect = new ArrayList<>();
        ArrayList<PlayableCard> fungi = new ArrayList<>();
        for (PlayableCard c : deck) {
            c.flip();
            // Separate cards based on their kingdom
            switch (c.getKingdom()) {
                case ANIMAL -> animal.add(c);
                case PLANT -> plant.add(c);
                case INSECT -> insect.add(c);
                case FUNGI ->  fungi.add(c);
            }
        }

        try {
            field.addCard(fungi.removeFirst(), 1, 0);
            for (PlayableCard c : field.getPlayedCards()) {
                System.out.println(c.getX()+" "+c.getY()+" "+c.getKingdom());
            }
            field.addCard(fungi.removeFirst(), 0, -1);
            for (PlayableCard c : field.getPlayedCards()) {
                System.out.println(c.getX()+" "+c.getY()+" "+c.getKingdom());
            }
            field.addCard(plant.removeFirst(), 0, -2);
            for (PlayableCard c : field.getPlayedCards()) {
                System.out.println(c.getX()+" "+c.getY()+" "+c.getKingdom());
            }
            field.addCard(fungi.getFirst(), -1, -2);
            field.addCard(plant.getFirst(), -1, -3);
        } catch (IllegalPlacementException e) {
            throw new RuntimeException(e);
        }

        // Earn 3 points every matching pattern, excluding the ones containing at least one card included in another pattern
        // On the field there are 2 patterns, but one card is included in both, so only one pattern is counted
        assertEquals(3, obj.getObjective().calculatePoints(field.getPlayedCards()));
    }
}