package it.polimi.ingsw.leaderCardsTest;

import it.polimi.ingsw.leaderCards.InvalidPointsValueException;
import it.polimi.ingsw.leaderCards.InvalidRequirementsException;
import it.polimi.ingsw.leaderCards.ProductionLeaderCard;
import it.polimi.ingsw.playerBoard.Board;
import it.polimi.ingsw.playerBoard.InvalidBoardException;
import it.polimi.ingsw.resources.*;
import it.polimi.ingsw.resources.resourceSets.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class ProductionLeaderCardTest {
    @Test

    public void constructorTest() {


        ChoiceSet choiceSet = new ChoiceSet();

        choiceSet.addChoice(ConcreteResource.COIN);
        choiceSet.addChoice(ConcreteResource.STONE);
        choiceSet.addChoice(ConcreteResource.SERVANT);
        choiceSet.addChoice(ConcreteResource.SHIELD);

        ChoiceResource choiceResource = new ChoiceResource(choiceSet);
        ChoiceResourceSet choiceResourceSet = new ChoiceResourceSet();
        choiceResourceSet.addResource(choiceResource);
        choiceResourceSet.addResource(ConcreteResource.SHIELD);
        choiceResourceSet.addResource(ConcreteResource.STONE);
        SpendableResourceSet spendableResourceSet = new SpendableResourceSet(choiceResourceSet);
        choiceResourceSet.addResource(choiceResource);
        ObtainableResourceSet obtainableResourceSet = new ObtainableResourceSet(choiceResourceSet, 3);

        try {
            ProductionLeaderCard productionLeaderCard = new ProductionLeaderCard(2, null, spendableResourceSet, obtainableResourceSet);
        } catch (InvalidPointsValueException e) {
            fail();
        } catch (InvalidRequirementsException e) {
            assertTrue(true);
        }

        ConcreteResourceSet requirements = new ConcreteResourceSet();
        requirements.addResource(ConcreteResource.SERVANT, 3);
        requirements.addResource(ConcreteResource.STONE);


        try {
            ProductionLeaderCard productionLeaderCard = new ProductionLeaderCard(2, requirements, null, obtainableResourceSet);
        } catch (InvalidPointsValueException | InvalidRequirementsException e) {
            fail();
        } catch (InvalidResourceSetException e) {
            assertTrue(true);
        }


        try {
            ProductionLeaderCard productionLeaderCard = new ProductionLeaderCard(2, requirements, spendableResourceSet, null);
        } catch (InvalidPointsValueException | InvalidRequirementsException e) {
            fail();
        } catch (InvalidResourceSetException e) {
            assertTrue(true);
        }


        try {
            ProductionLeaderCard productionLeaderCard = new ProductionLeaderCard(2, requirements, null, null);
        } catch (InvalidPointsValueException | InvalidRequirementsException e) {
            fail();
        } catch (InvalidResourceSetException e) {
            assertTrue(true);
        }

        try {
            ProductionLeaderCard productionLeaderCard = new ProductionLeaderCard(-5, requirements, spendableResourceSet, obtainableResourceSet);
        } catch (InvalidResourceSetException | InvalidRequirementsException e) {
            fail();
        } catch (InvalidPointsValueException e) {
            assertTrue(true);
        }
    }

    @Test
    public void playTest(){
        Board board = new Board();

        ConcreteResourceSet requirements = new ConcreteResourceSet();
        requirements.addResource(ConcreteResource.SERVANT,3);
        requirements.addResource(ConcreteResource.STONE);


        ChoiceSet choiceSet = new ChoiceSet();

        choiceSet.addChoice(ConcreteResource.COIN);
        choiceSet.addChoice(ConcreteResource.STONE);
        choiceSet.addChoice(ConcreteResource.SERVANT);
        choiceSet.addChoice(ConcreteResource.SHIELD);


        ConcreteResourceSet concreteResourceSet = new ConcreteResourceSet();
        concreteResourceSet.addResource(ConcreteResource.SERVANT,3);


        ConcreteResourceSet concreteResourceSet2 = new ConcreteResourceSet();
        concreteResourceSet2.addResource(ConcreteResource.STONE);

        board.getWarehouse().addResources(2,concreteResourceSet);
        board.getWarehouse().addResources(0,concreteResourceSet2);



        ChoiceResource choiceResource = new ChoiceResource(choiceSet);
        ChoiceResourceSet choiceResourceSet = new ChoiceResourceSet();
        choiceResourceSet.addResource(choiceResource);
        choiceResourceSet.addResource(ConcreteResource.SHIELD);
        choiceResourceSet.addResource(ConcreteResource.STONE);
        SpendableResourceSet spendableResourceSet = new SpendableResourceSet(choiceResourceSet);
        choiceResourceSet.addResource(choiceResource);
        ObtainableResourceSet obtainableResourceSet = new ObtainableResourceSet(choiceResourceSet, 3);



        try {
            ProductionLeaderCard productionLeaderCard = new ProductionLeaderCard(8,requirements,spendableResourceSet,obtainableResourceSet);
            productionLeaderCard.assignToBoard(board);
            assertFalse(productionLeaderCard.isActive());
            productionLeaderCard.play();
            assertTrue(productionLeaderCard.isActive());
        } catch (InvalidPointsValueException | InvalidRequirementsException | InvalidBoardException e) {
            fail();
        }


        //TODO: Check if adds the production correctly to the productionArea. TBD when everything's been implemented.
    }
}
