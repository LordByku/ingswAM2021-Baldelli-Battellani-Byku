package it.polimi.ingsw.model.resourcesTest;

import it.polimi.ingsw.model.resources.ChoiceSet;
import it.polimi.ingsw.model.resources.ConcreteResource;
import it.polimi.ingsw.model.resources.FullChoiceSet;
import it.polimi.ingsw.model.resources.InvalidResourceException;
import org.junit.Test;

import static org.junit.Assert.*;

public class ChoiceSetTest {
    @Test
    public void addChoiceTest() {
        ChoiceSet choiceSet = new ChoiceSet();

        choiceSet.addChoice(ConcreteResource.COIN);
        assertTrue(choiceSet.containsResource(ConcreteResource.COIN));
        assertFalse(choiceSet.containsResource(ConcreteResource.STONE));
        assertFalse(choiceSet.containsResource(ConcreteResource.SHIELD));
        assertFalse(choiceSet.containsResource(ConcreteResource.SERVANT));

        choiceSet.addChoice(ConcreteResource.SHIELD);
        assertTrue(choiceSet.containsResource(ConcreteResource.COIN));
        assertFalse(choiceSet.containsResource(ConcreteResource.STONE));
        assertTrue(choiceSet.containsResource(ConcreteResource.SHIELD));
        assertFalse(choiceSet.containsResource(ConcreteResource.SERVANT));

        try {
            choiceSet.addChoice(null);
            fail();
        } catch (InvalidResourceException e) {
            assertTrue(choiceSet.containsResource(ConcreteResource.COIN));
            assertFalse(choiceSet.containsResource(ConcreteResource.STONE));
            assertTrue(choiceSet.containsResource(ConcreteResource.SHIELD));
            assertFalse(choiceSet.containsResource(ConcreteResource.SERVANT));
        }

        try {
            boolean result = choiceSet.containsResource(null);
            fail();
        } catch (InvalidResourceException e) {
            assertTrue(choiceSet.containsResource(ConcreteResource.COIN));
            assertFalse(choiceSet.containsResource(ConcreteResource.STONE));
            assertTrue(choiceSet.containsResource(ConcreteResource.SHIELD));
            assertFalse(choiceSet.containsResource(ConcreteResource.SERVANT));
        }
    }

    @Test
    public void fullConstructorTest() {
        ChoiceSet choiceSetFull = new FullChoiceSet();
        ChoiceSet choiceSetEmpty = new ChoiceSet();

        for(ConcreteResource resource: ConcreteResource.values()) {
            assertTrue(choiceSetFull.containsResource(resource));
            assertFalse(choiceSetEmpty.containsResource(resource));
        }
    }

    @Test
    public void cloneTest() {
        ChoiceSet choiceSet1 = new ChoiceSet();

        choiceSet1.addChoice(ConcreteResource.COIN);

        ChoiceSet choiceSet2 = choiceSet1.clone();

        assertTrue(choiceSet2.containsResource(ConcreteResource.COIN));
        assertFalse(choiceSet2.containsResource(ConcreteResource.STONE));
        assertFalse(choiceSet2.containsResource(ConcreteResource.SHIELD));
        assertFalse(choiceSet2.containsResource(ConcreteResource.SERVANT));

        choiceSet1.addChoice(ConcreteResource.SHIELD);

        assertTrue(choiceSet2.containsResource(ConcreteResource.COIN));
        assertFalse(choiceSet2.containsResource(ConcreteResource.STONE));
        assertFalse(choiceSet2.containsResource(ConcreteResource.SHIELD));
        assertFalse(choiceSet2.containsResource(ConcreteResource.SERVANT));
    }
}
