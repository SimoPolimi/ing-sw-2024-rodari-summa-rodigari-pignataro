package it.polimi.ingsw.gc42;

import it.polimi.ingsw.gc42.model.classes.cards.*;
import it.polimi.ingsw.gc42.model.classes.game.Game;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Collections;

public class ObjectiveTest {

    @Test
    void kingdomCountObjective() {
        Game game = new Game();
        ArrayList<PlayableCard> playArea = new ArrayList<>();
        ItemCountObjective objective = new ItemCountObjective(2, 1, KingdomResource.FUNGI, null, null);
        int num = game.getResourcePlayingDeck().getDeck().getNumberOfCards();
        int count = 0;
        for (int i = 0; i < num; i++) {
            ResourceCard card = (ResourceCard) game.getResourcePlayingDeck().getDeck().draw();
            playArea.add(card);
            if (card.getShowingSide().getTopLeftCorner() != null &&
                    !card.getShowingSide().getTopLeftCorner().isCovered() &&
                    (card.getShowingSide().getTopLeftCorner()).getItem() == KingdomResource.FUNGI) {
                count++;
            }
            if (card.getShowingSide().getTopRightCorner() != null &&
                    !card.getShowingSide().getTopRightCorner().isCovered() &&
                    (card.getShowingSide().getTopRightCorner()).getItem() == KingdomResource.FUNGI) {
                count++;
            }
            if (card.getShowingSide().getBottomLeftCorner() != null &&
                    !card.getShowingSide().getBottomLeftCorner().isCovered() &&
                    (card.getShowingSide().getBottomLeftCorner()).getItem() == KingdomResource.FUNGI) {
                count++;
            }
            if (card.getShowingSide().getBottomRightCorner() != null &&
                    !card.getShowingSide().getBottomRightCorner().isCovered() &&
                    (card.getShowingSide().getBottomRightCorner()).getItem() == KingdomResource.FUNGI) {
                count++;
            }
            if (!card.isFrontFacing() && card.getPermanentResources().getFirst() == KingdomResource.FUNGI) {
                count++;
            }
        }

        assertEquals(count * objective.getPoints(), objective.calculatePoints(playArea));
    }

    @Test
    void kingdomCountObjectiveAllFronts() {
        Game game = new Game();
        ArrayList<PlayableCard> test = new ArrayList<>();
        ItemCountObjective objective = new ItemCountObjective(1, 1, KingdomResource.FUNGI, null, null);
        int num = game.getResourcePlayingDeck().getDeck().getNumberOfCards();

        // Extracting the Cards with ID 1, 4, 16, 27 and 36.
        // Those Cards contain exactly 7 Fungi Resources on their Front Sides.
        switch (game.getResourcePlayingDeck().getSlot(1).getId()) {
            case 1, 4, 16, 27, 36:
                test.add((PlayableCard) game.getResourcePlayingDeck().getSlot(1));
                break;
            default:
                break;
        }
        switch (game.getResourcePlayingDeck().getSlot(2).getId()) {
            case 1, 4, 16, 27, 36:
                test.add((PlayableCard) game.getResourcePlayingDeck().getSlot(2));
                break;
            default:
                break;
        }
        for (int i = 0; i < num; i++) {
            PlayableCard card = (PlayableCard) game.getResourcePlayingDeck().getDeck().draw();
            switch (card.getId()) {
                case 1, 4, 16, 27, 36:
                    test.add(card);
                    break;
                default:
                    break;
            }
        }

        assertEquals(7, objective.calculatePoints(test));
    }

    @Test
    void kingdomCountObjectiveFrontAndBack() {
        Game game = new Game();
        ArrayList<PlayableCard> test = new ArrayList<>();
        ItemCountObjective objective = new ItemCountObjective(1, 1, KingdomResource.FUNGI, null, null);
        int num = game.getResourcePlayingDeck().getDeck().getNumberOfCards();

        // Extracting the Cards with ID 1, 4, 16, 27 and 36.
        // 1 and 16 will be flipped, so their Corners are Empty, but their Permanent Resource is visible.
        // 1 has a Fungi Permanent Resource, 16 a Plant one.
        // Those Cards contain exactly 5 Fungi Resources.
        switch (game.getResourcePlayingDeck().getSlot(1).getId()) {
            case 4, 27, 36:
                test.add((PlayableCard) game.getResourcePlayingDeck().getSlot(1));
                break;
            case 1, 16:
                game.getResourcePlayingDeck().getSlot(1).flip();
                test.add((PlayableCard) game.getResourcePlayingDeck().getSlot(1));
                break;
            default:
                break;
        }
        switch (game.getResourcePlayingDeck().getSlot(2).getId()) {
            case 4, 27, 36:
                test.add((PlayableCard) game.getResourcePlayingDeck().getSlot(2));
                break;
            case 1, 16:
                game.getResourcePlayingDeck().getSlot(2).flip();
                test.add((PlayableCard) game.getResourcePlayingDeck().getSlot(2));
                break;
            default:
                break;
        }
        for (int i = 0; i < num; i++) {
            PlayableCard card = (PlayableCard) game.getResourcePlayingDeck().getDeck().draw();
            switch (card.getId()) {
                case 4, 27, 36:
                    test.add(card);
                    break;
                case 1, 16:
                    card.flip();
                    test.add(card);
                default:
                    break;
            }
        }

        assertEquals(5, objective.calculatePoints(test));
    }

    @Test
    void resourceCountObjective() {
        Game game = new Game();
        ArrayList<PlayableCard> playArea = new ArrayList<>();
        ItemCountObjective objective = new ItemCountObjective(2, 1, Resource.FEATHER, null, null);
        int num = game.getResourcePlayingDeck().getDeck().getNumberOfCards();
        int count = 0;
        for (int i = 0; i < num; i++) {
            ResourceCard card = (ResourceCard) game.getResourcePlayingDeck().getDeck().draw();
            playArea.add(card);
            if (card.getShowingSide().getTopLeftCorner() != null
                    && (card.getShowingSide().getTopLeftCorner()).getItem() == Resource.FEATHER) {
                count++;
            }
            if (card.getShowingSide().getTopRightCorner() != null
                    && (card.getShowingSide().getTopRightCorner()).getItem() == Resource.FEATHER) {
                count++;
            }
            if (card.getShowingSide().getBottomLeftCorner() != null
                    && (card.getShowingSide().getBottomLeftCorner()).getItem() == Resource.FEATHER) {
                count++;
            }
            if (card.getShowingSide().getBottomRightCorner() != null
                    && (card.getShowingSide().getBottomRightCorner()).getItem() == Resource.FEATHER) {
                count++;
            }
        }

        assertEquals(count * objective.getPoints(), objective.calculatePoints(playArea));
    }

    @Test
    void setItemCountObjective() {
        Game game = new Game();
        SetItemCountObjective objective = new SetItemCountObjective(1, 1, null, null);
        int feathers = 0;
        int potions = 0;
        int scrolls = 0;
        int num = game.getResourcePlayingDeck().getDeck().getNumberOfCards();
        ArrayList<PlayableCard> playArea = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            ResourceCard card = (ResourceCard) game.getResourcePlayingDeck().getDeck().draw();
            playArea.add(card);
            if (card.getShowingSide().getTopLeftCorner() != null) {
                if ((card.getShowingSide().getTopLeftCorner()).getItem() == Resource.FEATHER) {
                    feathers++;
                } else if ((card.getShowingSide().getTopLeftCorner()).getItem() == Resource.POTION) {
                    potions++;
                } else if ((card.getShowingSide().getTopLeftCorner()).getItem() == Resource.SCROLL) {
                    scrolls++;
                }
            }
            if (card.getShowingSide().getTopRightCorner() != null) {
                if ((card.getShowingSide().getTopRightCorner()).getItem() == Resource.FEATHER) {
                    feathers++;
                } else if ((card.getShowingSide().getTopRightCorner()).getItem() == Resource.POTION) {
                    potions++;
                } else if ((card.getShowingSide().getTopRightCorner()).getItem() == Resource.SCROLL) {
                    scrolls++;
                }
            }
            if (card.getShowingSide().getBottomLeftCorner() != null) {
                if ((card.getShowingSide().getBottomLeftCorner()).getItem() == Resource.FEATHER) {
                    feathers++;
                } else if ((card.getShowingSide().getBottomLeftCorner()).getItem() == Resource.POTION) {
                    potions++;
                } else if ((card.getShowingSide().getBottomLeftCorner()).getItem() == Resource.SCROLL) {
                    scrolls++;
                }
            }
            if (card.getShowingSide().getBottomRightCorner() != null) {
                if ((card.getShowingSide().getBottomRightCorner()).getItem() == Resource.FEATHER) {
                    feathers++;
                } else if ((card.getShowingSide().getBottomRightCorner()).getItem() == Resource.POTION) {
                    potions++;
                } else if ((card.getShowingSide().getBottomRightCorner()).getItem() == Resource.SCROLL) {
                    scrolls++;
                }
            }
        }
        ArrayList<Integer> container = new ArrayList<>();
        container.add(potions);
        container.add(feathers);
        container.add(scrolls);
        int result = (int) Math.floor((Collections.min(container).doubleValue() / container.size()));

        assertEquals(result * objective.getPoints(), objective.calculatePoints(playArea));
    }

}
