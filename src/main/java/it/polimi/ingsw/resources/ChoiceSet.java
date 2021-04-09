package it.polimi.ingsw.resources;

import java.awt.*;
import java.util.Arrays;
import java.util.HashSet;

/**
 * ChoiceSet represents a set of resources to choose from
 */
public class ChoiceSet implements Cloneable {
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
     * @param resource The ConcreteResource to add
     */
    public void addChoice(ConcreteResource resource) {
        set.add(resource);
    }

    /**
     * containsResource checks whether a given resource is in the set
     * @param resource The ConcreteResource to check
     * @return True iff resource is contained in the set
     */
    public boolean containsResource(ConcreteResource resource) {
        return set.contains(resource);
    }

    /**
     * empty() checks whether the set is empty
     * @return True iff the set is empty
     */
    public boolean empty() {
        return set.isEmpty();
    }

    /**
     * size() returns the number of ConcreteResources contained in the set
     * @return The size of the set
     */
    public int size() {
        return set.size();
    }

    /**
     * clone returns a copy of the object
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
}