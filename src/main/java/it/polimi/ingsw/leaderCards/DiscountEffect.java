package it.polimi.ingsw.leaderCards;

import it.polimi.ingsw.resources.ConcreteResource;
import it.polimi.ingsw.resources.InvalidResourceException;
import it.polimi.ingsw.resources.NotEnoughResourcesException;
import it.polimi.ingsw.resources.resourceSets.ConcreteResourceSet;
import it.polimi.ingsw.resources.resourceSets.InvalidResourceSetException;

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
     * @throws InvalidResourceException type is null.
     */
    public DiscountEffect(ConcreteResource type) throws InvalidResourceException {
        if(type == null) {
            throw new InvalidResourceException();
        }
        this.type = type;
    }

    /**
     * applyDiscount to a concrete resource set.
     * @param set of resource to discount.
     * @return a set discounted.
     * @throws InvalidResourceSetException set is null.
     */
    public ConcreteResourceSet applyDiscount(ConcreteResourceSet set) throws InvalidResourceSetException {
        try {
            if(set == null){
                throw new InvalidResourceSetException();
            }
            set.removeResource(type);
        } catch (NotEnoughResourcesException | InvalidResourceException e) {}
        return set;
    }
}
