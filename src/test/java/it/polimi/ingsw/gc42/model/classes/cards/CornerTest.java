package it.polimi.ingsw.gc42.model.classes.cards;

import it.polimi.ingsw.gc42.model.classes.game.Game;
import it.polimi.ingsw.gc42.model.classes.game.Player;
import it.polimi.ingsw.gc42.model.classes.game.Token;
import it.polimi.ingsw.gc42.model.exceptions.IllegalActionException;
import it.polimi.ingsw.gc42.model.exceptions.IllegalPlacementException;
import it.polimi.ingsw.gc42.model.exceptions.PlacementConditionNotMetException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CornerTest {

    @Test
    void generalTesting(){
        Item item = Resource.FEATHER;
        Corner corner = new Corner(item, false);
        assertEquals(corner.getItem(), item);

        // now set
        Item item2 = Resource.POTION;
        corner.setItem(item2);
        assertEquals(corner.getItem(), item2);
    }

    @Test
    void isCovered() {
        Game game = new Game();
        Player player = new Player(Token.BLUE);

        // Checks if all 4 are not covered at first
        PlayableCard card = (PlayableCard) game.getStarterDeck().draw();
        card.flip();
        int coveredCornersAtFirst = 0;
        if (card.getShowingSide().topLeftCorner().isCovered()) {
            coveredCornersAtFirst++;
        }
        if (card.getShowingSide().topRightCorner().isCovered()) {
            coveredCornersAtFirst++;
        }
        if (card.getShowingSide().bottomLeftCorner().isCovered()) {
            coveredCornersAtFirst++;
        }
        if (card.getShowingSide().bottomRightCorner().isCovered()) {
            coveredCornersAtFirst++;
        }
        assertEquals(0, coveredCornersAtFirst);

        player.setTemporaryStarterCard((StarterCard) card);
        player.setStarterCard();

        // Places 4 Cards in the nearby positions, so they cover the StarterCard's corners.
        PlayableCard card2 = (PlayableCard) game.getResourcePlayingDeck().getDeck().draw();
        try {


            player.setHandCard(0, card2);
            player.drawCard(game.getResourcePlayingDeck());
            player.drawCard(game.getResourcePlayingDeck());
            player.playCard(1, 1, 0);
            player.drawCard(game.getResourcePlayingDeck());
            card2 = (PlayableCard) game.getResourcePlayingDeck().getDeck().draw();
            player.setHandCard(0, card2);
            player.playCard(1, 0, 1);
            player.drawCard(game.getResourcePlayingDeck());
            card2 = (PlayableCard) game.getResourcePlayingDeck().getDeck().draw();
            player.setHandCard(0, card2);
            player.playCard(1, -1, 0);
            player.drawCard(game.getResourcePlayingDeck());
            card2 = (PlayableCard) game.getResourcePlayingDeck().getDeck().draw();
            player.setHandCard(0, card2);
            player.playCard(1, 0, -1);
            player.drawCard(game.getResourcePlayingDeck());
        } catch (IllegalPlacementException | PlacementConditionNotMetException | IllegalActionException e) {
            e.printStackTrace();
        }

        // Checks if all the 4 Corners are now covered in that same Starter Card
        int coveredCornersAtLast = 0;
        if (card.getShowingSide().topLeftCorner().isCovered()) {
            coveredCornersAtLast++;
        }
        if (card.getShowingSide().topRightCorner().isCovered()) {
            coveredCornersAtLast++;
        }
        if (card.getShowingSide().bottomLeftCorner().isCovered()) {
            coveredCornersAtLast++;
        }
        if (card.getShowingSide().bottomRightCorner().isCovered()) {
            coveredCornersAtLast++;
        }
        assertEquals(4, coveredCornersAtLast);
    }
}