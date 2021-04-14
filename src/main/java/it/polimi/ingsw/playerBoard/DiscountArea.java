package it.polimi.ingsw.playerBoard;

import it.polimi.ingsw.leaderCards.DiscountEffect;
import it.polimi.ingsw.resources.resourceSets.ConcreteResourceSet;

import java.util.ArrayList;

public class DiscountArea {
    private ArrayList<DiscountEffect> discountEffects;

    public DiscountArea() {
        discountEffects = new ArrayList<>();
    }

    public void addDiscountEffect(DiscountEffect effect){
        discountEffects.add(effect);
    }

    public ConcreteResourceSet applyDiscount(ConcreteResourceSet concreteResourceSet) {
        for(DiscountEffect discountEffect: discountEffects) {
            concreteResourceSet = discountEffect.applyDiscount(concreteResourceSet);
        }
        return concreteResourceSet;
    }
}
