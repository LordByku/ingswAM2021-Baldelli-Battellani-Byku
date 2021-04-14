package it.polimi.ingsw.leaderCardsTest;

import it.polimi.ingsw.leaderCards.DiscountLeaderCard;
import it.polimi.ingsw.leaderCards.InvalidPointsValueException;
import it.polimi.ingsw.leaderCards.InvalidRequirementsException;
import it.polimi.ingsw.playerBoard.Board;
import it.polimi.ingsw.playerBoard.InvalidBoardException;
import it.polimi.ingsw.resources.ConcreteResource;
import it.polimi.ingsw.resources.InvalidResourceException;
import it.polimi.ingsw.resources.resourceSets.ConcreteResourceSet;
import org.junit.Test;

import static org.junit.Assert.*;

public class DiscountLeaderCardTest {
    @Test

    public void constructorTest(){

        try {
            DiscountLeaderCard discountLeaderCard = new DiscountLeaderCard(2,null, ConcreteResource.STONE);
        } catch (InvalidPointsValueException | InvalidResourceException e) {
            fail();
        } catch (InvalidRequirementsException e) {
            assertTrue(true);
        }

        ConcreteResourceSet concreteResourceSet = new ConcreteResourceSet();
        concreteResourceSet.addResource(ConcreteResource.STONE,2);
        concreteResourceSet.addResource(ConcreteResource.COIN,1);


        try {
            DiscountLeaderCard discountLeaderCard = new DiscountLeaderCard(2,concreteResourceSet,null);
        } catch (InvalidPointsValueException | InvalidRequirementsException e) {
            fail();
        } catch (InvalidResourceException e) {
            assertTrue(true);
        }

        try {
            DiscountLeaderCard discountLeaderCard = new DiscountLeaderCard(-2,concreteResourceSet,ConcreteResource.STONE);
        } catch (InvalidPointsValueException e) {
            assertTrue(true);
        } catch (InvalidResourceException | InvalidRequirementsException e) {
            fail();
        }

        try {
            DiscountLeaderCard discountLeaderCard = new DiscountLeaderCard(9,concreteResourceSet,ConcreteResource.STONE);
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
        board.addResourcesToWarehouse(2,concreteResourceSet);

        try {
            DiscountLeaderCard discountLeaderCard = new DiscountLeaderCard(2,concreteResourceSet,ConcreteResource.SERVANT);
            discountLeaderCard.assignToBoard(board);
            assertFalse(discountLeaderCard.isActive());
            discountLeaderCard.play();
            assertTrue(discountLeaderCard.isActive());
        } catch (InvalidPointsValueException | InvalidRequirementsException | InvalidResourceException | InvalidBoardException e) {
            fail();
        }

        //TODO: Check if discounts correctly the requirements of devCards. TBD when everything's been implemented.
    }
}
