package it.polimi.ingsw.model.playerBoardTest;

import it.polimi.ingsw.model.leaderCards.DiscountEffect;
import it.polimi.ingsw.model.playerBoard.DiscountArea;
import it.polimi.ingsw.model.resources.ConcreteResource;
import it.polimi.ingsw.model.resources.resourceSets.ConcreteResourceSet;
import it.polimi.ingsw.model.resources.resourceSets.InvalidResourceSetException;
import org.junit.Test;

import static org.junit.Assert.*;

public class DiscountAreaTest {
    @Test
    public void applyDiscountTest() {
        DiscountArea discountArea = new DiscountArea();

        discountArea.addDiscountEffect(new DiscountEffect(ConcreteResource.COIN, 1));
        discountArea.addDiscountEffect(new DiscountEffect(ConcreteResource.STONE, 1));

        ConcreteResourceSet concreteResourceSet = new ConcreteResourceSet();

        concreteResourceSet.addResource(ConcreteResource.COIN, 2);
        concreteResourceSet.addResource(ConcreteResource.STONE);
        concreteResourceSet.addResource(ConcreteResource.SHIELD);

        ConcreteResourceSet discounted = discountArea.applyDiscount(concreteResourceSet);

        assertEquals(1, discounted.getCount(ConcreteResource.COIN));
        assertEquals(0, discounted.getCount(ConcreteResource.STONE));
        assertEquals(1, discounted.getCount(ConcreteResource.SHIELD));
        assertEquals(0, discounted.getCount(ConcreteResource.SERVANT));
    }

    @Test
    public void advancedApplyDiscountTest() {
        DiscountArea discountArea = new DiscountArea();

        try {
            ConcreteResourceSet tmp = discountArea.applyDiscount(null);
            fail();
        } catch (InvalidResourceSetException e) {
            assertTrue(true);
        }

        discountArea.addDiscountEffect(new DiscountEffect(ConcreteResource.COIN, 1));
        discountArea.addDiscountEffect(new DiscountEffect(ConcreteResource.STONE, 1));

        ConcreteResourceSet concreteResourceSet = new ConcreteResourceSet();
        ConcreteResourceSet discounted = discountArea.applyDiscount(concreteResourceSet);

        assertEquals(0, discounted.getCount(ConcreteResource.COIN));
        assertEquals(0, discounted.getCount(ConcreteResource.STONE));
        assertEquals(0, discounted.getCount(ConcreteResource.SHIELD));
        assertEquals(0, discounted.getCount(ConcreteResource.SERVANT));

        concreteResourceSet.addResource(ConcreteResource.COIN);
        discounted = discountArea.applyDiscount((ConcreteResourceSet) concreteResourceSet.clone());

        assertEquals(0, discounted.getCount(ConcreteResource.COIN));
        assertEquals(0, discounted.getCount(ConcreteResource.STONE));
        assertEquals(0, discounted.getCount(ConcreteResource.SHIELD));
        assertEquals(0, discounted.getCount(ConcreteResource.SERVANT));

        concreteResourceSet.addResource(ConcreteResource.STONE);
        discounted = discountArea.applyDiscount((ConcreteResourceSet) concreteResourceSet.clone());

        assertEquals(0, discounted.getCount(ConcreteResource.COIN));
        assertEquals(0, discounted.getCount(ConcreteResource.STONE));
        assertEquals(0, discounted.getCount(ConcreteResource.SHIELD));
        assertEquals(0, discounted.getCount(ConcreteResource.SERVANT));

        concreteResourceSet.addResource(ConcreteResource.SHIELD);
        discounted = discountArea.applyDiscount((ConcreteResourceSet) concreteResourceSet.clone());

        assertEquals(0, discounted.getCount(ConcreteResource.COIN));
        assertEquals(0, discounted.getCount(ConcreteResource.STONE));
        assertEquals(1, discounted.getCount(ConcreteResource.SHIELD));
        assertEquals(0, discounted.getCount(ConcreteResource.SERVANT));

        concreteResourceSet.removeResource(ConcreteResource.STONE);
        concreteResourceSet.addResource(ConcreteResource.COIN);
        discounted = discountArea.applyDiscount((ConcreteResourceSet) concreteResourceSet.clone());

        assertEquals(1, discounted.getCount(ConcreteResource.COIN));
        assertEquals(0, discounted.getCount(ConcreteResource.STONE));
        assertEquals(1, discounted.getCount(ConcreteResource.SHIELD));
        assertEquals(0, discounted.getCount(ConcreteResource.SERVANT));

        concreteResourceSet.addResource(ConcreteResource.STONE, 3);
        discounted = discountArea.applyDiscount((ConcreteResourceSet) concreteResourceSet.clone());

        assertEquals(1, discounted.getCount(ConcreteResource.COIN));
        assertEquals(2, discounted.getCount(ConcreteResource.STONE));
        assertEquals(1, discounted.getCount(ConcreteResource.SHIELD));
        assertEquals(0, discounted.getCount(ConcreteResource.SERVANT));
    }
}
