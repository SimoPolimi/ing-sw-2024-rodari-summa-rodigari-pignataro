package it.polimi.ingsw.gc42.model.classes.cards;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PlayableCardTest {
    @Test
    void generalTesting(){
        // given
        int id = 0;
        int earnedPoints = 0;
        CardSide frontSide = null;
        CardSide backSide = null;
        Coordinates coordinates = new Coordinates(1, 1);
        // Resource
        ResourceCard resourceCard = new ResourceCard(frontSide, backSide, true, id, coordinates.getX(), coordinates.getY(), new ArrayList<>(), earnedPoints, null, null, null);
        // Gold
        int fungiPoints = 1;
        int plantPoints = 2;
        int animalPoints = 3;
        int insectPoints = 4;
        GoldCard goldCard = new GoldCard(frontSide, backSide, true, id, coordinates.getX(), coordinates.getY(), new ArrayList<>(), plantPoints, animalPoints, fungiPoints, insectPoints, null, earnedPoints, null, null, KingdomResource.FUNGI);


        // then
        // Resource
        assertEquals(id, resourceCard.getId());
        assertEquals(earnedPoints, resourceCard.getEarnedPoints());
        assertEquals(frontSide, resourceCard.getFrontSide());
        assertEquals(backSide, resourceCard.getBackSide());
        assertEquals(coordinates.getX(), resourceCard.getCoordinates().getX());
        assertEquals(coordinates.getY(), resourceCard.getCoordinates().getY());

        // Gold
        assertEquals(id, goldCard.getId());
        assertEquals(earnedPoints, goldCard.getEarnedPoints());
        assertEquals(frontSide, goldCard.getFrontSide());
        assertEquals(backSide, goldCard.getBackSide());
        assertEquals(coordinates.getX(), goldCard.getCoordinates().getX());
        assertEquals(coordinates.getY(), goldCard.getCoordinates().getY());
        assertEquals(goldCard.getCost(KingdomResource.FUNGI), fungiPoints);
        assertEquals(goldCard.getCost(KingdomResource.PLANT), plantPoints);
        assertEquals(goldCard.getCost(KingdomResource.ANIMAL), animalPoints);
        assertEquals(goldCard.getCost(KingdomResource.INSECT), insectPoints);
    }
}