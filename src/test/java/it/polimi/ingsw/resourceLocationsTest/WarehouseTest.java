package it.polimi.ingsw.resourceLocationsTest;

import it.polimi.ingsw.leaderCards.InvalidLeaderCardDepotException;
import it.polimi.ingsw.leaderCards.LeaderCardDepot;
import it.polimi.ingsw.playerBoard.resourceLocations.InvalidDepotIndexException;
import it.polimi.ingsw.playerBoard.resourceLocations.InvalidResourceLocationOperationException;
import it.polimi.ingsw.playerBoard.resourceLocations.Warehouse;
import it.polimi.ingsw.resources.ConcreteResource;
import it.polimi.ingsw.resources.resourceSets.ConcreteResourceSet;
import it.polimi.ingsw.resources.resourceSets.InvalidResourceSetException;
import org.junit.Test;

import static org.junit.Assert.*;

public class WarehouseTest {
    @Test
    public void canAddNullResourceSetTest() {
        Warehouse warehouse = new Warehouse();

        try {
            boolean tmp = warehouse.canAdd(0, null);
            fail();
        } catch (InvalidResourceSetException e) {
            assertNull(warehouse.getResources().getResourceType());
        }
    }

    @Test
    public void canAddInvalidIndexTest() {
        Warehouse warehouse = new Warehouse();

        try {
            boolean tmp = warehouse.canAdd(3, new ConcreteResourceSet());
            fail();
        } catch (InvalidDepotIndexException e) {
            assertNull(warehouse.getResources().getResourceType());
        }
    }

    @Test
    public void canAddEmptyResourceSetTest() {
        Warehouse warehouse = new Warehouse();

        ConcreteResourceSet concreteResourceSet = new ConcreteResourceSet();

        assertTrue(warehouse.canAdd(0, concreteResourceSet));
        assertTrue(warehouse.canAdd(1, concreteResourceSet));
        assertTrue(warehouse.canAdd(2, concreteResourceSet));
        assertNull(warehouse.getResources().getResourceType());

        warehouse.addLeaderCardDepot(new LeaderCardDepot(ConcreteResource.COIN));
        warehouse.addLeaderCardDepot(new LeaderCardDepot(ConcreteResource.STONE));

        assertTrue(warehouse.canAdd(3, concreteResourceSet));
        assertTrue(warehouse.canAdd(4, concreteResourceSet));
        assertNull(warehouse.getResources().getResourceType());
    }

    @Test
    public void canAddTest() {
        Warehouse warehouse = new Warehouse();

        ConcreteResourceSet concreteResourceSet = new ConcreteResourceSet();

        concreteResourceSet.addResource(ConcreteResource.COIN);

        assertTrue(warehouse.canAdd(0, concreteResourceSet));
        assertTrue(warehouse.canAdd(1, concreteResourceSet));
        assertTrue(warehouse.canAdd(2, concreteResourceSet));

        assertNull(warehouse.getResources().getResourceType());

        concreteResourceSet.addResource(ConcreteResource.COIN);

        assertFalse(warehouse.canAdd(0, concreteResourceSet));
        assertTrue(warehouse.canAdd(1, concreteResourceSet));
        assertTrue(warehouse.canAdd(2, concreteResourceSet));

        assertNull(warehouse.getResources().getResourceType());

        concreteResourceSet.addResource(ConcreteResource.COIN);

        assertFalse(warehouse.canAdd(0, concreteResourceSet));
        assertFalse(warehouse.canAdd(1, concreteResourceSet));
        assertTrue(warehouse.canAdd(2, concreteResourceSet));

        assertNull(warehouse.getResources().getResourceType());

        concreteResourceSet.addResource(ConcreteResource.COIN);

        assertFalse(warehouse.canAdd(0, concreteResourceSet));
        assertFalse(warehouse.canAdd(1, concreteResourceSet));
        assertFalse(warehouse.canAdd(2, concreteResourceSet));

        assertNull(warehouse.getResources().getResourceType());

        concreteResourceSet = new ConcreteResourceSet();
        concreteResourceSet.addResource(ConcreteResource.COIN);
        concreteResourceSet.addResource(ConcreteResource.SHIELD);

        assertFalse(warehouse.canAdd(0, concreteResourceSet));
        assertFalse(warehouse.canAdd(1, concreteResourceSet));
        assertFalse(warehouse.canAdd(2, concreteResourceSet));

        assertNull(warehouse.getResources().getResourceType());

        concreteResourceSet = new ConcreteResourceSet();

        concreteResourceSet.addResource(ConcreteResource.COIN);

        warehouse.addResources(1, concreteResourceSet);

        assertFalse(warehouse.canAdd(0, concreteResourceSet));
        assertTrue(warehouse.canAdd(1, concreteResourceSet));
        assertFalse(warehouse.canAdd(2, concreteResourceSet));
    }

    @Test
    public void addLeaderCardDepotTest() {
        Warehouse warehouse = new Warehouse();
        try {
            warehouse.addLeaderCardDepot(null);
            fail();
        } catch (InvalidLeaderCardDepotException e) {
            assertEquals(3, warehouse.numberOfDepots());
        }

        LeaderCardDepot leaderCardDepot = new LeaderCardDepot(ConcreteResource.COIN);

        warehouse.addLeaderCardDepot(leaderCardDepot);

        ConcreteResourceSet concreteResourceSet = new ConcreteResourceSet();
        assertTrue(warehouse.canAdd(3, concreteResourceSet));

        concreteResourceSet.addResource(ConcreteResource.COIN);
        assertTrue(warehouse.canAdd(3, concreteResourceSet));

        concreteResourceSet.addResource(ConcreteResource.SHIELD);
        assertFalse(warehouse.canAdd(3, concreteResourceSet));

        concreteResourceSet.removeResource(ConcreteResource.COIN);
        assertFalse(warehouse.canAdd(3, concreteResourceSet));

        concreteResourceSet.removeResource(ConcreteResource.SHIELD);
        concreteResourceSet.addResource(ConcreteResource.COIN, 2);
        assertTrue(warehouse.canAdd(3, concreteResourceSet));

        concreteResourceSet.addResource(ConcreteResource.COIN);
        assertFalse(warehouse.canAdd(3, concreteResourceSet));
    }

    @Test
    public void getResourcesTest() {
        Warehouse warehouse = new Warehouse();

        ConcreteResourceSet resources = warehouse.getResources();

        assertEquals(0, resources.getCount(ConcreteResource.COIN));
        assertEquals(0, resources.getCount(ConcreteResource.STONE));
        assertEquals(0, resources.getCount(ConcreteResource.SHIELD));
        assertEquals(0, resources.getCount(ConcreteResource.SERVANT));

        ConcreteResourceSet concreteResourceSet;
        concreteResourceSet = new ConcreteResourceSet();
        concreteResourceSet.addResource(ConcreteResource.COIN);
        warehouse.addResources(0, concreteResourceSet);

        concreteResourceSet = new ConcreteResourceSet();
        concreteResourceSet.addResource(ConcreteResource.STONE, 2);
        warehouse.addResources(2, concreteResourceSet);

        warehouse.addLeaderCardDepot(new LeaderCardDepot(ConcreteResource.COIN));
        concreteResourceSet = new ConcreteResourceSet();
        concreteResourceSet.addResource(ConcreteResource.COIN, 2);
        warehouse.addResources(3, concreteResourceSet);

        resources = warehouse.getResources();

        assertEquals(3, resources.getCount(ConcreteResource.COIN));
        assertEquals(2, resources.getCount(ConcreteResource.STONE));
        assertEquals(0, resources.getCount(ConcreteResource.SHIELD));
        assertEquals(0, resources.getCount(ConcreteResource.SERVANT));
    }

    @Test
    public void addResourcesNullResourceSetTest() {
        Warehouse warehouse = new Warehouse();

        try {
            warehouse.addResources(0, null);
            fail();
        } catch (InvalidResourceSetException e) {
            assertNull(warehouse.getResources().getResourceType());
        }
    }

    @Test
    public void addResourcesInvalidIndexTest() {
        Warehouse warehouse = new Warehouse();

        try {
            warehouse.addResources(-1, new ConcreteResourceSet());
            fail();
        } catch (InvalidDepotIndexException e) {
            assertNull(warehouse.getResources().getResourceType());
        }
    }

    @Test
    public void addResourcesTest() {
        Warehouse warehouse = new Warehouse();
        ConcreteResourceSet concreteResourceSet = new ConcreteResourceSet();

        concreteResourceSet.addResource(ConcreteResource.COIN, 1);

        warehouse.addResources(1, concreteResourceSet);

        ConcreteResourceSet resources = warehouse.getResources();
        assertEquals(1, resources.getCount(ConcreteResource.COIN));
        assertEquals(0, resources.getCount(ConcreteResource.STONE));
        assertEquals(0, resources.getCount(ConcreteResource.SHIELD));
        assertEquals(0, resources.getCount(ConcreteResource.SERVANT));

        try {
            warehouse.addResources(0, concreteResourceSet);
            fail();
        } catch (InvalidResourceLocationOperationException e) {
            resources = warehouse.getResources();
            assertEquals(1, resources.getCount(ConcreteResource.COIN));
            assertEquals(0, resources.getCount(ConcreteResource.STONE));
            assertEquals(0, resources.getCount(ConcreteResource.SHIELD));
            assertEquals(0, resources.getCount(ConcreteResource.SERVANT));
        }

        concreteResourceSet = new ConcreteResourceSet();
        concreteResourceSet.addResource(ConcreteResource.STONE, 3);

        try {
            warehouse.addResources(0, concreteResourceSet);
            fail();
        } catch (InvalidResourceLocationOperationException e) {
            resources = warehouse.getResources();
            assertEquals(1, resources.getCount(ConcreteResource.COIN));
            assertEquals(0, resources.getCount(ConcreteResource.STONE));
            assertEquals(0, resources.getCount(ConcreteResource.SHIELD));
            assertEquals(0, resources.getCount(ConcreteResource.SERVANT));
        }

        warehouse.addResources(2, concreteResourceSet);

        resources = warehouse.getResources();
        assertEquals(1, resources.getCount(ConcreteResource.COIN));
        assertEquals(3, resources.getCount(ConcreteResource.STONE));
        assertEquals(0, resources.getCount(ConcreteResource.SHIELD));
        assertEquals(0, resources.getCount(ConcreteResource.SERVANT));

        concreteResourceSet = new ConcreteResourceSet();
        concreteResourceSet.addResource(ConcreteResource.COIN);

        warehouse.addLeaderCardDepot(new LeaderCardDepot(ConcreteResource.COIN));
        warehouse.addResources(3, concreteResourceSet);

        resources = warehouse.getResources();
        assertEquals(2, resources.getCount(ConcreteResource.COIN));
        assertEquals(3, resources.getCount(ConcreteResource.STONE));
        assertEquals(0, resources.getCount(ConcreteResource.SHIELD));
        assertEquals(0, resources.getCount(ConcreteResource.SERVANT));

        concreteResourceSet = new ConcreteResourceSet();
        concreteResourceSet.addResource(ConcreteResource.SHIELD, 2);

        warehouse.addLeaderCardDepot(new LeaderCardDepot(ConcreteResource.SHIELD));
        warehouse.addResources(4, concreteResourceSet);

        resources = warehouse.getResources();
        assertEquals(2, resources.getCount(ConcreteResource.COIN));
        assertEquals(3, resources.getCount(ConcreteResource.STONE));
        assertEquals(2, resources.getCount(ConcreteResource.SHIELD));
        assertEquals(0, resources.getCount(ConcreteResource.SERVANT));
    }

    @Test
    public void removeResourcesNullResourceSetTest() {
        Warehouse warehouse = new Warehouse();

        try {
            warehouse.removeResources(0, null);
            fail();
        } catch (InvalidResourceSetException e) {
            assertNull(warehouse.getResources().getResourceType());
        }
    }

    @Test
    public void removeResourcesInvalidIndexTest() {
        Warehouse warehouse = new Warehouse();

        warehouse.addLeaderCardDepot(new LeaderCardDepot(ConcreteResource.COIN));

        try {
            warehouse.removeResources(4, new ConcreteResourceSet());
            fail();
        } catch (InvalidDepotIndexException e) {
            assertNull(warehouse.getResources().getResourceType());
        }
    }

    @Test
    public void removeResourcesTest() {
        Warehouse warehouse = new Warehouse();

        ConcreteResourceSet concreteResourceSet = new ConcreteResourceSet();
        concreteResourceSet.addResource(ConcreteResource.COIN, 1);
        warehouse.addResources(2, concreteResourceSet);

        concreteResourceSet = new ConcreteResourceSet();
        concreteResourceSet.addResource(ConcreteResource.STONE, 2);
        warehouse.addResources(1, concreteResourceSet);

        warehouse.addLeaderCardDepot(new LeaderCardDepot(ConcreteResource.COIN));
        concreteResourceSet = new ConcreteResourceSet();
        concreteResourceSet.addResource(ConcreteResource.COIN, 2);
        warehouse.addResources(3, concreteResourceSet);

        try {
            warehouse.removeResources(2, concreteResourceSet);
            fail();
        } catch (InvalidResourceLocationOperationException e) {
            ConcreteResourceSet resources = warehouse.getResources();
            assertEquals(3, resources.getCount(ConcreteResource.COIN));
            assertEquals(2, resources.getCount(ConcreteResource.STONE));
            assertEquals(0, resources.getCount(ConcreteResource.SHIELD));
            assertEquals(0, resources.getCount(ConcreteResource.SERVANT));
        }

        concreteResourceSet.removeResource(ConcreteResource.COIN);
        warehouse.removeResources(2, concreteResourceSet);

        ConcreteResourceSet resources = warehouse.getResources();
        assertEquals(2, resources.getCount(ConcreteResource.COIN));
        assertEquals(2, resources.getCount(ConcreteResource.STONE));
        assertEquals(0, resources.getCount(ConcreteResource.SHIELD));
        assertEquals(0, resources.getCount(ConcreteResource.SERVANT));

        warehouse.removeResources(3, concreteResourceSet);

        resources = warehouse.getResources();
        assertEquals(1, resources.getCount(ConcreteResource.COIN));
        assertEquals(2, resources.getCount(ConcreteResource.STONE));
        assertEquals(0, resources.getCount(ConcreteResource.SHIELD));
        assertEquals(0, resources.getCount(ConcreteResource.SERVANT));

        warehouse.removeResources(1, new ConcreteResourceSet());

        resources = warehouse.getResources();
        assertEquals(1, resources.getCount(ConcreteResource.COIN));
        assertEquals(2, resources.getCount(ConcreteResource.STONE));
        assertEquals(0, resources.getCount(ConcreteResource.SHIELD));
        assertEquals(0, resources.getCount(ConcreteResource.SERVANT));
    }

    @Test
    public void swapResourcesTest() {
        Warehouse warehouse = new Warehouse();

        warehouse.swapResources(0, 1);
        warehouse.swapResources(1, 2);
        warehouse.swapResources(2, 0);
        assertNull(warehouse.getResources().getResourceType());

        ConcreteResourceSet concreteResourceSet = new ConcreteResourceSet();
        concreteResourceSet.addResource(ConcreteResource.COIN);
        warehouse.addResources(0, concreteResourceSet);

        ConcreteResourceSet resources0, resources1, resources2, resources3;

        resources0 = warehouse.getDepotResources(0);
        assertEquals(1, resources0.getCount(ConcreteResource.COIN));
        assertEquals(0, resources0.getCount(ConcreteResource.STONE));
        assertEquals(0, resources0.getCount(ConcreteResource.SHIELD));
        assertEquals(0, resources0.getCount(ConcreteResource.SERVANT));

        warehouse.swapResources(0, 1);

        resources0 = warehouse.getDepotResources(0);
        assertEquals(0, resources0.getCount(ConcreteResource.COIN));
        assertEquals(0, resources0.getCount(ConcreteResource.STONE));
        assertEquals(0, resources0.getCount(ConcreteResource.SHIELD));
        assertEquals(0, resources0.getCount(ConcreteResource.SERVANT));

        resources1 = warehouse.getDepotResources(1);
        assertEquals(1, resources1.getCount(ConcreteResource.COIN));
        assertEquals(0, resources1.getCount(ConcreteResource.STONE));
        assertEquals(0, resources1.getCount(ConcreteResource.SHIELD));
        assertEquals(0, resources1.getCount(ConcreteResource.SERVANT));

        concreteResourceSet = new ConcreteResourceSet();
        concreteResourceSet.addResource(ConcreteResource.STONE, 2);
        warehouse.addResources(2, concreteResourceSet);

        resources2 = warehouse.getDepotResources(2);
        assertEquals(0, resources2.getCount(ConcreteResource.COIN));
        assertEquals(2, resources2.getCount(ConcreteResource.STONE));
        assertEquals(0, resources2.getCount(ConcreteResource.SHIELD));
        assertEquals(0, resources2.getCount(ConcreteResource.SERVANT));

        warehouse.swapResources(2, 1);

        resources1 = warehouse.getDepotResources(1);
        assertEquals(0, resources1.getCount(ConcreteResource.COIN));
        assertEquals(2, resources1.getCount(ConcreteResource.STONE));
        assertEquals(0, resources1.getCount(ConcreteResource.SHIELD));
        assertEquals(0, resources1.getCount(ConcreteResource.SERVANT));

        resources2 = warehouse.getDepotResources(2);
        assertEquals(1, resources2.getCount(ConcreteResource.COIN));
        assertEquals(0, resources2.getCount(ConcreteResource.STONE));
        assertEquals(0, resources2.getCount(ConcreteResource.SHIELD));
        assertEquals(0, resources2.getCount(ConcreteResource.SERVANT));

        try {
            warehouse.swapResources(0, 1);
            fail();
        } catch (InvalidResourceLocationOperationException e) {
            resources0 = warehouse.getDepotResources(0);
            assertEquals(0, resources0.getCount(ConcreteResource.COIN));
            assertEquals(0, resources0.getCount(ConcreteResource.STONE));
            assertEquals(0, resources0.getCount(ConcreteResource.SHIELD));
            assertEquals(0, resources0.getCount(ConcreteResource.SERVANT));

            resources1 = warehouse.getDepotResources(1);
            assertEquals(0, resources1.getCount(ConcreteResource.COIN));
            assertEquals(2, resources1.getCount(ConcreteResource.STONE));
            assertEquals(0, resources1.getCount(ConcreteResource.SHIELD));
            assertEquals(0, resources1.getCount(ConcreteResource.SERVANT));
        }

        warehouse.addLeaderCardDepot(new LeaderCardDepot(ConcreteResource.STONE));

        warehouse.swapResources(1, 3);

        resources1 = warehouse.getDepotResources(1);
        assertEquals(0, resources1.getCount(ConcreteResource.COIN));
        assertEquals(0, resources1.getCount(ConcreteResource.STONE));
        assertEquals(0, resources1.getCount(ConcreteResource.SHIELD));
        assertEquals(0, resources1.getCount(ConcreteResource.SERVANT));

        resources3 = warehouse.getDepotResources(3);
        assertEquals(0, resources3.getCount(ConcreteResource.COIN));
        assertEquals(2, resources3.getCount(ConcreteResource.STONE));
        assertEquals(0, resources3.getCount(ConcreteResource.SHIELD));
        assertEquals(0, resources3.getCount(ConcreteResource.SERVANT));

        try {
            warehouse.swapResources(2, 3);
            fail();
        } catch (InvalidResourceLocationOperationException e) {
            resources2 = warehouse.getDepotResources(2);
            assertEquals(1, resources2.getCount(ConcreteResource.COIN));
            assertEquals(0, resources2.getCount(ConcreteResource.STONE));
            assertEquals(0, resources2.getCount(ConcreteResource.SHIELD));
            assertEquals(0, resources2.getCount(ConcreteResource.SERVANT));

            resources3 = warehouse.getDepotResources(3);
            assertEquals(0, resources3.getCount(ConcreteResource.COIN));
            assertEquals(2, resources3.getCount(ConcreteResource.STONE));
            assertEquals(0, resources3.getCount(ConcreteResource.SHIELD));
            assertEquals(0, resources3.getCount(ConcreteResource.SERVANT));
        }
    }
}
