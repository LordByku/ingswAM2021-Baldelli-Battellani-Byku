package it.polimi.ingsw.faithTrack;

import it.polimi.ingsw.playerBoard.faithTrack.PopeFavor;
import it.polimi.ingsw.resources.resourceSets.InvalidQuantityException;
import org.junit.Test;

import static org.junit.Assert.*;

public class PopeFavorTest {

    @Test
    public void constructor(){
        PopeFavor popeFavor;
        popeFavor = new PopeFavor(2);
        assertEquals(2, popeFavor.getPoints());

        try {
            popeFavor = new PopeFavor(0);
            fail();
        } catch (InvalidQuantityException e) {
            assertEquals(2, popeFavor.getPoints());
        }
    }
}
