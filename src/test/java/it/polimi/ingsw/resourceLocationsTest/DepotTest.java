package it.polimi.ingsw.resourceLocationsTest;

import it.polimi.ingsw.playerBoard.resourceLocations.Depot;
import it.polimi.ingsw.playerBoard.resourceLocations.InvalidDepotSizeException;
import it.polimi.ingsw.playerBoard.resourceLocations.InvalidResourceLocationOperationException;
import it.polimi.ingsw.resources.ConcreteResource;
import it.polimi.ingsw.resources.InvalidResourceException;
import it.polimi.ingsw.resources.NotEnoughResourcesException;
import it.polimi.ingsw.resources.resourceSets.ConcreteResourceSet;
import it.polimi.ingsw.resources.resourceSets.InvalidQuantityException;
import it.polimi.ingsw.resources.resourceSets.InvalidResourceSetException;
import org.junit.Test;

import static org.junit.Assert.*;

public class DepotTest {
    @Test
    public void constructorTest() {
        try {
            Depot depot = new Depot(1);

            ConcreteResourceSet resourceSet = depot.getResources();

            assertNotNull(resourceSet);
            assertEquals(0, resourceSet.getCount(ConcreteResource.COIN));
            assertEquals(0, resourceSet.getCount(ConcreteResource.STONE));
            assertEquals(0, resourceSet.getCount(ConcreteResource.SHIELD));
            assertEquals(0, resourceSet.getCount(ConcreteResource.SERVANT));
        } catch (InvalidDepotSizeException | InvalidResourceException e) {
            fail();
        }

        try {
            Depot depot = new Depot(-1);
            fail();
        } catch (InvalidDepotSizeException e) {
            assertTrue(true);
        }
    }

    @Test
    public void addResourcesTest() {
        try {
            Depot depot = new Depot(2);

            try {
                depot.addResources(null);
                fail();
            } catch (InvalidResourceSetException e) {
                assertTrue(true);
            } catch (InvalidResourceLocationOperationException e) {
                fail();
            }

            ConcreteResourceSet concreteResourceSet = new ConcreteResourceSet();
            try {
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
            } catch (InvalidResourceLocationOperationException | InvalidResourceSetException | InvalidResourceException e) {
                fail();
            }

            try {
                depot.addResources(concreteResourceSet);
                fail();
            } catch (InvalidResourceSetException e) {
                fail();
            } catch (InvalidResourceLocationOperationException e) {
                ConcreteResourceSet resources = depot.getResources();

                try {
                    assertEquals(1, resources.getCount(ConcreteResource.COIN));
                    assertEquals(0, resources.getCount(ConcreteResource.STONE));
                    assertEquals(0, resources.getCount(ConcreteResource.SHIELD));
                    assertEquals(0, resources.getCount(ConcreteResource.SERVANT));
                } catch (InvalidResourceException e1) {
                    fail();
                }
            }

            try {
                concreteResourceSet.removeResource(ConcreteResource.COIN, 2);
                concreteResourceSet.addResource(ConcreteResource.STONE);
            } catch (NotEnoughResourcesException | InvalidQuantityException | InvalidResourceException e) {
                fail();
            }

            try {
                depot.addResources(concreteResourceSet);
                fail();
            } catch (InvalidResourceSetException e) {
                fail();
            } catch (InvalidResourceLocationOperationException e) {
                ConcreteResourceSet resources = depot.getResources();

                try {
                    assertEquals(1, resources.getCount(ConcreteResource.COIN));
                    assertEquals(0, resources.getCount(ConcreteResource.STONE));
                    assertEquals(0, resources.getCount(ConcreteResource.SHIELD));
                    assertEquals(0, resources.getCount(ConcreteResource.SERVANT));
                } catch (InvalidResourceException e1) {
                    fail();
                }
            }

            try {
                concreteResourceSet.removeResource(ConcreteResource.STONE, 1);
                concreteResourceSet.addResource(ConcreteResource.COIN);
            } catch (NotEnoughResourcesException | InvalidQuantityException | InvalidResourceException e) {
                fail();
            }

            try {
                depot.addResources(concreteResourceSet);

                ConcreteResourceSet resources = depot.getResources();

                assertEquals(2, resources.getCount(ConcreteResource.COIN));
                assertEquals(0, resources.getCount(ConcreteResource.STONE));
                assertEquals(0, resources.getCount(ConcreteResource.SHIELD));
                assertEquals(0, resources.getCount(ConcreteResource.SERVANT));
            } catch (InvalidResourceSetException | InvalidResourceLocationOperationException | InvalidResourceException e) {
                fail();
            }

            try {
                depot.addResources(concreteResourceSet);
                fail();
            } catch (InvalidResourceSetException e) {
                fail();
            } catch (InvalidResourceLocationOperationException e) {
                assertTrue(true);
            }

            try {
                concreteResourceSet.removeResource(ConcreteResource.COIN);
            } catch (InvalidResourceException | NotEnoughResourcesException e) {
                fail();
            }

            try {
                depot.addResources(concreteResourceSet);

                ConcreteResourceSet resources = depot.getResources();

                assertEquals(2, resources.getCount(ConcreteResource.COIN));
                assertEquals(0, resources.getCount(ConcreteResource.STONE));
                assertEquals(0, resources.getCount(ConcreteResource.SHIELD));
                assertEquals(0, resources.getCount(ConcreteResource.SERVANT));
            } catch (InvalidResourceSetException | InvalidResourceLocationOperationException | InvalidResourceException e) {
                fail();
            }
        } catch (InvalidDepotSizeException e) {
            fail();
        }
    }
}
