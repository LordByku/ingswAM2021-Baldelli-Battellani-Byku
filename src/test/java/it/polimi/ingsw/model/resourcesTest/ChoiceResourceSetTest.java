package it.polimi.ingsw.model.resourcesTest;

import it.polimi.ingsw.model.resources.*;
import it.polimi.ingsw.model.resources.resourceSets.ChoiceResourceSet;
import it.polimi.ingsw.model.resources.resourceSets.ConcreteResourceSet;
import it.polimi.ingsw.model.resources.NotConcreteException;
import it.polimi.ingsw.model.resources.resourceSets.InvalidResourceSetException;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class ChoiceResourceSetTest {
    @Test
    public void addResourceTest() {
        ChoiceResourceSet choiceResourceSet = new ChoiceResourceSet();
        choiceResourceSet.addResource(ConcreteResource.COIN);
        choiceResourceSet.addResource(new ChoiceResource(new FullChoiceSet()));
        choiceResourceSet.addResource(ConcreteResource.COIN);

        ArrayList<Resource> choiceResources = choiceResourceSet.getChoiceResources();
        ConcreteResourceSet concreteResources = choiceResourceSet.getConcreteResources();

        assertEquals(3, choiceResourceSet.size());
        assertEquals(1, choiceResources.size());
        assertEquals(2, concreteResources.size());

        assertEquals(2, concreteResources.getCount(ConcreteResource.COIN));
        assertFalse(choiceResources.get(0).isConcrete());
        assertFalse(choiceResourceSet.isConcrete());

        ChoiceResource choiceResource = (ChoiceResource) choiceResources.get(0);
        choiceResource.makeChoice(ConcreteResource.SHIELD);
        assertTrue(choiceResourceSet.isConcrete());

        try {
            choiceResourceSet.addResource(null);
            fail();
        } catch (InvalidResourceException e) {
            choiceResources = choiceResourceSet.getChoiceResources();
            concreteResources = choiceResourceSet.getConcreteResources();

            assertEquals(3, choiceResourceSet.size());
            assertEquals(1, choiceResources.size());
            assertEquals(2, concreteResources.size());

            assertEquals(2, concreteResources.getCount(ConcreteResource.COIN));
            assertEquals(ConcreteResource.SHIELD, choiceResources.get(0).getResource());
        }
    }

    @Test
    public void toConcreteTest() {
        ChoiceSet choiceSet = new FullChoiceSet();

        ChoiceResourceSet choiceResourceSet = new ChoiceResourceSet();
        choiceResourceSet.addResource(ConcreteResource.COIN);
        choiceResourceSet.addResource(new ChoiceResource(choiceSet));
        choiceResourceSet.addResource(new ChoiceResource(choiceSet));

        assertFalse(choiceResourceSet.isConcrete());

        try {
            ConcreteResourceSet concreteResourceSet = choiceResourceSet.toConcrete();
            fail();
        } catch (NotConcreteException e) {
            assertTrue(true);
        }

        ArrayList<Resource> choiceResources = choiceResourceSet.getChoiceResources();
        ChoiceResource choiceResource1 = (ChoiceResource) choiceResources.get(0);
        ChoiceResource choiceResource2 = (ChoiceResource) choiceResources.get(1);

        choiceResource1.makeChoice(ConcreteResource.COIN);
        choiceResource2.makeChoice(ConcreteResource.STONE);

        ConcreteResourceSet concreteResourceSet = choiceResourceSet.toConcrete();

        assertEquals(2, concreteResourceSet.getCount(ConcreteResource.COIN));
        assertEquals(1, concreteResourceSet.getCount(ConcreteResource.STONE));
        assertEquals(0, concreteResourceSet.getCount(ConcreteResource.SHIELD));
        assertEquals(0, concreteResourceSet.getCount(ConcreteResource.SERVANT));
    }

    @Test
    public void unionTest() {
        ChoiceResourceSet choiceResourceSet1 = new ChoiceResourceSet();
        ChoiceResourceSet choiceResourceSet2 = new ChoiceResourceSet();

        ChoiceSet choiceSet = new FullChoiceSet();

        choiceResourceSet1.addResource(ConcreteResource.COIN);
        choiceResourceSet1.addResource(new ChoiceResource(choiceSet));

        choiceResourceSet2.addResource(new ChoiceResource(choiceSet));
        choiceResourceSet2.addResource(ConcreteResource.STONE);

        choiceResourceSet1.union(choiceResourceSet2);

        ArrayList<Resource> choiceResources = choiceResourceSet1.getChoiceResources();
        ConcreteResourceSet concreteResources = choiceResourceSet1.getConcreteResources();

        assertEquals(4, choiceResourceSet1.size());
        assertEquals(2, choiceResources.size());
        assertEquals(2, concreteResources.size());
        assertEquals(1, concreteResources.getCount(ConcreteResource.COIN));
        assertEquals(1, concreteResources.getCount(ConcreteResource.STONE));
        assertFalse(choiceResources.get(0).isConcrete());
        assertFalse(choiceResources.get(1).isConcrete());
    }

    @Test
    public void advancedUnionTest() {
        ChoiceResourceSet choiceResourceSet1 = new ChoiceResourceSet();
        ChoiceResourceSet choiceResourceSet2 = new ChoiceResourceSet();

        ConcreteResourceSet concreteResourceSet = new ConcreteResourceSet();

        choiceResourceSet1.addResource(ConcreteResource.STONE);
        choiceResourceSet1.addResource(ConcreteResource.SHIELD);

        choiceResourceSet1.union(choiceResourceSet2);

        ArrayList<Resource> choiceResources = choiceResourceSet1.getChoiceResources();
        ConcreteResourceSet concreteResources = choiceResourceSet1.getConcreteResources();

        assertEquals(2, choiceResourceSet1.size());
        assertEquals(0, choiceResources.size());
        assertEquals(2, concreteResources.size());
        assertEquals(1, concreteResources.getCount(ConcreteResource.STONE));
        assertEquals(1, concreteResources.getCount(ConcreteResource.SHIELD));

        try {
            ConcreteResourceSet nullSet = null;
            choiceResourceSet1.union(nullSet);
            fail();
        } catch (InvalidResourceSetException e) {
            choiceResources = choiceResourceSet1.getChoiceResources();
            concreteResources = choiceResourceSet1.getConcreteResources();

            assertEquals(2, choiceResourceSet1.size());
            assertEquals(0, choiceResources.size());
            assertEquals(2, concreteResources.size());
            assertEquals(1, concreteResources.getCount(ConcreteResource.STONE));
            assertEquals(1, concreteResources.getCount(ConcreteResource.SHIELD));

        }
    }

    @Test
    public void cloneTest() {
        ChoiceResourceSet choiceResourceSet1 = new ChoiceResourceSet();
        ChoiceSet choiceSet = new FullChoiceSet();

        choiceResourceSet1.addResource(ConcreteResource.COIN);
        choiceResourceSet1.addResource(new ChoiceResource(choiceSet));

        ChoiceResourceSet choiceResourceSet2 = (ChoiceResourceSet) choiceResourceSet1.clone();
        ArrayList<Resource> choiceResources1 = choiceResourceSet1.getChoiceResources();
        ArrayList<Resource> choiceResources2 = choiceResourceSet2.getChoiceResources();

        choiceResourceSet1.addResource(ConcreteResource.SHIELD);
        ((ChoiceResource) choiceResources1.get(0)).makeChoice(ConcreteResource.SERVANT);
        ((ChoiceResource) choiceResources2.get(0)).makeChoice(ConcreteResource.STONE);

        choiceResources1 = choiceResourceSet1.getChoiceResources();
        choiceResources2 = choiceResourceSet2.getChoiceResources();
        assertEquals(1, choiceResources1.size());
        assertEquals(1, choiceResources2.size());
        assertEquals(ConcreteResource.STONE, choiceResources1.get(0).getResource());
        assertEquals(ConcreteResource.STONE, choiceResources2.get(0).getResource());
    }

    @Test
    public void toStringTest() {
        ChoiceResourceSet choiceResourceSet = new ChoiceResourceSet();

        choiceResourceSet.addResource(ConcreteResource.COIN);
        choiceResourceSet.addResource(new ChoiceResource(new FullChoiceSet()));
        choiceResourceSet.addResource(ConcreteResource.STONE);

        ChoiceResource choiceResource = new ChoiceResource(new FullChoiceSet());
        choiceResourceSet.addResource(choiceResource);
        choiceResource.makeChoice(ConcreteResource.SHIELD);

        choiceResourceSet.addResource(ConcreteResource.SERVANT);
        choiceResourceSet.addResource(ConcreteResource.COIN);

        System.out.println(choiceResourceSet.getCLIString());
    }
}
