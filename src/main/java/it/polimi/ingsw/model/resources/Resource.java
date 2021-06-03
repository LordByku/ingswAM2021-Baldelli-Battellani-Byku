package it.polimi.ingsw.model.resources;

import it.polimi.ingsw.view.cli.CLIPrintable;
import it.polimi.ingsw.view.gui.images.resources.ResourceImageType;

/**
 * Resource is the interface for resources, including ChoiceResources
 */
public interface Resource extends CLIPrintable {
    /**
     * getResource returns what this Resource represents
     *
     * @return The ConcreteResource represented by this Resource
     */
    ConcreteResource getResource();

    /**
     * isConcrete checks whether this resource is concrete,
     * i.e. whether it is not a ChoiceResource where no choice has been made yet
     *
     * @return True iff this Resource represents a ConcreteResource
     */
    boolean isConcrete();

    @Override
    String getCLIString();

    ResourceImageType getResourceImageType();
}
