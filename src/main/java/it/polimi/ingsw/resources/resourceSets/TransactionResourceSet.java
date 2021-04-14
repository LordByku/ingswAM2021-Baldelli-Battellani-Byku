package it.polimi.ingsw.resources.resourceSets;

import it.polimi.ingsw.resources.NotConcreteException;

/**
 * TransactionResourceSet is a container for all the resources (including potentially
 * faith points) that are being moved from a resource location to another one.
 */
public abstract class TransactionResourceSet implements Cloneable{
    /**
     * resources is the ResourceSet that contains regular resources (not faith points)
     * Initially this ResourceSet is a ChoiceResourceSet, then, after all choices for that
     * resource set have been made, it is converted into a ConcreteResourceSet
     */
    ResourceSet resources;
    /**
     * converted is a flag that indicates if resources has already been
     * converted to a ConcreteResourceSet
     */
    boolean converted;

    /**
     * The default constructor initializes resources to an empty ChoiceSet and converted to false
     */
    public TransactionResourceSet() {
        resources = new ChoiceResourceSet();
        converted = false;
    }

    /**
     * This constructor initializes resources to a given ChoiceResourceSet
     * @param choiceResourceSet The ChoiceResourceSet to copy from
     * @throws InvalidResourceSetException choiceResourceSet is null
     */
    public TransactionResourceSet(ChoiceResourceSet choiceResourceSet) throws InvalidResourceSetException {
        if(choiceResourceSet == null) {
            throw new InvalidResourceSetException();
        }
        resources = choiceResourceSet.clone();
        converted = false;
    }

    /**
     * toConcrete converts resources to a ConcreteResourceSet
     * If resources is already a ConcreteResourceSet, it will not be modified
     * @throws NotConcreteException Not all choices have been made for the ChoiceResourceSet
     */
    public void toConcrete() throws NotConcreteException {
        if(!converted) {
            resources = ((ChoiceResourceSet) resources).toConcrete();
            converted = true;
        }
    }

    /**
     * getResourceSet returns a copy of resources
     * @return A copy of resources
     */
    public ResourceSet getResourceSet() {
        return (ResourceSet) resources.clone();
    }

    /**
     * isConverted checks whether resources is a ConcreteResourceSet
     * @return true iff resources has already been converted to a ConcreteResourceSet
     */
    public boolean isConverted() {
        return converted;
    }

    /**
     * clone returns a copy of the object
     * @return A copy of the object
     */
    public Object clone(){
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }
}
