package it.polimi.ingsw.faithTrack.devCardsTest;

import it.polimi.ingsw.devCards.CardColour;
import org.junit.Test;

import static org.junit.Assert.assertSame;

public class CardColourTest {
    @Test
    public void getColourTest(){
        CardColour blue = CardColour.BLUE;
        CardColour yellow = CardColour.YELLOW;
        CardColour green = CardColour.GREEN;
        CardColour purple  = CardColour.PURPLE;

        assertSame(blue.getColour(), CardColour.BLUE);
        assertSame(yellow.getColour(), CardColour.YELLOW);
        assertSame(green.getColour(), CardColour.GREEN);
        assertSame(purple.getColour(), CardColour.PURPLE);

    }
}
