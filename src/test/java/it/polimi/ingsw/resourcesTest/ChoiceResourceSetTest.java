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

        try {
            choiceResourceSet.addResource(ConcreteResource.COIN);
        } catch (InvalidResourceException e) {
            fail();
        }

        ChoiceSet choiceSet = new FullChoiceSet();
        try {
            choiceResourceSet.addResource(new ChoiceResource(choiceSet));
        } catch (InvalidChoiceSetException | InvalidResourceException e) {
            fail();
        }

        try {
            choiceResourceSet.addResource(ConcreteResource.COIN);
        } catch (InvalidResourceException e) {
            fail();
        }

        ArrayList<Resource> resources = choiceResourceSet.getResources();

        assertEquals(3, resources.size());
        assertSame(resources.get(0).getResource(), ConcreteResource.COIN);
        assertFalse(resources.get(1).isConcrete());
        assertSame(resources.get(2).getResource(), ConcreteResource.COIN);
        assertFalse(choiceResourceSet.isConcrete());

        ChoiceResource choiceResource = (ChoiceResource) resources.get(1);
        try {
            choiceResource.makeChoice(ConcreteResource.SHIELD);
        } catch(InvalidResourceException e) {
            fail();
        }
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
         ChoiceResourceSet choiceResourceSet = new ChoiceResourceSet();

         try {
             choiceResourceSet.addResource(ConcreteResource.COIN);
         } catch (InvalidResourceException e) {
             fail();
         }

        ChoiceSet choiceSet = new FullChoiceSet();
        try {
            choiceResourceSet.addResource(new ChoiceResource(choiceSet));
            choiceResourceSet.addResource(new ChoiceResource(choiceSet));
        } catch (InvalidChoiceSetException | InvalidResourceException e) {
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
        } catch (NotConcreteException | InvalidResourceException e) {
            fail();
        }
    }

    @Test
    public void unionTest() {
        ChoiceResourceSet choiceResourceSet1 = new ChoiceResourceSet();
        ChoiceResourceSet choiceResourceSet2 = new ChoiceResourceSet();

        ChoiceSet choiceSet = new FullChoiceSet();

        try {
            choiceResourceSet1.addResource(ConcreteResource.COIN);
            choiceResourceSet1.addResource(new ChoiceResource(choiceSet));

            choiceResourceSet2.addResource(new ChoiceResource(choiceSet));
            choiceResourceSet2.addResource(ConcreteResource.STONE);
        } catch (InvalidChoiceSetException | InvalidResourceException e) {
            fail();
        }

        try {
            choiceResourceSet1.union(choiceResourceSet2);
        } catch (InvalidResourceSetException e) {
            fail();
        }

        ArrayList<Resource> resources = choiceResourceSet1.getResources();

        assertEquals(4, resources.size());
        assertEquals(ConcreteResource.COIN, resources.get(0));
        assertFalse(resources.get(1).isConcrete());
        assertFalse(resources.get(2).isConcrete());
        assertEquals(ConcreteResource.STONE, resources.get(3));
    }

    @Test
    public void advancedUnionTest() {
        ChoiceResourceSet choiceResourceSet1 = new ChoiceResourceSet();
        ChoiceResourceSet choiceResourceSet2 = new ChoiceResourceSet();
        ChoiceResourceSet choiceResourceSet3 = null;

        ConcreteResourceSet concreteResourceSet = new ConcreteResourceSet();

        try {
            choiceResourceSet1.addResource(ConcreteResource.STONE);
            choiceResourceSet1.addResource(ConcreteResource.SHIELD);
        } catch (InvalidResourceException e) {
            fail();
        }

        try {
            choiceResourceSet1.union(choiceResourceSet2);

            ArrayList<Resource> resources = choiceResourceSet1.getResources();

            assertEquals(2, resources.size());
            assertEquals(ConcreteResource.STONE, resources.get(0));
            assertEquals(ConcreteResource.SHIELD, resources.get(1));
        } catch (InvalidResourceSetException e) {
            fail();
        }

        try {
            choiceResourceSet1.union(choiceResourceSet3);
            fail();
        } catch (InvalidResourceSetException e) {
            assertTrue(true);
        }

        try {
            choiceResourceSet1.union(concreteResourceSet);
            fail();
        } catch (InvalidResourceSetException e) {
            assertTrue(true);
        }

        ArrayList<Resource> resources = choiceResourceSet1.getResources();
        assertEquals(2, resources.size());
        assertEquals(ConcreteResource.STONE, resources.get(0));
        assertEquals(ConcreteResource.SHIELD, resources.get(1));
    }

    @Test
    public void cloneTest() {
        ChoiceResourceSet choiceResourceSet1 = new ChoiceResourceSet();
        ChoiceSet choiceSet = new FullChoiceSet();

        try {
            choiceResourceSet1.addResource(ConcreteResource.COIN);
            choiceResourceSet1.addResource(new ChoiceResource(choiceSet));
        } catch (InvalidChoiceSetException | InvalidResourceException e) {
            fail();
        }

        ChoiceResourceSet choiceResourceSet2 = (ChoiceResourceSet) choiceResourceSet1.clone();
        ArrayList<Resource> resources1 = choiceResourceSet1.getResources();
        ArrayList<Resource> resources2 = choiceResourceSet2.getResources();

        try {
            choiceResourceSet1.addResource(ConcreteResource.SHIELD);
            ((ChoiceResource) resources2.get(1)).makeChoice(ConcreteResource.SERVANT);
            ((ChoiceResource) resources1.get(1)).makeChoice(ConcreteResource.STONE);
        } catch (InvalidResourceException e) {
            fail();
        }

        resources1 = choiceResourceSet1.getResources();
        resources2 = choiceResourceSet2.getResources();
        assertEquals(3, resources1.size());
        assertEquals(2, resources2.size());
        assertEquals(ConcreteResource.STONE, resources1.get(1).getResource());
        assertEquals(ConcreteResource.STONE, resources2.get(1).getResource());
    }
}
