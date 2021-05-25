package it.polimi.ingsw.model.resourcesTest;

import it.polimi.ingsw.model.resources.ChoiceResource;
import it.polimi.ingsw.model.resources.ConcreteResource;
import it.polimi.ingsw.model.resources.FullChoiceSet;
import it.polimi.ingsw.model.resources.Resource;
import it.polimi.ingsw.model.resources.resourceSets.ChoiceResourceSet;
import it.polimi.ingsw.model.resources.resourceSets.ConcreteResourceSet;
import it.polimi.ingsw.model.resources.resourceSets.InvalidResourceSetException;
import it.polimi.ingsw.model.resources.resourceSets.ObtainableResourceSet;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class ObtainableResourceSetTest {
    @Test
    public void constructorTest() {
        ObtainableResourceSet obtainableResourceSet = new ObtainableResourceSet();

        ChoiceResourceSet resources = obtainableResourceSet.getResourceSet();

        assertEquals(0, resources.size());
        assertEquals(0, obtainableResourceSet.getFaithPoints());

        ChoiceResourceSet choiceResourceSet = new ChoiceResourceSet();
        choiceResourceSet.addResource(ConcreteResource.COIN);
        choiceResourceSet.addResource(ConcreteResource.SHIELD);

        obtainableResourceSet = new ObtainableResourceSet(choiceResourceSet);

        resources = obtainableResourceSet.getResourceSet();
        ArrayList<ChoiceResource> choiceResources = resources.getChoiceResources();
        ConcreteResourceSet concreteResourcs = resources.getConcreteResources();

        assertEquals(2, resources.size());
        assertEquals(0, choiceResources.size());
        assertEquals(2, concreteResourcs.size());
        assertEquals(1, concreteResourcs.getCount(ConcreteResource.COIN));
        assertEquals(1, concreteResourcs.getCount(ConcreteResource.SHIELD));
        assertEquals(0, obtainableResourceSet.getFaithPoints());

        choiceResourceSet.addResource(new ChoiceResource(new FullChoiceSet()));

        obtainableResourceSet = new ObtainableResourceSet(choiceResourceSet, 2);

        resources = obtainableResourceSet.getResourceSet();
        choiceResources = resources.getChoiceResources();
        concreteResourcs = resources.getConcreteResources();

        assertEquals(3, resources.size());
        assertEquals(1, choiceResources.size());
        assertEquals(2, concreteResourcs.size());
        assertFalse(choiceResources.get(0).isConcrete());
        assertEquals(1, concreteResourcs.getCount(ConcreteResource.COIN));
        assertEquals(1, concreteResourcs.getCount(ConcreteResource.SHIELD));
        assertEquals(2, obtainableResourceSet.getFaithPoints());

        try {
            obtainableResourceSet = new ObtainableResourceSet(null);
            fail();
        } catch (InvalidResourceSetException e) {
            resources = obtainableResourceSet.getResourceSet();

            choiceResources = resources.getChoiceResources();
            concreteResourcs = resources.getConcreteResources();

            assertEquals(3, resources.size());
            assertEquals(1, choiceResources.size());
            assertEquals(2, concreteResourcs.size());
            assertFalse(choiceResources.get(0).isConcrete());
            assertEquals(1, concreteResourcs.getCount(ConcreteResource.COIN));
            assertEquals(1, concreteResourcs.getCount(ConcreteResource.SHIELD));
            assertEquals(2, obtainableResourceSet.getFaithPoints());
        }

        try {
            obtainableResourceSet = new ObtainableResourceSet(null, 2);
            fail();
        } catch (InvalidResourceSetException e) {
            resources = obtainableResourceSet.getResourceSet();

            choiceResources = resources.getChoiceResources();
            concreteResourcs = resources.getConcreteResources();

            assertEquals(3, resources.size());
            assertEquals(1, choiceResources.size());
            assertEquals(2, concreteResourcs.size());
            assertFalse(choiceResources.get(0).isConcrete());
            assertEquals(1, concreteResourcs.getCount(ConcreteResource.COIN));
            assertEquals(1, concreteResourcs.getCount(ConcreteResource.SHIELD));
            assertEquals(2, obtainableResourceSet.getFaithPoints());
        }
    }

    @Test
    public void unionTest() {
        ChoiceResourceSet choiceResourceSet = new ChoiceResourceSet();
        choiceResourceSet.addResource(new ChoiceResource(new FullChoiceSet()));

        ObtainableResourceSet obtainableResourceSet1 = new ObtainableResourceSet(choiceResourceSet);
        ObtainableResourceSet obtainableResourceSet2 = new ObtainableResourceSet(new ChoiceResourceSet(), 1);

        obtainableResourceSet1 = obtainableResourceSet1.union(obtainableResourceSet2);

        ChoiceResourceSet resources = obtainableResourceSet1.getResourceSet();
        ArrayList<ChoiceResource> choiceResources = resources.getChoiceResources();
        ConcreteResourceSet concreteResources = resources.getConcreteResources();
        assertEquals(1, resources.size());
        assertEquals(1, choiceResources.size());
        assertEquals(0, concreteResources.size());
        assertFalse(choiceResources.get(0).isConcrete());

        assertEquals(1, obtainableResourceSet1.getFaithPoints());

        resources = obtainableResourceSet1.getResourceSet();
        choiceResources = resources.getChoiceResources();
        choiceResources.get(0).makeChoice(ConcreteResource.COIN);
    }
}
