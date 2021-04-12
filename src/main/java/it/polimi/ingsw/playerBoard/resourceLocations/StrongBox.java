package it.polimi.ingsw.playerBoard.resourceLocations;

import it.polimi.ingsw.resources.NotEnoughResourcesException;
import it.polimi.ingsw.resources.resourceSets.ConcreteResourceSet;
import it.polimi.ingsw.resources.resourceSets.InvalidResourceSetException;

public class StrongBox implements ConcreteResourceLocation {
    private ConcreteResourceSet resources;

    public StrongBox() {
        resources = new ConcreteResourceSet();
    }

    @Override
    public void addResources(ConcreteResourceSet concreteResourceSet) throws InvalidResourceSetException {
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
        return true;
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
