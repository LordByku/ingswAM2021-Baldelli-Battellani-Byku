package it.polimi.ingsw.model.resources.resourceSets;

/**
 * ResourceSet is the interface for containers of resources
 */
public interface ResourceSet extends Cloneable {
    /**
     * clone returns a copy of the object
     * @return A copy of the object
     */
    Object clone();

    /**
     * size returns the number of Resources in this ResourceSet
     * @return The number of Resources in this ResourceSet
     */
    int size();
}
