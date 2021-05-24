package it.polimi.ingsw.model.playerBoardTest;

import it.polimi.ingsw.model.devCards.ProductionDetails;
import it.polimi.ingsw.model.playerBoard.ProductionArea;
import it.polimi.ingsw.model.resources.ChoiceResource;
import it.polimi.ingsw.model.resources.ConcreteResource;
import it.polimi.ingsw.model.resources.FullChoiceSet;
import it.polimi.ingsw.model.resources.Resource;
import it.polimi.ingsw.model.resources.resourceSets.ChoiceResourceSet;
import it.polimi.ingsw.model.resources.resourceSets.ConcreteResourceSet;
import it.polimi.ingsw.model.resources.resourceSets.ObtainableResourceSet;
import it.polimi.ingsw.model.resources.resourceSets.SpendableResourceSet;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class ProductionAreaTest {
    @Test
    public void ConstructorTest() {
        ProductionArea productionArea = new ProductionArea();

        ArrayList<ProductionDetails> productions = productionArea.getProductionDetails();

        assertEquals(1, productions.size());

        SpendableResourceSet spendableResourceSet = productions.get(0).getInput();
        ObtainableResourceSet obtainableResourceSet = productions.get(0).getOutput();

        ConcreteResourceSet concreteResourceSet = new ConcreteResourceSet();

        concreteResourceSet.addResource(ConcreteResource.COIN);
        assertFalse(spendableResourceSet.match(concreteResourceSet));

        concreteResourceSet.addResource(ConcreteResource.COIN);
        assertTrue(spendableResourceSet.match(concreteResourceSet));

        concreteResourceSet.removeResource(ConcreteResource.COIN);
        concreteResourceSet.addResource(ConcreteResource.STONE);
        assertTrue(spendableResourceSet.match(concreteResourceSet));

        ChoiceResourceSet obtained = obtainableResourceSet.getResourceSet();
        assertFalse(obtained.isConcrete());

        ArrayList<ChoiceResource> choiceResources = obtained.getChoiceResources();
        ConcreteResourceSet concreteResources = obtained.getConcreteResources();

        assertEquals(1, obtained.size());
        assertEquals(1, choiceResources.size());
        assertEquals(0, concreteResources.size());
        assertFalse(choiceResources.get(0).isConcrete());

        ChoiceResource choiceResource = choiceResources.get(0);
        assertTrue(choiceResource.canChoose(ConcreteResource.COIN));
        assertTrue(choiceResource.canChoose(ConcreteResource.STONE));
        assertTrue(choiceResource.canChoose(ConcreteResource.SHIELD));
        assertTrue(choiceResource.canChoose(ConcreteResource.SERVANT));
    }

    @Test
    public void addDevCardProductionTest() {
        ProductionArea productionArea = new ProductionArea();

        ChoiceResourceSet inputChoiceResourceSet = new ChoiceResourceSet();
        inputChoiceResourceSet.addResource(ConcreteResource.COIN);
        inputChoiceResourceSet.addResource(ConcreteResource.STONE);

        ChoiceResourceSet outputChoiceResourceSet = new ChoiceResourceSet();
        outputChoiceResourceSet.addResource(ConcreteResource.SHIELD);
        outputChoiceResourceSet.addResource(ConcreteResource.SHIELD);

        SpendableResourceSet spendableResourceSet = new SpendableResourceSet(inputChoiceResourceSet);
        ObtainableResourceSet obtainableResourceSet = new ObtainableResourceSet(outputChoiceResourceSet);

        productionArea.addDevCardProduction(new ProductionDetails(spendableResourceSet, obtainableResourceSet), 1);

        ArrayList<ProductionDetails> productions = productionArea.getProductionDetails();

        assertEquals(2, productions.size());
        ChoiceResourceSet input = productions.get(1).getInput().getResourceSet();
        ChoiceResourceSet output = productions.get(1).getOutput().getResourceSet();

        ConcreteResourceSet concreteInput = input.toConcrete();
        ConcreteResourceSet concreteOutput = output.toConcrete();
        ConcreteResourceSet expectedInput = inputChoiceResourceSet.toConcrete();
        ConcreteResourceSet expectedOutput = outputChoiceResourceSet.toConcrete();

        assertTrue(concreteInput.contains(expectedInput));
        assertTrue(expectedInput.contains(concreteInput));
        assertTrue(concreteOutput.contains(expectedOutput));
        assertTrue(expectedOutput.contains(concreteOutput));
    }

    @Test
    public void advancedAddDevCardProductionTest() {
        ProductionArea productionArea = new ProductionArea();

        ChoiceResourceSet inputChoiceResourceSet = new ChoiceResourceSet();
        ChoiceResourceSet outputChoiceResourceSet = new ChoiceResourceSet();

        inputChoiceResourceSet.addResource(ConcreteResource.COIN);
        outputChoiceResourceSet.addResource(ConcreteResource.SHIELD);

        productionArea.addDevCardProduction(new ProductionDetails(
                new SpendableResourceSet(inputChoiceResourceSet),
                new ObtainableResourceSet(outputChoiceResourceSet)), 0);

        productionArea.addDevCardProduction(new ProductionDetails(
                new SpendableResourceSet(inputChoiceResourceSet),
                new ObtainableResourceSet(outputChoiceResourceSet, 1)), 1);

        inputChoiceResourceSet.addResource(new ChoiceResource(new FullChoiceSet()));

        productionArea.addDevCardProduction(new ProductionDetails(
                new SpendableResourceSet(inputChoiceResourceSet),
                new ObtainableResourceSet(outputChoiceResourceSet)), 2);

        ArrayList<ProductionDetails> productions = productionArea.getProductionDetails();

        assertEquals(4, productions.size());

        ConcreteResourceSet toMatch = new ConcreteResourceSet();
        toMatch.addResource(ConcreteResource.COIN);
        assertTrue(productions.get(1).getInput().match(toMatch));
        assertTrue(productions.get(2).getInput().match(toMatch));
        assertFalse(productions.get(3).getInput().match(toMatch));

        toMatch.addResource(ConcreteResource.STONE);
        assertFalse(productions.get(1).getInput().match(toMatch));
        assertFalse(productions.get(2).getInput().match(toMatch));
        assertTrue(productions.get(3).getInput().match(toMatch));

        toMatch.removeResource(ConcreteResource.COIN);
        assertFalse(productions.get(1).getInput().match(toMatch));
        assertFalse(productions.get(2).getInput().match(toMatch));
        assertFalse(productions.get(3).getInput().match(toMatch));

        ObtainableResourceSet result = productions.get(1).getOutput().union(productions.get(2).getOutput()).union(productions.get(3).getOutput());
        ConcreteResourceSet obtained = result.getResourceSet().toConcrete();

        assertEquals(1, result.getFaithPoints());
        assertEquals(0, obtained.getCount(ConcreteResource.COIN));
        assertEquals(0, obtained.getCount(ConcreteResource.STONE));
        assertEquals(3, obtained.getCount(ConcreteResource.SHIELD));
        assertEquals(0, obtained.getCount(ConcreteResource.SERVANT));

        inputChoiceResourceSet = new ChoiceResourceSet();
        inputChoiceResourceSet.addResource(ConcreteResource.STONE);
        outputChoiceResourceSet = new ChoiceResourceSet();
        outputChoiceResourceSet.addResource(ConcreteResource.SERVANT);

        productionArea.addDevCardProduction(new ProductionDetails(
                new SpendableResourceSet(inputChoiceResourceSet),
                new ObtainableResourceSet(outputChoiceResourceSet, 2)), 0);

        productions = productionArea.getProductionDetails();

        assertEquals(4, productions.size());

        toMatch = new ConcreteResourceSet();
        toMatch.addResource(ConcreteResource.COIN);
        assertFalse(productions.get(1).getInput().match(toMatch));
        assertTrue(productions.get(2).getInput().match(toMatch));
        assertFalse(productions.get(3).getInput().match(toMatch));

        toMatch.addResource(ConcreteResource.STONE);
        assertFalse(productions.get(1).getInput().match(toMatch));
        assertFalse(productions.get(2).getInput().match(toMatch));
        assertTrue(productions.get(3).getInput().match(toMatch));

        toMatch.removeResource(ConcreteResource.COIN);
        assertTrue(productions.get(1).getInput().match(toMatch));
        assertFalse(productions.get(2).getInput().match(toMatch));
        assertFalse(productions.get(3).getInput().match(toMatch));

        result = productions.get(1).getOutput().union(productions.get(2).getOutput()).union(productions.get(3).getOutput());
        obtained = result.getResourceSet().toConcrete();

        assertEquals(3, result.getFaithPoints());
        assertEquals(0, obtained.getCount(ConcreteResource.COIN));
        assertEquals(0, obtained.getCount(ConcreteResource.STONE));
        assertEquals(2, obtained.getCount(ConcreteResource.SHIELD));
        assertEquals(1, obtained.getCount(ConcreteResource.SERVANT));
    }

    @Test
    public void addLeaderCardProductionTest() {
        ProductionArea productionArea = new ProductionArea();

        ChoiceResourceSet inputChoiceResourceSet = new ChoiceResourceSet();
        ChoiceResourceSet outputChoiceResourceSet = new ChoiceResourceSet();

        inputChoiceResourceSet.addResource(ConcreteResource.COIN);
        outputChoiceResourceSet.addResource(new ChoiceResource(new FullChoiceSet()));

        productionArea.addLeaderCardProduction(new ProductionDetails(
                new SpendableResourceSet(inputChoiceResourceSet),
                new ObtainableResourceSet(outputChoiceResourceSet, 1)));

        ArrayList<ProductionDetails> productions = productionArea.getProductionDetails();

        assertEquals(2, productions.size());

        assertTrue(productions.get(1).getInput().getResourceSet().isConcrete());
        assertFalse(productions.get(1).getOutput().getResourceSet().isConcrete());
        assertEquals(1, productions.get(1).getOutput().getFaithPoints());

        outputChoiceResourceSet = new ChoiceResourceSet();
        outputChoiceResourceSet.addResource(ConcreteResource.SHIELD);

        productionArea.addDevCardProduction(new ProductionDetails(
                new SpendableResourceSet(inputChoiceResourceSet),
                new ObtainableResourceSet(outputChoiceResourceSet)), 1);

        inputChoiceResourceSet = new ChoiceResourceSet();
        inputChoiceResourceSet.addResource(ConcreteResource.STONE);

        outputChoiceResourceSet = new ChoiceResourceSet();
        outputChoiceResourceSet.addResource(new ChoiceResource(new FullChoiceSet()));

        productionArea.addLeaderCardProduction(new ProductionDetails(
                new SpendableResourceSet(inputChoiceResourceSet),
                new ObtainableResourceSet(outputChoiceResourceSet, 1)));

        productions = productionArea.getProductionDetails();

        assertEquals(4, productions.size());

        ConcreteResourceSet toMatch = new ConcreteResourceSet();
        toMatch.addResource(ConcreteResource.COIN);
        assertTrue(productions.get(1).getInput().match(toMatch));
        assertTrue(productions.get(2).getInput().match(toMatch));
        assertFalse(productions.get(3).getInput().match(toMatch));

        toMatch.removeResource(ConcreteResource.COIN);
        toMatch.addResource(ConcreteResource.STONE);
        assertFalse(productions.get(1).getInput().match(toMatch));
        assertFalse(productions.get(2).getInput().match(toMatch));
        assertTrue(productions.get(3).getInput().match(toMatch));

        ObtainableResourceSet result = productions.get(1).getOutput().union(productions.get(2).getOutput()).union(productions.get(3).getOutput());
        assertEquals(2, result.getFaithPoints());

        ChoiceResourceSet obtained = result.getResourceSet();
        assertFalse(obtained.isConcrete());

        ArrayList<ChoiceResource> choiceResources = obtained.getChoiceResources();
        ConcreteResourceSet concreteResources = obtained.getConcreteResources();

        assertEquals(2, choiceResources.size());
        assertEquals(1, concreteResources.size());
        assertEquals(1, concreteResources.getCount(ConcreteResource.SHIELD));
    }
}
