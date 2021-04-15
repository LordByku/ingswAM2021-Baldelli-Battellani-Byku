package it.polimi.ingsw.resources.resourceSets;

/**
 * ResourceSet is the interface for containers of resources
 */
public interface ResourceSet extends Cloneable {
    /**
     * clone returns a copy of the object
     * @return A copy of the object
     */
    Object clone();

    int size();
}
