package it.polimi.ingsw.model.devCards;

import it.polimi.ingsw.model.resources.resourceSets.InvalidResourceSetException;
import it.polimi.ingsw.model.resources.resourceSets.ObtainableResourceSet;
import it.polimi.ingsw.model.resources.resourceSets.SpendableResourceSet;

/**
 * ProductionDetails represents the details of a production, both spent and obtained resources
 */
public class ProductionDetails implements Cloneable {
    private final SpendableResourceSet input;
    private final ObtainableResourceSet output;

    /**
     * @param input the resources to be spent
     * @param output the resources to be obtained
     */
    public ProductionDetails(SpendableResourceSet input,ObtainableResourceSet output) throws InvalidResourceSetException {
        if(input == null || output == null) {
            throw new InvalidResourceSetException();
        }
        this.input = input.clone();
        this.output= output.clone();
    }

    /**
     * @return the resources to be spent in accord to the prod details
     */
    public SpendableResourceSet getInput() {
        return input.clone();
    }

    /**
     * @return the resources to be obtained in accord to the prod details
     */
    public ObtainableResourceSet getOutput() {
        return output.clone();
    }

    public ProductionDetails clone() {
        try {
            return (ProductionDetails) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }
}
