package it.polimi.ingsw.resources;

import java.util.ArrayList;

public class ChoiceResourceSet implements ResourceSet {
    private final ArrayList<Resource> resources;

    ChoiceResourceSet() {
        resources = new ArrayList<>();
    }

    public boolean isConcrete() {
        for(Resource resource : resources)
            if(!resource.isConcrete())
                return false;
        return true;
    }

    public ConcreteResourceSet toConcrete() throws NotConcreteException {
        if(!isConcrete()) {
            throw new NotConcreteException();
        }
        ConcreteResourceSet concreteResourceSet = new ConcreteResourceSet();
        for(Resource resource: resources)
            concreteResourceSet.addResource(resource.getResource());
        return concreteResourceSet;
    }

    public void addResource(Resource resource) {
        resources.add(resource);
    }

    public ArrayList<Resource> getResources() {
        return new ArrayList<>(resources);
    }
}