package it.polimi.ingsw.leaderCards;

import it.polimi.ingsw.resources.ConcreteResource;
import it.polimi.ingsw.resources.NotEnoughResourcesException;
import it.polimi.ingsw.resources.resourceSets.ConcreteResourceSet;

/**
 * DiscountEffect refers to the discount effects of active leader cards.
 */
public class DiscountEffect {

    /**
     * The type of ConcreteResource to convert the white marble into.
     */
    private ConcreteResource type;

    /**
     * @param type of ConcreteResource to convert the white marble into.
     */
    DiscountEffect(ConcreteResource type){
        this.type = type;
    }

    /**
     * applyDiscount to a concrete resource set.
     * @param set to discount.
     * @return a set discounted.
     */
    public ConcreteResourceSet applyDiscount(ConcreteResourceSet set){
        try {
            set.removeResource(type);
        } catch (NotEnoughResourcesException e) {
            e.printStackTrace();
        }
        return set;
    }
}
