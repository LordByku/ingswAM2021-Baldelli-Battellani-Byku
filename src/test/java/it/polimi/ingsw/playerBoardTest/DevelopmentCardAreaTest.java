package it.polimi.ingsw.playerBoardTest;

import it.polimi.ingsw.devCards.*;
import it.polimi.ingsw.playerBoard.DevelopmentCardArea;
import it.polimi.ingsw.resources.ConcreteResource;
import it.polimi.ingsw.resources.resourceSets.ChoiceResourceSet;
import it.polimi.ingsw.resources.resourceSets.ConcreteResourceSet;
import it.polimi.ingsw.resources.resourceSets.ObtainableResourceSet;
import it.polimi.ingsw.resources.resourceSets.SpendableResourceSet;
import org.junit.Test;
import org.junit.experimental.theories.suppliers.TestedOn;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class DevelopmentCardAreaTest {
    @Test
    public void constructorTest() {
        DevelopmentCardArea developmentCardArea = new DevelopmentCardArea();

        ArrayList<DevCard> cards = developmentCardArea.getCards();

        assertTrue(cards.isEmpty());
    }

    @Test
    public void invalidAddDevCardTest() {
        DevelopmentCardArea developmentCardArea = new DevelopmentCardArea();

        try {
            developmentCardArea.addDevCard(null, 0);
            fail();
        } catch (InvalidDevCardException e) {
            ArrayList<DevCard> cards = developmentCardArea.getCards();
            assertTrue(cards.isEmpty());
        }

        ConcreteResourceSet concreteResourceSet = new ConcreteResourceSet();
        concreteResourceSet.addResource(ConcreteResource.COIN);

        ChoiceResourceSet inputChoiceResourceSet = new ChoiceResourceSet();
        ChoiceResourceSet outputChoiceResourceSet = new ChoiceResourceSet();

        inputChoiceResourceSet.addResource(ConcreteResource.STONE);
        inputChoiceResourceSet.addResource(ConcreteResource.STONE);
        outputChoiceResourceSet.addResource(ConcreteResource.SHIELD);

        ProductionDetails productionDetails = new ProductionDetails(
                new SpendableResourceSet(inputChoiceResourceSet),
                new ObtainableResourceSet(outputChoiceResourceSet, 2));

        DevCard devCard = new DevCard(concreteResourceSet, CardColour.BLUE, CardLevel.I, productionDetails, 1);

        try {
            developmentCardArea.addDevCard(devCard, 3);
            fail();
        } catch (InvalidDevCardDeckException e) {
            ArrayList<DevCard> cards = developmentCardArea.getCards();
            assertTrue(cards.isEmpty());
        }
    }

    @Test
    public void addDevCardTest() {
        DevelopmentCardArea developmentCardArea = new DevelopmentCardArea();

        ConcreteResourceSet concreteResourceSet = new ConcreteResourceSet();
        concreteResourceSet.addResource(ConcreteResource.COIN);
        concreteResourceSet.addResource(ConcreteResource.STONE);

        ChoiceResourceSet inputChoiceResourceSet = new ChoiceResourceSet();
        ChoiceResourceSet outputChoiceResourceSet = new ChoiceResourceSet();

        inputChoiceResourceSet.addResource(ConcreteResource.COIN);
        outputChoiceResourceSet.addResource(ConcreteResource.SHIELD);

        ProductionDetails productionDetails = new ProductionDetails(
                new SpendableResourceSet(inputChoiceResourceSet),
                new ObtainableResourceSet(outputChoiceResourceSet, 1));

        DevCard devCard = new DevCard(concreteResourceSet, CardColour.BLUE, CardLevel.I, productionDetails, 1);

        developmentCardArea.addDevCard(devCard, 1);

        devCard = new DevCard(concreteResourceSet, CardColour.YELLOW, CardLevel.II, productionDetails, 2);

        try {
            developmentCardArea.addDevCard(devCard, 0);
            fail();
        } catch (InvalidAddTopException e) {
            ArrayList<DevCard> devCards = developmentCardArea.getCards();

            assertEquals(1, devCards.size());
            DevCard devCard1 = devCards.get(0);

            assertEquals(CardColour.BLUE, devCard1.getColour());
            assertEquals(CardLevel.I, devCard1.getLevel());
        }

        developmentCardArea.addDevCard(devCard, 1);

        ArrayList<DevCard> devCards = developmentCardArea.getCards();
        assertEquals(2, devCards.size());
        DevCard devCard1 = devCards.get(0);
        DevCard devCard2 = devCards.get(1);

        assertEquals(CardColour.BLUE, devCard1.getColour());
        assertEquals(CardLevel.I, devCard1.getLevel());

        assertEquals(CardColour.YELLOW, devCard2.getColour());
        assertEquals(CardLevel.II, devCard2.getLevel());
    }

    @Test
    public void getTopLevel() {
        DevelopmentCardArea developmentCardArea = new DevelopmentCardArea();

        ConcreteResourceSet concreteResourceSet = new ConcreteResourceSet();
        concreteResourceSet.addResource(ConcreteResource.COIN);
        concreteResourceSet.addResource(ConcreteResource.SHIELD);

        ChoiceResourceSet inputChoiceResourceSet = new ChoiceResourceSet();
        ChoiceResourceSet outputChoiceResourceSet = new ChoiceResourceSet();

        inputChoiceResourceSet.addResource(ConcreteResource.STONE);
        outputChoiceResourceSet.addResource(ConcreteResource.SHIELD);
        outputChoiceResourceSet.addResource(ConcreteResource.SERVANT);

        ProductionDetails productionDetails = new ProductionDetails(
                new SpendableResourceSet(inputChoiceResourceSet),
                new ObtainableResourceSet(outputChoiceResourceSet));

        DevCard devCard1 = new DevCard(concreteResourceSet, CardColour.BLUE, CardLevel.I, productionDetails, 1);
        DevCard devCard2 = new DevCard(concreteResourceSet, CardColour.YELLOW, CardLevel.I, productionDetails, 2);

        concreteResourceSet.addResource(ConcreteResource.COIN);
        outputChoiceResourceSet.addResource(ConcreteResource.SERVANT);
        productionDetails = new ProductionDetails(new SpendableResourceSet(inputChoiceResourceSet),
                                                  new ObtainableResourceSet(outputChoiceResourceSet));

        DevCard devCard3 = new DevCard(concreteResourceSet, CardColour.PURPLE, CardLevel.II, productionDetails, 2);

        developmentCardArea.addDevCard(devCard1, 0);
        developmentCardArea.addDevCard(devCard2, 2);
        developmentCardArea.addDevCard(devCard3, 2);

        assertEquals(CardLevel.I, developmentCardArea.getTopLevel(0));
        assertNull(developmentCardArea.getTopLevel(1));
        assertEquals(CardLevel.II, developmentCardArea.getTopLevel(2));
    }
}