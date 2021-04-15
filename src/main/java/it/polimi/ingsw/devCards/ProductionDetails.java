package it.polimi.ingsw.devCards;

import it.polimi.ingsw.resources.resourceSets.InvalidResourceSetException;
import it.polimi.ingsw.resources.resourceSets.ObtainableResourceSet;
import it.polimi.ingsw.resources.resourceSets.SpendableResourceSet;

/**
 * ProductionDetails represents the details of a production, both spent and obtained resources
 */
public class ProductionDetails {


    private final SpendableResourceSet input;
    private final ObtainableResourceSet output;

    /**
     * @param input the resources to be spend
     * @param output the resources to be obtained
     */
    public ProductionDetails(SpendableResourceSet input,ObtainableResourceSet output) throws InvalidResourceSetException {
        if(input==null || output==null)
            throw new InvalidResourceSetException();
        this.input = input.clone();
        this.output= output.clone();
    }

    public SpendableResourceSet getInput() {
        return input;
    }

    public ObtainableResourceSet getOutput() {
        return output;
    }
}
