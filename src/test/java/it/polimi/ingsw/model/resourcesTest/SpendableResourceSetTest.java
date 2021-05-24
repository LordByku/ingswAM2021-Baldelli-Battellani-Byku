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

        ChoiceResourceSet internalResources = spendableResourceSet.getResourceSet();

        internalResources.getChoiceResources();
        ArrayList<ChoiceResource> choiceResources;
        internalResources.getConcreteResources();
        ConcreteResourceSet concreteResources;

        assertEquals(0, internalResources.size());

        ChoiceResourceSet choiceResourceSet = new ChoiceResourceSet();
        choiceResourceSet.addResource(ConcreteResource.COIN);
        choiceResourceSet.addResource(ConcreteResource.COIN);

        spendableResourceSet = new SpendableResourceSet(choiceResourceSet);

        internalResources = spendableResourceSet.getResourceSet();
        choiceResources = internalResources.getChoiceResources();
        concreteResources = internalResources.getConcreteResources();

        assertEquals(2, internalResources.size());
        assertEquals(0, choiceResources.size());
        assertEquals(2, concreteResources.size());
        assertEquals(2, concreteResources.getCount(ConcreteResource.COIN));

        choiceResourceSet.addResource(ConcreteResource.SHIELD);

        internalResources = spendableResourceSet.getResourceSet();
        choiceResources = internalResources.getChoiceResources();
        concreteResources = internalResources.getConcreteResources();

        assertEquals(2, internalResources.size());
        assertEquals(0, choiceResources.size());
        assertEquals(2, concreteResources.size());
        assertEquals(2, concreteResources.getCount(ConcreteResource.COIN));

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
        ArrayList<ChoiceResource> choiceResources = resourceSet.getChoiceResources();
        ConcreteResourceSet concreteResources = resourceSet.getConcreteResources();

        assertEquals(5, resourceSet.size());
        assertEquals(2, choiceResources.size());
        assertEquals(3, concreteResources.size());
        assertFalse(choiceResources.get(0).isConcrete());
        assertFalse(choiceResources.get(1).isConcrete());
        assertEquals(2, concreteResources.getCount(ConcreteResource.COIN));
        assertEquals(1, concreteResources.getCount(ConcreteResource.SHIELD));
    }

    @Test
    public void advancedUnionTest() {
        SpendableResourceSet spendableResourceSet1 = new SpendableResourceSet();
        SpendableResourceSet spendableResourceSet2 = new SpendableResourceSet();

        spendableResourceSet1 = spendableResourceSet1.union(spendableResourceSet2);

        ChoiceResourceSet resources1 = spendableResourceSet1.getResourceSet();
        ChoiceResourceSet resources2 = spendableResourceSet2.getResourceSet();

        assertEquals(0, resources1.size());
        assertEquals(0, resources2.size());

        ChoiceResourceSet choiceResourceSet = new ChoiceResourceSet();
        choiceResourceSet.addResource(ConcreteResource.COIN);
        choiceResourceSet.addResource(new ChoiceResource(new FullChoiceSet()));

        spendableResourceSet1 = new SpendableResourceSet(choiceResourceSet);

        spendableResourceSet1 = spendableResourceSet1.union(spendableResourceSet2);

        resources1 = spendableResourceSet1.getResourceSet();
        resources2 = spendableResourceSet2.getResourceSet();

        ArrayList<ChoiceResource> choiceResources1 = resources1.getChoiceResources();
        ConcreteResourceSet concreteResources1 = resources1.getConcreteResources();

        assertEquals(2, resources1.size());
        assertEquals(1, choiceResources1.size());
        assertEquals(1, concreteResources1.size());

        assertFalse(choiceResources1.get(0).isConcrete());
        assertEquals(1, concreteResources1.getCount(ConcreteResource.COIN));

        assertEquals(0, resources2.size());

        spendableResourceSet2 = spendableResourceSet2.union(spendableResourceSet1);

        resources1 = spendableResourceSet1.getResourceSet();
        resources2 = spendableResourceSet2.getResourceSet();

        choiceResources1 = resources1.getChoiceResources();
        concreteResources1 = resources1.getConcreteResources();
        ArrayList<ChoiceResource> choiceResources2 = resources2.getChoiceResources();
        ConcreteResourceSet concreteResources2 = resources2.getConcreteResources();

        assertEquals(2, resources1.size());
        assertEquals(1, choiceResources1.size());
        assertEquals(1, concreteResources1.size());

        assertFalse(choiceResources1.get(0).isConcrete());
        assertEquals(1, concreteResources1.getCount(ConcreteResource.COIN));

        assertEquals(2, resources2.size());
        assertEquals(1, choiceResources2.size());
        assertEquals(1, concreteResources2.size());

        assertFalse(choiceResources2.get(0).isConcrete());
        assertEquals(1, concreteResources2.getCount(ConcreteResource.COIN));

        choiceResources1.get(0).makeChoice(ConcreteResource.STONE);

        resources1 = spendableResourceSet1.getResourceSet();
        choiceResources1 = resources1.getChoiceResources();
        concreteResources1 = resources1.getConcreteResources();

        assertEquals(2, resources1.size());
        assertEquals(1, choiceResources1.size());
        assertEquals(1, concreteResources1.size());

        assertFalse(choiceResources1.get(0).isConcrete());
        assertEquals(1, concreteResources1.getCount(ConcreteResource.COIN));

        spendableResourceSet1 = spendableResourceSet1.union(spendableResourceSet1);

        resources1 = spendableResourceSet1.getResourceSet();
        choiceResources1 = resources1.getChoiceResources();
        concreteResources1 = resources1.getConcreteResources();

        assertEquals(4, resources1.size());
        assertEquals(2, choiceResources1.size());
        assertEquals(2, concreteResources1.size());

        assertFalse(choiceResources1.get(0).isConcrete());
        assertFalse(choiceResources1.get(1).isConcrete());
        assertEquals(2, concreteResources1.getCount(ConcreteResource.COIN));
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