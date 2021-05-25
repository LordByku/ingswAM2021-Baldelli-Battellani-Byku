package it.polimi.ingsw.model.playerBoard.resourceLocations;

import it.polimi.ingsw.model.resources.ConcreteResource;
import it.polimi.ingsw.model.resources.resourceSets.ConcreteResourceSet;
import it.polimi.ingsw.model.resources.resourceSets.InvalidResourceSetException;

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
    private final ConcreteResourceSet resources;

    /**
     * The constructor creates a new Depot with an empty ConcreteResourceSet and the capacity
     * received as parameter
     *
     * @param slots The required capacity
     * @throws InvalidDepotSizeException slots is not strictly positive
     */
    public Depot(int slots) throws InvalidDepotSizeException {
        if (slots <= 0) {
            throw new InvalidDepotSizeException();
        }
        this.slots = slots;
        resources = new ConcreteResourceSet();
    }

    /**
     * getResourceType returns the type of resource contained in this Depot
     *
     * @return The type of ConcreteResource contained in this Depot or null if
     * no resources are present
     */
    public ConcreteResource getResourceType() {
        return resources.getResourceType();
    }

    /**
     * addResources adds a given ConcreteResourceSet to this Depot
     *
     * @param concreteResourceSet The ConcreteResourceSet to add
     * @throws InvalidResourceSetException               concreteResourceSet is null
     * @throws InvalidResourceLocationOperationException concreteResourcesSet cannot
     *                                                   be added to this Depot
     */
    @Override
    public void addResources(ConcreteResourceSet concreteResourceSet) throws InvalidResourceSetException, InvalidResourceLocationOperationException {
        if (!canAdd(concreteResourceSet)) {
            throw new InvalidResourceLocationOperationException();
        }

        resources.union(concreteResourceSet);
    }

    /**
     * removeResources removes a given ConcreteResourceSet to this Depot
     *
     * @param concreteResourceSet The ConcreteResourceSet to remove
     * @throws InvalidResourceSetException               concreteResourceSet is null
     * @throws InvalidResourceLocationOperationException concreteResourceSet cannot
     *                                                   be removed from this Depot
     */
    @Override
    public void removeResources(ConcreteResourceSet concreteResourceSet) throws InvalidResourceSetException, InvalidResourceLocationOperationException {
        if (!containsResources(concreteResourceSet)) {
            throw new InvalidResourceLocationOperationException();
        }
        resources.difference(concreteResourceSet);
    }

    /**
     * canAdd checks whether a given ConcreteResourceSet can be added to this Depot
     *
     * @param concreteResourceSet The ConcreteResourceSet to check
     * @return True iff concreteResourceSet can be added to this Depot
     * @throws InvalidResourceSetException concreteResourceSet is null
     */
    @Override
    public boolean canAdd(ConcreteResourceSet concreteResourceSet) throws InvalidResourceSetException {
        if (concreteResourceSet == null) {
            throw new InvalidResourceSetException();
        }

        // concreteResourceSet has more than one type of resources: return false
        if (concreteResourceSet.hasMultipleTypes()) {
            return false;
        }

        ConcreteResource otherResourceType = concreteResourceSet.getResourceType();

        // concreteResourceSet has no resources: return true
        if (otherResourceType == null) {
            return true;
        }

        // concreteResourceSet is single type
        int otherAmount = concreteResourceSet.getCount(otherResourceType);

        ConcreteResource currentResourceType = getResourceType();

        // There are currently no resources in this depot: return true
        // if the amount of resources in concreteResourceSet is
        // less than the capacity of the depot
        if (currentResourceType == null) {
            return otherAmount <= slots;
        }

        // The resource type in this depot is fixed to currentResourceType
        int currentAmount = resources.getCount(currentResourceType);

        // Return true if the resource types match and the total amount
        // of resources fits within the capacity of the depot
        return otherResourceType == currentResourceType && otherAmount + currentAmount <= slots;
    }

    /**
     * containsResources checks whether a given ConcreteResourceSet is contained in this depot
     *
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
     *
     * @return A ConcreteResourceSet representing the resources in this Depot
     */
    @Override
    public ConcreteResourceSet getResources() {
        return (ConcreteResourceSet) resources.clone();
    }
}
