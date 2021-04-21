package it.polimi.ingsw.model.playerBoardTest;

import it.polimi.ingsw.model.leaderCards.ConversionEffect;
import it.polimi.ingsw.model.leaderCards.InvalidConversionEffectException;
import it.polimi.ingsw.model.playerBoard.ConversionEffectArea;
import it.polimi.ingsw.model.resources.ChoiceSet;
import it.polimi.ingsw.model.resources.ConcreteResource;
import org.junit.Test;

import static org.junit.Assert.*;

public class ConversionEffectAreaTest {
    @Test
    public void constructorTest() {
        ConversionEffectArea conversionEffectArea = new ConversionEffectArea();

        ChoiceSet choiceSet = conversionEffectArea.getConversionEffects();

        assertTrue(choiceSet.empty());
    }

    @Test
    public void addConversionEffectTest() {
        ConversionEffectArea conversionEffectArea = new ConversionEffectArea();

        try {
            conversionEffectArea.addConversionEffect(null);
            fail();
        } catch (InvalidConversionEffectException e) {
            assertTrue(conversionEffectArea.getConversionEffects().empty());
        }

        conversionEffectArea.addConversionEffect(new ConversionEffect(ConcreteResource.COIN));

        ChoiceSet choiceSet = conversionEffectArea.getConversionEffects();

        assertTrue(choiceSet.containsResource(ConcreteResource.COIN));
        assertFalse(choiceSet.containsResource(ConcreteResource.STONE));
        assertFalse(choiceSet.containsResource(ConcreteResource.SHIELD));
        assertFalse(choiceSet.containsResource(ConcreteResource.SERVANT));

        conversionEffectArea.addConversionEffect(new ConversionEffect(ConcreteResource.SHIELD));

        choiceSet = conversionEffectArea.getConversionEffects();

        assertTrue(choiceSet.containsResource(ConcreteResource.COIN));
        assertFalse(choiceSet.containsResource(ConcreteResource.STONE));
        assertTrue(choiceSet.containsResource(ConcreteResource.SHIELD));
        assertFalse(choiceSet.containsResource(ConcreteResource.SERVANT));
    }
}
