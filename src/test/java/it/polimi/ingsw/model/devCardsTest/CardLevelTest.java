package it.polimi.ingsw.model.devCardsTest;

import it.polimi.ingsw.model.devCards.CardLevel;

import org.junit.Test;

import static org.junit.Assert.*;

public class CardLevelTest {
    @Test
    public void nextTest(){
        CardLevel level1 = CardLevel.I;
        CardLevel level2 = CardLevel.II;
        CardLevel level3 = CardLevel.III;

        assertSame(level2.next(), level3);
        assertSame(level1.next(), level2);
        assertNotSame(level1.next(),level3);
        assertNotSame(level1.next(),level1);
        assertNotSame(level2.next(),level1);
        assertNotSame(level3.next(),level2);
        assertNotSame(level3.next(),level1);
    }

    @Test
    public void prevTest() {
        CardLevel level1 = CardLevel.I;
        CardLevel level2 = CardLevel.II;
        CardLevel level3 = CardLevel.III;

        assertSame(level2.prev(), level1);
        assertSame(level3.prev(), level2);
        assertNotSame(level1.prev(),level1);
        assertNotSame(level3.prev(),level1);
        assertNotSame(level2.prev(),level3);
    }
}
