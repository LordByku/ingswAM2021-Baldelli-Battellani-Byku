package it.polimi.ingsw.resources;

import org.junit.Test;

import static org.junit.Assert.*;

public class ChoiceResourceTest {
    @Test
    public void constructorTest() {
        ChoiceSet emptyChoiceSet = new ChoiceSet();
        try {
            ChoiceResource choiceResource = new ChoiceResource(emptyChoiceSet);
            fail();
        } catch(EmptyChoiceSetException e) {
            assertTrue(true);
        }

        ChoiceSet nonEmptyChoiceSet = new ChoiceSet();
        nonEmptyChoiceSet.addChoice(ConcreteResource.COIN);
        nonEmptyChoiceSet.addChoice(ConcreteResource.SHIELD);
        try {
            ChoiceResource choiceResource = new ChoiceResource(nonEmptyChoiceSet);
            assertTrue(choiceResource.canChoose(ConcreteResource.COIN));
            assertTrue(choiceResource.canChoose(ConcreteResource.SHIELD));
            assertFalse(choiceResource.canChoose(ConcreteResource.SERVANT));
            assertFalse(choiceResource.canChoose(ConcreteResource.STONE));
        } catch(EmptyChoiceSetException e) {
            fail();
        }
    }

    @Test
    public void onlyChoiceTest() {
        ChoiceSet choiceSet = new ChoiceSet();
        choiceSet.addChoice(ConcreteResource.COIN);

        try {
            ChoiceResource choiceResource = new ChoiceResource(choiceSet);

            assertTrue(choiceResource.isConcrete());
            assertSame(choiceResource.getResource(), ConcreteResource.COIN);
        } catch (EmptyChoiceSetException e) {
            fail();
        }
    }

    @Test
    public void makeChoiceTest() {
        ChoiceSet choiceSet = new ChoiceSet();
        choiceSet.addChoice(ConcreteResource.COIN);
        choiceSet.addChoice(ConcreteResource.SHIELD);
        ChoiceResource choiceResource;
        try {
            choiceResource = new ChoiceResource(choiceSet);

            assertFalse(choiceResource.isConcrete());

            try {
                choiceResource.makeChoice(ConcreteResource.COIN);
                assertTrue(choiceResource.isConcrete());
                assertSame(choiceResource.getResource(), ConcreteResource.COIN);
            } catch (InvalidResourceException e) {
                fail();
            }

            try {
                choiceResource.makeChoice(ConcreteResource.STONE);
                fail();
            } catch (InvalidResourceException e) {
                assertTrue(choiceResource.isConcrete());
                assertSame(choiceResource.getResource(), ConcreteResource.COIN);
            }
        } catch (EmptyChoiceSetException e) {
            fail();
        }
    }
}
