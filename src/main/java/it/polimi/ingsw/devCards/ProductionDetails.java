package it.polimi.ingsw.devCards;

import it.polimi.ingsw.resources.resourceSets.ObtainableResourceSet;
import it.polimi.ingsw.resources.resourceSets.SpendableResourceSet;

public class ProductionDetails {
    SpendableResourceSet input;
    ObtainableResourceSet output;

    public ProductionDetails(SpendableResourceSet input,ObtainableResourceSet output) {
        this.input = input;
        this.output= output;
    }
}
