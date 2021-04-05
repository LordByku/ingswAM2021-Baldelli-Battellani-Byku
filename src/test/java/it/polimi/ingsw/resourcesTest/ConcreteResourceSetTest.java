package it.polimi.ingsw.resourcesTest;

import it.polimi.ingsw.resources.ConcreteResource;
import it.polimi.ingsw.resources.ConcreteResourceSet;
import it.polimi.ingsw.resources.InvalidQuantityException;
import it.polimi.ingsw.resources.NotEnoughResourcesException;
import org.junit.Test;

import static org.junit.Assert.*;

public class ConcreteResourceSetTest {
    @Test
    public void addResourceTest() {
        ConcreteResourceSet concreteResourceSet = new ConcreteResourceSet();

        for (ConcreteResource resource : ConcreteResource.values()) {
            assertEquals(0, concreteResourceSet.getCount(resource));
        }

        try {
            concreteResourceSet.addResource(ConcreteResource.COIN, 2);
        } catch (InvalidQuantityException e) {
            fail();
        }
        for (ConcreteResource resource : ConcreteResource.values()) {
            if (resource == ConcreteResource.COIN) {
                assertEquals(2, concreteResourceSet.getCount(resource));
            } else {
                assertEquals(0, concreteResourceSet.getCount(resource));
            }
        }
    }

    @Test
    public void removeResourceTest() {
        ConcreteResourceSet concreteResourceSet = new ConcreteResourceSet();

        try {
            concreteResourceSet.addResource(ConcreteResource.COIN, 3);
            concreteResourceSet.addResource(ConcreteResource.SERVANT, 1);
            concreteResourceSet.addResource(ConcreteResource.STONE, 2);
        } catch (InvalidQuantityException e) {
            fail();
        }

        try {
            concreteResourceSet.removeResource(ConcreteResource.COIN, 2);
            concreteResourceSet.removeResource(ConcreteResource.STONE, 2);

            assertEquals(1, concreteResourceSet.getCount(ConcreteResource.COIN));
            assertEquals(0, concreteResourceSet.getCount(ConcreteResource.STONE));
            assertEquals(1, concreteResourceSet.getCount(ConcreteResource.SERVANT));
            assertEquals(0, concreteResourceSet.getCount(ConcreteResource.SHIELD));
        } catch (NotEnoughResourcesException e) {
            fail();
        } catch (InvalidQuantityException e) {
            fail();
        }

        try {
            concreteResourceSet.removeResource(ConcreteResource.SERVANT, 3);
            fail();
        } catch (NotEnoughResourcesException e) {
            assertEquals(1, concreteResourceSet.getCount(ConcreteResource.SERVANT));
        } catch (InvalidQuantityException e) {
            fail();
        }

        try {
            concreteResourceSet.removeResource(ConcreteResource.SHIELD, 2);
            fail();
        } catch (NotEnoughResourcesException e) {
            assertEquals(0, concreteResourceSet.getCount(ConcreteResource.SHIELD));
        } catch (InvalidQuantityException e) {
            fail();
        }
    }

    @Test
    public void invalidQuantityTest() {
        ConcreteResourceSet concreteResourceSet = new ConcreteResourceSet();

        try {
            concreteResourceSet.addResource(ConcreteResource.COIN, 2);
            concreteResourceSet.addResource(ConcreteResource.SHIELD, 3);
        } catch (InvalidQuantityException e) {
            fail();
        }

        try {
            concreteResourceSet.addResource(ConcreteResource.COIN, 0);
            fail();
        } catch (InvalidQuantityException e) {
            assertTrue(true);
        }

        try {
            concreteResourceSet.addResource(ConcreteResource.STONE, -2);
            fail();
        } catch (InvalidQuantityException e) {
            assertTrue(true);
        }

        try {
            concreteResourceSet.removeResource(ConcreteResource.COIN, 0);
            fail();
        } catch (NotEnoughResourcesException e) {
            fail();
        } catch (InvalidQuantityException e) {
            assertTrue(true);
        }

        try {
            concreteResourceSet.removeResource(ConcreteResource.SHIELD, -1);
            fail();
        } catch (NotEnoughResourcesException e) {
            fail();
        } catch (InvalidQuantityException e) {
            assertTrue(true);
        }
    }

    @Test
    public void unionTest() {
        ConcreteResourceSet concreteResourceSet1 = new ConcreteResourceSet();
        ConcreteResourceSet concreteResourceSet2 = new ConcreteResourceSet();

        try {
            concreteResourceSet1.addResource(ConcreteResource.COIN, 2);
            concreteResourceSet1.addResource(ConcreteResource.SHIELD, 1);

            concreteResourceSet2.addResource(ConcreteResource.SHIELD, 3);
            concreteResourceSet2.addResource(ConcreteResource.STONE, 1);
        } catch (InvalidQuantityException e) {
            fail();
        }

        concreteResourceSet1.union(concreteResourceSet2);

        assertEquals(2, concreteResourceSet1.getCount(ConcreteResource.COIN));
        assertEquals(4, concreteResourceSet1.getCount(ConcreteResource.SHIELD));
        assertEquals(1, concreteResourceSet1.getCount(ConcreteResource.STONE));
        assertEquals(0, concreteResourceSet1.getCount(ConcreteResource.SERVANT));
    }
}
