package it.polimi.ingsw.model.gameZoneTest;

import it.polimi.ingsw.model.gameZone.MarbleMarket;
import it.polimi.ingsw.model.gameZone.marbles.*;
import it.polimi.ingsw.model.leaderCards.ConversionEffect;
import it.polimi.ingsw.model.playerBoard.Board;
import it.polimi.ingsw.model.resources.ChoiceResource;
import it.polimi.ingsw.model.resources.ChoiceSet;
import it.polimi.ingsw.model.resources.ConcreteResource;
import it.polimi.ingsw.model.resources.Resource;
import it.polimi.ingsw.model.resources.resourceSets.ChoiceResourceSet;
import it.polimi.ingsw.model.resources.resourceSets.ConcreteResourceSet;
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
        Marble yellow = new YellowMarble();
        Marble grey = new GreyMarble();
        Marble blue = new BlueMarble();
        Marble purple = new PurpleMarble();
        Marble red = new RedMarble();
        Marble white = new WhiteMarble();

        ArrayList<ChoiceSet> choiceSets = new ArrayList<>();

        ChoiceSet choiceSetCoin =  new ChoiceSet();
        choiceSetCoin.addChoice(ConcreteResource.COIN);
        choiceSets.add(choiceSetCoin);
        ChoiceSet choiceSetStone =  new ChoiceSet();
        choiceSetStone.addChoice(ConcreteResource.STONE);
        choiceSets.add(choiceSetStone);
        ChoiceSet choiceSetShield =  new ChoiceSet();
        choiceSetShield.addChoice(ConcreteResource.SHIELD);
        choiceSets.add(choiceSetShield);
        ChoiceSet choiceSetServant =  new ChoiceSet();
        choiceSetServant.addChoice(ConcreteResource.SERVANT);
        choiceSets.add(choiceSetServant);
        ChoiceSet choiceSetServantCoin =  new ChoiceSet();
        choiceSetServantCoin.addChoice(ConcreteResource.SERVANT);
        choiceSetServantCoin.addChoice(ConcreteResource.COIN);
        choiceSets.add(choiceSetServantCoin);


        for(ChoiceSet choiceSet: choiceSets) {
            ChoiceResourceSet resourceSet = yellow.collect(choiceSet).getResourceSet();
            int faithPoints = yellow.collect(choiceSet).getFaithPoints();
            assertEquals(0, faithPoints);
            assertEquals(1, resourceSet.size());
            assertEquals(resourceSet.getConcreteResources().getResourceType(), ConcreteResource.COIN);

            resourceSet = grey.collect(choiceSet).getResourceSet();
            faithPoints = grey.collect(choiceSet).getFaithPoints();
            assertEquals(0, faithPoints);
            assertEquals(1, resourceSet.size());
            assertEquals(resourceSet.getConcreteResources().getResourceType(), ConcreteResource.STONE);

            resourceSet = blue.collect(choiceSet).getResourceSet();
            faithPoints = blue.collect(choiceSet).getFaithPoints();
            assertEquals(0, faithPoints);
            assertEquals(1, resourceSet.size());
            assertEquals(resourceSet.getConcreteResources().getResourceType(), ConcreteResource.SHIELD);

            resourceSet = purple.collect(choiceSet).getResourceSet();
            faithPoints = purple.collect(choiceSet).getFaithPoints();
            assertEquals(0, faithPoints);
            assertEquals(1, resourceSet.size());
            assertEquals(resourceSet.getConcreteResources().getResourceType(), ConcreteResource.SERVANT);


            resourceSet = red.collect(choiceSet).getResourceSet();
            faithPoints = red.collect(choiceSet).getFaithPoints();
            assertEquals(0, resourceSet.size());
            assertEquals(1, faithPoints);
        }

        ChoiceResourceSet resourceSet = white.collect(choiceSetCoin).getResourceSet();
        int faithPoints = white.collect(choiceSetCoin).getFaithPoints();
        assertEquals(1, resourceSet.size());
        assertEquals(0, faithPoints);
        assertEquals(resourceSet.getConcreteResources().getResourceType(), ConcreteResource.COIN);

        resourceSet = white.collect(choiceSetShield).getResourceSet();
        faithPoints = white.collect(choiceSetShield).getFaithPoints();
        assertEquals(1, resourceSet.size());
        assertEquals(0, faithPoints);
        assertEquals(resourceSet.getConcreteResources().getResourceType(), ConcreteResource.SHIELD);

        resourceSet = white.collect(choiceSetStone).getResourceSet();
        faithPoints = white.collect(choiceSetStone).getFaithPoints();
        assertEquals(1, resourceSet.size());
        assertEquals(0, faithPoints);
        assertEquals(resourceSet.getConcreteResources().getResourceType(), ConcreteResource.STONE);

        resourceSet = white.collect(choiceSetServant).getResourceSet();
        faithPoints = white.collect(choiceSetServant).getFaithPoints();
        assertEquals(1, resourceSet.size());
        assertEquals(0, faithPoints);
        assertEquals(resourceSet.getConcreteResources().getResourceType(), ConcreteResource.SERVANT);

        resourceSet = white.collect(choiceSetServantCoin).getResourceSet();
        faithPoints = white.collect(choiceSetServantCoin).getFaithPoints();
        assertEquals(1, resourceSet.size());
        assertEquals(0, faithPoints);

        assertTrue(resourceSet.getChoiceResources().get(0).canChoose(ConcreteResource.COIN));
        assertTrue(resourceSet.getChoiceResources().get(0).canChoose(ConcreteResource.SERVANT));
        assertFalse(resourceSet.getChoiceResources().get(0).canChoose(ConcreteResource.STONE));
        assertFalse(resourceSet.getChoiceResources().get(0).canChoose(ConcreteResource.SHIELD));
    }
}
