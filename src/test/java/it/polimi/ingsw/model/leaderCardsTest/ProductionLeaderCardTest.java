package it.polimi.ingsw.model.leaderCardsTest;

import it.polimi.ingsw.model.devCards.ProductionDetails;
import it.polimi.ingsw.model.leaderCards.InvalidPointsValueException;
import it.polimi.ingsw.model.leaderCards.InvalidRequirementsException;
import it.polimi.ingsw.model.leaderCards.ProductionLeaderCard;
import it.polimi.ingsw.model.playerBoard.Board;
import it.polimi.ingsw.model.playerBoard.InvalidBoardException;
import it.polimi.ingsw.model.resources.*;
import it.polimi.ingsw.model.resources.resourceSets.*;
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
            ProductionLeaderCard productionLeaderCard = new ProductionLeaderCard(2, null, new ProductionDetails(spendableResourceSet, obtainableResourceSet), 1012);
        } catch (InvalidPointsValueException e) {
            fail();
        } catch (InvalidRequirementsException e) {
            assertTrue(true);
        }

        ConcreteResourceSet requirements = new ConcreteResourceSet();
        requirements.addResource(ConcreteResource.SERVANT, 3);
        requirements.addResource(ConcreteResource.STONE);


        try {
            ProductionLeaderCard productionLeaderCard = new ProductionLeaderCard(2, requirements, new ProductionDetails(null, obtainableResourceSet), 1013);
        } catch (InvalidPointsValueException | InvalidRequirementsException e) {
            fail();
        } catch (InvalidResourceSetException e) {
            assertTrue(true);
        }


        try {
            ProductionLeaderCard productionLeaderCard = new ProductionLeaderCard(2, requirements, new ProductionDetails(spendableResourceSet, null), 1014);
        } catch (InvalidPointsValueException | InvalidRequirementsException e) {
            fail();
        } catch (InvalidResourceSetException e) {
            assertTrue(true);
        }


        try {
            ProductionLeaderCard productionLeaderCard = new ProductionLeaderCard(2, requirements, new ProductionDetails(null, null), 1015);
        } catch (InvalidPointsValueException | InvalidRequirementsException e) {
            fail();
        } catch (InvalidResourceSetException e) {
            assertTrue(true);
        }

        try {
            ProductionLeaderCard productionLeaderCard = new ProductionLeaderCard(-5, requirements, new ProductionDetails(spendableResourceSet, obtainableResourceSet), 1016);
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
            ProductionLeaderCard productionLeaderCard = new ProductionLeaderCard(8,requirements,new ProductionDetails(spendableResourceSet,obtainableResourceSet), 1017);
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
