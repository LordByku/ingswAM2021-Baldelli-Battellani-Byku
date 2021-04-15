package it.polimi.ingsw.devCardsTest;

import it.polimi.ingsw.devCards.*;
import it.polimi.ingsw.resources.ConcreteResource;
import it.polimi.ingsw.resources.resourceSets.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class DevCardTest {
    @Test
    public void devCardTest(){
        DevCard devCard1;
        DevCard devCard2;
        DevCard devCard3;
        CardColour blue = CardColour.BLUE;
        CardColour yellow = CardColour.YELLOW;
        ConcreteResourceSet concreteResourceSet1 = new ConcreteResourceSet();
        concreteResourceSet1.addResource(ConcreteResource.COIN, 2);
        ConcreteResourceSet concreteResourceSet2 = new ConcreteResourceSet();
        concreteResourceSet2.addResource(ConcreteResource.STONE, 2);

        ChoiceResourceSet choiceResourceSet1 = new ChoiceResourceSet();
        choiceResourceSet1.addResource(ConcreteResource.SHIELD);
        ChoiceResourceSet choiceResourceSet2 = new ChoiceResourceSet();
        choiceResourceSet1.addResource(ConcreteResource.SERVANT);

        SpendableResourceSet input1 = new SpendableResourceSet(choiceResourceSet1);
        ObtainableResourceSet output1 = new ObtainableResourceSet(choiceResourceSet2);
        ProductionDetails details1 = new ProductionDetails(input1,output1);

        devCard1 = new DevCardLev1(concreteResourceSet1,blue,details1);
        devCard2 = new DevCardLev2(concreteResourceSet2,yellow,details1);
        assertEquals(CardColour.BLUE,devCard1.getColour());
        assertEquals(CardLevel.I,devCard1.getLevel());
        assertEquals(CardColour.YELLOW,devCard2.getColour());
        assertEquals(CardLevel.II,devCard2.getLevel());
        assertNotEquals(CardColour.YELLOW,devCard1.getColour());
        assertNotEquals(CardColour.PURPLE,devCard1.getColour());
        assertNotEquals(CardColour.GREEN,devCard1.getColour());
        assertNotEquals(CardLevel.III,devCard1.getLevel());
        try {
            devCard1=new DevCardLev1(concreteResourceSet1, null, details1);
            fail();
        }catch (InvalidResourceSetException| InvalidCardColour e){
            assertEquals(CardColour.BLUE,devCard1.getColour());
            assertEquals(CardLevel.I,devCard1.getLevel());
        }
    }

}
