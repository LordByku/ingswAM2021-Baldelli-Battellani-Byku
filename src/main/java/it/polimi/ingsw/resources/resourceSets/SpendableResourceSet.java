package it.polimi.ingsw.resources.resourceSets;

import it.polimi.ingsw.resources.Resource;

import java.util.ArrayList;

/**
 * SpendableResourceSet is a TransactionResourceSet where the resource set
 * is going to be consumed by a player (board)
 * This TransactionResourceSet does not include faith points as they cannot be spent.
 */
public class SpendableResourceSet extends TransactionResourceSet {
    /**
     * The default constructor calls the default constructor for TransactionResourceSet
     */
    public SpendableResourceSet() {
        super();
    }

    /**
     * This constructor calls the constructor for TransactionResourceSet which uses a ChoiceResourceSet
     * @param choiceResourceSet The ChoiceResourceSet to copy from
     * @throws InvalidResourceSetException choiceResourceSet is null
     */
    public SpendableResourceSet(ChoiceResourceSet choiceResourceSet) throws InvalidResourceSetException {
        super(choiceResourceSet);
    }

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
        resources.union(other.getResourceSet());
    }

    /**
     * clone returns a copy of the object
     * @return A copy of the object
     */
    @Override
    public SpendableResourceSet clone() {
        return (SpendableResourceSet) super.clone();
    }
    
    public boolean match(ConcreteResourceSet concreteResourceSet) {
        if(getResourceSet().size() != concreteResourceSet.size()) {
            return false;
        }

        ArrayList<Resource> resources = getResourceSet().getResources();

        ConcreteResourceSet currentConcreteResourceSet = new ConcreteResourceSet();

        // ChoiceResources in SpendableResourceSet always have a FullChoiceSet
        for(Resource resource: resources) {
            if(resource.isConcrete()) {
                currentConcreteResourceSet.addResource(resource.getResource());
            }
        }

        return concreteResourceSet.contains(currentConcreteResourceSet);
    }
}
