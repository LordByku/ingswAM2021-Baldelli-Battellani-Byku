package it.polimi.ingsw.devCards;

public class ProductionDetails {
    SpendableResourceSet input;
    ObtainableResourceSet output;

    public void ProductionDetails(SpendableResourceSet input,ObtainableResourceSet output) {
        this.input = input;
        this.output= output;
    }
}
