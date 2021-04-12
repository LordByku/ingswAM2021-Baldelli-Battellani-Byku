package it.polimi.ingsw.playerBoard.resourceLocations;

import it.polimi.ingsw.resources.resourceSets.ConcreteResourceSet;
import it.polimi.ingsw.resources.resourceSets.InvalidResourceSetException;

public interface ResourceLocation {
    boolean containsResources(ConcreteResourceSet concreteResourceSet) throws InvalidResourceSetException;

    ConcreteResourceSet getResources();
}
