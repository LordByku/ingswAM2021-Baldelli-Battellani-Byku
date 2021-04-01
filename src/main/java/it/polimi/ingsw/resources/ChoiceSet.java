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