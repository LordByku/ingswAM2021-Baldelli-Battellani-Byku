package it.polimi.ingsw.model.leaderCards;

import it.polimi.ingsw.model.resources.ConcreteResource;
import it.polimi.ingsw.model.resources.InvalidResourceException;
import it.polimi.ingsw.model.resources.resourceSets.ConcreteResourceSet;
import it.polimi.ingsw.model.resources.resourceSets.InvalidQuantityException;
import it.polimi.ingsw.model.resources.resourceSets.InvalidResourceSetException;

/**
 * DiscountEffect refers to the discount effects of active leader cards.
 */
public class DiscountEffect implements Cloneable {
    /**
     * The type of ConcreteResource to convert the white marble into.
     */
    private final ConcreteResource type;

    private final int quantity;

    /**
     * @param type of ConcreteResource to convert the white marble into.
     * @throws InvalidResourceException type is null.
     */
    public DiscountEffect(ConcreteResource type, int quantity) throws InvalidResourceException {
        if (type == null) {
            throw new InvalidResourceException();
        }
        if (quantity <= 0) {
            throw new InvalidQuantityException();
        }
        this.type = type;
        this.quantity = quantity;
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
        ConcreteResourceSet result = (ConcreteResourceSet) set.clone();

        int count = result.getCount(type);
        if (count > 0) {
            result.removeResource(type, Math.min(count, quantity));
        }
        return result;
    }

    public int getDiscount() {
        return quantity;
    }

    public ConcreteResource getType() {
        return type;
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

    public String getFilename() {
        switch (type) {
            case COIN: {
                return "3";
            }
            case STONE: {
                return "2";
            }
            case SHIELD: {
                return "1";
            }
            case SERVANT: {
                return "0";
            }
            default: {
                return null;
            }
        }
    }
}
