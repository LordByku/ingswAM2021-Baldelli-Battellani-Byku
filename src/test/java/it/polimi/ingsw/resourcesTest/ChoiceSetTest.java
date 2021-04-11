package it.polimi.ingsw.resourcesTest;

import it.polimi.ingsw.resources.ChoiceSet;
import it.polimi.ingsw.resources.ConcreteResource;
import it.polimi.ingsw.resources.FullChoiceSet;
import it.polimi.ingsw.resources.InvalidResourceException;
import org.junit.Test;

import static org.junit.Assert.*;

public class ChoiceSetTest {
    @Test
    public void addChoiceTest() {
        ChoiceSet choiceSet = new ChoiceSet();

        try {
            choiceSet.addChoice(ConcreteResource.COIN);
            assertTrue(choiceSet.containsResource(ConcreteResource.COIN));
            assertFalse(choiceSet.containsResource(ConcreteResource.SHIELD));
        } catch (InvalidResourceException e) {
            fail();
        }

        try {
            choiceSet.addChoice(ConcreteResource.SHIELD);
            assertTrue(choiceSet.containsResource(ConcreteResource.COIN));
            assertTrue(choiceSet.containsResource(ConcreteResource.SHIELD));
        } catch (InvalidResourceException e) {
            fail();
        }

        try {
            choiceSet.addChoice(null);
            fail();
        } catch (InvalidResourceException e) {
            try {
                assertTrue(choiceSet.containsResource(ConcreteResource.COIN));
                assertTrue(choiceSet.containsResource(ConcreteResource.SHIELD));
            } catch (InvalidResourceException e1) {
                fail();
            }
        }

        try {
            boolean result = choiceSet.containsResource(null);
            fail();
        } catch (InvalidResourceException e) {
            assertTrue(true);
        }
    }

    @Test
    public void fullConstructorTest() {
        ChoiceSet choiceSetFull = new FullChoiceSet();
        ChoiceSet choiceSetEmpty = new ChoiceSet();

        try {
            for(ConcreteResource resource: ConcreteResource.values()) {
                assertTrue(choiceSetFull.containsResource(resource));
                assertFalse(choiceSetEmpty.containsResource(resource));
            }
        } catch (InvalidResourceException e) {
            fail();
        }
    }

    @Test
    public void cloneTest() {
        ChoiceSet choiceSet1 = new ChoiceSet();

        try {
            choiceSet1.addChoice(ConcreteResource.COIN);
        } catch (InvalidResourceException e) {
            fail();
        }

        ChoiceSet choiceSet2 = choiceSet1.clone();

        try {
            assertTrue(choiceSet2.containsResource(ConcreteResource.COIN));
            assertFalse(choiceSet2.containsResource(ConcreteResource.STONE));
            assertFalse(choiceSet2.containsResource(ConcreteResource.SHIELD));
            assertFalse(choiceSet2.containsResource(ConcreteResource.SERVANT));
        } catch (InvalidResourceException e) {
            fail();
        }

        try {
            choiceSet1.addChoice(ConcreteResource.SHIELD);
        } catch (InvalidResourceException e) {
            fail();
        }

        try {
            assertFalse(choiceSet2.containsResource(ConcreteResource.SHIELD));
        } catch (InvalidResourceException e) {
            fail();
        }
    }
}
