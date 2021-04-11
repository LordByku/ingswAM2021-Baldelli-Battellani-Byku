package it.polimi.ingsw.playerBoard;

import it.polimi.ingsw.devCards.ProductionDetails;
import it.polimi.ingsw.resources.ChoiceResource;
import it.polimi.ingsw.resources.FullChoiceSet;
import it.polimi.ingsw.resources.InvalidChoiceSetException;
import it.polimi.ingsw.resources.resourceSets.ChoiceResourceSet;
import it.polimi.ingsw.resources.resourceSets.ObtainableResourceSet;
import it.polimi.ingsw.resources.resourceSets.SpendableResourceSet;

import java.util.ArrayList;

public class ProductionArea {
    private ArrayList<ProductionDetails> productions;

    public ProductionArea() {
        productions = new ArrayList<>();

        ChoiceResourceSet inputChoiceResourceSet = new ChoiceResourceSet();
        ChoiceResourceSet outputChoiceResourceSet = new ChoiceResourceSet();
        try {
            inputChoiceResourceSet.addResource(new ChoiceResource(new FullChoiceSet()));
            inputChoiceResourceSet.addResource(new ChoiceResource(new FullChoiceSet()));
            outputChoiceResourceSet.addResource(new ChoiceResource(new FullChoiceSet()));
        } catch (InvalidChoiceSetException e) {}
        productions.add(new ProductionDetails(new SpendableResourceSet(inputChoiceResourceSet),
                                              new ObtainableResourceSet(outputChoiceResourceSet)));
    }
}
