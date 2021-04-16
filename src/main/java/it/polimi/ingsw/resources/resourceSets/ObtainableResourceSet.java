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
     * The default constructor calls the default constructor for TransactionResourceSet
     * and initializes faithPoints to 0
     */
    public ObtainableResourceSet() {
        super();
        faithPoints = 0;
    }

    /**
     * This constructor calls the constructor for TransactionResourceSet which uses a ChoiceResourceSet
     * @param choiceResourceSet The ChoiceResourceSet to copy from
     * @throws InvalidResourceSetException choiceResourceSet is null
     */
    public ObtainableResourceSet(ChoiceResourceSet choiceResourceSet) throws InvalidResourceSetException {
        super(choiceResourceSet);
        faithPoints = 0;
    }

    /**
     * This constructor calls the constructor for TransactionResourceSet which uses a ChoiceResourceSet
     * and initializes faithPoints to the given value
     * @param choiceResourceSet The ChoiceResourceSet to copy from
     * @param faithPoints The initial value for faithPoints
     * @throws InvalidResourceSetException choiceResourceSet is null
     * @throws InvalidQuantityException faithPoints is negative
     */
    public ObtainableResourceSet(ChoiceResourceSet choiceResourceSet, int faithPoints)
            throws InvalidResourceSetException, InvalidQuantityException {
        super(choiceResourceSet);
        if(faithPoints < 0) {
            throw new InvalidQuantityException();
        }
        this.faithPoints = faithPoints;
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
    public ObtainableResourceSet union(ObtainableResourceSet other) throws InvalidResourceSetException {
        if(other == null) {
            throw new InvalidResourceSetException();
        }
        ChoiceResourceSet resourceSet = getResourceSet();
        resourceSet.union(other.getResourceSet());
        return new ObtainableResourceSet(resourceSet, faithPoints + other.getFaithPoints());
    }

    /**
     * clone returns a copy of the object
     * @return A copy of the object
     */
    @Override
    public ObtainableResourceSet clone() {
        ObtainableResourceSet cloneORS = (ObtainableResourceSet) super.clone();
        cloneORS.faithPoints = faithPoints;
        return cloneORS;
    }
}
