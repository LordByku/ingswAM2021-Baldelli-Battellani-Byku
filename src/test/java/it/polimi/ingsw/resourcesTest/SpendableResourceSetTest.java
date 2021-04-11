package it.polimi.ingsw.resourcesTest;

import it.polimi.ingsw.resources.*;
import it.polimi.ingsw.resources.resourceSets.ChoiceResourceSet;
import it.polimi.ingsw.resources.resourceSets.ConcreteResourceSet;
import it.polimi.ingsw.resources.resourceSets.InvalidResourceSetException;
import it.polimi.ingsw.resources.resourceSets.SpendableResourceSet;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class SpendableResourceSetTest {
    @Test
    public void constructorTest() {
        SpendableResourceSet spendableResourceSet = new SpendableResourceSet();

        ArrayList<Resource> resources = ((ChoiceResourceSet) spendableResourceSet.getResourceSet()).getResources();

        assertTrue(resources.isEmpty());

        ChoiceResourceSet choiceResourceSet = new ChoiceResourceSet();
        try {
            choiceResourceSet.addResource(ConcreteResource.COIN);
            choiceResourceSet.addResource(ConcreteResource.COIN);
        } catch (InvalidResourceException e) {
            fail();
        }

        try {
            spendableResourceSet = new SpendableResourceSet(choiceResourceSet);
        } catch (InvalidResourceSetException e) {
            fail();
        }

        resources = ((ChoiceResourceSet) spendableResourceSet.getResourceSet()).getResources();

        assertEquals(2, resources.size());
        assertEquals(ConcreteResource.COIN, resources.get(0));
        assertEquals(ConcreteResource.COIN, resources.get(1));

        try {
            choiceResourceSet.addResource(ConcreteResource.SHIELD);
        } catch (InvalidResourceException e) {
            fail();
        }

        resources = ((ChoiceResourceSet) spendableResourceSet.getResourceSet()).getResources();

        assertEquals(2, resources.size());

        try {
            spendableResourceSet = new SpendableResourceSet(null);
            fail();
        } catch (InvalidResourceSetException e) {
            assertTrue(true);
        }
    }

    @Test
    public void toConcreteTest() {
        SpendableResourceSet spendableResourceSet = new SpendableResourceSet();

        try {
            spendableResourceSet.toConcrete();

            ConcreteResourceSet concreteResourceSet = (ConcreteResourceSet) spendableResourceSet.getResourceSet();
            assertEquals(0, concreteResourceSet.getCount(ConcreteResource.COIN));
            assertEquals(0, concreteResourceSet.getCount(ConcreteResource.SHIELD));
            assertEquals(0, concreteResourceSet.getCount(ConcreteResource.STONE));
            assertEquals(0, concreteResourceSet.getCount(ConcreteResource.SERVANT));
        } catch (NotConcreteException | InvalidResourceException e) {
            fail();
        }

        ChoiceResourceSet choiceResourceSet = new ChoiceResourceSet();

        try {
            choiceResourceSet.addResource(new ChoiceResource(new FullChoiceSet()));
            choiceResourceSet.addResource(ConcreteResource.SHIELD);
        } catch (InvalidChoiceSetException | InvalidResourceException e) {
            fail();
        }

        try {
            spendableResourceSet = new SpendableResourceSet(choiceResourceSet);
        } catch (InvalidResourceSetException e) {
            fail();
        }

        try {
            spendableResourceSet.toConcrete();
            fail();
        } catch (NotConcreteException e) {
            ArrayList<Resource> resources = ((ChoiceResourceSet) spendableResourceSet.getResourceSet()).getResources();

            assertEquals(2, resources.size());
            assertFalse(resources.get(0).isConcrete());
            assertEquals(ConcreteResource.SHIELD, resources.get(1));
        }

        ArrayList<Resource> resources = ((ChoiceResourceSet) spendableResourceSet.getResourceSet()).getResources();
        try {
            ((ChoiceResource) resources.get(0)).makeChoice(ConcreteResource.STONE);
        } catch (InvalidResourceException e) {
            fail();
        }

        try {
            spendableResourceSet.toConcrete();

            ConcreteResourceSet concreteResourceSet = (ConcreteResourceSet) spendableResourceSet.getResourceSet();
            assertEquals(0, concreteResourceSet.getCount(ConcreteResource.COIN));
            assertEquals(1, concreteResourceSet.getCount(ConcreteResource.SHIELD));
            assertEquals(1, concreteResourceSet.getCount(ConcreteResource.STONE));
            assertEquals(0, concreteResourceSet.getCount(ConcreteResource.SERVANT));
        } catch (NotConcreteException | InvalidResourceException e) {
            fail();
        }
    }

    @Test
    public void unionTest() {
        ChoiceSet choiceSet = new FullChoiceSet();
        ChoiceResourceSet choiceResourceSet1 = new ChoiceResourceSet();
        ChoiceResourceSet choiceResourceSet2 = new ChoiceResourceSet();

        try {
            choiceResourceSet1.addResource(ConcreteResource.COIN);
            choiceResourceSet1.addResource(ConcreteResource.SHIELD);
            choiceResourceSet1.addResource(new ChoiceResource(choiceSet));
            choiceResourceSet2.addResource(new ChoiceResource(choiceSet));
            choiceResourceSet2.addResource(ConcreteResource.COIN);
        } catch (InvalidChoiceSetException | InvalidResourceException e) {
            fail();
        }

        try {
            SpendableResourceSet spendableResourceSet1 = new SpendableResourceSet(choiceResourceSet1);
            SpendableResourceSet spendableResourceSet2 = new SpendableResourceSet(choiceResourceSet2);

            try {
                spendableResourceSet1.union(spendableResourceSet2);

                ChoiceResourceSet resourceSet = (ChoiceResourceSet) spendableResourceSet1.getResourceSet();
                ArrayList<Resource> resources = resourceSet.getResources();

                assertEquals(5, resources.size());
                assertEquals(ConcreteResource.COIN, resources.get(0));
                assertEquals(ConcreteResource.SHIELD, resources.get(1));
                assertFalse(resources.get(2).isConcrete());
                assertFalse(resources.get(3).isConcrete());
                assertEquals(ConcreteResource.COIN, resources.get(4));
            } catch (InvalidResourceSetException e) {
                fail();
            }
        } catch (InvalidResourceSetException e) {
            fail();
        }
    }

    @Test
    public void advancedUnionTest() {
        SpendableResourceSet spendableResourceSet1 = new SpendableResourceSet();
        SpendableResourceSet spendableResourceSet2 = new SpendableResourceSet();

        try {
            spendableResourceSet1.union(spendableResourceSet2);

            ArrayList<Resource> resources = ((ChoiceResourceSet) spendableResourceSet1.getResourceSet()).getResources();
            assertTrue(resources.isEmpty());
        } catch (InvalidResourceSetException e) {
            fail();
        }

        ChoiceResourceSet choiceResourceSet = new ChoiceResourceSet();
        try {
            choiceResourceSet.addResource(ConcreteResource.COIN);
            choiceResourceSet.addResource(new ChoiceResource(new FullChoiceSet()));
        } catch (InvalidChoiceSetException | InvalidResourceException e) {
            fail();
        }

        try {
            spendableResourceSet1 = new SpendableResourceSet(choiceResourceSet);
        } catch (InvalidResourceSetException e) {
            fail();
        }

        try {
            spendableResourceSet1.union(spendableResourceSet2);

            ArrayList<Resource> resources = ((ChoiceResourceSet) spendableResourceSet1.getResourceSet()).getResources();
            assertEquals(2, resources.size());
            assertEquals(ConcreteResource.COIN, resources.get(0));
            assertFalse(resources.get(1).isConcrete());
        } catch (InvalidResourceSetException e) {
            fail();
        }

        try {
            spendableResourceSet2.union(spendableResourceSet1);

            ArrayList<Resource> resources = ((ChoiceResourceSet) spendableResourceSet2.getResourceSet()).getResources();
            assertEquals(2, resources.size());
            assertEquals(ConcreteResource.COIN, resources.get(0));
            assertFalse(resources.get(1).isConcrete());
        } catch (InvalidResourceSetException e) {
            fail();
        }

        try {
            spendableResourceSet1.union(spendableResourceSet1);

            ArrayList<Resource> resources = ((ChoiceResourceSet) spendableResourceSet1.getResourceSet()).getResources();
            assertEquals(4, resources.size());
            assertEquals(ConcreteResource.COIN, resources.get(0));
            assertFalse(resources.get(1).isConcrete());
            assertEquals(ConcreteResource.COIN, resources.get(2));
            assertFalse(resources.get(3).isConcrete());
        } catch (InvalidResourceSetException e) {
            fail();
        }

        try {
            ArrayList<Resource> resources = ((ChoiceResourceSet) spendableResourceSet2.getResourceSet()).getResources();
            ((ChoiceResource) resources.get(1)).makeChoice(ConcreteResource.SHIELD);

            spendableResourceSet2.union(spendableResourceSet2);

            resources = ((ChoiceResourceSet) spendableResourceSet2.getResourceSet()).getResources();
            assertEquals(4, resources.size());
            assertEquals(ConcreteResource.COIN, resources.get(0).getResource());
            assertEquals(ConcreteResource.SHIELD, resources.get(1).getResource());
            assertEquals(ConcreteResource.COIN, resources.get(2).getResource());
            assertEquals(ConcreteResource.SHIELD, resources.get(3).getResource());
        } catch (InvalidResourceSetException | InvalidResourceException e) {
            fail();
        }

        try {
            spendableResourceSet2.toConcrete();
        } catch (NotConcreteException e) {
            fail();
        }

        try {
            spendableResourceSet1.union(spendableResourceSet2);
            fail();
        } catch (InvalidResourceSetException e) {
            ArrayList<Resource> resources = ((ChoiceResourceSet) spendableResourceSet1.getResourceSet()).getResources();
            assertEquals(4, resources.size());
        }

        try {
            spendableResourceSet2.union(spendableResourceSet1);
            fail();
        } catch (InvalidResourceSetException e) {
            ConcreteResourceSet concreteResourceSet = (ConcreteResourceSet) spendableResourceSet2.getResourceSet();
            try {
                assertEquals(2, concreteResourceSet.getCount(ConcreteResource.COIN));
                assertEquals(2, concreteResourceSet.getCount(ConcreteResource.SHIELD));
                assertEquals(0, concreteResourceSet.getCount(ConcreteResource.STONE));
                assertEquals(0, concreteResourceSet.getCount(ConcreteResource.SERVANT));
            } catch (InvalidResourceException e1) {
                fail();
            }
        }
    }
}