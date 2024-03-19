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
        ArrayList<Card> playArea = new ArrayList<>();
        KingdomCountObjective objective = new KingdomCountObjective(2, 1, KingdomResource.FUNGI, null);
        int num = game.getResourcePlayingDeck().getDeck().getCardsNumber();
        int count = 0;
        for (int i = 0; i < num; i++) {
            ResourceCard card = (ResourceCard) game.getResourcePlayingDeck().getDeck().draw();
            playArea.add(card);
            if (card.getShowingSide().getTopLeftCorner() instanceof KingdomCorner &&
                    !card.getShowingSide().getTopLeftCorner().isCovered() &&
                    ((KingdomCorner) card.getShowingSide().getTopLeftCorner()).getKingdom() == KingdomResource.FUNGI) {
                count++;
            }
            if (card.getShowingSide().getTopRightCorner() instanceof KingdomCorner &&
                    !card.getShowingSide().getTopRightCorner().isCovered() &&
                    ((KingdomCorner) card.getShowingSide().getTopRightCorner()).getKingdom() == KingdomResource.FUNGI) {
                count++;
            }
            if (card.getShowingSide().getBottomLeftCorner() instanceof KingdomCorner &&
                    !card.getShowingSide().getBottomLeftCorner().isCovered() &&
                    ((KingdomCorner) card.getShowingSide().getBottomLeftCorner()).getKingdom() == KingdomResource.FUNGI) {
                count++;
            }
            if (card.getShowingSide().getBottomRightCorner() instanceof KingdomCorner &&
                    !card.getShowingSide().getBottomRightCorner().isCovered() &&
                    ((KingdomCorner) card.getShowingSide().getBottomRightCorner()).getKingdom() == KingdomResource.FUNGI) {
                count++;
            }
            if (card.getPermanentResource() == KingdomResource.FUNGI) {
                count++;
            }
        }

        assertEquals(count * objective.getPoints(), objective.calculatePoints(playArea));
    }

    @Test
    void resourceCountObjective() {
        Game game = new Game();
        ArrayList<Card> playArea = new ArrayList<>();
        ItemCountObjective objective = new ItemCountObjective(2, 1, Resource.FEATHER, null);
        int num = game.getResourcePlayingDeck().getDeck().getCardsNumber();
        int count = 0;
        for (int i = 0; i < num; i++) {
            ResourceCard card = (ResourceCard) game.getResourcePlayingDeck().getDeck().draw();
            playArea.add(card);
            if (card.getShowingSide().getTopLeftCorner() instanceof ResourceCorner
                    && ((ResourceCorner) card.getShowingSide().getTopLeftCorner()).getResource() == Resource.FEATHER) {
                count++;
            }
            if (card.getShowingSide().getTopRightCorner() instanceof ResourceCorner
                    && ((ResourceCorner) card.getShowingSide().getTopRightCorner()).getResource() == Resource.FEATHER) {
                count++;
            }
            if (card.getShowingSide().getBottomLeftCorner() instanceof ResourceCorner
                    && ((ResourceCorner) card.getShowingSide().getBottomLeftCorner()).getResource() == Resource.FEATHER) {
                count++;
            }
            if (card.getShowingSide().getBottomRightCorner() instanceof ResourceCorner
                    && ((ResourceCorner) card.getShowingSide().getBottomRightCorner()).getResource() == Resource.FEATHER) {
                count++;
            }
        }

        assertEquals(count * objective.getPoints(), objective.calculatePoints(playArea));
    }

    @Test
    void setItemCountObjective() {
        Game game = new Game();
        SetItemCountObjective objective = new SetItemCountObjective(1, 1, null);
        int feathers = 0;
        int potions = 0;
        int scrolls = 0;
        int num = game.getResourcePlayingDeck().getDeck().getCardsNumber();
        ArrayList<Card> playArea = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            ResourceCard card = (ResourceCard) game.getResourcePlayingDeck().getDeck().draw();
            playArea.add(card);
            if (card.getShowingSide().getTopLeftCorner() instanceof ResourceCorner) {
                if (((ResourceCorner) card.getShowingSide().getTopLeftCorner()).getResource() == Resource.FEATHER) {
                    feathers++;
                } else if (((ResourceCorner) card.getShowingSide().getTopLeftCorner()).getResource() == Resource.POTION) {
                    potions++;
                } else if (((ResourceCorner) card.getShowingSide().getTopLeftCorner()).getResource() == Resource.SCROLL) {
                    scrolls++;
                }
            }
            if (card.getShowingSide().getTopRightCorner() instanceof ResourceCorner) {
                if (((ResourceCorner) card.getShowingSide().getTopRightCorner()).getResource() == Resource.FEATHER) {
                    feathers++;
                } else if (((ResourceCorner) card.getShowingSide().getTopRightCorner()).getResource() == Resource.POTION) {
                    potions++;
                } else if (((ResourceCorner) card.getShowingSide().getTopRightCorner()).getResource() == Resource.SCROLL) {
                    scrolls++;
                }
            }
            if (card.getShowingSide().getBottomLeftCorner() instanceof ResourceCorner) {
                if (((ResourceCorner) card.getShowingSide().getBottomLeftCorner()).getResource() == Resource.FEATHER) {
                    feathers++;
                } else if (((ResourceCorner) card.getShowingSide().getBottomLeftCorner()).getResource() == Resource.POTION) {
                    potions++;
                } else if (((ResourceCorner) card.getShowingSide().getBottomLeftCorner()).getResource() == Resource.SCROLL) {
                    scrolls++;
                }
            }
            if (card.getShowingSide().getBottomRightCorner() instanceof ResourceCorner) {
                if (((ResourceCorner) card.getShowingSide().getBottomRightCorner()).getResource() == Resource.FEATHER) {
                    feathers++;
                } else if (((ResourceCorner) card.getShowingSide().getBottomRightCorner()).getResource() == Resource.POTION) {
                    potions++;
                } else if (((ResourceCorner) card.getShowingSide().getBottomRightCorner()).getResource() == Resource.SCROLL) {
                    scrolls++;
                }
            }
        }
        ArrayList<Integer> container = new ArrayList<>();
        container.add(potions);
        container.add(feathers);
        container.add(scrolls);
        int result = (int) Math.floor((Collections.min( container).doubleValue() / container.size()));

        assertEquals(result * objective.getPoints(), objective.calculatePoints(playArea));
    }

}
