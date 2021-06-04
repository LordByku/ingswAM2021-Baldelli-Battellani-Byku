package it.polimi.ingsw.model.devCardsTest;

import it.polimi.ingsw.model.devCards.*;
import it.polimi.ingsw.model.resources.ConcreteResource;
import it.polimi.ingsw.model.resources.resourceSets.ChoiceResourceSet;
import it.polimi.ingsw.model.resources.resourceSets.ConcreteResourceSet;
import it.polimi.ingsw.model.resources.resourceSets.ObtainableResourceSet;
import it.polimi.ingsw.model.resources.resourceSets.SpendableResourceSet;
import org.junit.Test;

import static org.junit.Assert.*;

public class CardTypeDetailsTest {
    @Test
    public void constructor (){
        CardTypeDetails cardTypeDetails;

        cardTypeDetails = new CardTypeDetails(CardColour.BLUE, 1, CardLevel.I);
        assertNotNull(cardTypeDetails);

        try{
            cardTypeDetails = new CardTypeDetails(null, 1);
        }catch(InvalidCardColourException e){
            assertNotNull(cardTypeDetails);
        }
        try{
            cardTypeDetails = new CardTypeDetails(CardColour.PURPLE, 0);
        }catch(InvalidCardLevelException e) {
            assertNotNull(cardTypeDetails);
        }
    }
    @Test
    public void isSatisfiedTest(){
        DevCard devCard1;
        DevCard devCard2;
        DevCard devCard3;
        DevCard devCard4;
        CardTypeDetails cardTypeDetails = new CardTypeDetails(CardColour.BLUE, 1);
        ConcreteResourceSet concreteResourceSet1 = new ConcreteResourceSet();
        concreteResourceSet1.addResource(ConcreteResource.COIN, 2);
        ConcreteResourceSet concreteResourceSet2 = new ConcreteResourceSet();
        concreteResourceSet2.addResource(ConcreteResource.STONE, 2);

        ChoiceResourceSet choiceResourceSet1 = new ChoiceResourceSet();
        choiceResourceSet1.addResource(ConcreteResource.SHIELD);
        ChoiceResourceSet choiceResourceSet2 = new ChoiceResourceSet();
        choiceResourceSet2.addResource(ConcreteResource.SERVANT);

        SpendableResourceSet input1 = new SpendableResourceSet(choiceResourceSet1);
        ObtainableResourceSet output1 = new ObtainableResourceSet(choiceResourceSet2);
        ProductionDetails details1 = new ProductionDetails(input1,output1);
        devCard1 = new DevCard(concreteResourceSet1,CardColour.BLUE,CardLevel.I,details1,3, 1019);
        devCard2 = new DevCard(concreteResourceSet1,CardColour.YELLOW,CardLevel.II,details1,2,1020);
        devCard3 = new DevCard(concreteResourceSet1,CardColour.PURPLE,CardLevel.I,details1,2, 1021);
        devCard4 = new DevCard(concreteResourceSet1,CardColour.BLUE,CardLevel.III,details1,1, 1022);


        assertTrue(cardTypeDetails.isSatisfied(devCard1));
        assertFalse(cardTypeDetails.isSatisfied(devCard2));
        assertFalse(cardTypeDetails.isSatisfied(devCard3));
        assertTrue(cardTypeDetails.isSatisfied(devCard4));

        CardTypeDetails cardTypeDetails2 = new CardTypeDetails(CardColour.PURPLE, 1);
        assertTrue(cardTypeDetails2.isSatisfied(devCard3));
        assertFalse(cardTypeDetails2.isSatisfied(devCard2));
        assertFalse(cardTypeDetails2.isSatisfied(devCard4));

        assertTrue(cardTypeDetails.isSatisfied(devCard4));

    }
}
