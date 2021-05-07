package it.polimi.ingsw.model.resources;

/**
 * Resource is the interface for resources, including ChoiceResources
 */
public interface Resource {
    /**
     * getResource returns what this Resource represents
     * @return The ConcreteResource represented by this Resource
     */
    ConcreteResource getResource();

    /**
     * isConcrete checks whether this resource is concrete,
     * i.e. whether it is not a ChoiceResource where no choice has been made yet
     * @return True iff this Resource represents a ConcreteResource
     */
    boolean isConcrete();

    /**
     * cleanClone returns a new copy of this Resource
     * @return A copy of this Resource
     */
    Resource cleanClone();

    String getCLIString();
}
