package it.polimi.ingsw.gc42.model.classes.cards;

import it.polimi.ingsw.gc42.model.classes.game.Game;
import it.polimi.ingsw.gc42.model.classes.game.Player;
import it.polimi.ingsw.gc42.model.classes.game.Token;
import it.polimi.ingsw.gc42.model.exceptions.IllegalPlacementException;
import it.polimi.ingsw.gc42.model.exceptions.PlacementConditionNotMetException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CornerTest {

    @Test
    void isCovered() {
        Game game = new Game();
        Player player = new Player(Token.BLUE);

        // Checks if all 4 are not covered at first
        PlayableCard card = (PlayableCard) game.getStarterDeck().draw();
        card.flip();
        int coveredCornersAtFirst = 0;
        if (card.getShowingSide().getTopLeftCorner().isCovered()) {
            coveredCornersAtFirst++;
        }
        if (card.getShowingSide().getTopRightCorner().isCovered()) {
            coveredCornersAtFirst++;
        }
        if (card.getShowingSide().getBottomLeftCorner().isCovered()) {
            coveredCornersAtFirst++;
        }
        if (card.getShowingSide().getBottomRightCorner().isCovered()) {
            coveredCornersAtFirst++;
        }
        assertEquals(0, coveredCornersAtFirst);

        player.setStarterCard((StarterCard) card);

        // Places 4 Cards in the nearby positions, so they cover the StarterCard's corners.
        PlayableCard card2 = (PlayableCard) game.getResourcePlayingDeck().getDeck().draw();
        try {
            player.playCard(card2, 1, 0);
            card2 = (PlayableCard) game.getResourcePlayingDeck().getDeck().draw();
            player.playCard(card2, 0, 1);
            card2 = (PlayableCard) game.getResourcePlayingDeck().getDeck().draw();
            player.playCard(card2, -1, 0);
            card2 = (PlayableCard) game.getResourcePlayingDeck().getDeck().draw();
            player.playCard(card2, 0, -1);
        } catch (IllegalPlacementException | PlacementConditionNotMetException e) {
            e.printStackTrace();
        }

        // Checks if all the 4 Corners are now covered in that same Starter Card
        int coveredCornersAtLast = 0;
        if (card.getShowingSide().getTopLeftCorner().isCovered()) {
            coveredCornersAtLast++;
        }
        if (card.getShowingSide().getTopRightCorner().isCovered()) {
            coveredCornersAtLast++;
        }
        if (card.getShowingSide().getBottomLeftCorner().isCovered()) {
            coveredCornersAtLast++;
        }
        if (card.getShowingSide().getBottomRightCorner().isCovered()) {
            coveredCornersAtLast++;
        }
        assertEquals(4, coveredCornersAtLast);
    }
}