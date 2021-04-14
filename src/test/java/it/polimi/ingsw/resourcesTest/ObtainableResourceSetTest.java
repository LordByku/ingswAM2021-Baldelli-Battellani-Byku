package it.polimi.ingsw.resourcesTest;

import it.polimi.ingsw.resources.*;
import it.polimi.ingsw.resources.resourceSets.ChoiceResourceSet;
import it.polimi.ingsw.resources.resourceSets.ConcreteResourceSet;
import it.polimi.ingsw.resources.resourceSets.InvalidResourceSetException;
import it.polimi.ingsw.resources.resourceSets.ObtainableResourceSet;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class ObtainableResourceSetTest {
    @Test
    public void constructorTest() {
        ObtainableResourceSet obtainableResourceSet = new ObtainableResourceSet();

        ArrayList<Resource> resources = ((ChoiceResourceSet) obtainableResourceSet.getResourceSet()).getResources();

        assertTrue(resources.isEmpty());
        assertEquals(0, obtainableResourceSet.getFaithPoints());

        ChoiceResourceSet choiceResourceSet = new ChoiceResourceSet();
        choiceResourceSet.addResource(ConcreteResource.COIN);
        choiceResourceSet.addResource(ConcreteResource.SHIELD);

        obtainableResourceSet = new ObtainableResourceSet(choiceResourceSet);

        resources = ((ChoiceResourceSet) obtainableResourceSet.getResourceSet()).getResources();

        assertEquals(2, resources.size());
        assertEquals(ConcreteResource.COIN, resources.get(0).getResource());
        assertEquals(ConcreteResource.SHIELD, resources.get(1).getResource());
        assertEquals(0, obtainableResourceSet.getFaithPoints());

        choiceResourceSet.addResource(new ChoiceResource(new FullChoiceSet()));

        obtainableResourceSet = new ObtainableResourceSet(choiceResourceSet, 2);

        resources = ((ChoiceResourceSet) obtainableResourceSet.getResourceSet()).getResources();

        assertEquals(3, resources.size());
        assertEquals(ConcreteResource.COIN, resources.get(0).getResource());
        assertEquals(ConcreteResource.SHIELD, resources.get(1).getResource());
        assertFalse(resources.get(2).isConcrete());
        assertEquals(2, obtainableResourceSet.getFaithPoints());

        try {
            obtainableResourceSet = new ObtainableResourceSet(null);
            fail();
        } catch (InvalidResourceSetException e) {
            resources = ((ChoiceResourceSet) obtainableResourceSet.getResourceSet()).getResources();

            assertEquals(3, resources.size());
            assertEquals(ConcreteResource.COIN, resources.get(0).getResource());
            assertEquals(ConcreteResource.SHIELD, resources.get(1).getResource());
            assertFalse(resources.get(2).isConcrete());
            assertEquals(2, obtainableResourceSet.getFaithPoints());
        }

        try {
            obtainableResourceSet = new ObtainableResourceSet(null, 2);
            fail();
        } catch (InvalidResourceSetException e) {
            resources = ((ChoiceResourceSet) obtainableResourceSet.getResourceSet()).getResources();

            assertEquals(3, resources.size());
            assertEquals(ConcreteResource.COIN, resources.get(0).getResource());
            assertEquals(ConcreteResource.SHIELD, resources.get(1).getResource());
            assertFalse(resources.get(2).isConcrete());
            assertEquals(2, obtainableResourceSet.getFaithPoints());
        }
    }

    @Test
    public void toConcreteTest() {
        ChoiceResourceSet choiceResourceSet = new ChoiceResourceSet();

        choiceResourceSet.addResource(ConcreteResource.COIN);
        choiceResourceSet.addResource(new ChoiceResource(new FullChoiceSet()));
        choiceResourceSet.addResource(new ChoiceResource(new FullChoiceSet()));

        ObtainableResourceSet obtainableResourceSet = new ObtainableResourceSet(choiceResourceSet, 1);

        ArrayList<Resource> resources = ((ChoiceResourceSet) obtainableResourceSet.getResourceSet()).getResources();

        ((ChoiceResource) resources.get(1)).makeChoice(ConcreteResource.COIN);

        try {
            obtainableResourceSet.toConcrete();
            fail();
        } catch (NotConcreteException e) {
            resources = ((ChoiceResourceSet) obtainableResourceSet.getResourceSet()).getResources();
            assertEquals(3, resources.size());
            assertEquals(ConcreteResource.COIN, resources.get(0).getResource());
            assertEquals(ConcreteResource.COIN, resources.get(1).getResource());
            assertFalse(resources.get(2).isConcrete());

            assertEquals(1, obtainableResourceSet.getFaithPoints());
        }

        ((ChoiceResource) resources.get(2)).makeChoice(ConcreteResource.STONE);

        obtainableResourceSet.toConcrete();

        ConcreteResourceSet concreteResourceSet = (ConcreteResourceSet) obtainableResourceSet.getResourceSet();
        assertEquals(2, concreteResourceSet.getCount(ConcreteResource.COIN));
        assertEquals(0, concreteResourceSet.getCount(ConcreteResource.SHIELD));
        assertEquals(1, concreteResourceSet.getCount(ConcreteResource.STONE));
        assertEquals(0, concreteResourceSet.getCount(ConcreteResource.SERVANT));

        assertEquals(1, obtainableResourceSet.getFaithPoints());

        obtainableResourceSet.toConcrete();

        concreteResourceSet = (ConcreteResourceSet) obtainableResourceSet.getResourceSet();
        assertEquals(2, concreteResourceSet.getCount(ConcreteResource.COIN));
        assertEquals(0, concreteResourceSet.getCount(ConcreteResource.SHIELD));
        assertEquals(1, concreteResourceSet.getCount(ConcreteResource.STONE));
        assertEquals(0, concreteResourceSet.getCount(ConcreteResource.SERVANT));

        assertEquals(1, obtainableResourceSet.getFaithPoints());
    }

    @Test
    public void unionTest() {
        ChoiceResourceSet choiceResourceSet = new ChoiceResourceSet();
        choiceResourceSet.addResource(new ChoiceResource(new FullChoiceSet()));

        ObtainableResourceSet obtainableResourceSet1 = new ObtainableResourceSet(choiceResourceSet);
        ObtainableResourceSet obtainableResourceSet2 = new ObtainableResourceSet(new ChoiceResourceSet(), 1);

        obtainableResourceSet1.union(obtainableResourceSet2);

        ArrayList<Resource> resources = ((ChoiceResourceSet) obtainableResourceSet1.getResourceSet()).getResources();
        assertEquals(1, resources.size());
        assertFalse(resources.get(0).isConcrete());

        assertEquals(1, obtainableResourceSet1.getFaithPoints());

        resources = ((ChoiceResourceSet) obtainableResourceSet1.getResourceSet()).getResources();
        ((ChoiceResource) resources.get(0)).makeChoice(ConcreteResource.COIN);

        obtainableResourceSet1.toConcrete();

        try {
            obtainableResourceSet1.union(obtainableResourceSet2);
            fail();
        } catch (InvalidResourceSetException e) {
            ConcreteResourceSet concreteResourceSet = (ConcreteResourceSet) obtainableResourceSet1.getResourceSet();

            assertEquals(1, concreteResourceSet.getCount(ConcreteResource.COIN));
            assertEquals(0, concreteResourceSet.getCount(ConcreteResource.SHIELD));
            assertEquals(0, concreteResourceSet.getCount(ConcreteResource.STONE));
            assertEquals(0, concreteResourceSet.getCount(ConcreteResource.SERVANT));

            assertEquals(1, obtainableResourceSet1.getFaithPoints());
        }

        obtainableResourceSet2.toConcrete();

        obtainableResourceSet1.union(obtainableResourceSet2);

        ConcreteResourceSet concreteResourceSet = (ConcreteResourceSet) obtainableResourceSet1.getResourceSet();
        assertEquals(1, concreteResourceSet.getCount(ConcreteResource.COIN));
        assertEquals(0, concreteResourceSet.getCount(ConcreteResource.SHIELD));
        assertEquals(0, concreteResourceSet.getCount(ConcreteResource.STONE));
        assertEquals(0, concreteResourceSet.getCount(ConcreteResource.SERVANT));

        assertEquals(2, obtainableResourceSet1.getFaithPoints());
    }
}
