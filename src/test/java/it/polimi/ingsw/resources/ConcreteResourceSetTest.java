package it.polimi.ingsw.resources;

import org.junit.Test;

import static org.junit.Assert.*;

public class ConcreteResourceSetTest {
    @Test
    public void addResourceTest() {
        ConcreteResourceSet concreteResourceSet = new ConcreteResourceSet();

        for(ConcreteResource resource: ConcreteResource.values()) {
            assertEquals(0, concreteResourceSet.getCount(resource));
        }

        concreteResourceSet.addResource(ConcreteResource.COIN, 2);
        for(ConcreteResource resource: ConcreteResource.values()) {
            if(resource == ConcreteResource.COIN) {
                assertEquals(2, concreteResourceSet.getCount(resource));
            } else {
                assertEquals(0, concreteResourceSet.getCount(resource));
            }
        }
    }

    @Test
    public void removeResourceTest() {
        ConcreteResourceSet concreteResourceSet = new ConcreteResourceSet();

        concreteResourceSet.addResource(ConcreteResource.COIN, 3);
        concreteResourceSet.addResource(ConcreteResource.SERVANT, 1);
        concreteResourceSet.addResource(ConcreteResource.STONE, 2);

        try {
            concreteResourceSet.removeResource(ConcreteResource.COIN, 2);
            concreteResourceSet.removeResource(ConcreteResource.STONE, 2);

            assertEquals(1, concreteResourceSet.getCount(ConcreteResource.COIN));
            assertEquals(0, concreteResourceSet.getCount(ConcreteResource.STONE));
            assertEquals(1, concreteResourceSet.getCount(ConcreteResource.SERVANT));
            assertEquals(0, concreteResourceSet.getCount(ConcreteResource.SHIELD));
        } catch (NotEnoughResourcesException e) {
            fail();
        }

        try {
            concreteResourceSet.removeResource(ConcreteResource.SERVANT, 3);
            fail();
        } catch (NotEnoughResourcesException e) {
            assertEquals(1, concreteResourceSet.getCount(ConcreteResource.SERVANT));
        }

        try {
            concreteResourceSet.removeResource(ConcreteResource.SHIELD, 2);
            fail();
        } catch (NotEnoughResourcesException e) {
            assertEquals(0, concreteResourceSet.getCount(ConcreteResource.SHIELD));
        }
    }

    @Test
    public void unionTest() {
        ConcreteResourceSet concreteResourceSet1 = new ConcreteResourceSet();
        ConcreteResourceSet concreteResourceSet2 = new ConcreteResourceSet();

        concreteResourceSet1.addResource(ConcreteResource.COIN, 2);
        concreteResourceSet1.addResource(ConcreteResource.SHIELD, 1);

        concreteResourceSet2.addResource(ConcreteResource.SHIELD, 3);
        concreteResourceSet2.addResource(ConcreteResource.STONE, 1);

        concreteResourceSet1.union(concreteResourceSet2);

        assertEquals(2, concreteResourceSet1.getCount(ConcreteResource.COIN));
        assertEquals(4, concreteResourceSet1.getCount(ConcreteResource.SHIELD));
        assertEquals(1, concreteResourceSet1.getCount(ConcreteResource.STONE));
        assertEquals(0, concreteResourceSet1.getCount(ConcreteResource.SERVANT));
    }
}
