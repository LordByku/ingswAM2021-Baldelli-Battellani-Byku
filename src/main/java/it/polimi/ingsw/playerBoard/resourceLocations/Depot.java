package it.polimi.ingsw.playerBoard.resourceLocations;

import it.polimi.ingsw.resources.ConcreteResource;
import it.polimi.ingsw.resources.InvalidResourceException;
import it.polimi.ingsw.resources.NotEnoughResourcesException;
import it.polimi.ingsw.resources.resourceSets.ConcreteResourceSet;
import it.polimi.ingsw.resources.resourceSets.InvalidResourceSetException;

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
        ConcreteResource resourceType = null;

        for(ConcreteResource resource: ConcreteResource.values()) {
            try {
                if(resources.getCount(resource) > 0) {
                    resourceType = resource;
                }
            } catch (InvalidResourceException e) {}
        }

        return resourceType;
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

        int resourceTypes = 0;
        ConcreteResource otherResourceType = null;
        for(ConcreteResource resource: ConcreteResource.values()) {
            try {
                if(concreteResourceSet.getCount(resource) > 0) {
                    otherResourceType = resource;
                    resourceTypes++;
                }
            } catch (InvalidResourceException e) {}
        }

        ConcreteResource currentResourceType = getResourceType();

        if(resourceTypes == 0) {
            return true;
        }

        if(resourceTypes > 1) {
            return false;
        }

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
