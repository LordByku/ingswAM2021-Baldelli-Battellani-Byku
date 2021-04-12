package it.polimi.ingsw.playerBoard.resourceLocations;

import it.polimi.ingsw.resources.resourceSets.ConcreteResourceSet;
import it.polimi.ingsw.resources.resourceSets.InvalidResourceSetException;

public interface ConcreteResourceLocation extends ResourceLocation {
    void addResources(ConcreteResourceSet concreteResourceSet) throws InvalidResourceSetException, InvalidResourceLocationOperationException;

    void removeResources(ConcreteResourceSet concreteResourceSet) throws InvalidResourceSetException, InvalidResourceLocationOperationException;

    boolean canAdd(ConcreteResourceSet concreteResourceSet) throws InvalidResourceSetException;
}
