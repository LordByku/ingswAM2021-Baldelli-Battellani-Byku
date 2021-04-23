package it.polimi.ingsw.model.leaderCardsTest;

import it.polimi.ingsw.model.leaderCards.DepotLeaderCard;
import it.polimi.ingsw.model.leaderCards.InvalidPointsValueException;
import it.polimi.ingsw.model.leaderCards.InvalidRequirementsException;
import it.polimi.ingsw.model.playerBoard.Board;
import it.polimi.ingsw.model.playerBoard.InvalidBoardException;
import it.polimi.ingsw.model.playerBoard.resourceLocations.InvalidDepotIndexException;
import it.polimi.ingsw.model.playerBoard.resourceLocations.InvalidResourceLocationOperationException;
import it.polimi.ingsw.model.resources.ConcreteResource;
import it.polimi.ingsw.model.resources.InvalidResourceException;
import it.polimi.ingsw.model.resources.resourceSets.ConcreteResourceSet;

import static org.junit.Assert.*;

import it.polimi.ingsw.model.resources.resourceSets.InvalidResourceSetException;
import org.junit.Test;

public class DepotLeaderCardTest {
    @Test

    public void constructorTest(){

        try {
            DepotLeaderCard depotLeaderCard = new DepotLeaderCard(2,null,ConcreteResource.SERVANT, 2);
        } catch (InvalidPointsValueException | InvalidResourceException e) {
            fail();
        } catch (InvalidRequirementsException e) {
            assertTrue(true);
        }

        ConcreteResourceSet concreteResourceSet = new ConcreteResourceSet();
        concreteResourceSet.addResource(ConcreteResource.SERVANT,3);
        concreteResourceSet.addResource(ConcreteResource.STONE,1);


        try {
            DepotLeaderCard depotLeaderCard = new DepotLeaderCard(2,concreteResourceSet,null, 2);
        } catch (InvalidPointsValueException | InvalidRequirementsException e) {
            fail();
        } catch (InvalidResourceException e) {
            assertTrue(true);
        }

        try {
            DepotLeaderCard depotLeaderCard = new DepotLeaderCard(0,concreteResourceSet,ConcreteResource.SERVANT, 2);
        } catch (InvalidPointsValueException e) {
            assertTrue(true);
        } catch (InvalidResourceException | InvalidRequirementsException e) {
            fail();
        }

        try {
            DepotLeaderCard depotLeaderCard = new DepotLeaderCard(4,concreteResourceSet,ConcreteResource.SERVANT, 2);
            assertTrue(true);
        } catch (InvalidPointsValueException | InvalidResourceException | InvalidRequirementsException e) {
            fail();
        }
    }

    @Test
    public void playTest(){
        Board board = new Board();

        ConcreteResourceSet concreteResourceSet = new ConcreteResourceSet();
        concreteResourceSet.addResource(ConcreteResource.SERVANT,3);
        ConcreteResourceSet concreteResourceSet2 = new ConcreteResourceSet();
        concreteResourceSet2.addResource(ConcreteResource.COIN);
        ConcreteResourceSet concreteResourceSet3 = new ConcreteResourceSet();
        concreteResourceSet3.addResource(ConcreteResource.SERVANT);

        try {
            board.getWarehouse().addResources(2,concreteResourceSet);
        } catch (InvalidResourceSetException | InvalidDepotIndexException | InvalidResourceLocationOperationException e) {
            fail();
        }

        try {
            DepotLeaderCard depotLeaderCard = new DepotLeaderCard(2,concreteResourceSet,ConcreteResource.COIN, 2);
            depotLeaderCard.assignToBoard(board);
            assertFalse(depotLeaderCard.isActive());
            depotLeaderCard.play();
            assertTrue(depotLeaderCard.isActive());
            board.getWarehouse().addResources(3,concreteResourceSet2);
            assertTrue(true);
            DepotLeaderCard depotLeaderCardServant = new DepotLeaderCard(2,concreteResourceSet,ConcreteResource.SERVANT, 2);
            depotLeaderCardServant.assignToBoard(board);
            depotLeaderCardServant.play();
            board.getWarehouse().addResources(4,concreteResourceSet3);
            assertTrue(true);
        } catch (InvalidPointsValueException | InvalidRequirementsException | InvalidResourceException | InvalidBoardException | InvalidResourceSetException | InvalidDepotIndexException | InvalidResourceLocationOperationException e) {
            fail();
        }
    }

}
