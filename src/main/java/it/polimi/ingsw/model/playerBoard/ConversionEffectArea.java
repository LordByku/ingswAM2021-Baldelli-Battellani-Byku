package it.polimi.ingsw.model.playerBoard;

import it.polimi.ingsw.model.leaderCards.ConversionEffect;
import it.polimi.ingsw.model.leaderCards.InvalidConversionEffectException;
import it.polimi.ingsw.model.resources.ChoiceSet;

import java.util.HashSet;

/**
 * ConversionEffectArea is a container for ConversionEffects in a Board
 */
public class ConversionEffectArea {
    /**
     * conversionEffects is the set of ConversionEffects contained
     * in this ConversionEffectArea
     */
    private HashSet<ConversionEffect> conversionEffects;

    /**
     * The constructor initializes conversionEffects to an empty set
     */
    public ConversionEffectArea() {
        conversionEffects = new HashSet<>();
    }

    /**
     * addConversionEffect adds a new ConversionEffect to this ConversionEffectArea
     * @param effect The ConversionEffect to add
     * @throws InvalidConversionEffectException effect is null
     */
    public void addConversionEffect(ConversionEffect effect) throws InvalidConversionEffectException {
        if(effect == null) {
            throw new InvalidConversionEffectException();
        }
        conversionEffects.add(effect);
    }

    /**
     * getConversionEffects returns a ChoiceSet where possible choices are
     * the ConversionEffects provided by this ConversionEffectArea
     * @return A ChoiceSet containing the ConversionEffects in this
     * ConversionEffectArea as choices
     */
    public ChoiceSet getConversionEffects() {
        ChoiceSet choiceSet = new ChoiceSet();

        for(ConversionEffect conversionEffect: conversionEffects) {
            choiceSet.addChoice(conversionEffect.getResource());
        }

        return choiceSet;
    }
}
