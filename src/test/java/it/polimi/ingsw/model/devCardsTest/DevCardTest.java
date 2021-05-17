package it.polimi.ingsw.model.devCardsTest;

import it.polimi.ingsw.model.devCards.*;
import it.polimi.ingsw.model.playerBoard.Board;
import it.polimi.ingsw.model.resources.ConcreteResource;
import it.polimi.ingsw.model.resources.resourceSets.*;
import it.polimi.ingsw.parsing.DevCardsParser;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class DevCardTest {
    @Test
    public void devCardTest(){
        DevCard devCard1;
        DevCard devCard2;
        // DevCard devCard3;
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

        devCard1 = new DevCard(concreteResourceSet1,blue,CardLevel.I,details1,5, 1011);
        devCard2 = new DevCard(concreteResourceSet2,yellow,CardLevel.II,details1,6, 1012);
        assertEquals(CardColour.BLUE,devCard1.getColour());
        assertEquals(CardLevel.I,devCard1.getLevel());
        assertEquals(CardColour.YELLOW,devCard2.getColour());
        assertEquals(CardLevel.II,devCard2.getLevel());
        assertNotEquals(CardColour.YELLOW,devCard1.getColour());
        assertNotEquals(CardColour.PURPLE,devCard1.getColour());
        assertNotEquals(CardColour.GREEN,devCard1.getColour());
        assertNotEquals(CardLevel.III,devCard1.getLevel());
        try {
            devCard1=new DevCard(concreteResourceSet1, null, CardLevel.I, details1,1, 1013);
            fail();
        }catch (InvalidResourceSetException| InvalidCardColourException e){
            assertEquals(CardColour.BLUE,devCard1.getColour());
            assertEquals(CardLevel.I,devCard1.getLevel());
        }
    }

    @Test
    public void playTest () {
        Board board1 = new Board();
        //Board board2 = new Board();
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
        //DevCard devCard4;
        //DevCard devCard5;

        CardColour blue = CardColour.BLUE;
        CardColour yellow = CardColour.YELLOW;

        ConcreteResourceSet concreteResourceSet1 = new ConcreteResourceSet();
        concreteResourceSet1.addResource(ConcreteResource.COIN, 2);
        ConcreteResourceSet concreteResourceSet2 = new ConcreteResourceSet();
        concreteResourceSet2.addResource(ConcreteResource.STONE, 2);
        concreteResourceSet2.addResource(ConcreteResource.SHIELD);

        ConcreteResourceSet concreteResourceSet3 = new ConcreteResourceSet();
        concreteResourceSet3.addResource(ConcreteResource.SHIELD);
        ConcreteResourceSet concreteResourceSet4 = new ConcreteResourceSet();
        concreteResourceSet4.addResource(ConcreteResource.SERVANT);


        ChoiceResourceSet choiceResourceSet1 = new ChoiceResourceSet();
        choiceResourceSet1.addResource(ConcreteResource.SHIELD);
        ChoiceResourceSet choiceResourceSet2 = new ChoiceResourceSet();
        choiceResourceSet2.addResource(ConcreteResource.SERVANT);

        SpendableResourceSet input1 = new SpendableResourceSet(choiceResourceSet1);
        ObtainableResourceSet output1 = new ObtainableResourceSet(choiceResourceSet2);
        ProductionDetails details1 = new ProductionDetails(input1,output1);

        devCard1 = new DevCard(concreteResourceSet1,blue,CardLevel.I,details1,5, 1014);
        devCard2 = new DevCard(concreteResourceSet2,yellow,CardLevel.I,details1,6, 1015);
        devCard3 = new DevCard(concreteResourceSet1,blue,CardLevel.II,details1,3, 1016);
        //devCard4 = new DevCard(concreteResourceSet2,yellow,CardLevel.III,details1,1, 1017);
        //devCard5 = new DevCard(concreteResourceSet2,CardColour.PURPLE,CardLevel.II,details1,1, 1018);

        board1.getStrongBox().addResources(concreteResourceSet2);
        board1.getStrongBox().addResources(concreteResourceSet1);

        assertTrue(devCard1.canPlay(board1,0));
        assertFalse(devCard3.canPlay(board1,0));

        assertTrue(board1.containsResources(concreteResourceSet3));
        devCard1.play(board1,0);
        assertSame(board1.getDevelopmentCardArea().getCards().get(0).getLevel(), devCard1.getLevel());
        assertSame(board1.getDevelopmentCardArea().getCards().get(0).getColour(), devCard1.getColour());
        assertSame(board1.getDevelopmentCardArea().getCards().get(0).getReqResources().getResourceType().getResource(), devCard1.getReqResources().getResourceType().getResource());
        assertSame(board1.getDevelopmentCardArea().getCards().get(0).getPoints(), devCard1.getPoints());

        assertFalse((devCard2.canPlay(board1,0)));
        assertTrue(devCard2.canPlay(board1,1));
        devCard2.play(board1,1);

        assertSame(board1.getDevelopmentCardArea().getCards().get(1).getLevel(), devCard2.getLevel());
        assertSame(board1.getDevelopmentCardArea().getCards().get(1).getColour(), devCard2.getColour());
        assertSame(board1.getDevelopmentCardArea().getCards().get(1).getPoints(), devCard2.getPoints());


    }
    @Test
    public void getCLIStringTest() {
        ArrayList<DevCard> devCards = new ArrayList<>();
        //DevCardDeck devCardDeck = new DevCardDeck();

        for(int i = 0; i < 48; ++i) {
            DevCard devCard = DevCardsParser.getInstance().getCard(i);
            devCard.addCLISupport();
            devCards.add(devCard);
        }

        for(DevCard devCard: devCards) {
            System.out.println(devCard.getCLIString());
        }


    }

    @Test
    public void getCLIStringsDeckTest() {
        //ArrayList<DevCard> devCards = new ArrayList<>();
        DevCardDeck devCardDeck = new DevCardDeck();

        CardColour blue = CardColour.BLUE;
        CardColour yellow = CardColour.YELLOW;

        ConcreteResourceSet concreteResourceSet1 = new ConcreteResourceSet();
        concreteResourceSet1.addResource(ConcreteResource.COIN, 2);
        ConcreteResourceSet concreteResourceSet2 = new ConcreteResourceSet();
        concreteResourceSet2.addResource(ConcreteResource.STONE, 2);
        concreteResourceSet2.addResource(ConcreteResource.SHIELD);

        ConcreteResourceSet concreteResourceSet3 = new ConcreteResourceSet();
        concreteResourceSet3.addResource(ConcreteResource.SHIELD);
        ConcreteResourceSet concreteResourceSet4 = new ConcreteResourceSet();
        concreteResourceSet4.addResource(ConcreteResource.SERVANT);


        ChoiceResourceSet choiceResourceSet1 = new ChoiceResourceSet();
        choiceResourceSet1.addResource(ConcreteResource.SHIELD);
        ChoiceResourceSet choiceResourceSet2 = new ChoiceResourceSet();
        choiceResourceSet2.addResource(ConcreteResource.SERVANT);

        SpendableResourceSet input1 = new SpendableResourceSet(choiceResourceSet1);
        ObtainableResourceSet output1 = new ObtainableResourceSet(choiceResourceSet2);
        ProductionDetails details1 = new ProductionDetails(input1,output1);

        DevCard devCard2 = new DevCard(concreteResourceSet2,yellow,CardLevel.I,details1,6, 1023);
        devCard2.addCLISupport();
        DevCard devCard3 = new DevCard(concreteResourceSet1,blue,CardLevel.II,details1,3, 1024);
        devCard3.addCLISupport();
        DevCard devCard4 = new DevCard(concreteResourceSet2,yellow,CardLevel.III,details1,1, 1025);

        devCardDeck.add(devCard2);
        devCardDeck.add(devCard3);
        devCardDeck.add(devCard4);


        System.out.println(devCardDeck.getCLIString());


    }
}
