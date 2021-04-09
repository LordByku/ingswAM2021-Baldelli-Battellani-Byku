package it.polimi.ingsw.resources.resourceSets;

/**
 * ObtainableResourceSet is a TransactionResourceSet where the resource set
 * is going to be given to a player (board)
 * This TransactionResourceSet includes faith points as they can be obtained
 */
public class ObtainableResourceSet extends TransactionResourceSet {
    /**
     * faithPoints is the amount of faith points that the player will receive
     */
    private int faithPoints;

    /**
     * The constructor calls the default constructor for TransactionResourceSet
     * and initializes faithPoints to 0
     */
    public ObtainableResourceSet() {
        super();
        faithPoints = 0;
    }

    /**
     * getFaithPoints returns the faith points in this set
     * @return The faith points in this set
     */
    public int getFaithPoints() {
        return faithPoints;
    }

    /**
     * union adds to this ObtainableResourceSet all the resources and faith points
     * contained in another ObtainableResourceSet
     * @param other The ObtainableResourceSet to add resources from
     * @throws InvalidResourceSetException other is null, or this ObtainableResourceSet
     * has been converted but other has not, or vice versa
     */
    public void union(ObtainableResourceSet other) throws InvalidResourceSetException {
        if(other == null) {
            throw new InvalidResourceSetException();
        }
        ResourceSet otherResources = other.getResourceSet();
        resources.union(otherResources);
        faithPoints += other.getFaithPoints();
    }
}
