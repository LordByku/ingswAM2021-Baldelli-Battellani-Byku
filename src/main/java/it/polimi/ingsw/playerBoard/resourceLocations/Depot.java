package it.polimi.ingsw.playerBoard.resourceLocations;

import it.polimi.ingsw.resources.resourceSets.ConcreteResourceSet;

public class Depot implements ConcreteResourceLocation {
    private int slots;
    private ConcreteResourceSet resources;

    public Depot(int slots) {
        this.slots = slots;
        resources = new ConcreteResourceSet();
    }
}
