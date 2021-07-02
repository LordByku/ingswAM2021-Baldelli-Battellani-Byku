package it.polimi.ingsw.model.devCards;

import it.polimi.ingsw.model.resources.resourceSets.InvalidResourceSetException;
import it.polimi.ingsw.model.resources.resourceSets.ObtainableResourceSet;
import it.polimi.ingsw.model.resources.resourceSets.SpendableResourceSet;
import it.polimi.ingsw.view.cli.CLIPrintable;

/**
 * ProductionDetails represents the details of a production, both spent and obtained resources
 */
public class ProductionDetails implements Cloneable, CLIPrintable {
    /**
     * input is the input SpendableResourceSet for this production power
     */
    private final SpendableResourceSet input;
    /**
     * output is the output ObtainableResourceSet for this production power
     */
    private final ObtainableResourceSet output;

    /**
     * Default constructor
     * @param input  the resources to be spent
     * @param output the resources to be obtained
     */
    public ProductionDetails(SpendableResourceSet input, ObtainableResourceSet output) throws InvalidResourceSetException {
        if (input == null || output == null) {
            throw new InvalidResourceSetException();
        }
        if (input.getResourceSet().size() <= 0 || input.getResourceSet().size() >= 10 ||
            output.getResourceSet().size() + output.getFaithPoints() <= 0 ||
            output.getResourceSet().size() + output.getFaithPoints() >= 10) {
            throw new InvalidResourceSetException();
        }
        this.input = input.clone();
        this.output = output.clone();
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

    @Override
    public ProductionDetails clone() {
        try {
            return (ProductionDetails) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String getCLIString() {
        return input.getCLIString() + "} " + output.getCLIString();
    }
}
