package it.polimi.ingsw.resourcesTest;

import it.polimi.ingsw.resources.*;
import it.polimi.ingsw.resources.resourceSets.ChoiceResourceSet;
import it.polimi.ingsw.resources.resourceSets.ConcreteResourceSet;
import it.polimi.ingsw.resources.NotConcreteException;
import it.polimi.ingsw.resources.resourceSets.InvalidResourceSetException;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class ChoiceResourceSetTest {
    @Test
    public void addResourceTest() {
        ChoiceResourceSet choiceResourceSet = new ChoiceResourceSet();
        choiceResourceSet.addResource(ConcreteResource.COIN);
        choiceResourceSet.addResource(new ChoiceResource(new FullChoiceSet()));
        choiceResourceSet.addResource(ConcreteResource.COIN);

        ArrayList<Resource> resources = choiceResourceSet.getResources();

        assertEquals(3, resources.size());
        assertSame(resources.get(0).getResource(), ConcreteResource.COIN);
        assertFalse(resources.get(1).isConcrete());
        assertSame(resources.get(2).getResource(), ConcreteResource.COIN);
        assertFalse(choiceResourceSet.isConcrete());

        ChoiceResource choiceResource = (ChoiceResource) resources.get(1);
        choiceResource.makeChoice(ConcreteResource.SHIELD);
        assertTrue(choiceResourceSet.isConcrete());

        try {
            choiceResourceSet.addResource(null);
            fail();
        } catch (InvalidResourceException e) {
            assertSame(resources.get(0).getResource(), ConcreteResource.COIN);
            assertSame(resources.get(1).getResource(), ConcreteResource.SHIELD);
            assertSame(resources.get(2).getResource(), ConcreteResource.COIN);
        }
    }

    @Test
    public void toConcreteTest() {
        ChoiceSet choiceSet = new FullChoiceSet();

        ChoiceResourceSet choiceResourceSet = new ChoiceResourceSet();
        choiceResourceSet.addResource(ConcreteResource.COIN);
        choiceResourceSet.addResource(new ChoiceResource(choiceSet));
        choiceResourceSet.addResource(new ChoiceResource(choiceSet));

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

        choiceResource1.makeChoice(ConcreteResource.COIN);
        choiceResource2.makeChoice(ConcreteResource.STONE);

        ConcreteResourceSet concreteResourceSet = choiceResourceSet.toConcrete();

        assertEquals(2, concreteResourceSet.getCount(ConcreteResource.COIN));
        assertEquals(1, concreteResourceSet.getCount(ConcreteResource.STONE));
        assertEquals(0, concreteResourceSet.getCount(ConcreteResource.SHIELD));
        assertEquals(0, concreteResourceSet.getCount(ConcreteResource.SERVANT));
    }

    @Test
    public void unionTest() {
        ChoiceResourceSet choiceResourceSet1 = new ChoiceResourceSet();
        ChoiceResourceSet choiceResourceSet2 = new ChoiceResourceSet();

        ChoiceSet choiceSet = new FullChoiceSet();

        choiceResourceSet1.addResource(ConcreteResource.COIN);
        choiceResourceSet1.addResource(new ChoiceResource(choiceSet));

        choiceResourceSet2.addResource(new ChoiceResource(choiceSet));
        choiceResourceSet2.addResource(ConcreteResource.STONE);

        choiceResourceSet1.union(choiceResourceSet2);

        ArrayList<Resource> resources = choiceResourceSet1.getResources();

        assertEquals(4, resources.size());
        assertEquals(ConcreteResource.COIN, resources.get(0).getResource());
        assertFalse(resources.get(1).isConcrete());
        assertFalse(resources.get(2).isConcrete());
        assertEquals(ConcreteResource.STONE, resources.get(3).getResource());
    }

    @Test
    public void advancedUnionTest() {
        ChoiceResourceSet choiceResourceSet1 = new ChoiceResourceSet();
        ChoiceResourceSet choiceResourceSet2 = new ChoiceResourceSet();

        ConcreteResourceSet concreteResourceSet = new ConcreteResourceSet();

        choiceResourceSet1.addResource(ConcreteResource.STONE);
        choiceResourceSet1.addResource(ConcreteResource.SHIELD);

        choiceResourceSet1.union(choiceResourceSet2);

        ArrayList<Resource> resources = choiceResourceSet1.getResources();

        assertEquals(2, resources.size());
        assertEquals(ConcreteResource.STONE, resources.get(0));
        assertEquals(ConcreteResource.SHIELD, resources.get(1));

        try {
            choiceResourceSet1.union(null);
            fail();
        } catch (InvalidResourceSetException e) {
            resources = choiceResourceSet1.getResources();

            assertEquals(2, resources.size());
            assertEquals(ConcreteResource.STONE, resources.get(0));
            assertEquals(ConcreteResource.SHIELD, resources.get(1));
        }
    }

    @Test
    public void cloneTest() {
        ChoiceResourceSet choiceResourceSet1 = new ChoiceResourceSet();
        ChoiceSet choiceSet = new FullChoiceSet();

        choiceResourceSet1.addResource(ConcreteResource.COIN);
        choiceResourceSet1.addResource(new ChoiceResource(choiceSet));

        ChoiceResourceSet choiceResourceSet2 = (ChoiceResourceSet) choiceResourceSet1.clone();
        ArrayList<Resource> resources1 = choiceResourceSet1.getResources();
        ArrayList<Resource> resources2 = choiceResourceSet2.getResources();

        choiceResourceSet1.addResource(ConcreteResource.SHIELD);
        ((ChoiceResource) resources2.get(1)).makeChoice(ConcreteResource.SERVANT);
        ((ChoiceResource) resources1.get(1)).makeChoice(ConcreteResource.STONE);

        resources1 = choiceResourceSet1.getResources();
        resources2 = choiceResourceSet2.getResources();
        assertEquals(3, resources1.size());
        assertEquals(2, resources2.size());
        assertEquals(ConcreteResource.STONE, resources1.get(1).getResource());
        assertEquals(ConcreteResource.STONE, resources2.get(1).getResource());
    }
}
