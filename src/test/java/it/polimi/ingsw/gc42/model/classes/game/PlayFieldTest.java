package it.polimi.ingsw.gc42.model.classes.game;

import it.polimi.ingsw.gc42.model.classes.cards.Coordinates;
import it.polimi.ingsw.gc42.model.classes.cards.PlayableCard;
import it.polimi.ingsw.gc42.model.classes.cards.StarterCard;
import it.polimi.ingsw.gc42.model.exceptions.IllegalActionException;
import it.polimi.ingsw.gc42.model.exceptions.IllegalPlacementException;
import it.polimi.ingsw.gc42.model.exceptions.PlacementConditionNotMetException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PlayFieldTest {

    @Test
    void getAvailablePlacements_StarterCard_Only() {
        Game game = new Game();
        Player player = new Player(Token.BLUE);

        // A Starter Card is flipped, so it always has 4 non-null Corners
        StarterCard starterCard = (StarterCard) game.getStarterDeck().draw();
        starterCard.flip();
        player.setStarterCard(starterCard);
        assertEquals(4, player.getPlayField().getAvailablePlacements().size());
    }

    @Test
    void getAvailablePlacements_AllCornersAvailable() {
        Game game = new Game();
        Player player = new Player(Token.BLUE);

        // A Starter Card is flipped, so it always has 4 non-null Corners
        StarterCard starterCard = (StarterCard) game.getStarterDeck().draw();
        starterCard.flip();
        player.setStarterCard(starterCard);

        try {
            player.drawCard(game.getResourcePlayingDeck());
            player.drawCard(game.getResourcePlayingDeck());
            player.drawCard(game.getResourcePlayingDeck());
        } catch (IllegalActionException e) {
            e.printStackTrace();
        }

        // Draws 7 random ResourceCards and flips them all, so they all have 4 non-null Corners
        ArrayList<PlayableCard> testCards = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            PlayableCard card = (PlayableCard) game.getResourcePlayingDeck().getDeck().draw();
            card.flip();
            testCards.add(card);
        }
        // Plays these Cards in some chosen Coordinates, to create a specific condition where we know how many
        // available spots should be found.
        ArrayList<Coordinates> testCoordinates = new ArrayList<>();
        testCoordinates.add(new Coordinates(-1, 0));
        testCoordinates.add(new Coordinates(-1, -1));
        testCoordinates.add(new Coordinates(1, 0));
        testCoordinates.add(new Coordinates(0, 1));
        testCoordinates.add(new Coordinates(1, 1));
        testCoordinates.add(new Coordinates(1, 2));
        testCoordinates.add(new Coordinates(1, 3));

        for (int i = 0; i < 7; i++) {
            try {
                player.playCard(testCards.get(i), testCoordinates.get(i).getX(), testCoordinates.get(i).getY());
            } catch (IllegalPlacementException | PlacementConditionNotMetException | IllegalActionException e) {
                e.printStackTrace();
            }
        }

        // There should be 13 available spots with those Cards played that way
        assertEquals(13, player.getPlayField().getAvailablePlacements().size());

        // Create an ArrayList containing the Coordinates where those spots are located in this specific situation
        ArrayList<Coordinates> expectedCoordinates = new ArrayList<>();
        expectedCoordinates.add(new Coordinates(1, -1));
        expectedCoordinates.add(new Coordinates(-1, -2));
        expectedCoordinates.add(new Coordinates(-2, -1));
        expectedCoordinates.add(new Coordinates(-2, 0));
        expectedCoordinates.add(new Coordinates(-1, 1));
        expectedCoordinates.add(new Coordinates(0, 2));
        expectedCoordinates.add(new Coordinates(0, -1));
        expectedCoordinates.add(new Coordinates(1, 4));
        expectedCoordinates.add(new Coordinates(0, 3));
        expectedCoordinates.add(new Coordinates(2, 0));
        expectedCoordinates.add(new Coordinates(2, 1));
        expectedCoordinates.add(new Coordinates(2, 2));
        expectedCoordinates.add(new Coordinates(2, 3));

        // Compares the result to see if the available spots it found are the real ones
        ArrayList<Coordinates> actualCoordinates = player.getPlayField().getAvailablePlacements();
        int matchedCoordinates = 0;
        for (Coordinates c : actualCoordinates) {
            for (Coordinates e : expectedCoordinates) {
                if (e.getX() == c.getX() && e.getY() == c.getY()) {
                    matchedCoordinates++;
                }
            }
        }
        // All 13 Coordinates should match
        assertEquals(13, matchedCoordinates);
    }
}