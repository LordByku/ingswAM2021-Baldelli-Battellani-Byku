package it.polimi.ingsw.model.resources.resourceSets;

import it.polimi.ingsw.model.resources.ChoiceResource;
import it.polimi.ingsw.model.resources.ConcreteResource;
import it.polimi.ingsw.model.resources.Resource;

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
     *
     * @param choiceResourceSet The ChoiceResourceSet to copy from
     * @throws InvalidResourceSetException choiceResourceSet is null
     */
    public SpendableResourceSet(ChoiceResourceSet choiceResourceSet) throws InvalidResourceSetException {
        super(choiceResourceSet);
    }

    /**
     * union adds to this SpendableResourceSet all the resources
     * contained in another SpendableResourceSet
     *
     * @param other The SpendableResourceSet to add resources from
     * @throws InvalidResourceSetException other is null, or this SpendableResourceSet
     *                                     has been converted but other has not, or vice versa
     */
    public SpendableResourceSet union(SpendableResourceSet other) throws InvalidResourceSetException {
        if (other == null) {
            throw new InvalidResourceSetException();
        }
        ChoiceResourceSet resourceSet = getResourceSet();
        resourceSet.union(other.getResourceSet());
        return new SpendableResourceSet(resourceSet);
    }

    /**
     * clone returns a copy of the object
     *
     * @return A copy of the object
     */
    @Override
    public SpendableResourceSet clone() {
        return (SpendableResourceSet) super.clone();
    }

    /**
     * match checks whether a given ConcreteResourceSet matches the ChoiceResourceSet in
     * this SpendableResourceSet
     * Note that the check is made assuming that all ChoiceResources in a
     * SpendableResourceSet have a FullChoiceSet to choose from
     *
     * @param concreteResourceSet The ConcreteResourceSet to check
     * @return True iff the ChoiceResourceSet in this SpendableResourceSet could be
     * converted to concreteResourceSet
     * @throws InvalidResourceSetException concreteResourceSet is null
     */
    public boolean exactMatch(ConcreteResourceSet concreteResourceSet) throws InvalidResourceSetException {
        return getResourceSet().size() == concreteResourceSet.size() && match(concreteResourceSet);
    }

    public boolean match(ConcreteResourceSet concreteResourceSet) {
        if (concreteResourceSet == null) {
            throw new InvalidResourceSetException();
        }

        ChoiceResourceSet toMatch = getResourceSet();

        ArrayList<ChoiceResource> choiceResources = toMatch.getChoiceResources();
        ConcreteResourceSet currentConcreteResourceSet = toMatch.getConcreteResources();

        int choices = 0;
        // ChoiceResources in SpendableResourceSet always have a FullChoiceSet
        for (Resource resource : choiceResources) {
            if (resource.isConcrete()) {
                currentConcreteResourceSet.addResource(resource.getResource());
            } else {
                choices++;
            }
        }

        for (ConcreteResource resource : ConcreteResource.values()) {
            if (concreteResourceSet.getCount(resource) > currentConcreteResourceSet.getCount(resource)) {
                choices -= concreteResourceSet.getCount(resource) - currentConcreteResourceSet.getCount(resource);
            }
        }

        return choices >= 0;
    }
}
