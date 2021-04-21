package it.polimi.ingsw.model.resourceLocationsTest;

import it.polimi.ingsw.model.playerBoard.resourceLocations.InvalidResourceLocationOperationException;
import it.polimi.ingsw.model.playerBoard.resourceLocations.StrongBox;
import it.polimi.ingsw.model.resources.ConcreteResource;
import it.polimi.ingsw.model.resources.resourceSets.ConcreteResourceSet;
import it.polimi.ingsw.model.resources.resourceSets.InvalidResourceSetException;
import org.junit.Test;

import static org.junit.Assert.*;

public class StrongBoxTest {
    @Test
    public void NullResourceSetTest() {
        StrongBox strongBox = new StrongBox();

        try {
            boolean tmp = strongBox.canAdd(null);
            fail();
        } catch (InvalidResourceSetException e) {
            assertNull(strongBox.getResources().getResourceType());
        }

        try {
            boolean tmp = strongBox.containsResources(null);
            fail();
        } catch (InvalidResourceSetException e) {
            assertNull(strongBox.getResources().getResourceType());
        }

        try {
            strongBox.addResources(null);
            fail();
        } catch (InvalidResourceSetException e) {
            assertNull(strongBox.getResources().getResourceType());
        }

        try {
            strongBox.removeResources(null);
            fail();
        } catch (InvalidResourceSetException e) {
            assertNull(strongBox.getResources().getResourceType());
        }
    }

    @Test
    public void canAddTest() {
        StrongBox strongBox = new StrongBox();

        ConcreteResourceSet concreteResourceSet = new ConcreteResourceSet();

        assertTrue(strongBox.canAdd(concreteResourceSet));

        concreteResourceSet.addResource(ConcreteResource.COIN);
        concreteResourceSet.addResource(ConcreteResource.STONE, 2);
        concreteResourceSet.addResource(ConcreteResource.SHIELD, 3);
        concreteResourceSet.addResource(ConcreteResource.SERVANT, 4);

        assertTrue(strongBox.canAdd(concreteResourceSet));
    }

    @Test
    public void containsResourcesTest() {
        StrongBox strongBox = new StrongBox();

        ConcreteResourceSet concreteResourceSet = new ConcreteResourceSet();

        assertTrue(strongBox.containsResources(concreteResourceSet));

        concreteResourceSet.addResource(ConcreteResource.COIN);
        concreteResourceSet.addResource(ConcreteResource.STONE, 3);

        strongBox.addResources(concreteResourceSet);

        assertTrue(strongBox.containsResources(concreteResourceSet));
        assertTrue(strongBox.containsResources(new ConcreteResourceSet()));

        concreteResourceSet.addResource(ConcreteResource.SHIELD);

        assertFalse(strongBox.containsResources(concreteResourceSet));

        ConcreteResourceSet concreteResourceSet1 = new ConcreteResourceSet();
        concreteResourceSet1.addResource(ConcreteResource.SHIELD, 2);

        strongBox.addResources(concreteResourceSet1);

        assertTrue(strongBox.containsResources(concreteResourceSet));
    }

    @Test
    public void addResourcesTest() {
        StrongBox strongBox = new StrongBox();

        strongBox.addResources(new ConcreteResourceSet());

        ConcreteResourceSet resources = strongBox.getResources();

        assertEquals(0, resources.getCount(ConcreteResource.COIN));
        assertEquals(0, resources.getCount(ConcreteResource.STONE));
        assertEquals(0, resources.getCount(ConcreteResource.SHIELD));
        assertEquals(0, resources.getCount(ConcreteResource.SERVANT));

        ConcreteResourceSet toAdd = new ConcreteResourceSet();

        toAdd.addResource(ConcreteResource.COIN, 3);
        toAdd.addResource(ConcreteResource.SHIELD, 2);

        strongBox.addResources(toAdd);

        resources = strongBox.getResources();

        assertEquals(3, resources.getCount(ConcreteResource.COIN));
        assertEquals(0, resources.getCount(ConcreteResource.STONE));
        assertEquals(2, resources.getCount(ConcreteResource.SHIELD));
        assertEquals(0, resources.getCount(ConcreteResource.SERVANT));

        toAdd.removeResource(ConcreteResource.COIN, 3);
        toAdd.addResource(ConcreteResource.STONE, 1);

        strongBox.addResources(toAdd);

        resources = strongBox.getResources();

        assertEquals(3, resources.getCount(ConcreteResource.COIN));
        assertEquals(1, resources.getCount(ConcreteResource.STONE));
        assertEquals(4, resources.getCount(ConcreteResource.SHIELD));
        assertEquals(0, resources.getCount(ConcreteResource.SERVANT));
    }

    @Test
    public void removeResources() {
        StrongBox strongBox = new StrongBox();

        strongBox.removeResources(new ConcreteResourceSet());

        ConcreteResourceSet resources = strongBox.getResources();

        assertEquals(0, resources.getCount(ConcreteResource.COIN));
        assertEquals(0, resources.getCount(ConcreteResource.STONE));
        assertEquals(0, resources.getCount(ConcreteResource.SHIELD));
        assertEquals(0, resources.getCount(ConcreteResource.SERVANT));

        ConcreteResourceSet toAdd = new ConcreteResourceSet();

        toAdd.addResource(ConcreteResource.COIN, 3);
        toAdd.addResource(ConcreteResource.STONE, 4);
        toAdd.addResource(ConcreteResource.SHIELD, 1);

        strongBox.addResources(toAdd);

        ConcreteResourceSet toRemove = new ConcreteResourceSet();

        toRemove.addResource(ConcreteResource.COIN, 1);
        toRemove.addResource(ConcreteResource.STONE, 2);

        strongBox.removeResources(toRemove);

        resources = strongBox.getResources();
        assertEquals(2, resources.getCount(ConcreteResource.COIN));
        assertEquals(2, resources.getCount(ConcreteResource.STONE));
        assertEquals(1, resources.getCount(ConcreteResource.SHIELD));
        assertEquals(0, resources.getCount(ConcreteResource.SERVANT));

        toRemove.addResource(ConcreteResource.SERVANT);

        try {
            strongBox.removeResources(toRemove);
            fail();
        } catch (InvalidResourceLocationOperationException e) {
            resources = strongBox.getResources();
            assertEquals(2, resources.getCount(ConcreteResource.COIN));
            assertEquals(2, resources.getCount(ConcreteResource.STONE));
            assertEquals(1, resources.getCount(ConcreteResource.SHIELD));
            assertEquals(0, resources.getCount(ConcreteResource.SERVANT));
        }

        toRemove.removeResource(ConcreteResource.SERVANT);
        toRemove.addResource(ConcreteResource.SHIELD);

        strongBox.removeResources(toRemove);

        resources = strongBox.getResources();
        assertEquals(1, resources.getCount(ConcreteResource.COIN));
        assertEquals(0, resources.getCount(ConcreteResource.STONE));
        assertEquals(0, resources.getCount(ConcreteResource.SHIELD));
        assertEquals(0, resources.getCount(ConcreteResource.SERVANT));

        strongBox.removeResources(new ConcreteResourceSet());

        resources = strongBox.getResources();
        assertEquals(1, resources.getCount(ConcreteResource.COIN));
        assertEquals(0, resources.getCount(ConcreteResource.STONE));
        assertEquals(0, resources.getCount(ConcreteResource.SHIELD));
        assertEquals(0, resources.getCount(ConcreteResource.SERVANT));
    }
}
