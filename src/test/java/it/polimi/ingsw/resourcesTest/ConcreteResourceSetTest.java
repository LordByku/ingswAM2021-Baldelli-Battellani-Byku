package it.polimi.ingsw.resourcesTest;

import it.polimi.ingsw.playerBoard.Board;
import it.polimi.ingsw.playerBoard.InvalidBoardException;
import it.polimi.ingsw.playerBoard.resourceLocations.InvalidDepotIndexException;
import it.polimi.ingsw.playerBoard.resourceLocations.InvalidResourceLocationOperationException;
import it.polimi.ingsw.resources.*;
import it.polimi.ingsw.resources.resourceSets.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class ConcreteResourceSetTest {
    @Test
    public void addResourceTest() {
        ConcreteResourceSet concreteResourceSet = new ConcreteResourceSet();

        try {
            for (ConcreteResource resource : ConcreteResource.values()) {
                assertEquals(0, concreteResourceSet.getCount(resource));
            }
        } catch (InvalidResourceException e) {
            fail();
        }

        try {
            concreteResourceSet.addResource(ConcreteResource.COIN, 2);
        } catch (InvalidQuantityException | InvalidResourceException e) {
            fail();
        }
        try {
            assertEquals(2, concreteResourceSet.getCount(ConcreteResource.COIN));
            assertEquals(0, concreteResourceSet.getCount(ConcreteResource.STONE));
            assertEquals(0, concreteResourceSet.getCount(ConcreteResource.SHIELD));
            assertEquals(0, concreteResourceSet.getCount(ConcreteResource.SERVANT));
        } catch (InvalidResourceException e) {
            fail();
        }

        try {
            concreteResourceSet.addResource(null);
            fail();
        } catch (InvalidResourceException e) {
            try {
                assertEquals(2, concreteResourceSet.getCount(ConcreteResource.COIN));
                assertEquals(0, concreteResourceSet.getCount(ConcreteResource.STONE));
                assertEquals(0, concreteResourceSet.getCount(ConcreteResource.SHIELD));
                assertEquals(0, concreteResourceSet.getCount(ConcreteResource.SERVANT));
            } catch (InvalidResourceException e1) {
                fail();
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
        } catch (InvalidQuantityException | InvalidResourceException e) {
            fail();
        }

        try {
            concreteResourceSet.removeResource(ConcreteResource.COIN, 2);
            concreteResourceSet.removeResource(ConcreteResource.STONE, 2);

            assertEquals(1, concreteResourceSet.getCount(ConcreteResource.COIN));
            assertEquals(0, concreteResourceSet.getCount(ConcreteResource.STONE));
            assertEquals(1, concreteResourceSet.getCount(ConcreteResource.SERVANT));
            assertEquals(0, concreteResourceSet.getCount(ConcreteResource.SHIELD));
        } catch (NotEnoughResourcesException | InvalidQuantityException | InvalidResourceException e) {
            fail();
        }

        try {
            concreteResourceSet.removeResource(ConcreteResource.SERVANT, 3);
            fail();
        } catch (NotEnoughResourcesException e) {
            try {
                assertEquals(1, concreteResourceSet.getCount(ConcreteResource.SERVANT));
            } catch (InvalidResourceException e1) {
                fail();
            }
        } catch (InvalidQuantityException | InvalidResourceException e) {
            fail();
        }

        try {
            concreteResourceSet.removeResource(ConcreteResource.SHIELD, 2);
            fail();
        } catch (NotEnoughResourcesException e) {
            try {
                assertEquals(0, concreteResourceSet.getCount(ConcreteResource.SHIELD));
            } catch (InvalidResourceException e1) {
                fail();
            }
        } catch (InvalidQuantityException | InvalidResourceException e) {
            fail();
        }

        try {
            concreteResourceSet.removeResource(null);
            fail();
        } catch (NotEnoughResourcesException e) {
            fail();
        } catch (InvalidResourceException e) {
            try {
                assertEquals(1, concreteResourceSet.getCount(ConcreteResource.COIN));
                assertEquals(0, concreteResourceSet.getCount(ConcreteResource.STONE));
                assertEquals(1, concreteResourceSet.getCount(ConcreteResource.SERVANT));
                assertEquals(0, concreteResourceSet.getCount(ConcreteResource.SHIELD));
            } catch (InvalidResourceException e1) {
                fail();
            }
        }
    }

    @Test
    public void invalidQuantityTest() {
        ConcreteResourceSet concreteResourceSet = new ConcreteResourceSet();

        try {
            concreteResourceSet.addResource(ConcreteResource.COIN, 2);
            concreteResourceSet.addResource(ConcreteResource.SHIELD, 3);
        } catch (InvalidQuantityException | InvalidResourceException e) {
            fail();
        }

        try {
            concreteResourceSet.addResource(ConcreteResource.COIN, 0);
            fail();
        } catch (InvalidQuantityException e) {
            assertTrue(true);
        } catch (InvalidResourceException e) {
            fail();
        }

        try {
            concreteResourceSet.addResource(ConcreteResource.STONE, -2);
            fail();
        } catch (InvalidQuantityException e) {
            assertTrue(true);
        } catch (InvalidResourceException e) {
            fail();
        }

        try {
            concreteResourceSet.removeResource(ConcreteResource.COIN, 0);
            fail();
        } catch (NotEnoughResourcesException | InvalidResourceException e) {
            fail();
        } catch (InvalidQuantityException e) {
            assertTrue(true);
        }

        try {
            concreteResourceSet.removeResource(ConcreteResource.SHIELD, -1);
            fail();
        } catch (NotEnoughResourcesException | InvalidResourceException e) {
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
        } catch (InvalidQuantityException | InvalidResourceException e) {
            fail();
        }

        try {
            concreteResourceSet1.union(concreteResourceSet2);
        } catch (InvalidResourceSetException e) {
            fail();
        }

        try {
            assertEquals(2, concreteResourceSet1.getCount(ConcreteResource.COIN));
            assertEquals(4, concreteResourceSet1.getCount(ConcreteResource.SHIELD));
            assertEquals(1, concreteResourceSet1.getCount(ConcreteResource.STONE));
            assertEquals(0, concreteResourceSet1.getCount(ConcreteResource.SERVANT));
        } catch (InvalidResourceException e) {
            fail();
        }
    }

    @Test
    public void advancedUnionTest() {
        ConcreteResourceSet concreteResourceSet1 = new ConcreteResourceSet();
        ConcreteResourceSet concreteResourceSet2 = new ConcreteResourceSet();
        ConcreteResourceSet concreteResourceSet3 = null;
        ChoiceResourceSet choiceResourceSet = new ChoiceResourceSet();

        try {
            concreteResourceSet1.addResource(ConcreteResource.COIN, 3);
            concreteResourceSet1.addResource(ConcreteResource.STONE, 1);
        } catch (InvalidQuantityException | InvalidResourceException e) {
            fail();
        }

        try {
            concreteResourceSet1.union(concreteResourceSet3);
            fail();
        } catch (InvalidResourceSetException e) {
            assertTrue(true);
        }

        try {
            concreteResourceSet1.union(concreteResourceSet2);
        } catch (InvalidResourceSetException e) {
            fail();
        }

        try {
            choiceResourceSet.addResource(ConcreteResource.COIN);
            choiceResourceSet.addResource(ConcreteResource.SHIELD);
        } catch(InvalidResourceException e) {
            fail();
        }

        try {
            concreteResourceSet1.union(concreteResourceSet3);
            fail();
        } catch (InvalidResourceSetException e) {
            assertTrue(true);
        }

        try {
            assertEquals(3, concreteResourceSet1.getCount(ConcreteResource.COIN));
            assertEquals(1, concreteResourceSet1.getCount(ConcreteResource.STONE));
        } catch (InvalidResourceException e) {
            fail();
        }
    }

    @Test
    public void cloneTest() {
        ConcreteResourceSet concreteResourceSet1 = new ConcreteResourceSet();

        try {
            concreteResourceSet1.addResource(ConcreteResource.COIN, 4);
            concreteResourceSet1.addResource(ConcreteResource.SHIELD, 3);
        } catch (InvalidQuantityException | InvalidResourceException e) {
            fail();
        }

        ConcreteResourceSet concreteResourceSet2 = (ConcreteResourceSet) concreteResourceSet1.clone();

        try {
            assertEquals(4, concreteResourceSet2.getCount(ConcreteResource.COIN));
            assertEquals(3, concreteResourceSet2.getCount(ConcreteResource.SHIELD));
            assertEquals(0, concreteResourceSet2.getCount(ConcreteResource.STONE));
            assertEquals(0, concreteResourceSet2.getCount(ConcreteResource.SERVANT));
        } catch (InvalidResourceException e) {
            fail();
        }

        try {
            concreteResourceSet1.addResource(ConcreteResource.STONE, 1);
            concreteResourceSet1.removeResource(ConcreteResource.COIN, 2);
        } catch (InvalidQuantityException | NotEnoughResourcesException | InvalidResourceException e) {
            fail();
        }

        try {
            assertEquals(4, concreteResourceSet2.getCount(ConcreteResource.COIN));
            assertEquals(3, concreteResourceSet2.getCount(ConcreteResource.SHIELD));
            assertEquals(0, concreteResourceSet2.getCount(ConcreteResource.STONE));
            assertEquals(0, concreteResourceSet2.getCount(ConcreteResource.SERVANT));
        } catch (InvalidResourceException e) {
            fail();
        }
    }

    @Test
    public void containsTest() {
        ConcreteResourceSet concreteResourceSet1 = new ConcreteResourceSet();
        ConcreteResourceSet concreteResourceSet2 = null;

        try {
            boolean tmp = concreteResourceSet1.contains(concreteResourceSet2);
            fail();
        } catch (InvalidResourceSetException e) {
            assertTrue(true);
        }

        concreteResourceSet2 = new ConcreteResourceSet();

        try {
            assertTrue(concreteResourceSet1.contains(concreteResourceSet2));
            assertTrue(concreteResourceSet2.contains(concreteResourceSet1));
        } catch (InvalidResourceSetException e) {
            fail();
        }

        try {
            concreteResourceSet1.addResource(ConcreteResource.COIN, 3);
            concreteResourceSet1.addResource(ConcreteResource.STONE, 1);
        } catch (InvalidQuantityException | InvalidResourceException e) {
            fail();
        }

        try {
            assertTrue(concreteResourceSet1.contains(concreteResourceSet2));
            assertFalse(concreteResourceSet2.contains(concreteResourceSet1));
        } catch (InvalidResourceSetException e) {
            fail();
        }

        try {
            concreteResourceSet2.addResource(ConcreteResource.COIN, 2);
        } catch (InvalidQuantityException | InvalidResourceException e) {
            fail();
        }

        try {
            assertTrue(concreteResourceSet1.contains(concreteResourceSet2));
            assertFalse(concreteResourceSet2.contains(concreteResourceSet1));
        } catch (InvalidResourceSetException e) {
            fail();
        }

        try {
            concreteResourceSet2.addResource(ConcreteResource.SHIELD, 2);
        } catch (InvalidQuantityException | InvalidResourceException e) {
            fail();
        }

        try {
            assertFalse(concreteResourceSet1.contains(concreteResourceSet2));
            assertFalse(concreteResourceSet2.contains(concreteResourceSet1));
        } catch (InvalidResourceSetException e) {
            fail();
        }
    }

    @Test
    public void isSatisfiedTest() {
        ConcreteResourceSet concreteResourceSet = new ConcreteResourceSet();

        try {
            boolean tmp = concreteResourceSet.isSatisfied(null);
            fail();
        } catch (InvalidBoardException e) {
            assertTrue(true);
        }

        Board board = new Board();

        ConcreteResourceSet toAdd;

        try {
            toAdd = new ConcreteResourceSet();
            toAdd.addResource(ConcreteResource.COIN, 1);
            board.addResourceToWarehouse(1, toAdd);

            toAdd = new ConcreteResourceSet();
            toAdd.addResource(ConcreteResource.STONE, 3);
            board.addResourceToWarehouse(2, toAdd);

            toAdd = new ConcreteResourceSet();
            toAdd.addResource(ConcreteResource.COIN, 2);
            toAdd.addResource(ConcreteResource.SHIELD, 3);
            board.addResourceToStrongbox(toAdd);
        } catch (InvalidResourceSetException | InvalidDepotIndexException | InvalidResourceLocationOperationException | InvalidQuantityException | InvalidResourceException e) {
            fail();
        }

        try {
            assertTrue(concreteResourceSet.isSatisfied(board));
        } catch (InvalidBoardException e) {
            fail();
        }

        try {
            concreteResourceSet.addResource(ConcreteResource.COIN, 3);
            concreteResourceSet.addResource(ConcreteResource.STONE, 2);
        } catch (InvalidQuantityException | InvalidResourceException e) {
            fail();
        }

        try {
            assertTrue(concreteResourceSet.isSatisfied(board));
        } catch (InvalidBoardException e) {
            fail();
        }

        try {
            concreteResourceSet.addResource(ConcreteResource.SHIELD, 4);
        } catch (InvalidQuantityException | InvalidResourceException e) {
            fail();
        }

        try {
            assertFalse(concreteResourceSet.isSatisfied(board));
        } catch (InvalidBoardException e) {
            fail();
        }
    }

    @Test
    public void getResourceTypeTest() {
        ConcreteResourceSet concreteResourceSet = new ConcreteResourceSet();

        try {
            assertNull(concreteResourceSet.getResourceType());
        } catch (NotSingleTypeException e) {
            fail();
        }

        try {
            concreteResourceSet.addResource(ConcreteResource.COIN);
        } catch (InvalidResourceException e) {
            fail();
        }

        try {
            assertEquals(ConcreteResource.COIN, concreteResourceSet.getResourceType());
        } catch (NotSingleTypeException e) {
            fail();
        }

        try {
            concreteResourceSet.addResource(ConcreteResource.COIN);
        } catch (InvalidResourceException e) {
            fail();
        }

        try {
            assertEquals(ConcreteResource.COIN, concreteResourceSet.getResourceType());
        } catch (NotSingleTypeException e) {
            fail();
        }

        try {
            concreteResourceSet.addResource(ConcreteResource.STONE);
        } catch (InvalidResourceException e) {
            fail();
        }

        try {
            ConcreteResource tmp = concreteResourceSet.getResourceType();
            fail();
        } catch (NotSingleTypeException e) {
            assertTrue(true);
        }

        try {
            concreteResourceSet.removeResource(ConcreteResource.STONE);
            concreteResourceSet.removeResource(ConcreteResource.COIN, 2);
        } catch (NotEnoughResourcesException | InvalidQuantityException | InvalidResourceException e) {
            fail();
        }

        try {
            assertNull(concreteResourceSet.getResourceType());
        } catch (NotSingleTypeException e) {
            fail();
        }
    }
}
