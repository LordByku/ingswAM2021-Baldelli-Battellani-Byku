package it.polimi.ingsw.model.leaderCardsTest;

import it.polimi.ingsw.model.leaderCards.InvalidPointsValueException;
import it.polimi.ingsw.model.leaderCards.InvalidRequirementsException;
import it.polimi.ingsw.model.leaderCards.WhiteConversionLeaderCard;
import it.polimi.ingsw.model.playerBoard.Board;
import it.polimi.ingsw.model.playerBoard.InvalidBoardException;
import it.polimi.ingsw.model.resources.ConcreteResource;
import it.polimi.ingsw.model.resources.InvalidResourceException;
import it.polimi.ingsw.model.resources.resourceSets.ConcreteResourceSet;
import org.junit.Test;

import static org.junit.Assert.*;

public class WhiteConversionLeaderCardTest {
    @Test

    public void constructorTest(){

        try {
            WhiteConversionLeaderCard whiteConversionLeaderCard = new WhiteConversionLeaderCard(2,null, ConcreteResource.STONE);
        } catch (InvalidPointsValueException | InvalidResourceException e) {
            fail();
        } catch (InvalidRequirementsException e) {
            assertTrue(true);
        }

        ConcreteResourceSet concreteResourceSet = new ConcreteResourceSet();
        concreteResourceSet.addResource(ConcreteResource.SERVANT,3);
        concreteResourceSet.addResource(ConcreteResource.STONE,1);


        try {
            WhiteConversionLeaderCard whiteConversionLeaderCard = new WhiteConversionLeaderCard(2,concreteResourceSet,null);
        } catch (InvalidPointsValueException | InvalidRequirementsException e) {
            fail();
        } catch (InvalidResourceException e) {
            assertTrue(true);
        }

        try {
            WhiteConversionLeaderCard whiteConversionLeaderCard = new WhiteConversionLeaderCard(-2,concreteResourceSet,ConcreteResource.STONE);
        } catch (InvalidPointsValueException e) {
            assertTrue(true);
        } catch (InvalidResourceException | InvalidRequirementsException e) {
            fail();
        }

        try {
            WhiteConversionLeaderCard whiteConversionLeaderCard = new WhiteConversionLeaderCard(9,concreteResourceSet,ConcreteResource.STONE);
            assertTrue(true);
        } catch (InvalidPointsValueException | InvalidResourceException | InvalidRequirementsException e) {
            fail();
        }
    }

    @Test
    public void playTest(){
        Board board = new Board();

        ConcreteResourceSet concreteResourceSet = new ConcreteResourceSet();
        concreteResourceSet.addResource(ConcreteResource.SHIELD,3);
        board.getWarehouse().addResources(2,concreteResourceSet);


        try {
            WhiteConversionLeaderCard whiteConversionLeaderCard = new WhiteConversionLeaderCard(2,concreteResourceSet,ConcreteResource.SERVANT);
            whiteConversionLeaderCard.assignToBoard(board);
            assertFalse(whiteConversionLeaderCard.isActive());
            whiteConversionLeaderCard.play();
            assertTrue(whiteConversionLeaderCard.isActive());
        } catch (InvalidPointsValueException | InvalidRequirementsException | InvalidResourceException | InvalidBoardException e) {
            fail();
        }

        //TODO: Check if converts correctly. TBD when everything's been implemented.
    }
}
