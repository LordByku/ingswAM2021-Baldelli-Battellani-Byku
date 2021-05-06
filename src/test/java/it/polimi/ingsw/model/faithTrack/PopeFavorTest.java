package it.polimi.ingsw.model.faithTrack;

import it.polimi.ingsw.model.playerBoard.faithTrack.PopeFavor;
import it.polimi.ingsw.model.resources.resourceSets.InvalidQuantityException;
import org.junit.Test;

import static org.junit.Assert.*;

public class PopeFavorTest {

    @Test
    public void constructor(){
        PopeFavor popeFavor;
        popeFavor = new PopeFavor(2, 1005);
        assertEquals(2, popeFavor.getPoints());

        try {
            popeFavor = new PopeFavor(0, 1006);
            fail();
        } catch (InvalidQuantityException e) {
            assertEquals(2, popeFavor.getPoints());
        }
    }
}
