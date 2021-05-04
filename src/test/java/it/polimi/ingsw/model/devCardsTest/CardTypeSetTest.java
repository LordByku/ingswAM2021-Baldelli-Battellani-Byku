package it.polimi.ingsw.model.devCardsTest;

import it.polimi.ingsw.model.devCards.*;
import it.polimi.ingsw.model.playerBoard.Board;
import it.polimi.ingsw.model.resources.ConcreteResource;
import it.polimi.ingsw.model.resources.resourceSets.ChoiceResourceSet;
import it.polimi.ingsw.model.resources.resourceSets.ConcreteResourceSet;
import it.polimi.ingsw.model.resources.resourceSets.ObtainableResourceSet;
import it.polimi.ingsw.model.resources.resourceSets.SpendableResourceSet;
import org.junit.Test;

import static org.junit.Assert.*;

public class CardTypeSetTest {
    @Test
    public void isSatisfiedTest(){

        Board board1 = new Board();
        Board board2 = new Board();
        CardType cardType1 = new CardType(CardColour.BLUE);

        cardType1.addLevel(CardLevel.I);
        cardType1.addLevel(CardLevel.II);

        CardType cardType2 = new CardType(CardColour.YELLOW);
        CardType cardType3 = new CardType(CardColour.PURPLE) ;

        cardType2.addLevel(CardLevel.III);
        cardType2.addLevel(CardLevel.I);

        CardTypeSet cardTypeSet1 = new CardTypeSet();
        CardTypeSet cardTypeSet2 = new CardTypeSet();

        cardTypeSet1.add(cardType1);
        cardTypeSet1.add(cardType2);

        cardTypeSet2.add(cardType3);

        DevCard devCard1;
        DevCard devCard2;
        DevCard devCard3;
        DevCard devCard4;
        DevCard devCard5;

        CardColour blue = CardColour.BLUE;
        CardColour yellow = CardColour.YELLOW;

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

        devCard1 = new DevCard(concreteResourceSet1,blue,CardLevel.I,details1,5, 1006);
        devCard2 = new DevCard(concreteResourceSet2,yellow,CardLevel.I,details1,6, 1007);
        devCard3 = new DevCard(concreteResourceSet1,blue,CardLevel.II,details1,3, 1008);
        devCard4 = new DevCard(concreteResourceSet2,yellow,CardLevel.III,details1,1, 1009);
        devCard5 = new DevCard(concreteResourceSet2,CardColour.PURPLE,CardLevel.II,details1,1, 1010);


        board1.getDevelopmentCardArea().addDevCard(devCard1,1);
        board1.getDevelopmentCardArea().addDevCard(devCard2,2);
        board1.getDevelopmentCardArea().addDevCard(devCard3,1);
        board1.getDevelopmentCardArea().addDevCard(devCard4,1);
        board1.getDevelopmentCardArea().addDevCard(devCard5,2);
        board1.getDevelopmentCardArea().addDevCard(devCard4,2);

        board2.getDevelopmentCardArea().addDevCard(devCard1,0);
        board2.getDevelopmentCardArea().addDevCard(devCard3,0);


        assertTrue(cardTypeSet1.isSatisfied(board1));

        assertFalse(cardTypeSet1.isSatisfied(board2));

        board2.getDevelopmentCardArea().addDevCard(devCard2,1);
        board2.getDevelopmentCardArea().addDevCard(devCard4,0);

        assertTrue(cardTypeSet1.isSatisfied(board2));

        assertFalse(cardTypeSet2.isSatisfied(board1));

    }
}
