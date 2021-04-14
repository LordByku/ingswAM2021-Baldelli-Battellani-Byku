package it.polimi.ingsw.leaderCardsTest;

import it.polimi.ingsw.leaderCards.InvalidPointsValueException;
import it.polimi.ingsw.leaderCards.InvalidRequirementsException;
import it.polimi.ingsw.leaderCards.WhiteConversionLeaderCard;
import it.polimi.ingsw.playerBoard.Board;
import it.polimi.ingsw.playerBoard.InvalidBoardException;
import it.polimi.ingsw.playerBoard.resourceLocations.InvalidDepotIndexException;
import it.polimi.ingsw.playerBoard.resourceLocations.InvalidResourceLocationOperationException;
import it.polimi.ingsw.resources.ConcreteResource;
import it.polimi.ingsw.resources.InvalidResourceException;
import it.polimi.ingsw.resources.resourceSets.ConcreteResourceSet;
import it.polimi.ingsw.resources.resourceSets.InvalidQuantityException;
import it.polimi.ingsw.resources.resourceSets.InvalidResourceSetException;
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
        try {
            concreteResourceSet.addResource(ConcreteResource.SERVANT,3);
        } catch (InvalidResourceException | InvalidQuantityException e) {
            fail();
        }
        try {
            concreteResourceSet.addResource(ConcreteResource.STONE,1);
        } catch (InvalidResourceException | InvalidQuantityException e) {
            fail();
        }

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
        try {
            concreteResourceSet.addResource(ConcreteResource.SHIELD,3);
        } catch (InvalidResourceException | InvalidQuantityException e) {
            fail();
        }


        try {
            board.addResourceToWarehouse(2,concreteResourceSet);
        } catch (InvalidResourceSetException | InvalidDepotIndexException | InvalidResourceLocationOperationException e) {
            fail();
        }

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
