package it.polimi.ingsw.playerBoard.resourceLocations;

import it.polimi.ingsw.resources.resourceSets.ConcreteResourceSet;

public class StrongBox implements ConcreteResourceLocation {
    private ConcreteResourceSet resources;

    public StrongBox() {
        resources = new ConcreteResourceSet();
    }
}
