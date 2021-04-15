package it.polimi.ingsw.devCardsTest;

import it.polimi.ingsw.devCards.ProductionDetails;
import it.polimi.ingsw.resources.ConcreteResource;
import it.polimi.ingsw.resources.resourceSets.ChoiceResourceSet;
import it.polimi.ingsw.resources.resourceSets.ObtainableResourceSet;
import it.polimi.ingsw.resources.resourceSets.SpendableResourceSet;
import org.junit.Test;

import static org.junit.Assert.assertTrue;


public class ProductionDetailsTest {
    @Test
    public void getProductionDetailsTest(){
        ChoiceResourceSet choiceResourceSet = new ChoiceResourceSet();
        choiceResourceSet.addResource(ConcreteResource.COIN);
        choiceResourceSet.addResource(ConcreteResource.SHIELD);
        SpendableResourceSet spendableResourceSet = new SpendableResourceSet(choiceResourceSet);
        choiceResourceSet.addResource(ConcreteResource.SHIELD);
        choiceResourceSet.addResource(ConcreteResource.COIN);
        ObtainableResourceSet obtainableResourceSet = new ObtainableResourceSet(choiceResourceSet);
        ProductionDetails productionDetails = new ProductionDetails(spendableResourceSet,obtainableResourceSet);


        //assertTrue(spendableResourceSet.getResourceSet().equals(productionDetails.getInput().getResourceSet()));



    }
}
