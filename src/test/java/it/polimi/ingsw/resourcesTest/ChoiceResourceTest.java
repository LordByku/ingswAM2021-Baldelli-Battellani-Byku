package it.polimi.ingsw.resourcesTest;

import it.polimi.ingsw.resources.*;
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
        try {
            nonEmptyChoiceSet.addChoice(ConcreteResource.COIN);
            nonEmptyChoiceSet.addChoice(ConcreteResource.SHIELD);
        } catch (InvalidResourceException e) {
            fail();
        }

        try {
            ChoiceResource choiceResource = new ChoiceResource(nonEmptyChoiceSet);
            assertTrue(choiceResource.canChoose(ConcreteResource.COIN));
            assertTrue(choiceResource.canChoose(ConcreteResource.SHIELD));
            assertFalse(choiceResource.canChoose(ConcreteResource.SERVANT));
            assertFalse(choiceResource.canChoose(ConcreteResource.STONE));
        } catch (InvalidChoiceSetException | InvalidResourceException e) {
            fail();
        }
    }

    @Test
    public void nullChoiceSetTest() {
        ChoiceSet nullChoiceSet = null;

        try {
            ChoiceResource choiceResource = new ChoiceResource(nullChoiceSet);
            fail();
        } catch (InvalidChoiceSetException e) {
            assertTrue(true);
        }
    }

    @Test
    public void modifyChoiceSetTest() {
        ChoiceSet choiceSet = new ChoiceSet();

        try {
            choiceSet.addChoice(ConcreteResource.COIN);
            choiceSet.addChoice(ConcreteResource.STONE);
        } catch (InvalidResourceException e) {
            fail();
        }

        try {
            ChoiceResource choiceResource = new ChoiceResource(choiceSet);
            assertFalse(choiceResource.canChoose(ConcreteResource.SHIELD));

            choiceSet.addChoice(ConcreteResource.SHIELD);
            assertFalse(choiceResource.canChoose(ConcreteResource.SHIELD));
        } catch (InvalidChoiceSetException | InvalidResourceException e) {
            fail();
        }
    }

    @Test
    public void modifyFinalChoiceTest() {
        ChoiceSet choiceSet = new ChoiceSet();

        try {
            choiceSet.addChoice(ConcreteResource.COIN);
            choiceSet.addChoice(ConcreteResource.SHIELD);
        } catch (InvalidResourceException e) {
            fail();
        }

        try {
            ChoiceResource choiceResource = new ChoiceResource(choiceSet);
            choiceResource.makeChoice(ConcreteResource.COIN);
            ConcreteResource finalChoice = choiceResource.getResource();
            finalChoice = ConcreteResource.STONE;

            assertEquals(ConcreteResource.COIN, choiceResource.getResource());
        } catch (InvalidChoiceSetException | InvalidResourceException e) {
            fail();
        }
    }

    @Test
    public void onlyChoiceTest() {
        ChoiceSet choiceSet = new ChoiceSet();
        try {
            choiceSet.addChoice(ConcreteResource.COIN);
        } catch (InvalidResourceException e) {
            fail();
        }

        try {
            ChoiceResource choiceResource = new ChoiceResource(choiceSet);

            assertTrue(choiceResource.isConcrete());
            assertSame(choiceResource.getResource(), ConcreteResource.COIN);
        } catch (InvalidChoiceSetException e) {
            fail();
        }
    }

    @Test
    public void makeChoiceTest() {
        ChoiceSet choiceSet = new ChoiceSet();
        try {
            choiceSet.addChoice(ConcreteResource.COIN);
            choiceSet.addChoice(ConcreteResource.SHIELD);
        } catch(InvalidResourceException e) {
            fail();
        }

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

            try {
                choiceResource.makeChoice(null);
                fail();
            } catch (InvalidResourceException e) {
                assertTrue(choiceResource.isConcrete());
                assertSame(choiceResource.getResource(), ConcreteResource.COIN);
            }
        } catch (InvalidChoiceSetException e) {
            fail();
        }
    }
}
