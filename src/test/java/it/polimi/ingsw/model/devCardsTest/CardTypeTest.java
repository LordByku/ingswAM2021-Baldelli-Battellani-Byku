package it.polimi.ingsw.model.devCardsTest;

import it.polimi.ingsw.model.devCards.*;
import it.polimi.ingsw.model.resources.ConcreteResource;
import it.polimi.ingsw.model.resources.resourceSets.ChoiceResourceSet;
import it.polimi.ingsw.model.resources.resourceSets.ConcreteResourceSet;
import it.polimi.ingsw.model.resources.resourceSets.ObtainableResourceSet;
import it.polimi.ingsw.model.resources.resourceSets.SpendableResourceSet;
import org.junit.Test;

import static org.junit.Assert.*;

public class CardTypeTest {
    @Test
    public void constructor (){
        CardType cardType ;

        cardType = new CardType(CardColour.BLUE);
        assertNotNull(cardType);
        cardType.addLevel(CardLevel.I);
        cardType.addLevel(CardLevel.II);
        assertNotNull(cardType);

        try{
            cardType = new CardType(null);
        }catch(InvalidCardColourException e){
            assertNotNull(cardType);
        }
        try{
            cardType.addLevel(null);
        }catch(InvalidCardLevelException e) {
            assertNotNull(cardType);
        }
    }
    @Test
    public void isSatisfiedTest(){
        DevCard devCard1;
        DevCard devCard2;
        DevCard devCard3;
        DevCard devCard4;
        CardType cardType = new CardType(CardColour.BLUE);
        cardType.addLevel(CardLevel.I);
        cardType.addLevel(CardLevel.II);
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


        assertTrue(cardType.isSatisfied(devCard1));
        assertFalse(cardType.isSatisfied(devCard2));
        assertFalse(cardType.isSatisfied(devCard3));
        assertFalse(cardType.isSatisfied(devCard4));

        CardType cardType2 = new CardType(CardColour.PURPLE);
        assertFalse(cardType2.isSatisfied(devCard3));
        assertFalse(cardType2.isSatisfied(devCard2));
        assertFalse(cardType2.isSatisfied(devCard4));

        cardType.addLevel(CardLevel.III);
        assertTrue(cardType.isSatisfied(devCard4));

    }
}
