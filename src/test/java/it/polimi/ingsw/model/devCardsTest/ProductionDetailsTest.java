package it.polimi.ingsw.model.devCardsTest;

import it.polimi.ingsw.model.devCards.ProductionDetails;
import it.polimi.ingsw.model.resources.ConcreteResource;
import it.polimi.ingsw.model.resources.Resource;
import it.polimi.ingsw.model.resources.resourceSets.*;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.junit.Assert.assertFalse;


public class ProductionDetailsTest {
    @Test
    public void getProductionDetailsTest(){
        // TODO
    }

    @Test
    public void cloneTest() {
        ChoiceResourceSet choiceResourceSet1 = new ChoiceResourceSet();
        ChoiceResourceSet choiceResourceSet2 = new ChoiceResourceSet();
        choiceResourceSet1.addResource(ConcreteResource.COIN);
        choiceResourceSet1.addResource(ConcreteResource.SHIELD);
        SpendableResourceSet spendableResourceSet = new SpendableResourceSet(choiceResourceSet1);
        choiceResourceSet2.addResource(ConcreteResource.STONE);
        choiceResourceSet2.addResource(ConcreteResource.SERVANT);
        ObtainableResourceSet obtainableResourceSet = new ObtainableResourceSet(choiceResourceSet2);
        ProductionDetails productionDetails1 = new ProductionDetails(spendableResourceSet,obtainableResourceSet);

        ProductionDetails productionDetails2 = productionDetails1.clone();

        ConcreteResourceSet inputSet = productionDetails2.getInput().getResourceSet().getConcreteResources();
        assertEquals(2, inputSet.size());
        assertEquals(1, inputSet.getCount(ConcreteResource.COIN));
        assertEquals(1, inputSet.getCount(ConcreteResource.SHIELD));

        ConcreteResourceSet outputSet = productionDetails2.getOutput().getResourceSet().getConcreteResources();
        assertEquals(2, outputSet.size());
        assertEquals(1, outputSet.getCount(ConcreteResource.STONE));
        assertEquals(1, outputSet.getCount(ConcreteResource.SERVANT));
    }
}
