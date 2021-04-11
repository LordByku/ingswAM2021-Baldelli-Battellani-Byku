package it.polimi.ingsw.playerBoard;

import it.polimi.ingsw.leaderCards.DiscountEffect;

import java.util.ArrayList;

public class DiscountArea {
    private ArrayList<DiscountEffect> discountEffects;

    public DiscountArea() {
        discountEffects = new ArrayList<>();
    }

    public void addDiscountEffect(DiscountEffect effect){
        discountEffects.add(effect);
    }
}
