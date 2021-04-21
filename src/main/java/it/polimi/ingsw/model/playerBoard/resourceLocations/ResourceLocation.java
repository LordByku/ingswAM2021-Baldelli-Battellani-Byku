package it.polimi.ingsw.model.playerBoard.resourceLocations;

import it.polimi.ingsw.model.resources.resourceSets.ConcreteResourceSet;
import it.polimi.ingsw.model.resources.resourceSets.InvalidResourceSetException;

/**
 * ResourceLocation is the interface for classes in the Board that contain resources
 */
public interface ResourceLocation {
    /**
     * containsResources checks whether a given ConcreteResourceSet
     * is contained in this ResourceLocation
     * @param concreteResourceSet The ConcreteResourceSet to check
     * @return True iff this ResourceLocation contains concreteResourceSet
     * @throws InvalidResourceSetException concreteResourceSet is null
     */
    boolean containsResources(ConcreteResourceSet concreteResourceSet) throws InvalidResourceSetException;

    /**
     * getResources returns a copy of the resources contained in this ResourceLocation
     * @return A ConcreteResourceSet representing the resources in this ResourceLocation
     */
    ConcreteResourceSet getResources();
}
