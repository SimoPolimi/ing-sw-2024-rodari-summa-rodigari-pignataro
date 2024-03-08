package it.polimi.ingsw.gc42;

import it.polimi.ingsw.gc42.HelloApplication;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeckTest {

    @Test
    void testCard() {
        boolean a = HelloApplication.testCard();

        assertTrue(a);
    }
}