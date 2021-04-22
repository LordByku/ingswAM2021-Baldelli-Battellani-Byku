package it.polimi.ingsw.model.leaderCardsTest;

import it.polimi.ingsw.model.leaderCards.DepotLeaderCard;
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
import static org.junit.Assert.fail;

public class LeaderCardTest {
    @Test
    public void assignToBoardTest(){
        Board board = new Board();

        ConcreteResourceSet concreteResourceSet = new ConcreteResourceSet();
        concreteResourceSet.addResource(ConcreteResource.STONE,3);


        try {
            WhiteConversionLeaderCard whiteConversionLeaderCard = new WhiteConversionLeaderCard(2,concreteResourceSet,ConcreteResource.SERVANT);
            whiteConversionLeaderCard.assignToBoard(null);
        } catch (InvalidPointsValueException | InvalidResourceException | InvalidRequirementsException e) {
            fail();
        } catch (InvalidBoardException e) {
            assertTrue(true);
        }

        try {
            WhiteConversionLeaderCard whiteConversionLeaderCard = new WhiteConversionLeaderCard(2,concreteResourceSet,ConcreteResource.SERVANT);
            whiteConversionLeaderCard.assignToBoard(board);
            assertTrue(true);
        } catch (InvalidPointsValueException | InvalidResourceException | InvalidRequirementsException | InvalidBoardException e) {
            fail();
        }
    }


    @Test
    public void isPlayableTest(){

        Board board = new Board();

        ConcreteResourceSet concreteResourceSet = new ConcreteResourceSet();
        concreteResourceSet.addResource(ConcreteResource.COIN, 3);


        ConcreteResourceSet concreteResourceSet2 = new ConcreteResourceSet();
        concreteResourceSet2.addResource(ConcreteResource.SHIELD, 2);
        board.getWarehouse().addResources(2,concreteResourceSet);
        board.getWarehouse().addResources(1,concreteResourceSet2);


        ConcreteResourceSet requirements = new ConcreteResourceSet();
        requirements.addResource(ConcreteResource.COIN, 2);
        requirements.addResource(ConcreteResource.SHIELD);



        try {
            DepotLeaderCard depotLeaderCard = new DepotLeaderCard(2, requirements, ConcreteResource.COIN);
            depotLeaderCard.assignToBoard(board);
            assertTrue(depotLeaderCard.isPlayable());
        } catch (InvalidPointsValueException | InvalidRequirementsException | InvalidResourceException | InvalidBoardException e) {
            fail();
        }

        try {
            DepotLeaderCard depotLeaderCard = new DepotLeaderCard(2, requirements, ConcreteResource.COIN);
            depotLeaderCard.assignToBoard(board);
            depotLeaderCard.initDiscard();
            assertFalse(depotLeaderCard.isPlayable());
        } catch (InvalidPointsValueException | InvalidRequirementsException | InvalidResourceException | InvalidBoardException e) {
            fail();
        }

        try {
            DepotLeaderCard depotLeaderCard = new DepotLeaderCard(2, requirements, ConcreteResource.COIN);
            depotLeaderCard.assignToBoard(board);
            depotLeaderCard.discard();
            assertFalse(depotLeaderCard.isPlayable());
        } catch (InvalidPointsValueException | InvalidRequirementsException | InvalidResourceException | InvalidBoardException e) {
            fail();
        }

        try {
            DepotLeaderCard depotLeaderCard = new DepotLeaderCard(2, requirements, ConcreteResource.COIN);
            depotLeaderCard.assignToBoard(board);
            depotLeaderCard.play();
            assertFalse(depotLeaderCard.isPlayable());
        } catch (InvalidPointsValueException | InvalidRequirementsException | InvalidResourceException | InvalidBoardException e) {
            fail();
        }

        ConcreteResourceSet concreteResourceSet3 = new ConcreteResourceSet();
        concreteResourceSet3.addResource(ConcreteResource.COIN, 2);
        board.getWarehouse().removeResources(2,concreteResourceSet3);

        try {
            DepotLeaderCard depotLeaderCard = new DepotLeaderCard(2, requirements, ConcreteResource.COIN);
            depotLeaderCard.assignToBoard(board);
            assertFalse(depotLeaderCard.isPlayable());
        } catch (InvalidPointsValueException | InvalidRequirementsException | InvalidResourceException | InvalidBoardException e) {
            fail();
        }
    }

}