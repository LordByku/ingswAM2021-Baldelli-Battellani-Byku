package it.polimi.ingsw.playerBoard;

import it.polimi.ingsw.devCards.ProductionDetails;
import it.polimi.ingsw.resources.ChoiceResource;
import it.polimi.ingsw.resources.FullChoiceSet;
import it.polimi.ingsw.resources.InvalidChoiceSetException;
import it.polimi.ingsw.resources.InvalidResourceException;
import it.polimi.ingsw.resources.resourceSets.ChoiceResourceSet;
import it.polimi.ingsw.resources.resourceSets.InvalidResourceSetException;
import it.polimi.ingsw.resources.resourceSets.ObtainableResourceSet;
import it.polimi.ingsw.resources.resourceSets.SpendableResourceSet;

import java.util.ArrayList;
import java.util.HashMap;

public class ProductionArea {
    private ArrayList<ProductionDetails> productions;

    public ProductionArea() {
        productions = new ArrayList<>();

        ChoiceResourceSet inputChoiceResourceSet = new ChoiceResourceSet();
        ChoiceResourceSet outputChoiceResourceSet = new ChoiceResourceSet();
        inputChoiceResourceSet.addResource(new ChoiceResource(new FullChoiceSet()));
        inputChoiceResourceSet.addResource(new ChoiceResource(new FullChoiceSet()));
        outputChoiceResourceSet.addResource(new ChoiceResource(new FullChoiceSet()));
        productions.add(new ProductionDetails(new SpendableResourceSet(inputChoiceResourceSet),
                                              new ObtainableResourceSet(outputChoiceResourceSet)));
        productions.add(null);
        productions.add(null);
        productions.add(null);
    }

    public void addDevCardProduction(ProductionDetails productionDetails, int deckIndex) throws InvalidProductionDetailsException {
        if(productionDetails == null) {
            throw new InvalidProductionDetailsException();
        }
        productions.set(deckIndex + 1, productionDetails);
    }

    public void addLeaderCardProduction(ProductionDetails productionDetails) throws InvalidProductionDetailsException {
        if(productionDetails == null) {
            throw new InvalidProductionDetailsException();
        }
        productions.add(productionDetails);
    }
}
