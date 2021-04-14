package it.polimi.ingsw.playerBoard;

import it.polimi.ingsw.leaderCards.ConversionEffect;
import it.polimi.ingsw.resources.ChoiceSet;

import java.util.HashSet;

public class ConversionEffectArea {
    private HashSet<ConversionEffect> conversionEffects;

    public ConversionEffectArea() {
        conversionEffects = new HashSet<>();
    }

    public void addConversionEffect(ConversionEffect effect){
        conversionEffects.add(effect);
    }

    public ChoiceSet getConversionEffects() {
        ChoiceSet choiceSet = new ChoiceSet();

        for(ConversionEffect conversionEffect: conversionEffects) {
            choiceSet.addChoice(conversionEffect.getResource());
        }

        return choiceSet;
    }
}
