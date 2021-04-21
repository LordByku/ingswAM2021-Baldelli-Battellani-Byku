package it.polimi.ingsw.model.resourceLocationsTest;

import it.polimi.ingsw.model.playerBoard.resourceLocations.Depot;
import it.polimi.ingsw.model.playerBoard.resourceLocations.InvalidDepotSizeException;
import it.polimi.ingsw.model.playerBoard.resourceLocations.InvalidResourceLocationOperationException;
import it.polimi.ingsw.model.resources.ConcreteResource;
import it.polimi.ingsw.model.resources.resourceSets.ConcreteResourceSet;
import it.polimi.ingsw.model.resources.resourceSets.InvalidResourceSetException;
import org.junit.Test;

import static org.junit.Assert.*;

public class DepotTest {
    @Test
    public void constructorTest() {
        Depot depot = new Depot(1);

        ConcreteResourceSet resourceSet = depot.getResources();

        assertNotNull(resourceSet);
        assertEquals(0, resourceSet.getCount(ConcreteResource.COIN));
        assertEquals(0, resourceSet.getCount(ConcreteResource.STONE));
        assertEquals(0, resourceSet.getCount(ConcreteResource.SHIELD));
        assertEquals(0, resourceSet.getCount(ConcreteResource.SERVANT));

        try {
            depot = new Depot(-1);
            fail();
        } catch (InvalidDepotSizeException e) {
            resourceSet = depot.getResources();

            assertNotNull(resourceSet);
            assertEquals(0, resourceSet.getCount(ConcreteResource.COIN));
            assertEquals(0, resourceSet.getCount(ConcreteResource.STONE));
            assertEquals(0, resourceSet.getCount(ConcreteResource.SHIELD));
            assertEquals(0, resourceSet.getCount(ConcreteResource.SERVANT));
        }
    }

    @Test
    public void getResourceTypeTest() {
        Depot depot = new Depot(2);

        assertNull(depot.getResourceType());

        ConcreteResourceSet concreteResourceSet = new ConcreteResourceSet();
        concreteResourceSet.addResource(ConcreteResource.COIN);

        depot.addResources(concreteResourceSet);

        assertEquals(ConcreteResource.COIN, depot.getResourceType());

        depot.addResources(concreteResourceSet);

        assertEquals(ConcreteResource.COIN, depot.getResourceType());

        concreteResourceSet.addResource(ConcreteResource.COIN);

        depot.removeResources(concreteResourceSet);

        assertNull(depot.getResourceType());

        concreteResourceSet.removeResource(ConcreteResource.COIN, 2);
        concreteResourceSet.addResource(ConcreteResource.STONE);

        depot.addResources(concreteResourceSet);

        assertEquals(ConcreteResource.STONE, depot.getResourceType());
    }

    @Test
    public void addNullResourceSetTest() {
        Depot depot = new Depot(2);

        try {
            depot.addResources(null);
            fail();
        } catch (InvalidResourceSetException e) {
            assertNull(depot.getResources().getResourceType());
        }
    }

    @Test
    public void addEmptyResourceSetTest() {
        Depot depot = new Depot(2);
        ConcreteResourceSet emptyResourceSet = new ConcreteResourceSet();

        depot.addResources(emptyResourceSet);

        assertNull(depot.getResourceType());
        ConcreteResourceSet resources = depot.getResources();

        assertEquals(0, resources.getCount(ConcreteResource.COIN));
        assertEquals(0, resources.getCount(ConcreteResource.STONE));
        assertEquals(0, resources.getCount(ConcreteResource.SHIELD));
        assertEquals(0, resources.getCount(ConcreteResource.SERVANT));

        ConcreteResourceSet concreteResourceSet = new ConcreteResourceSet();
        concreteResourceSet.addResource(ConcreteResource.COIN, 1);

        depot.addResources(concreteResourceSet);

        resources = depot.getResources();

        assertEquals(1, resources.getCount(ConcreteResource.COIN));
        assertEquals(0, resources.getCount(ConcreteResource.STONE));
        assertEquals(0, resources.getCount(ConcreteResource.SHIELD));
        assertEquals(0, resources.getCount(ConcreteResource.SERVANT));

        depot.addResources(emptyResourceSet);

        resources = depot.getResources();

        assertEquals(1, resources.getCount(ConcreteResource.COIN));
        assertEquals(0, resources.getCount(ConcreteResource.STONE));
        assertEquals(0, resources.getCount(ConcreteResource.SHIELD));
        assertEquals(0, resources.getCount(ConcreteResource.SERVANT));

        depot.addResources(concreteResourceSet);

        resources = depot.getResources();

        assertEquals(2, resources.getCount(ConcreteResource.COIN));
        assertEquals(0, resources.getCount(ConcreteResource.STONE));
        assertEquals(0, resources.getCount(ConcreteResource.SHIELD));
        assertEquals(0, resources.getCount(ConcreteResource.SERVANT));

        depot.addResources(emptyResourceSet);

        resources = depot.getResources();

        assertEquals(2, resources.getCount(ConcreteResource.COIN));
        assertEquals(0, resources.getCount(ConcreteResource.STONE));
        assertEquals(0, resources.getCount(ConcreteResource.SHIELD));
        assertEquals(0, resources.getCount(ConcreteResource.SERVANT));
    }

    @Test
    public void addResourcesTest() {
        Depot depot = new Depot(2);

        ConcreteResourceSet concreteResourceSet = new ConcreteResourceSet();

        concreteResourceSet.addResource(ConcreteResource.COIN);
        depot.addResources(concreteResourceSet);

        ConcreteResourceSet resources = depot.getResources();

        assertEquals(1, resources.getCount(ConcreteResource.COIN));
        assertEquals(0, resources.getCount(ConcreteResource.STONE));
        assertEquals(0, resources.getCount(ConcreteResource.SHIELD));
        assertEquals(0, resources.getCount(ConcreteResource.SERVANT));

        concreteResourceSet.addResource(ConcreteResource.COIN);

        resources = depot.getResources();

        assertEquals(1, resources.getCount(ConcreteResource.COIN));
        assertEquals(0, resources.getCount(ConcreteResource.STONE));
        assertEquals(0, resources.getCount(ConcreteResource.SHIELD));
        assertEquals(0, resources.getCount(ConcreteResource.SERVANT));

        try {
            depot.addResources(concreteResourceSet);
            fail();
        } catch (InvalidResourceLocationOperationException e) {
            resources = depot.getResources();

            assertEquals(1, resources.getCount(ConcreteResource.COIN));
            assertEquals(0, resources.getCount(ConcreteResource.STONE));
            assertEquals(0, resources.getCount(ConcreteResource.SHIELD));
            assertEquals(0, resources.getCount(ConcreteResource.SERVANT));
        }

        concreteResourceSet.removeResource(ConcreteResource.COIN, 2);
        concreteResourceSet.addResource(ConcreteResource.STONE);

        try {
            depot.addResources(concreteResourceSet);
            fail();
        } catch (InvalidResourceLocationOperationException e) {
            resources = depot.getResources();

            assertEquals(1, resources.getCount(ConcreteResource.COIN));
            assertEquals(0, resources.getCount(ConcreteResource.STONE));
            assertEquals(0, resources.getCount(ConcreteResource.SHIELD));
            assertEquals(0, resources.getCount(ConcreteResource.SERVANT));
        }

        concreteResourceSet.removeResource(ConcreteResource.STONE, 1);
        concreteResourceSet.addResource(ConcreteResource.COIN);

        depot.addResources(concreteResourceSet);

        resources = depot.getResources();

        assertEquals(2, resources.getCount(ConcreteResource.COIN));
        assertEquals(0, resources.getCount(ConcreteResource.STONE));
        assertEquals(0, resources.getCount(ConcreteResource.SHIELD));
        assertEquals(0, resources.getCount(ConcreteResource.SERVANT));

        try {
            depot.addResources(concreteResourceSet);
            fail();
        } catch (InvalidResourceLocationOperationException e) {
            resources = depot.getResources();

            assertEquals(2, resources.getCount(ConcreteResource.COIN));
            assertEquals(0, resources.getCount(ConcreteResource.STONE));
            assertEquals(0, resources.getCount(ConcreteResource.SHIELD));
            assertEquals(0, resources.getCount(ConcreteResource.SERVANT));
        }
    }

    @Test
    public void removeResourcesTest() {
        Depot depot = new Depot(2);

        ConcreteResourceSet concreteResourceSet = new ConcreteResourceSet();
        concreteResourceSet.addResource(ConcreteResource.COIN, 2);

        depot.addResources(concreteResourceSet);

        ConcreteResourceSet resources = depot.getResources();
        assertEquals(2, resources.getCount(ConcreteResource.COIN));
        assertEquals(0, resources.getCount(ConcreteResource.STONE));
        assertEquals(0, resources.getCount(ConcreteResource.SHIELD));
        assertEquals(0, resources.getCount(ConcreteResource.SERVANT));

        concreteResourceSet.removeResource(ConcreteResource.COIN);

        depot.removeResources(concreteResourceSet);

        resources = depot.getResources();
        assertEquals(1, resources.getCount(ConcreteResource.COIN));
        assertEquals(0, resources.getCount(ConcreteResource.STONE));
        assertEquals(0, resources.getCount(ConcreteResource.SHIELD));
        assertEquals(0, resources.getCount(ConcreteResource.SERVANT));

        concreteResourceSet.addResource(ConcreteResource.COIN);

        try {
            depot.removeResources(concreteResourceSet);
            fail();
        } catch (InvalidResourceLocationOperationException e) {
            resources = depot.getResources();

            assertEquals(1, resources.getCount(ConcreteResource.COIN));
            assertEquals(0, resources.getCount(ConcreteResource.STONE));
            assertEquals(0, resources.getCount(ConcreteResource.SHIELD));
            assertEquals(0, resources.getCount(ConcreteResource.SERVANT));
        }

        concreteResourceSet.removeResource(ConcreteResource.COIN);
        concreteResourceSet.addResource(ConcreteResource.SHIELD);

        try {
            depot.removeResources(concreteResourceSet);
            fail();
        } catch (InvalidResourceLocationOperationException e) {
            resources = depot.getResources();

            assertEquals(1, resources.getCount(ConcreteResource.COIN));
            assertEquals(0, resources.getCount(ConcreteResource.STONE));
            assertEquals(0, resources.getCount(ConcreteResource.SHIELD));
            assertEquals(0, resources.getCount(ConcreteResource.SERVANT));
        }

        concreteResourceSet.removeResource(ConcreteResource.SHIELD);
        concreteResourceSet.removeResource(ConcreteResource.COIN);

        depot.removeResources(concreteResourceSet);

        resources = depot.getResources();
        assertEquals(1, resources.getCount(ConcreteResource.COIN));
        assertEquals(0, resources.getCount(ConcreteResource.STONE));
        assertEquals(0, resources.getCount(ConcreteResource.SHIELD));
        assertEquals(0, resources.getCount(ConcreteResource.SERVANT));

        try {
            depot.removeResources(null);
            fail();
        } catch (InvalidResourceSetException e) {
            resources = depot.getResources();

            assertEquals(1, resources.getCount(ConcreteResource.COIN));
            assertEquals(0, resources.getCount(ConcreteResource.STONE));
            assertEquals(0, resources.getCount(ConcreteResource.SHIELD));
            assertEquals(0, resources.getCount(ConcreteResource.SERVANT));
        }

        concreteResourceSet.addResource(ConcreteResource.COIN);

        depot.removeResources(concreteResourceSet);

        resources = depot.getResources();
        assertEquals(0, resources.getCount(ConcreteResource.COIN));
        assertEquals(0, resources.getCount(ConcreteResource.STONE));
        assertEquals(0, resources.getCount(ConcreteResource.SHIELD));
        assertEquals(0, resources.getCount(ConcreteResource.SERVANT));

        concreteResourceSet.removeResource(ConcreteResource.COIN);
        concreteResourceSet.addResource(ConcreteResource.SHIELD);

        depot.addResources(concreteResourceSet);

        resources = depot.getResources();
        assertEquals(0, resources.getCount(ConcreteResource.COIN));
        assertEquals(0, resources.getCount(ConcreteResource.STONE));
        assertEquals(1, resources.getCount(ConcreteResource.SHIELD));
        assertEquals(0, resources.getCount(ConcreteResource.SERVANT));

        depot.removeResources(concreteResourceSet);
        resources = depot.getResources();
        assertEquals(0, resources.getCount(ConcreteResource.COIN));
        assertEquals(0, resources.getCount(ConcreteResource.STONE));
        assertEquals(0, resources.getCount(ConcreteResource.SHIELD));
        assertEquals(0, resources.getCount(ConcreteResource.SERVANT));
    }
}
