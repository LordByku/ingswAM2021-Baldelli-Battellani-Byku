package it.polimi.ingsw.leaderCardsTest;

import it.polimi.ingsw.leaderCards.DiscountEffect;
import it.polimi.ingsw.resources.ConcreteResource;
import it.polimi.ingsw.resources.resourceSets.ConcreteResourceSet;
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

        DiscountEffect discountEffect = new DiscountEffect(ConcreteResource.STONE);
        assertFalse(concreteResourceSet.equals(concreteResourceSetDiscounted));
        assertTrue(concreteResourceSet.equals(discountEffect.applyDiscount(concreteResourceSet)));
    }
}
