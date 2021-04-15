package it.polimi.ingsw.devCardsTest;

import it.polimi.ingsw.devCards.*;
import it.polimi.ingsw.resources.ConcreteResource;
import it.polimi.ingsw.resources.resourceSets.ChoiceResourceSet;
import it.polimi.ingsw.resources.resourceSets.ConcreteResourceSet;
import it.polimi.ingsw.resources.resourceSets.ObtainableResourceSet;
import it.polimi.ingsw.resources.resourceSets.SpendableResourceSet;
import org.junit.Test;

import static org.junit.Assert.*;

public class DevCardDeckTest {
    @Test
    public void structTest(){
        DevCard devCard1;
        DevCard devCard2;
        DevCard devCard3;
        ConcreteResourceSet concreteResourceSet1 = new ConcreteResourceSet();
        concreteResourceSet1.addResource(ConcreteResource.COIN, 2);
        ConcreteResourceSet concreteResourceSet2 = new ConcreteResourceSet();
        concreteResourceSet2.addResource(ConcreteResource.STONE, 2);
        DevCardDeck devCardDeck = new DevCardDeck();
        ChoiceResourceSet choiceResourceSet1 = new ChoiceResourceSet();
        choiceResourceSet1.addResource(ConcreteResource.SHIELD);
        ChoiceResourceSet choiceResourceSet2 = new ChoiceResourceSet();
        choiceResourceSet2.addResource(ConcreteResource.SERVANT);

        SpendableResourceSet input1 = new SpendableResourceSet(choiceResourceSet1);
        ObtainableResourceSet output1 = new ObtainableResourceSet(choiceResourceSet2);
        ProductionDetails details1 = new ProductionDetails(input1,output1);

        devCard1 = new DevCard(concreteResourceSet1, CardColour.BLUE, CardLevel.I,details1,2);
        devCard2 = new DevCard(concreteResourceSet1,CardColour.YELLOW,CardLevel.II,details1,3);
        devCard3 = new DevCard(concreteResourceSet1,CardColour.PURPLE,CardLevel.II,details1,4);


        assertTrue(devCardDeck.isEmpty());

        devCardDeck.add(devCard1);
        assertSame(devCard1,devCardDeck.top());
        assertSame(devCard1,devCardDeck.top());

        devCardDeck.add(devCard2);
        assertSame(devCard2,devCardDeck.top());

        try{
            devCardDeck.add(null);
        }catch (InvalidDevCardException e){

            assertSame(devCard2,devCardDeck.top());

        }

        try{
            devCardDeck.add(devCard3);
        }catch (InvalidAddTopException e){

            assertSame(devCard2,devCardDeck.top());

        }

        assertFalse(devCardDeck.isEmpty());
        assertTrue(devCardDeck.getCards().contains(devCard1));
        assertTrue(devCardDeck.getCards().contains(devCard2));
        assertEquals(devCardDeck.topLevel(), devCard2.getLevel());
        assertNotEquals(devCardDeck.topLevel(), devCard1.getLevel());

        assertEquals(devCardDeck.getPoints(), devCard1.getPoints() + devCard2.getPoints());


    }
}
