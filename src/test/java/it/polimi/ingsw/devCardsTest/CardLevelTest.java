package it.polimi.ingsw.devCardsTest;

import it.polimi.ingsw.devCards.CardLevel;

import org.junit.Test;

import static org.junit.Assert.*;

public class CardLevelTest {
    @Test
    public void getLevelTest(){
        CardLevel level1 = CardLevel.I;
        CardLevel level2 = CardLevel.II;
        CardLevel level3 = CardLevel.III;

        assertSame(level1.getLevel(),CardLevel.I);
        assertSame(level2.getLevel(),CardLevel.II);
        assertSame(level3.getLevel(),CardLevel.III);
    }
    @Test
    public void NextNPrevTest(){
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

        assertSame(level2.prev(), level1);
        assertSame(level3.prev(), level2);
        assertNotSame(level1.prev(),level1);
        assertNotSame(level3.prev(),level1);
        assertNotSame(level2.prev(),level3);

    }
}
