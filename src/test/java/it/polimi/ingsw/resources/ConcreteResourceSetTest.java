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
    public void unionTest() {
        // TODO
    }
}
