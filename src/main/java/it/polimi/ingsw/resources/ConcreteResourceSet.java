package it.polimi.ingsw.resources;

import java.util.HashMap;

public class ConcreteResourceSet implements ResourceSet {
    private final HashMap<ConcreteResource, Integer> resources;

    ConcreteResourceSet() {
        resources = new HashMap<>();
    }

    public int getCount(ConcreteResource resource) {
        return resources.getOrDefault(resource, 0);
    }

    public void addResource(ConcreteResource resource, int quantity) {
        int prevQuantity = getCount(resource);
        int newQuantity = prevQuantity + quantity;
        resources.put(resource, newQuantity);
    }

    public void addResource(ConcreteResource resource) {
        addResource(resource, 1);
    }

    public void removeResource(ConcreteResource resource, int quantity) throws NotEnoughResourcesException {
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

    public void removeResource(ConcreteResource resource) throws NotEnoughResourcesException {
        removeResource(resource, 1);
    }

    public void union(ConcreteResourceSet other) {
        for(ConcreteResource resource: ConcreteResource.values()) {
            int quantity = other.getCount(resource);
            if(quantity > 0) {
                addResource(resource, quantity);
            }
        }
    }
}