package it.polimi.ingsw.resourcesTest;

import it.polimi.ingsw.resources.*;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class ChoiceResourceSetTest {
    @Test
    public void addResourceTest() {
        ChoiceResourceSet choiceResourceSet = new ChoiceResourceSet();

        choiceResourceSet.addResource(ConcreteResource.COIN);

        ChoiceSet choiceSet = new ChoiceSet(true);
        try {
            choiceResourceSet.addResource(new ChoiceResource(choiceSet));
        } catch (InvalidChoiceSetException e) {
            fail();
        }

        choiceResourceSet.addResource(ConcreteResource.COIN);

        ArrayList<Resource> resources = choiceResourceSet.getResources();

        assertEquals(3, resources.size());
        assertSame(resources.get(0), ConcreteResource.COIN);
        assertFalse(resources.get(1).isConcrete());
        assertSame(resources.get(2), ConcreteResource.COIN);
        assertFalse(choiceResourceSet.isConcrete());

        ChoiceResource choiceResource = (ChoiceResource) resources.get(1);
        try {
            choiceResource.makeChoice(ConcreteResource.SHIELD);
        } catch(InvalidResourceException e) {
            fail();
        }
        assertTrue(choiceResourceSet.isConcrete());
    }

    @Test
    public void toConcreteTest() {
         ChoiceResourceSet choiceResourceSet = new ChoiceResourceSet();

         choiceResourceSet.addResource(ConcreteResource.COIN);

        ChoiceSet choiceSet = new ChoiceSet(true);
        try {
            choiceResourceSet.addResource(new ChoiceResource(choiceSet));
            choiceResourceSet.addResource(new ChoiceResource(choiceSet));
        } catch (InvalidChoiceSetException e) {
            fail();
        }

        assertFalse(choiceResourceSet.isConcrete());

        try {
            ConcreteResourceSet concreteResourceSet = choiceResourceSet.toConcrete();
            fail();
        } catch (NotConcreteException e) {
            assertTrue(true);
        }

        ArrayList<Resource> resources = choiceResourceSet.getResources();
        ChoiceResource choiceResource1 = (ChoiceResource) resources.get(1);
        ChoiceResource choiceResource2 = (ChoiceResource) resources.get(2);

        try {
            choiceResource1.makeChoice(ConcreteResource.COIN);
            choiceResource2.makeChoice(ConcreteResource.STONE);
        } catch (InvalidResourceException e) {
            fail();
        }

        try {
            ConcreteResourceSet concreteResourceSet = choiceResourceSet.toConcrete();

            assertEquals(2, concreteResourceSet.getCount(ConcreteResource.COIN));
            assertEquals(1, concreteResourceSet.getCount(ConcreteResource.STONE));
            assertEquals(0, concreteResourceSet.getCount(ConcreteResource.SHIELD));
            assertEquals(0, concreteResourceSet.getCount(ConcreteResource.SERVANT));
        } catch (NotConcreteException e) {
            fail();
        }
    }
}
