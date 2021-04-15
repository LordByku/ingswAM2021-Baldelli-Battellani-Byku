package it.polimi.ingsw.faithTrack.devCardsTest;

import it.polimi.ingsw.devCards.ProductionDetails;
import it.polimi.ingsw.resources.ConcreteResource;
import it.polimi.ingsw.resources.resourceSets.ChoiceResourceSet;
import it.polimi.ingsw.resources.resourceSets.ObtainableResourceSet;
import it.polimi.ingsw.resources.resourceSets.SpendableResourceSet;
import org.junit.Test;

import java.util.Objects;

import static org.junit.Assert.*;


public class ProductionDetailsTest {
    @Test
    public void getProductionDetailsTest(){
        ChoiceResourceSet choiceResourceSet = new ChoiceResourceSet();
        choiceResourceSet.addResource(ConcreteResource.COIN);
        choiceResourceSet.addResource(ConcreteResource.COIN);
        SpendableResourceSet spendableResourceSet = new SpendableResourceSet(choiceResourceSet);
        choiceResourceSet.addResource(ConcreteResource.COIN);
        choiceResourceSet.addResource(ConcreteResource.SHIELD);
        ObtainableResourceSet obtainableResourceSet = new ObtainableResourceSet(choiceResourceSet);
        ProductionDetails productionDetails = new ProductionDetails(spendableResourceSet,obtainableResourceSet);

//        assertTrue(Objects.equals(spendableResourceSet, productionDetails.getInput()));


    }
}
