package it.polimi.ingsw.resources;

import it.polimi.ingsw.resources.resourceSets.ResourceSet;

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
     * copy returns a copy of the object
     * @return A copy of the object
     */
    Resource copy();
    // TODO: Maybe copyable interface? Can't use clone because ConcreteResource cannot override clone() from Enum
}
