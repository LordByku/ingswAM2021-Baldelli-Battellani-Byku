package it.polimi.ingsw.resourcesTest;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import it.polimi.ingsw.resources.ChoiceSet;
import it.polimi.ingsw.resources.ConcreteResource;
import org.junit.Test;

public class ChoiceSetTest {
    @Test
    public void addChoiceTest() {
        ChoiceSet choiceSet = new ChoiceSet();

        choiceSet.addChoice(ConcreteResource.COIN);
        assertTrue(choiceSet.containsResource(ConcreteResource.COIN));
        assertFalse(choiceSet.containsResource(ConcreteResource.SHIELD));

        choiceSet.addChoice(ConcreteResource.SHIELD);
        assertTrue(choiceSet.containsResource(ConcreteResource.COIN));
        assertTrue(choiceSet.containsResource(ConcreteResource.SHIELD));
    }

    @Test
    public void fullConstructorTest() {
        ChoiceSet choiceSetFull = new ChoiceSet(true);
        ChoiceSet choiceSetEmpty = new ChoiceSet(false);

        for(ConcreteResource resource: ConcreteResource.values()) {
            assertTrue(choiceSetFull.containsResource(resource));
            assertFalse(choiceSetEmpty.containsResource(resource));
        }
    }
}
