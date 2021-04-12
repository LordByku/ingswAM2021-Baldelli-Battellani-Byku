package it.polimi.ingsw.resources.resourceSets;

/**
 * ResourceSet is the interface for containers of resources
 */
public interface ResourceSet extends Cloneable {
    /**
     * This method allows to add the resources contained in a ResourceSet to this resource set
     * @param other The ResourceSet to add resources from
     * @throws InvalidResourceSetException other is null or this ResourceSet is not compatible with other
     */
    void union(ResourceSet other) throws InvalidResourceSetException;

    /**
     * clone returns a copy of the object
     * @return A copy of the object
     */
    Object clone();
}
