package it.polimi.ingsw.resources.resourceSets;

import it.polimi.ingsw.resources.NotConcreteException;

/**
 * TransactionResourceSet is a container for all the resources (including potentially
 * faith points) that are being moved from a resource location to another one.
 */
public abstract class TransactionResourceSet {
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
     * The constructor initializes resources to an empty ChoiceSet and converted to false
     */
    TransactionResourceSet() {
        resources = new ChoiceResourceSet();
        converted = false;
    }

    /**
     * toConcrete converts resources from a ChoiceResourceSet to a ConcreteResourceSet
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
        return resources.clone();
    }

    /**
     * isConverted checks whether resources is a ConcreteResourceSet
     * @return true iff resources has already been converted to a ConcreteResourceSet
     */
    public boolean isConverted() {
        return converted;
    }
}
