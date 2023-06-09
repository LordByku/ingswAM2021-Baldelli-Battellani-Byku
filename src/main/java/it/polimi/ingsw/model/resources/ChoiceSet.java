package it.polimi.ingsw.model.resources;

import it.polimi.ingsw.view.cli.CLIPrintable;

import java.util.HashSet;

/**
 * ChoiceSet represents a set of resources to choose from
 */
public class ChoiceSet implements Cloneable, CLIPrintable {
    /**
     * set contains the resources that can be chosen
     */
    private HashSet<ConcreteResource> set;

    /**
     * The constructor initializes set to an empty set
     */
    public ChoiceSet() {
        set = new HashSet<>();
    }

    /**
     * addChoice inserts a new resource into the set
     *
     * @param resource The ConcreteResource to add
     * @throws InvalidResourceException resource is null
     */
    public void addChoice(ConcreteResource resource) throws InvalidResourceException {
        if (resource == null) {
            throw new InvalidResourceException();
        }
        set.add(resource);
    }

    /**
     * containsResource checks whether a given resource is in the set
     *
     * @param resource The ConcreteResource to check
     * @return True iff resource is contained in the set
     * @throws InvalidResourceException resource is null
     */
    public boolean containsResource(ConcreteResource resource) throws InvalidResourceException {
        if (resource == null) {
            throw new InvalidResourceException();
        }
        return set.contains(resource);
    }

    /**
     * empty() checks whether the set is empty
     *
     * @return True iff the set is empty
     */
    public boolean empty() {
        return set.isEmpty();
    }

    /**
     * size() returns the number of ConcreteResources contained in the set
     *
     * @return The size of the set
     */
    public int size() {
        return set.size();
    }

    /**
     * clone returns a copy of the object
     *
     * @return A copy of the object
     */
    @Override
    public ChoiceSet clone() {
        try {
            ChoiceSet setClone = (ChoiceSet) super.clone();
            setClone.set = (HashSet<ConcreteResource>) set.clone();
            return setClone;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String getCLIString() {
        StringBuilder result = new StringBuilder("[");

        int count = 0;

        for (ConcreteResource concreteResource : ConcreteResource.values()) {
            if (containsResource(concreteResource)) {
                if (count++ > 0) {
                    result.append(", ");
                }
                result.append(concreteResource.getExtendedCLIString()).append(" ").append(concreteResource.getCLIString());
            }
        }

        result.append("]");

        return result.toString();
    }
}