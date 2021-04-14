package it.polimi.ingsw.leaderCardsTest;

import it.polimi.ingsw.leaderCards.DepotLeaderCard;
import it.polimi.ingsw.leaderCards.InvalidPointsValueException;
import it.polimi.ingsw.leaderCards.InvalidRequirementsException;
import it.polimi.ingsw.playerBoard.Board;
import it.polimi.ingsw.playerBoard.InvalidBoardException;
import it.polimi.ingsw.playerBoard.resourceLocations.InvalidDepotIndexException;
import it.polimi.ingsw.playerBoard.resourceLocations.InvalidResourceLocationOperationException;
import it.polimi.ingsw.resources.ConcreteResource;
import it.polimi.ingsw.resources.InvalidResourceException;
import it.polimi.ingsw.resources.resourceSets.ConcreteResourceSet;
import it.polimi.ingsw.resources.resourceSets.InvalidQuantityException;

import static org.junit.Assert.*;

import it.polimi.ingsw.resources.resourceSets.InvalidResourceSetException;
import org.junit.Test;

public class DepotLeaderCardTest {
    @Test

    public void constructorTest(){

        try {
            DepotLeaderCard depotLeaderCard = new DepotLeaderCard(2,null,ConcreteResource.SERVANT);
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
            DepotLeaderCard depotLeaderCard = new DepotLeaderCard(2,concreteResourceSet,null);
        } catch (InvalidPointsValueException | InvalidRequirementsException e) {
            fail();
        } catch (InvalidResourceException e) {
            assertTrue(true);
        }

        try {
            DepotLeaderCard depotLeaderCard = new DepotLeaderCard(0,concreteResourceSet,ConcreteResource.SERVANT);
        } catch (InvalidPointsValueException e) {
            assertTrue(true);
        } catch (InvalidResourceException | InvalidRequirementsException e) {
            fail();
        }

        try {
            DepotLeaderCard depotLeaderCard = new DepotLeaderCard(4,concreteResourceSet,ConcreteResource.SERVANT);
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
            concreteResourceSet.addResource(ConcreteResource.SERVANT,3);
        } catch (InvalidResourceException | InvalidQuantityException e) {
            fail();
        }

        ConcreteResourceSet concreteResourceSet2 = new ConcreteResourceSet();
        try {
            concreteResourceSet2.addResource(ConcreteResource.COIN);
        } catch (InvalidResourceException e) {
            fail();
        }

        ConcreteResourceSet concreteResourceSet3 = new ConcreteResourceSet();
        try {
            concreteResourceSet3.addResource(ConcreteResource.SERVANT);
        } catch (InvalidResourceException e) {
            fail();
        }

        try {
            board.addResourceToWarehouse(2,concreteResourceSet);
        } catch (InvalidResourceSetException | InvalidDepotIndexException | InvalidResourceLocationOperationException e) {
            fail();
        }

        try {
            DepotLeaderCard depotLeaderCard = new DepotLeaderCard(2,concreteResourceSet,ConcreteResource.COIN);
            depotLeaderCard.assignToBoard(board);
            assertFalse(depotLeaderCard.isActive());
            depotLeaderCard.play();
            assertTrue(depotLeaderCard.isActive());
            board.addResourceToWarehouse(3,concreteResourceSet2);
            assertTrue(true);
            DepotLeaderCard depotLeaderCardServant = new DepotLeaderCard(2,concreteResourceSet,ConcreteResource.SERVANT);
            depotLeaderCardServant.assignToBoard(board);
            depotLeaderCardServant.play();
            board.addResourceToWarehouse(4,concreteResourceSet3);
            assertTrue(true);
        } catch (InvalidPointsValueException | InvalidRequirementsException | InvalidResourceException | InvalidBoardException | InvalidResourceSetException | InvalidDepotIndexException | InvalidResourceLocationOperationException e) {
            fail();
        }
    }

}
