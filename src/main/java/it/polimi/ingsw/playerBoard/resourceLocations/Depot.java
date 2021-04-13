package it.polimi.ingsw.playerBoard.resourceLocations;

import it.polimi.ingsw.resources.ConcreteResource;
import it.polimi.ingsw.resources.InvalidResourceException;
import it.polimi.ingsw.resources.NotEnoughResourcesException;
import it.polimi.ingsw.resources.resourceSets.ConcreteResourceSet;
import it.polimi.ingsw.resources.resourceSets.InvalidResourceSetException;
import it.polimi.ingsw.resources.resourceSets.NotSingleTypeException;

public class Depot implements ConcreteResourceLocation {
    private int slots;
    private ConcreteResourceSet resources;

    public Depot(int slots) throws InvalidDepotSizeException {
        if(slots <= 0) {
            throw new InvalidDepotSizeException();
        }
        this.slots = slots;
        resources = new ConcreteResourceSet();
    }

    public ConcreteResource getResourceType() {
        try {
            return resources.getResourceType();
        } catch (NotSingleTypeException e) {
            return null;
        }
    }

    @Override
    public void addResources(ConcreteResourceSet concreteResourceSet) throws InvalidResourceSetException, InvalidResourceLocationOperationException {
        if(!canAdd(concreteResourceSet)) {
            throw new InvalidResourceLocationOperationException();
        }

        resources.union(concreteResourceSet);
    }

    @Override
    public void removeResources(ConcreteResourceSet concreteResourceSet) throws InvalidResourceSetException, InvalidResourceLocationOperationException {
        try {
            resources.difference(concreteResourceSet);
        } catch (NotEnoughResourcesException e) {
            throw new InvalidResourceLocationOperationException();
        }
    }

    @Override
    public boolean canAdd(ConcreteResourceSet concreteResourceSet) throws InvalidResourceSetException {
        if(concreteResourceSet == null) {
            throw new InvalidResourceSetException();
        }

        ConcreteResource otherResourceType;
        try {
            otherResourceType = concreteResourceSet.getResourceType();
        } catch (NotSingleTypeException e) {
            throw new InvalidResourceSetException();
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

    @Override
    public boolean containsResources(ConcreteResourceSet concreteResourceSet) throws InvalidResourceSetException {
        return resources.contains(concreteResourceSet);
    }

    @Override
    public ConcreteResourceSet getResources() {
        return (ConcreteResourceSet) resources.clone();
    }
}
