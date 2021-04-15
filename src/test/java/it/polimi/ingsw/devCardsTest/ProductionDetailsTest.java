package it.polimi.ingsw.devCardsTest;

import it.polimi.ingsw.devCards.ProductionDetails;
import it.polimi.ingsw.resources.ChoiceSet;
import it.polimi.ingsw.resources.ConcreteResource;
import it.polimi.ingsw.resources.Resource;
import it.polimi.ingsw.resources.resourceSets.ChoiceResourceSet;
import it.polimi.ingsw.resources.resourceSets.InvalidResourceSetException;
import it.polimi.ingsw.resources.resourceSets.ObtainableResourceSet;
import it.polimi.ingsw.resources.resourceSets.SpendableResourceSet;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.junit.Assert.assertFalse;


public class ProductionDetailsTest {
    @Test
    public void getProductionDetailsTest(){
        ChoiceResourceSet choiceResourceSet1 = new ChoiceResourceSet();
        ChoiceResourceSet choiceResourceSet2 = new ChoiceResourceSet();
        choiceResourceSet1.addResource(ConcreteResource.COIN);
        choiceResourceSet1.addResource(ConcreteResource.SHIELD);
        SpendableResourceSet spendableResourceSet = new SpendableResourceSet(choiceResourceSet1);
        choiceResourceSet2.addResource(ConcreteResource.STONE);
        choiceResourceSet2.addResource(ConcreteResource.SERVANT);
        ObtainableResourceSet obtainableResourceSet = new ObtainableResourceSet(choiceResourceSet2);
        ProductionDetails productionDetails = new ProductionDetails(spendableResourceSet,obtainableResourceSet);

        ArrayList<Resource> input = spendableResourceSet.getResourceSet().getResources();
        ArrayList<Resource> output = obtainableResourceSet.getResourceSet().getResources();

        assertSame(ConcreteResource.COIN,productionDetails.getInput().getResourceSet().getResources().get(0));
        assertSame(ConcreteResource.SHIELD,productionDetails.getInput().getResourceSet().getResources().get(1));

        assertSame(ConcreteResource.STONE,productionDetails.getOutput().getResourceSet().getResources().get(0));
        assertSame(productionDetails.getOutput().getResourceSet().getResources().get(1),(ConcreteResource.SERVANT));

        try {
            productionDetails = new ProductionDetails(null,obtainableResourceSet);
            fail();
        }catch (InvalidResourceSetException e){
            assertSame(ConcreteResource.COIN,productionDetails.getInput().getResourceSet().getResources().get(0));
            assertSame(ConcreteResource.SHIELD,productionDetails.getInput().getResourceSet().getResources().get(1));

            assertSame(ConcreteResource.STONE,productionDetails.getOutput().getResourceSet().getResources().get(0));
            assertSame(productionDetails.getOutput().getResourceSet().getResources().get(1),(ConcreteResource.SERVANT));

        }


        try {
            productionDetails = new ProductionDetails(spendableResourceSet,null);
            fail();
        }catch (InvalidResourceSetException e){
            assertSame(ConcreteResource.COIN,productionDetails.getInput().getResourceSet().getResources().get(0));
            assertSame(ConcreteResource.SHIELD,productionDetails.getInput().getResourceSet().getResources().get(1));

            assertSame(ConcreteResource.STONE,productionDetails.getOutput().getResourceSet().getResources().get(0));
            assertSame(productionDetails.getOutput().getResourceSet().getResources().get(1),(ConcreteResource.SERVANT));

        }



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

        assertTrue(productionDetails2.getInput().getResourceSet().getResources().contains(ConcreteResource.SHIELD));
        assertTrue(productionDetails2.getInput().getResourceSet().getResources().contains(ConcreteResource.COIN));
        assertFalse(productionDetails2.getInput().getResourceSet().getResources().contains(ConcreteResource.STONE));
        assertFalse(productionDetails2.getInput().getResourceSet().getResources().contains(ConcreteResource.SERVANT));

        assertFalse(productionDetails2.getOutput().getResourceSet().getResources().contains(ConcreteResource.SHIELD));
        assertFalse(productionDetails2.getOutput().getResourceSet().getResources().contains(ConcreteResource.COIN));
        assertTrue(productionDetails2.getOutput().getResourceSet().getResources().contains(ConcreteResource.STONE));
        assertTrue(productionDetails2.getOutput().getResourceSet().getResources().contains(ConcreteResource.SERVANT));


    }
}
