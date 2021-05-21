package it.polimi.ingsw.model.resources.resourceSets;

/**
 * TransactionResourceSet is a container for all the resources (including potentially
 * faith points) that are being moved from a resource location to another one.
 */
public abstract class TransactionResourceSet implements Cloneable {

    public int size() {
        return resources.size();
    }

    /**
     * resources is the ResourceSet that contains regular resources (not faith points)
     * Initially this ResourceSet is a ChoiceResourceSet, then, after all choices for that
     * resource set have been made, it is converted into a ConcreteResourceSet
     */
    ChoiceResourceSet resources;

    /**
     * The default constructor initializes resources to an empty ChoiceSet and converted to false
     */
    public TransactionResourceSet() {
        resources = new ChoiceResourceSet();
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
        resources = choiceResourceSet.cleanClone();
    }

    /**
     * getResourceSet returns a copy of resources
     * @return A copy of resources
     */
    public ChoiceResourceSet getResourceSet() {
        return resources.cleanClone();
    }

    /**
     * clone returns a copy of the object
     * @return A copy of the object
     */
    public Object clone(){
        try {
            TransactionResourceSet cloneResourceSet = (TransactionResourceSet) super.clone();
            cloneResourceSet.resources = getResourceSet();
            return cloneResourceSet;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getCLIString() {
        return resources.getCLIString();
    }
}
