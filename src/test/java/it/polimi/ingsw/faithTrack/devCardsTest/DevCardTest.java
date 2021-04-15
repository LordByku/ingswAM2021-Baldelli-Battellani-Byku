package it.polimi.ingsw.faithTrack.devCardsTest;

import it.polimi.ingsw.devCards.*;
import it.polimi.ingsw.resources.ConcreteResource;
import it.polimi.ingsw.resources.resourceSets.InvalidResourceSetException;
import it.polimi.ingsw.resources.resourceSets.ConcreteResourceSet;
import it.polimi.ingsw.resources.resourceSets.InvalidResourceSetException;
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
        concreteResourceSet1.addResource(ConcreteResource.STONE, 3);
        devCard1 = new DevCardLev1(concreteResourceSet1,blue);
        devCard2 = new DevCardLev2(concreteResourceSet2,yellow);
        assertEquals(CardColour.BLUE,devCard1.getColour());
        assertEquals(CardLevel.I,devCard1.getLevel());
        assertEquals(CardColour.YELLOW,devCard2.getColour());
        assertEquals(CardLevel.II,devCard2.getLevel());
        assertNotEquals(CardColour.YELLOW,devCard1.getColour());
        assertNotEquals(CardColour.PURPLE,devCard1.getColour());
        assertNotEquals(CardColour.GREEN,devCard1.getColour());
        assertNotEquals(CardLevel.III,devCard1.getLevel());
        try {
            new DevCardLev1(concreteResourceSet1, null);
            fail();
        }catch (InvalidResourceSetException| InvalidCardColour e){
            //TODO
        }
    }
}
