package it.polimi.ingsw.playerBoard.resourceLocations;

import it.polimi.ingsw.resources.resourceSets.ConcreteResourceSet;
import it.polimi.ingsw.resources.resourceSets.InvalidResourceSetException;

/**
 * ConcreteResourceLocation is the interface for resource locations
 * that directly store resources
 */
public interface ConcreteResourceLocation extends ResourceLocation {
    /**
     * addResources adds a given ConcreteResourceSet to this ConcreteResourceLocation
     * @param concreteResourceSet The ConcreteResourceSet to add
     * @throws InvalidResourceSetException concreteResourceSet is null
     * @throws InvalidResourceLocationOperationException concreteResourceSet cannot
     * be added to this ConcreteResourceLocation
     */
    void addResources(ConcreteResourceSet concreteResourceSet) throws InvalidResourceSetException, InvalidResourceLocationOperationException;

    /**
     * removeResources removes a given ConcreteResourceSet to this ConcreteResourceLocation
     * @param concreteResourceSet The ConcreteResourceSet to remove
     * @throws InvalidResourceSetException concreteResourceSet is null
     * @throws InvalidResourceLocationOperationException concreteResourceSet cannot
     * be removed from this ConcreteResourceLocation
     */
    void removeResources(ConcreteResourceSet concreteResourceSet) throws InvalidResourceSetException, InvalidResourceLocationOperationException;

    /**
     * canAdd checks whether a given ConcreteResourceSet can be added to this ConcreteResourceLocation
     * @param concreteResourceSet The ConcreteResourceSet to check
     * @return True iff concreteResourceSet can be added to this ConcreteResourceLocation
     * @throws InvalidResourceSetException concreteResourceSet is null
     */
    boolean canAdd(ConcreteResourceSet concreteResourceSet) throws InvalidResourceSetException;
}
