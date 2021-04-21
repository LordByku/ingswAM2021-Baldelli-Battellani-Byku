package it.polimi.ingsw.model.resourcesTest;

import it.polimi.ingsw.model.resources.*;
import it.polimi.ingsw.model.resources.resourceSets.ChoiceResourceSet;
import it.polimi.ingsw.model.resources.resourceSets.ConcreteResourceSet;
import it.polimi.ingsw.model.resources.resourceSets.InvalidResourceSetException;
import it.polimi.ingsw.model.resources.resourceSets.SpendableResourceSet;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class SpendableResourceSetTest {
    @Test
    public void constructorTest() {
        SpendableResourceSet spendableResourceSet = new SpendableResourceSet();

        ArrayList<Resource> resources = spendableResourceSet.getResourceSet().getResources();

        assertTrue(resources.isEmpty());

        ChoiceResourceSet choiceResourceSet = new ChoiceResourceSet();
        choiceResourceSet.addResource(ConcreteResource.COIN);
        choiceResourceSet.addResource(ConcreteResource.COIN);

        spendableResourceSet = new SpendableResourceSet(choiceResourceSet);

        resources = spendableResourceSet.getResourceSet().getResources();

        assertEquals(2, resources.size());
        assertEquals(ConcreteResource.COIN, resources.get(0).getResource());
        assertEquals(ConcreteResource.COIN, resources.get(1).getResource());

        choiceResourceSet.addResource(ConcreteResource.SHIELD);

        resources = spendableResourceSet.getResourceSet().getResources();

        assertEquals(2, resources.size());
        assertEquals(ConcreteResource.COIN, resources.get(0).getResource());
        assertEquals(ConcreteResource.COIN, resources.get(1).getResource());

        try {
            spendableResourceSet = new SpendableResourceSet(null);
            fail();
        } catch (InvalidResourceSetException e) {
            assertTrue(true);
        }
    }

    @Test
    public void unionTest() {
        ChoiceSet choiceSet = new FullChoiceSet();
        ChoiceResourceSet choiceResourceSet1 = new ChoiceResourceSet();
        ChoiceResourceSet choiceResourceSet2 = new ChoiceResourceSet();

        choiceResourceSet1.addResource(ConcreteResource.COIN);
        choiceResourceSet1.addResource(ConcreteResource.SHIELD);
        choiceResourceSet1.addResource(new ChoiceResource(choiceSet));

        choiceResourceSet2.addResource(new ChoiceResource(choiceSet));
        choiceResourceSet2.addResource(ConcreteResource.COIN);

        SpendableResourceSet spendableResourceSet1 = new SpendableResourceSet(choiceResourceSet1);
        SpendableResourceSet spendableResourceSet2 = new SpendableResourceSet(choiceResourceSet2);

        spendableResourceSet1 = spendableResourceSet1.union(spendableResourceSet2);

        ChoiceResourceSet resourceSet = spendableResourceSet1.getResourceSet();
        ArrayList<Resource> resources = resourceSet.getResources();

        assertEquals(5, resources.size());
        assertEquals(ConcreteResource.COIN, resources.get(0).getResource());
        assertEquals(ConcreteResource.SHIELD, resources.get(1).getResource());
        assertFalse(resources.get(2).isConcrete());
        assertFalse(resources.get(3).isConcrete());
        assertEquals(ConcreteResource.COIN, resources.get(4).getResource());
    }

    @Test
    public void advancedUnionTest() {
        SpendableResourceSet spendableResourceSet1 = new SpendableResourceSet();
        SpendableResourceSet spendableResourceSet2 = new SpendableResourceSet();

        spendableResourceSet1 = spendableResourceSet1.union(spendableResourceSet2);

        ArrayList<Resource> resources1 = spendableResourceSet1.getResourceSet().getResources();
        ArrayList<Resource> resources2 = spendableResourceSet2.getResourceSet().getResources();

        assertTrue(resources1.isEmpty());
        assertTrue(resources2.isEmpty());

        ChoiceResourceSet choiceResourceSet = new ChoiceResourceSet();
        choiceResourceSet.addResource(ConcreteResource.COIN);
        choiceResourceSet.addResource(new ChoiceResource(new FullChoiceSet()));

        spendableResourceSet1 = new SpendableResourceSet(choiceResourceSet);

        spendableResourceSet1 = spendableResourceSet1.union(spendableResourceSet2);

        resources1 = spendableResourceSet1.getResourceSet().getResources();
        resources2 = spendableResourceSet2.getResourceSet().getResources();

        assertEquals(2, resources1.size());
        assertEquals(ConcreteResource.COIN, resources1.get(0).getResource());
        assertFalse(resources1.get(1).isConcrete());
        assertTrue(resources2.isEmpty());

        spendableResourceSet2 = spendableResourceSet2.union(spendableResourceSet1);

        resources1 = spendableResourceSet1.getResourceSet().getResources();
        resources2 = spendableResourceSet2.getResourceSet().getResources();

        assertEquals(2, resources1.size());
        assertEquals(ConcreteResource.COIN, resources1.get(0).getResource());
        assertFalse(resources1.get(1).isConcrete());
        assertEquals(2, resources2.size());
        assertEquals(ConcreteResource.COIN, resources2.get(0).getResource());
        assertFalse(resources2.get(1).isConcrete());

        ((ChoiceResource) resources1.get(1)).makeChoice(ConcreteResource.STONE);

        resources1 = spendableResourceSet1.getResourceSet().getResources();
        assertEquals(2, resources1.size());
        assertEquals(ConcreteResource.COIN, resources1.get(0).getResource());
        assertFalse(resources1.get(1).isConcrete());

        spendableResourceSet1 = spendableResourceSet1.union(spendableResourceSet1);

        resources1 = spendableResourceSet1.getResourceSet().getResources();
        assertEquals(4, resources1.size());
        assertEquals(ConcreteResource.COIN, resources1.get(0).getResource());
        assertFalse(resources1.get(1).isConcrete());
        assertEquals(ConcreteResource.COIN, resources1.get(2).getResource());
        assertFalse(resources1.get(3).isConcrete());
    }

    @Test
    public void matchTest() {
        ChoiceResourceSet choiceResourceSet = new ChoiceResourceSet();
        choiceResourceSet.addResource(ConcreteResource.COIN);
        choiceResourceSet.addResource(ConcreteResource.COIN);
        choiceResourceSet.addResource(new ChoiceResource(new FullChoiceSet()));
        choiceResourceSet.addResource(new ChoiceResource(new FullChoiceSet()));

        SpendableResourceSet spendableResourceSet = new SpendableResourceSet(choiceResourceSet);

        ConcreteResourceSet concreteResourceSet = new ConcreteResourceSet();
        concreteResourceSet.addResource(ConcreteResource.COIN, 2);
        assertFalse(spendableResourceSet.match(concreteResourceSet));

        concreteResourceSet.addResource(ConcreteResource.COIN);
        assertFalse(spendableResourceSet.match(concreteResourceSet));

        concreteResourceSet.addResource(ConcreteResource.COIN);
        assertTrue(spendableResourceSet.match(concreteResourceSet));

        concreteResourceSet.addResource(ConcreteResource.COIN);
        assertFalse(spendableResourceSet.match(concreteResourceSet));

        concreteResourceSet = new ConcreteResourceSet();
        concreteResourceSet.addResource(ConcreteResource.COIN, 2);
        concreteResourceSet.addResource(ConcreteResource.STONE, 2);
        assertTrue(spendableResourceSet.match(concreteResourceSet));
    }
}