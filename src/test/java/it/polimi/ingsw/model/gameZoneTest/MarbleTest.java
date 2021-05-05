package it.polimi.ingsw.model.gameZoneTest;

import it.polimi.ingsw.model.gameZone.marbles.*;
import it.polimi.ingsw.model.leaderCards.ConversionEffect;
import it.polimi.ingsw.model.playerBoard.Board;
import it.polimi.ingsw.model.resources.ChoiceResource;
import it.polimi.ingsw.model.resources.ConcreteResource;
import it.polimi.ingsw.model.resources.Resource;
import it.polimi.ingsw.model.resources.resourceSets.ObtainableResourceSet;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class MarbleTest {
    @Test
    public void constructorTest() {
        Marble yellow = new YellowMarble();
        Marble grey = new GreyMarble();
        Marble blue = new BlueMarble();
        Marble purple = new PurpleMarble();
        Marble red = new RedMarble();
        Marble white = new WhiteMarble();

        assertEquals(MarbleColour.YELLOW, yellow.getColour());
        assertEquals(MarbleColour.GREY, grey.getColour());
        assertEquals(MarbleColour.BLUE, blue.getColour());
        assertEquals(MarbleColour.PURPLE, purple.getColour());
        assertEquals(MarbleColour.RED, red.getColour());
        assertEquals(MarbleColour.WHITE, white.getColour());
    }

    @Test
    public void collectTest() {
        // TODO
    }
}
