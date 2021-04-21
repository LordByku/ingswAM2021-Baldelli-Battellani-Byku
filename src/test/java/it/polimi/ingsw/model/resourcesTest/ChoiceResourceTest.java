package it.polimi.ingsw.model.resourcesTest;

import it.polimi.ingsw.model.resources.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class ChoiceResourceTest {
    @Test
    public void constructorTest() {
        ChoiceSet emptyChoiceSet = new ChoiceSet();
        try {
            ChoiceResource choiceResource = new ChoiceResource(emptyChoiceSet);
            fail();
        } catch (InvalidChoiceSetException e) {
            assertTrue(true);
        }

        ChoiceSet nonEmptyChoiceSet = new ChoiceSet();
        nonEmptyChoiceSet.addChoice(ConcreteResource.COIN);
        nonEmptyChoiceSet.addChoice(ConcreteResource.SHIELD);

        ChoiceResource choiceResource = new ChoiceResource(nonEmptyChoiceSet);
        assertTrue(choiceResource.canChoose(ConcreteResource.COIN));
        assertTrue(choiceResource.canChoose(ConcreteResource.SHIELD));
        assertFalse(choiceResource.canChoose(ConcreteResource.SERVANT));
        assertFalse(choiceResource.canChoose(ConcreteResource.STONE));
    }

    @Test
    public void nullChoiceSetTest() {
        try {
            ChoiceResource choiceResource = new ChoiceResource(null);
            fail();
        } catch (InvalidChoiceSetException e) {
            assertTrue(true);
        }
    }

    @Test
    public void modifyChoiceSetTest() {
        ChoiceSet choiceSet = new ChoiceSet();

        choiceSet.addChoice(ConcreteResource.COIN);
        choiceSet.addChoice(ConcreteResource.STONE);

        ChoiceResource choiceResource = new ChoiceResource(choiceSet);
        assertFalse(choiceResource.canChoose(ConcreteResource.SHIELD));

        choiceSet.addChoice(ConcreteResource.SHIELD);
        assertFalse(choiceResource.canChoose(ConcreteResource.SHIELD));
    }

    @Test
    public void modifyFinalChoiceTest() {
        ChoiceSet choiceSet = new ChoiceSet();

        choiceSet.addChoice(ConcreteResource.COIN);
        choiceSet.addChoice(ConcreteResource.SHIELD);

        ChoiceResource choiceResource = new ChoiceResource(choiceSet);
        choiceResource.makeChoice(ConcreteResource.COIN);
        ConcreteResource finalChoice = choiceResource.getResource();
        finalChoice = ConcreteResource.STONE;

        assertEquals(ConcreteResource.COIN, choiceResource.getResource());
    }

    @Test
    public void onlyChoiceTest() {
        ChoiceSet choiceSet = new ChoiceSet();
        choiceSet.addChoice(ConcreteResource.COIN);

        ChoiceResource choiceResource = new ChoiceResource(choiceSet);

        assertTrue(choiceResource.isConcrete());
        assertSame(choiceResource.getResource(), ConcreteResource.COIN);
    }

    @Test
    public void makeChoiceTest() {
        ChoiceSet choiceSet = new ChoiceSet();
        choiceSet.addChoice(ConcreteResource.COIN);
        choiceSet.addChoice(ConcreteResource.SHIELD);

        ChoiceResource choiceResource = new ChoiceResource(choiceSet);

        assertFalse(choiceResource.isConcrete());

        choiceResource.makeChoice(ConcreteResource.COIN);
        assertTrue(choiceResource.isConcrete());
        assertSame(choiceResource.getResource(), ConcreteResource.COIN);

        try {
            choiceResource.makeChoice(ConcreteResource.STONE);
            fail();
        } catch (InvalidResourceException e) {
            assertTrue(choiceResource.isConcrete());
            assertSame(choiceResource.getResource(), ConcreteResource.COIN);
        }

        try {
            choiceResource.makeChoice(null);
            fail();
        } catch (InvalidResourceException e) {
            assertTrue(choiceResource.isConcrete());
            assertSame(choiceResource.getResource(), ConcreteResource.COIN);
        }
    }
}
