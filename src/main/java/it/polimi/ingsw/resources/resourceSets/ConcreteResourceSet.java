package it.polimi.ingsw.resources.resourceSets;

import it.polimi.ingsw.resources.ConcreteResource;
import it.polimi.ingsw.resources.NotEnoughResourcesException;

import java.util.HashMap;

/**
 * ConcreteResourceSet is a container for ConcreteResources
 */
public class ConcreteResourceSet implements ResourceSet {
    /**
     * resources is where ConcreteResources are stored
     */
    private HashMap<ConcreteResource, Integer> resources;

    /**
     * The constructor initializes resources to an empty set
     */
    public ConcreteResourceSet() {
        resources = new HashMap<>();
    }

    /**
     * getCount returns the number of occurrences of the given resource
     * @param resource The ConcreteResource to count
     * @return The number of occurrences of resource in the set
     */
    public int getCount(ConcreteResource resource) {
        return resources.getOrDefault(resource, 0);
    }

    /**
     * AddResource adds new ConcreteResources to the set
     * @param resource The ConcreteResource to add
     * @param quantity The quantity to add
     * @throws InvalidQuantityException quantity is not strictly positive
     */
    public void addResource(ConcreteResource resource, int quantity) throws InvalidQuantityException {
        if(quantity <= 0) {
            throw new InvalidQuantityException();
        }
        int prevQuantity = getCount(resource);
        int newQuantity = prevQuantity + quantity;
        resources.put(resource, newQuantity);
    }

    /**
     * This method offers the option to add a single resource
     * @param resource The ConcreteResource to add
     */
    public void addResource(ConcreteResource resource) {
        try {
            addResource(resource, 1);
        } catch (InvalidQuantityException e) {}
    }

    /**
     * removeResource removes ConcreteResources from the set
     * @param resource The ConcreteResource to remove
     * @param quantity The quantity to remove
     * @throws InvalidQuantityException quantity is not strictly positive
     * @throws NotEnoughResourcesException There are less than quantity occurrences of resource in the set
     */
    public void removeResource(ConcreteResource resource, int quantity) throws InvalidQuantityException, NotEnoughResourcesException {
        if(quantity <= 0) {
            throw new InvalidQuantityException();
        }
        int prevQuantity = getCount(resource);
        if(prevQuantity < quantity) {
            throw new NotEnoughResourcesException();
        }

        int newQuantity = prevQuantity - quantity;
        if(newQuantity == 0) {
            resources.remove(resource);
        } else {
            resources.put(resource, newQuantity);
        }
    }

    /**
     * This method offers the option to remove a single resource
     * @param resource The ConcreteResource to remove
     * @throws NotEnoughResourcesException There are no occurrences of resource in the set
     */
    public void removeResource(ConcreteResource resource) throws NotEnoughResourcesException {
        try {
            removeResource(resource, 1);
        } catch (InvalidQuantityException e) {}
    }

    /**
     * This method allows to add the resources contained in a ResourceSet to this resource set
     * @param other The ResourceSet to add resources from
     * @throws InvalidResourceSetException other is null or other is not a ConcreteResourceSet
     */
    @Override
    public void union(ResourceSet other) throws InvalidResourceSetException {
        if(other == null) {
            throw new InvalidResourceSetException();
        }
        try {
            ConcreteResourceSet concreteOther = (ConcreteResourceSet) other;
            for(ConcreteResource resource: ConcreteResource.values()) {
                int quantity = concreteOther.getCount(resource);
                try {
                    addResource(resource, quantity);
                } catch (InvalidQuantityException e) {}
            }
        } catch (ClassCastException e) {
            throw new InvalidResourceSetException();
        }
    }

    /**
     * clone returns a copy of the object
     * @return A copy of the object
     */
    @Override
    public ResourceSet clone() {
        try {
            ConcreteResourceSet cloneResourceSet = (ConcreteResourceSet) super.clone();
            cloneResourceSet.resources = (HashMap<ConcreteResource, Integer>) resources.clone();
            return cloneResourceSet;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }
}