package it.polimi.ingsw.devCards;

import it.polimi.ingsw.resources.resourceSets.ObtainableResourceSet;
import it.polimi.ingsw.resources.resourceSets.SpendableResourceSet;

/**
 * ProductionDetails represents the details of a production, both spent and obtained resources
 */
public class ProductionDetails {
    SpendableResourceSet input;
    ObtainableResourceSet output;

    /**
     * @param input the resources to be spend
     * @param output the resources to be obtained
     */
    public ProductionDetails(SpendableResourceSet input,ObtainableResourceSet output) {
        this.input = input;
        this.output= output;
    }
}
