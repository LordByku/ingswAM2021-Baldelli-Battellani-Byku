package it.polimi.ingsw.model.leaderCardsTest;

import it.polimi.ingsw.model.leaderCards.DiscountEffect;
import it.polimi.ingsw.model.resources.ConcreteResource;
import it.polimi.ingsw.model.resources.resourceSets.ConcreteResourceSet;
import it.polimi.ingsw.model.resources.resourceSets.InvalidResourceSetException;
import org.junit.Test;

import static org.junit.Assert.*;

public class DiscountEffectTest {
    @Test

    public void applyDiscountTest(){
        ConcreteResourceSet concreteResourceSet = new ConcreteResourceSet();
        concreteResourceSet.addResource(ConcreteResource.STONE,2);
        concreteResourceSet.addResource(ConcreteResource.SHIELD);

        ConcreteResourceSet concreteResourceSetDiscounted = new ConcreteResourceSet();
        concreteResourceSetDiscounted.addResource(ConcreteResource.STONE);
        concreteResourceSetDiscounted.addResource(ConcreteResource.SHIELD);

        DiscountEffect discountEffect = new DiscountEffect(ConcreteResource.STONE, 1);
        assertNotEquals(concreteResourceSet, concreteResourceSetDiscounted);
        assertEquals(concreteResourceSet, discountEffect.applyDiscount(concreteResourceSet));

        try {
            discountEffect.applyDiscount(null);
            fail();
        } catch (InvalidResourceSetException e) {
            assertTrue(true);
        }
    }
}
