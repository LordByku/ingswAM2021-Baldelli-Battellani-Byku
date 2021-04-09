package it.polimi.ingsw.resources.resourceSets;

/**
 * SpendableResourceSet is a TransactionResourceSet where the resource set
 * is going to be consumed by a player (board)
 * This TransactionResourceSet does not include faith points as they cannot be spent.
 */
public class SpendableResourceSet extends TransactionResourceSet {
    /**
     * union adds to this SpendableResourceSet all the resources
     * contained in another SpendableResourceSet
     * @param other The SpendableResourceSet to add resources from
     * @throws InvalidResourceSetException other is null, or this SpendableResourceSet
     * has been converted but other has not, or vice versa
     */
    public void union(SpendableResourceSet other) throws InvalidResourceSetException {
        if(other == null) {
            throw new InvalidResourceSetException();
        }
        ResourceSet otherResources = other.getResourceSet();
        resources.union(otherResources);
    }
}
