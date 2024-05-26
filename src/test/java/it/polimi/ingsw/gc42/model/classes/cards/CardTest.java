package it.polimi.ingsw.gc42.model.classes.cards;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CardTest {

    @Test
    void generalTesting(){
        // given
        Card card = new Card(true, 0, null, null);
        int id = 0;
        String frontImage = "";
        String backImage = "";

        // when
        card.setId(id);
        card.setFrontImage(frontImage);
        card.setBackImage(backImage);

        // then
        assertEquals(card.getShowingImage(), frontImage);
        assertEquals(card.getId(), id);
        assertEquals(card.getFrontImage(), frontImage);
        assertEquals(card.getBackImage(), backImage);
    }
}
