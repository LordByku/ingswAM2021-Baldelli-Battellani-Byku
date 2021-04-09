package it.polimi.ingsw.resources;

/**
 * ChoiceResource represents a resource that can be spent or obtained,
 * but the player has the choice to select such resource
 */
public class ChoiceResource implements Resource {
    /**
     * choices is the set of valid choices (valid ConcreteResources to choose from)
     */
    private final ChoiceSet choices;
    /**
     * finalChoice is the resource chosen for this object
     */
    private ConcreteResource finalChoice;

    /**
     * The constructor creates an instance of a ChoiceResource given a ChoiceSet
     * Note that if the ChoiceSet has size 1 (only one choice is allowed),
     * than finalChoice is initialized to that choice
     * @param choices The ChoiceSet of valid ConcreteResources
     * @throws InvalidChoiceSetException choices is null or an empty set
     */
    public ChoiceResource(ChoiceSet choices) throws InvalidChoiceSetException {
        if(choices == null || choices.empty()) {
            throw new InvalidChoiceSetException();
        }
        this.choices = choices.clone();
        if(choices.size() == 1) {
            for(ConcreteResource resource: ConcreteResource.values()) {
                if(choices.containsResource(resource)) {
                    finalChoice = resource;
                }
            }
        } else {
            finalChoice = null;
        }
    }

    /**
     * canChoose returns whether a given resource can be chosen
     * @param resource The resource to check
     * @return True iff resource is contained in choices
     */
    public boolean canChoose(ConcreteResource resource) {
        return choices.containsResource(resource);
    }

    /**
     * makeChoice is the method that allows to select a (valid) resource
     * @param resource The resource to choose
     * @throws InvalidResourceException resource is not a valid choice
     */
    public void makeChoice(ConcreteResource resource) throws InvalidResourceException {
        if(!canChoose(resource)) {
            throw new InvalidResourceException();
        } else {
            finalChoice = resource;
        }
    }

    /**
     * getResource returns the chosen resource
     * Note that this can be null if no choice has been made yet
     * @return The chosen resource for this object
     */
    @Override
    public ConcreteResource getResource() {
        return finalChoice;
    }

    /**
     * isConcrete returns true when a choice has been made
     * @return True iff a valid resource has been selected
     */
    @Override
    public boolean isConcrete() {
        return finalChoice != null;
    }

    /**
     * copy returns a copy of the object
     * @return A copy of the object
     */
    @Override
    public Resource copy() {
        try {
            ChoiceResource copyResource = new ChoiceResource(choices);
            copyResource.finalChoice = finalChoice;
            return copyResource;
        } catch (InvalidChoiceSetException e) {
            return null;
        }
    }
}