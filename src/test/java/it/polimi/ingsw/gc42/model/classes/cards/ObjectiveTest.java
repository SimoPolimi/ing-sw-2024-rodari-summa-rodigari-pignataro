package it.polimi.ingsw.gc42.model.classes.cards;

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
            if (card.getShowingSide().topLeftCorner() != null &&
                    !card.getShowingSide().topLeftCorner().isCovered() &&
                    (card.getShowingSide().topLeftCorner()).getItem() == KingdomResource.FUNGI) {
                count++;
            }
            if (card.getShowingSide().topRightCorner() != null &&
                    !card.getShowingSide().topRightCorner().isCovered() &&
                    (card.getShowingSide().topRightCorner()).getItem() == KingdomResource.FUNGI) {
                count++;
            }
            if (card.getShowingSide().bottomLeftCorner() != null &&
                    !card.getShowingSide().bottomLeftCorner().isCovered() &&
                    (card.getShowingSide().bottomLeftCorner()).getItem() == KingdomResource.FUNGI) {
                count++;
            }
            if (card.getShowingSide().bottomRightCorner() != null &&
                    !card.getShowingSide().bottomRightCorner().isCovered() &&
                    (card.getShowingSide().bottomRightCorner()).getItem() == KingdomResource.FUNGI) {
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
            if (card.getShowingSide().topLeftCorner() != null
                    && (card.getShowingSide().topLeftCorner()).getItem() == Resource.FEATHER) {
                count++;
            }
            if (card.getShowingSide().topRightCorner() != null
                    && (card.getShowingSide().topRightCorner()).getItem() == Resource.FEATHER) {
                count++;
            }
            if (card.getShowingSide().bottomLeftCorner() != null
                    && (card.getShowingSide().bottomLeftCorner()).getItem() == Resource.FEATHER) {
                count++;
            }
            if (card.getShowingSide().bottomRightCorner() != null
                    && (card.getShowingSide().bottomRightCorner()).getItem() == Resource.FEATHER) {
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
            if (card.getShowingSide().topLeftCorner() != null) {
                if ((card.getShowingSide().topLeftCorner()).getItem() == Resource.FEATHER) {
                    feathers++;
                } else if ((card.getShowingSide().topLeftCorner()).getItem() == Resource.POTION) {
                    potions++;
                } else if ((card.getShowingSide().topLeftCorner()).getItem() == Resource.SCROLL) {
                    scrolls++;
                }
            }
            if (card.getShowingSide().topRightCorner() != null) {
                if ((card.getShowingSide().topRightCorner()).getItem() == Resource.FEATHER) {
                    feathers++;
                } else if ((card.getShowingSide().topRightCorner()).getItem() == Resource.POTION) {
                    potions++;
                } else if ((card.getShowingSide().topRightCorner()).getItem() == Resource.SCROLL) {
                    scrolls++;
                }
            }
            if (card.getShowingSide().bottomLeftCorner() != null) {
                if ((card.getShowingSide().bottomLeftCorner()).getItem() == Resource.FEATHER) {
                    feathers++;
                } else if ((card.getShowingSide().bottomLeftCorner()).getItem() == Resource.POTION) {
                    potions++;
                } else if ((card.getShowingSide().bottomLeftCorner()).getItem() == Resource.SCROLL) {
                    scrolls++;
                }
            }
            if (card.getShowingSide().bottomRightCorner() != null) {
                if ((card.getShowingSide().bottomRightCorner()).getItem() == Resource.FEATHER) {
                    feathers++;
                } else if ((card.getShowingSide().bottomRightCorner()).getItem() == Resource.POTION) {
                    potions++;
                } else if ((card.getShowingSide().bottomRightCorner()).getItem() == Resource.SCROLL) {
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

    @Test
    // Test Objective Setters and Getters
    void testGettersAndSetters() {
        Objective  objective =  new ItemCountObjective(1, 1, Resource.FEATHER, "A", "A");

        assertEquals(objective.getName(), "A");
        assertEquals(objective.getPoints(), 1);
        assertEquals(objective.getDescription(), "A");
    }

}
