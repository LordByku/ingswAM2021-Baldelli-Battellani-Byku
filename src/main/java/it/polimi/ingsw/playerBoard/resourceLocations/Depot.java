package it.polimi.ingsw.playerBoard.resourceLocations;

import it.polimi.ingsw.resources.ConcreteResource;
import it.polimi.ingsw.resources.InvalidResourceException;
import it.polimi.ingsw.resources.NotEnoughResourcesException;
import it.polimi.ingsw.resources.resourceSets.ConcreteResourceSet;
import it.polimi.ingsw.resources.resourceSets.InvalidResourceSetException;
import it.polimi.ingsw.resources.resourceSets.NotSingleTypeException;

/**
 * Depot is the class for warehouse depots
 */
public class Depot implements ConcreteResourceLocation {
    /**
     * slots is the capacity of this Depot
     */
    private final int slots;
    /**
     * resources is the set of ConcreteResources contained in this depot
     */
    private ConcreteResourceSet resources;

    /**
     * The constructor creates a new Depot with an empty ConcreteResourceSet and the capacity
     * received as parameter
     * @param slots The required capacity
     * @throws InvalidDepotSizeException slots is not strictly positive
     */
    public Depot(int slots) throws InvalidDepotSizeException {
        if(slots <= 0) {
            throw new InvalidDepotSizeException();
        }
        this.slots = slots;
        resources = new ConcreteResourceSet();
    }

    /**
     * getResourceType returns the type of resource contained in this Depot
     * @return The type of ConcreteResource contained in this Depot or null if
     * no resources are present
     */
    public ConcreteResource getResourceType() {
        try {
            return resources.getResourceType();
        } catch (NotSingleTypeException e) {
            return null;
        }
    }

    /**
     * addResources adds a given ConcreteResourceSet to this Depot
     * @param concreteResourceSet The ConcreteResourceSet to add
     * @throws InvalidResourceSetException concreteResourceSet is null
     * @throws InvalidResourceLocationOperationException concreteResourcesSet cannot
     * be added to this Depot
     */
    @Override
    public void addResources(ConcreteResourceSet concreteResourceSet) throws InvalidResourceSetException, InvalidResourceLocationOperationException {
        if(!canAdd(concreteResourceSet)) {
            throw new InvalidResourceLocationOperationException();
        }

        resources.union(concreteResourceSet);
    }

    /**
     * removeResources removes a given ConcreteResourceSet to this Depot
     * @param concreteResourceSet The ConcreteResourceSet to remove
     * @throws InvalidResourceSetException concreteResourceSet is null
     * @throws InvalidResourceLocationOperationException concreteResourceSet cannot
     * be removed from this Depot
     */
    @Override
    public void removeResources(ConcreteResourceSet concreteResourceSet) throws InvalidResourceSetException, InvalidResourceLocationOperationException {
        try {
            resources.difference(concreteResourceSet);
        } catch (NotEnoughResourcesException e) {
            throw new InvalidResourceLocationOperationException();
        }
    }

    /**
     * canAdd checks whether a given ConcreteResourceSet can be added to this Depot
     * @param concreteResourceSet The ConcreteResourceSet to check
     * @return True iff concreteResourceSet can be added to this Depot
     * @throws InvalidResourceSetException concreteResourceSet is null
     */
    @Override
    public boolean canAdd(ConcreteResourceSet concreteResourceSet) throws InvalidResourceSetException {
        if(concreteResourceSet == null) {
            throw new InvalidResourceSetException();
        }

        ConcreteResource otherResourceType;
        try {
            otherResourceType = concreteResourceSet.getResourceType();
        } catch (NotSingleTypeException e) {
            return false;
        }

        if(otherResourceType == null) {
            return true;
        }

        ConcreteResource currentResourceType = getResourceType();

        int otherAmount = 0, currentAmount = 0;
        try {
            otherAmount = concreteResourceSet.getCount(otherResourceType);
        } catch (InvalidResourceException e) {}

        if(currentResourceType == null && otherAmount <= slots) {
            return true;
        }

        try {
            currentAmount = resources.getCount(currentResourceType);
        } catch (InvalidResourceException e) {}

        return otherResourceType == currentResourceType && otherAmount + currentAmount <= slots;
    }

    /**
     * containsResources checks whether a given ConcreteResourceSet is contained in this depot
     * @param concreteResourceSet The ConcreteResourceSet to check
     * @return True iff this Depot contains concreteResourceSet
     * @throws InvalidResourceSetException concreteResourceSet is null
     */
    @Override
    public boolean containsResources(ConcreteResourceSet concreteResourceSet) throws InvalidResourceSetException {
        return resources.contains(concreteResourceSet);
    }

    /**
     * getResources returns a copy of the resources contained in this Depot
     * @return A ConcreteResourceSet representing the resources in this Depot
     */
    @Override
    public ConcreteResourceSet getResources() {
        return (ConcreteResourceSet) resources.clone();
    }
}
