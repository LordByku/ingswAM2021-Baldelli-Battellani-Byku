package it.polimi.ingsw.resourcesTest;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import it.polimi.ingsw.resources.ChoiceSet;
import it.polimi.ingsw.resources.ConcreteResource;
import it.polimi.ingsw.resources.FullChoiceSet;
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

        assertFalse(choiceSet2.containsResource(ConcreteResource.SHIELD));
    }
}
