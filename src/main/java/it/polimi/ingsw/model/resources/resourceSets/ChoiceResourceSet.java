package it.polimi.ingsw.model.resources.resourceSets;

import it.polimi.ingsw.model.resources.*;

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
     * i.e. a ChoiceResource that has not been selected.
     */
    public ConcreteResourceSet toConcrete() throws NotConcreteException {
        if(!isConcrete()) {
            throw new NotConcreteException();
        }
        ConcreteResourceSet concreteResourceSet = new ConcreteResourceSet();
        for(Resource resource: resources) {
            concreteResourceSet.addResource(resource.getResource());
        }
        return concreteResourceSet;
    }

    /**
     * addResource inserts a new Resource
     * @param resource The Resource to add
     * @throws InvalidResourceException resource is null
     */
    public void addResource(Resource resource) throws InvalidResourceException {
        if(resource == null) {
            throw new InvalidResourceException();
        }
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
     * union adds to this ChoiceResourceSet all the resources contained in another
     * ChoiceResourceSet
     * @param other The ChoiceResourceSet to add resources from
     * @throws InvalidResourceSetException other is null
     */
    public void union(ChoiceResourceSet other) throws InvalidResourceSetException {
        if(other == null) {
            throw new InvalidResourceSetException();
        }

        ArrayList<Resource> otherResources = other.getResources();
        for(Resource resource: otherResources) {
            addResource(resource);
        }
    }

    /**
     * clone returns a copy of the object
     * Note that ChoiceResources in resources are not copied
     * @return A copy of the object
     */
    @Override
    public ResourceSet clone() {
        try {
            ChoiceResourceSet cloneResourceSet = (ChoiceResourceSet) super.clone();
            cloneResourceSet.resources = getResources();
            return cloneResourceSet;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * size returns the number of Resources in this ResourceSet
     * @return The number of Resources in this ResourceSet
     */
    @Override
    public int size() {
        return resources.size();
    }

    /**
     * cleanClone returns a clone where ChoiceResources are cloned too
     * @return A new ChoiceResourceSet where all Resources are cleanCloned
     */
    public ChoiceResourceSet cleanClone() {
        ChoiceResourceSet cloneResourceSet = (ChoiceResourceSet) clone();

        cloneResourceSet.resources = new ArrayList<>();

        for(Resource resource: resources) {
            cloneResourceSet.resources.add(resource.cleanClone());
        }

        return cloneResourceSet;
    }
}