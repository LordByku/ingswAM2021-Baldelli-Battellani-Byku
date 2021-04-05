package it.polimi.ingsw.resources;

import java.util.Arrays;
import java.util.HashSet;

/**
 * ChoiceSet represents a set of resources to choose from
 */
public class ChoiceSet {
    /**
     * set contains the resources that can be chosen
     */
    private final HashSet<ConcreteResource> set;

    /**
     * The default constructor initializes set to an empty set
     */
    public ChoiceSet() {
        set = new HashSet<>();
    }

    /**
     * This constructor allows to build a ChoiceSet that contains all possible ConcreteResources
     * @param fullSet If True than set is initialized to contain all possible ConcreteResources
     */
    public ChoiceSet(boolean fullSet) {
        if(fullSet) {
            set = new HashSet<>(Arrays.asList(ConcreteResource.values()));
        } else {
            set = new HashSet<>();
        }
    }

    /**
     * This constructor builds a copy of another ChoiceSet
     * @param other The ChoiceSet to copy from
     */
    ChoiceSet(ChoiceSet other) {
        if(other == null) {
            set = null;
        } else {
            set = new HashSet<>();
            for(ConcreteResource resource: ConcreteResource.values()) {
                if(other.containsResource(resource)) {
                    set.add(resource);
                }
            }
        }
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
}