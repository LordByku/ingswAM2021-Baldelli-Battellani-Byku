package it.polimi.ingsw.model.playerBoard.resourceLocations;

import it.polimi.ingsw.model.resources.resourceSets.ConcreteResourceSet;
import it.polimi.ingsw.model.resources.resourceSets.InvalidResourceSetException;

/**
 * StrongBox is the class for the player board's strongbox
 */
public class StrongBox implements ConcreteResourceLocation {
    /**
     * resource is the set of ConcreteResources contained in this StrongBox
     */
    private ConcreteResourceSet resources;

    /**
     * The constructor initializes resources to an empty set
     */
    public StrongBox() {
        resources = new ConcreteResourceSet();
    }

    /**
     * addResources adds a given ConcreteResourceSet to this StrongBox
     * @param concreteResourceSet The ConcreteResourceSet to add
     * @throws InvalidResourceSetException concreteResourceSet is null
     */
    @Override
    public void addResources(ConcreteResourceSet concreteResourceSet) throws InvalidResourceSetException {
        resources.union(concreteResourceSet);
    }

    /**
     * removeResources removes a given ConcreteResourceSet to this StrongBox
     * @param concreteResourceSet The ConcreteResourceSet to remove
     * @throws InvalidResourceSetException concreteResourceSet is null
     * @throws InvalidResourceLocationOperationException concreteResourceSet cannot
     * be removed from this StrongBox
     */
    @Override
    public void removeResources(ConcreteResourceSet concreteResourceSet) throws InvalidResourceSetException, InvalidResourceLocationOperationException {
        if(!containsResources(concreteResourceSet)) {
            throw new InvalidResourceLocationOperationException();
        }
        resources.difference(concreteResourceSet);
    }

    /**
     * canAdd always returns true as StrongBox does not have any capacity limitations
     * @param concreteResourceSet The ConcreteResourceSet to check
     * @return True
     * @throws InvalidResourceSetException concreteResourceSet is null
     */
    @Override
    public boolean canAdd(ConcreteResourceSet concreteResourceSet) throws InvalidResourceSetException {
        if(concreteResourceSet == null) {
            throw new InvalidResourceSetException();
        }
        return true;
    }

    /**
     * containsResources checks whether a given ConcreteResourceSet is contained in this StrongBox
     * @param concreteResourceSet The ConcreteResourceSet to check
     * @return True iff this StrongBox contains concreteResourceSet
     * @throws InvalidResourceSetException concreteResourceSet is null
     */
    @Override
    public boolean containsResources(ConcreteResourceSet concreteResourceSet) throws InvalidResourceSetException {
        return resources.contains(concreteResourceSet);
    }

    /**
     * getResources returns a copy of the resources contained in this StrongBox
     * @return A ConcreteResourceSet representing the resources in this StrongBox
     */
    @Override
    public ConcreteResourceSet getResources() {
        return (ConcreteResourceSet) resources.clone();
    }
}
