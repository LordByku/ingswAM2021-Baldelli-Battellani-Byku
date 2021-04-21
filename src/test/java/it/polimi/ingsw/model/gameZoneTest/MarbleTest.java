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
        Board board = new Board();
        Marble yellow = new YellowMarble();
        Marble grey = new GreyMarble();
        Marble blue = new BlueMarble();
        Marble purple = new PurpleMarble();
        Marble red = new RedMarble();
        Marble white = new WhiteMarble();

        ObtainableResourceSet obtainableResourceSet = yellow.collect(board.getConversionEffectArea().getConversionEffects());

        ArrayList<Resource> resources = obtainableResourceSet.getResourceSet().getResources();

        assertEquals(1, resources.size());
        assertEquals(ConcreteResource.COIN, resources.get(0).getResource());
        assertEquals(0, obtainableResourceSet.getFaithPoints());

        obtainableResourceSet = red.collect(board.getConversionEffectArea().getConversionEffects());

        resources = obtainableResourceSet.getResourceSet().getResources();
        assertTrue(resources.isEmpty());
        assertEquals(1, obtainableResourceSet.getFaithPoints());

        obtainableResourceSet = white.collect(board.getConversionEffectArea().getConversionEffects());

        resources = obtainableResourceSet.getResourceSet().getResources();
        assertTrue(resources.isEmpty());
        assertEquals(0, obtainableResourceSet.getFaithPoints());

        board.getConversionEffectArea().addConversionEffect(new ConversionEffect(ConcreteResource.SHIELD));

        obtainableResourceSet = grey.collect(board.getConversionEffectArea().getConversionEffects());

        resources = obtainableResourceSet.getResourceSet().getResources();
        assertEquals(1, resources.size());
        assertEquals(ConcreteResource.STONE, resources.get(0).getResource());
        assertEquals(0, obtainableResourceSet.getFaithPoints());

        obtainableResourceSet = red.collect(board.getConversionEffectArea().getConversionEffects());

        resources = obtainableResourceSet.getResourceSet().getResources();
        assertTrue(resources.isEmpty());
        assertEquals(1, obtainableResourceSet.getFaithPoints());

        obtainableResourceSet = white.collect(board.getConversionEffectArea().getConversionEffects());

        resources = obtainableResourceSet.getResourceSet().getResources();
        assertEquals(1, resources.size());
        assertEquals(ConcreteResource.SHIELD, resources.get(0).getResource());
        assertEquals(0, obtainableResourceSet.getFaithPoints());

        board.getConversionEffectArea().addConversionEffect(new ConversionEffect(ConcreteResource.COIN));

        obtainableResourceSet = purple.collect(board.getConversionEffectArea().getConversionEffects());

        resources = obtainableResourceSet.getResourceSet().getResources();
        assertEquals(1, resources.size());
        assertEquals(ConcreteResource.SERVANT, resources.get(0).getResource());
        assertEquals(0, obtainableResourceSet.getFaithPoints());

        obtainableResourceSet = red.collect(board.getConversionEffectArea().getConversionEffects());

        resources = obtainableResourceSet.getResourceSet().getResources();
        assertTrue(resources.isEmpty());
        assertEquals(1, obtainableResourceSet.getFaithPoints());

        obtainableResourceSet = white.collect(board.getConversionEffectArea().getConversionEffects());

        resources = obtainableResourceSet.getResourceSet().getResources();
        assertEquals(1, resources.size());
        assertFalse(resources.get(0).isConcrete());
        ChoiceResource choiceResource = (ChoiceResource) resources.get(0);
        assertTrue(choiceResource.canChoose(ConcreteResource.COIN));
        assertTrue(choiceResource.canChoose(ConcreteResource.SHIELD));
        assertFalse(choiceResource.canChoose(ConcreteResource.STONE));
        assertFalse(choiceResource.canChoose(ConcreteResource.SERVANT));
        assertEquals(0, obtainableResourceSet.getFaithPoints());
    }
}
