package it.polimi.ingsw.model.leaderCards;

import it.polimi.ingsw.model.resources.ConcreteResource;
import it.polimi.ingsw.model.resources.InvalidResourceException;
import it.polimi.ingsw.model.resources.resourceSets.ConcreteResourceSet;
import it.polimi.ingsw.model.resources.resourceSets.InvalidResourceSetException;

/**
 * DiscountEffect refers to the discount effects of active leader cards.
 */
public class DiscountEffect implements Cloneable {

    /**
     * The type of ConcreteResource to convert the white marble into.
     */
    private final ConcreteResource type;

    /**
     * @param type of ConcreteResource to convert the white marble into.
     * @throws InvalidResourceException type is null.
     */
    public DiscountEffect(ConcreteResource type) throws InvalidResourceException {
        if (type == null) {
            throw new InvalidResourceException();
        }
        this.type = type;
    }

    /**
     * applyDiscount to a concrete resource set.
     *
     * @param set of resource to discount.
     * @return a set discounted.
     * @throws InvalidResourceSetException set is null.
     */
    public ConcreteResourceSet applyDiscount(ConcreteResourceSet set) throws InvalidResourceSetException {
        if (set == null) {
            throw new InvalidResourceSetException();
        }
        if (set.getCount(type) > 0) {
            set.removeResource(type);
        }
        return set;
    }

    /**
     * clone returns a copy of this object
     *
     * @return A copy of the object
     */
    @Override
    public DiscountEffect clone() {
        try {
            return (DiscountEffect) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }
}
