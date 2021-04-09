package it.polimi.ingsw.resources.resourceSets;

import it.polimi.ingsw.resources.NotConcreteException;
import it.polimi.ingsw.resources.Resource;

import java.util.ArrayList;

/**
 * ChoiceResourceSet is a container for generic resources, including ChoiceResources
 */
public class ChoiceResourceSet implements ResourceSet {
    /**
     * resources is where Resources are stored
     */
    private ArrayList<Resource> resources;

    /**
     * The constructor initializes resources to an empty ArrayList
     */
    public ChoiceResourceSet() {
        resources = new ArrayList<>();
    }

    /**
     * isConcrete checks if all resources contained in the set are concrete
     * @return True iff all resources are concrete
     */
    public boolean isConcrete() {
        for(Resource resource : resources)
            if(!resource.isConcrete())
                return false;
        return true;
    }

    /**
     * toConcrete() creates a new ConcreteResourceSet from the resources in this object
     * @return A ConcreteResourceSet that contains all resources in this object
     * @throws NotConcreteException There exist a non concrete resource,
     *         i.e. a ChoiceResource that has not been selected.
     */
    public ConcreteResourceSet toConcrete() throws NotConcreteException {
        if(!isConcrete()) {
            throw new NotConcreteException();
        }
        ConcreteResourceSet concreteResourceSet = new ConcreteResourceSet();
        for(Resource resource: resources)
            concreteResourceSet.addResource(resource.getResource());
        return concreteResourceSet;
    }

    /**
     * addResource inserts a new Resource
     * @param resource The Resource to add
     */
    public void addResource(Resource resource) {
        resources.add(resource);
    }

    /**
     * getResources returns a copy of the internal state of the object
     * @return An ArrayList of the Resources contained in this object
     */
    public ArrayList<Resource> getResources() {
        return new ArrayList<>(resources);
    }

    /**
     * This method allows to add the resources contained in a ResourceSet to this resource set
     * @param other The ResourceSet to add resources from
     * @throws InvalidResourceSetException other is null or other is not a ChoiceResourceSet
     */
    @Override
    public void union(ResourceSet other) throws InvalidResourceSetException {
        if(other == null) {
            throw new InvalidResourceSetException();
        }
        try {
            ChoiceResourceSet choiceOther = (ChoiceResourceSet) other;
            ArrayList<Resource> otherResources = choiceOther.getResources();
            for(Resource resource: otherResources) {
                addResource(resource);
            }
        } catch (ClassCastException e) {
            throw new InvalidResourceSetException();
        }
    }

    /**
     * clone returns a copy of the object
     * @return A copy of the object
     */
    @Override
    public ResourceSet clone() {
        try {
            ChoiceResourceSet cloneResourceSet = (ChoiceResourceSet) super.clone();
            cloneResourceSet.resources = (ArrayList<Resource>) resources.clone();
            for(int i = 0; i < resources.size(); i++) {
                cloneResourceSet.resources.set(i, resources.get(i).copy());
            }
            return cloneResourceSet;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }
}