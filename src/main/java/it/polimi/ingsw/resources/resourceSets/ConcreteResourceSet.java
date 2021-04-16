package it.polimi.ingsw.resources.resourceSets;

import it.polimi.ingsw.leaderCards.LeaderCardRequirements;
import it.polimi.ingsw.playerBoard.Board;
import it.polimi.ingsw.playerBoard.InvalidBoardException;
import it.polimi.ingsw.resources.ConcreteResource;
import it.polimi.ingsw.resources.InvalidResourceException;
import it.polimi.ingsw.resources.NotEnoughResourcesException;

import java.util.HashMap;

/**
 * ConcreteResourceSet is a container for ConcreteResources
 */
public class ConcreteResourceSet implements ResourceSet, LeaderCardRequirements {
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
     * @throws InvalidResourceException resource is null
     */
    public int getCount(ConcreteResource resource) throws InvalidResourceException {
        if(resource == null) {
            throw new InvalidResourceException();
        }
        return resources.getOrDefault(resource, 0);
    }

    /**
     * AddResource adds new ConcreteResources to the set
     * @param resource The ConcreteResource to add
     * @param quantity The quantity to add
     * @throws InvalidResourceException resource is null
     * @throws InvalidQuantityException quantity is not strictly positive
     */
    public void addResource(ConcreteResource resource, int quantity) throws InvalidResourceException, InvalidQuantityException {
        if(resource == null) {
            throw new InvalidResourceException();
        }
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
     * @throws InvalidResourceException resource is null
     */
    public void addResource(ConcreteResource resource) throws InvalidResourceException {
        addResource(resource, 1);
    }

    /**
     * removeResource removes ConcreteResources from the set
     * @param resource The ConcreteResource to remove
     * @param quantity The quantity to remove
     * @throws InvalidResourceException resource is null
     * @throws InvalidQuantityException quantity is not strictly positive
     * @throws NotEnoughResourcesException There are less than quantity occurrences of resource in the set
     */
    public void removeResource(ConcreteResource resource, int quantity)
            throws InvalidResourceException, InvalidQuantityException, NotEnoughResourcesException {
        if(resource == null) {
            throw new InvalidResourceException();
        }
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
     * @throws InvalidResourceException resource is null
     * @throws NotEnoughResourcesException There are no occurrences of resource in the set
     */
    public void removeResource(ConcreteResource resource) throws InvalidResourceException, NotEnoughResourcesException {
        removeResource(resource, 1);
    }

    /**
     * union adds to this ConcreteResourceSet all the resources contained
     * in another ConcreteResourceSet
     * @param other The ConcreteResourceSet to add resources from
     * @throws InvalidResourceSetException other is null
     */
    public void union(ConcreteResourceSet other) throws InvalidResourceSetException {
        if(other == null) {
            throw new InvalidResourceSetException();
        }
        for(ConcreteResource resource: ConcreteResource.values()) {
            int quantity = other.getCount(resource);
            if(quantity > 0) {
                addResource(resource, quantity);
            }
        }
    }

    /**
     * difference removes from this set all the resources contained in another ConcreteResourceSet
     * @param other The given ConcreteResourceSet
     * @throws InvalidResourceSetException other is null
     * @throws NotEnoughResourcesException There aren't enough resources in this set
     * to perform the operation
     */
    public void difference(ConcreteResourceSet other) throws InvalidResourceSetException, NotEnoughResourcesException {
        if(other == null) {
            throw new InvalidResourceSetException();
        }

        if(!contains(other)) {
            throw new NotEnoughResourcesException();
        }

        for(ConcreteResource resource: ConcreteResource.values()) {
            int otherQuantity = other.getCount(resource);
            if(otherQuantity > 0) {
                removeResource(resource, other.getCount(resource));
            }
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

    /**
     * contains checks whether a given ConcreteResourceSet is a subset of this ConcreteResourceSet
     * @param other The ConcreteResourceSet to check
     * @return True iff other is a subset of this ConcreteResourceSet
     * @throws InvalidResourceSetException other is null
     */
    public boolean contains(ConcreteResourceSet other) throws InvalidResourceSetException {
        if(other == null) {
            throw new InvalidResourceSetException();
        }

        for(ConcreteResource resource: ConcreteResource.values()) {
            if(other.getCount(resource) > getCount(resource)) {
                return false;
            }
        }

        return true;
    }

    /**
     * isSatisfied implements the method of the LeaderCardRequirements interface
     * It checks whether a given board contains all the resources indicated
     * by this ConcreteResourceSet
     * @param board The board of the current player.
     * @return True iff board contains this ConcreteResourceSet
     * @throws InvalidBoardException board is null
     */
    @Override
    public boolean isSatisfied(Board board) throws InvalidBoardException {
        if(board == null) {
            throw new InvalidBoardException();
        }
        return board.containsResources((ConcreteResourceSet) clone());
    }

    /**
     * getResourceType returns what type of resource is contained in this ConcreteResourceSet
     * @return The only type of ConcreteResource contained in this set or null if this set
     * contains no resources
     * @throws NotSingleTypeException This set contains more than one type of resources
     */
    public ConcreteResource getResourceType() throws NotSingleTypeException {
        if(!isSingleType()) {
            throw new NotSingleTypeException();
        }

        if(resources.size() == 0) {
            return null;
        }

        return (ConcreteResource) resources.keySet().toArray()[0];
    }

    /**
     * isSingleType checks whether this ConcreteResourceSet contains at most one type of resource
     * @return True iff this set contains at most one type of resource
     */
    public boolean isSingleType() {
        return resources.size() <= 1;
    }

    /**
     * size returns the number of Resources in this ResourceSet
     * @return The number of Resources in this ResourceSet
     */
    @Override
    public int size() {
        int result = 0;
        for(ConcreteResource resource: ConcreteResource.values()) {
            result += getCount(resource);
        }
        return result;
    }
}