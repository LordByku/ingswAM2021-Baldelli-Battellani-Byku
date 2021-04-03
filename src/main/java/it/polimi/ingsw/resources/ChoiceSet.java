package it.polimi.ingsw.resources;

import java.util.Arrays;
import java.util.HashSet;

public class ChoiceSet {
    private final HashSet<ConcreteResource> set;

    ChoiceSet() {
        set = new HashSet<>();
    }

    ChoiceSet(boolean fullSet) {
        if(fullSet) {
            set = new HashSet<>(Arrays.asList(ConcreteResource.values()));
        } else {
            set = new HashSet<>();
        }
    }

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

    public void addChoice(ConcreteResource resource) {
        set.add(resource);
    }

    public boolean containsResource(ConcreteResource resource) {
        return set.contains(resource);
    }

    public boolean empty() {
        return set.isEmpty();
    }

    public int size() {
        return set.size();
    }
}