package it.polimi.ingsw.model.playerBoard;

import it.polimi.ingsw.model.leaderCards.DiscountEffect;
import it.polimi.ingsw.model.leaderCards.InvalidDiscountEffectException;
import it.polimi.ingsw.model.resources.resourceSets.ConcreteResourceSet;
import it.polimi.ingsw.model.resources.resourceSets.InvalidResourceSetException;

import java.util.ArrayList;

/**
 * DiscountArea is a container for DiscountEffects in a Board
 */
public class DiscountArea {
    /**
     * discountEffects contains all the DiscountEffects in this DiscountArea
     */
    private ArrayList<DiscountEffect> discountEffects;

    /**
     * The constructor initializes discountEffects to an empty set
     */
    public DiscountArea() {
        discountEffects = new ArrayList<>();
    }

    /**
     * addDiscountEffect adds a new DiscountEffect to this DiscountArea
     * @param effect The DiscountEffect to add
     * @throws InvalidDiscountEffectException effect is null
     */
    public void addDiscountEffect(DiscountEffect effect) throws InvalidDiscountEffectException {
        if(effect == null) {
            throw new InvalidDiscountEffectException();
        }
        discountEffects.add(effect.clone());
    }

    /**
     * applyDiscount returns the result of applying all the discounts in this DiscountArea
     * to a given ConcreteResourceSet
     * Note that the return value is the modified parameter
     * @param concreteResourceSet The ConcreteResourceSet to apply discounts to
     * @return The discounted ConcreteResourceSet
     * @throws InvalidResourceSetException concreteResourceSet is null
     */
    public ConcreteResourceSet applyDiscount(ConcreteResourceSet concreteResourceSet) throws InvalidResourceSetException {
        if(concreteResourceSet == null) {
            throw new InvalidResourceSetException();
        }
        for(DiscountEffect discountEffect: discountEffects) {
            concreteResourceSet = discountEffect.applyDiscount(concreteResourceSet);
        }
        return concreteResourceSet;
    }
}
