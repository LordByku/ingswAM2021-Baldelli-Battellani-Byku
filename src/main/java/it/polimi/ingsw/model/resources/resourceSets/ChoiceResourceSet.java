package it.polimi.ingsw.model.resources.resourceSets;

import it.polimi.ingsw.model.resources.*;

import java.util.ArrayList;

/**
 * ChoiceResourceSet is a container for generic resources, including ChoiceResources
 */
public class ChoiceResourceSet implements ResourceSet {
    /**
     * choiceResources is where ChoiceResources are stored
     */
    private ArrayList<Resource> choiceResources;
    /**
     * concreteResources is where ConcreteResources are stored
     */
    private ConcreteResourceSet concreteResources;

    /**
     * The constructor initializes resources to an empty ArrayList
     */
    public ChoiceResourceSet() {
        choiceResources = new ArrayList<>();
        concreteResources = new ConcreteResourceSet();
    }

    /**
     * isConcrete checks if all resources contained in the set are concrete
     * @return True iff all resources are concrete
     */
    public boolean isConcrete() {
        for(Resource resource : choiceResources)
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
        ConcreteResourceSet concreteResourceSet = (ConcreteResourceSet) concreteResources.clone();
        for(Resource resource: choiceResources) {
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
        if(resource.isConcrete()) {
            concreteResources.addResource(resource.getResource());
        } else {
            choiceResources.add(resource);
        }
    }

    /**
     * getResources returns a copy of the internal state of the object
     * @return An ArrayList of the Resources contained in this object
     */
    public ArrayList<Resource> getChoiceResources() {
        return new ArrayList<>(choiceResources);
    }

    public ConcreteResourceSet getConcreteResources() {
        return (ConcreteResourceSet) concreteResources.clone();
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

        ArrayList<Resource> otherChoiceResources = other.getChoiceResources();
        ConcreteResourceSet otherConcreteResources = other.getConcreteResources();

        choiceResources.addAll(otherChoiceResources);
        concreteResources.union(otherConcreteResources);
    }

    public void union(ConcreteResourceSet other) throws InvalidResourceSetException {
        if(other == null) {
            throw new InvalidResourceSetException();
        }

        concreteResources.union(other);
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
            cloneResourceSet.choiceResources = getChoiceResources();
            cloneResourceSet.concreteResources = getConcreteResources();
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
        return choiceResources.size() + concreteResources.size();
    }

    /**
     * cleanClone returns a clone where ChoiceResources are cloned too
     * @return A new ChoiceResourceSet where all Resources are cleanCloned
     */
    public ChoiceResourceSet cleanClone() {
        ChoiceResourceSet cloneResourceSet = (ChoiceResourceSet) clone();

        cloneResourceSet.choiceResources = new ArrayList<>();

        for(Resource resource: choiceResources) {
            cloneResourceSet.choiceResources.add(resource.cleanClone());
        }

        return cloneResourceSet;
    }

    @Override
    public String getCLIString() {
        StringBuilder result = new StringBuilder(concreteResources.getCLIString());

        for(Resource resource: choiceResources) {
            result.append(resource.getCLIString()).append(" ");
        }

        return result.toString();
    }
}