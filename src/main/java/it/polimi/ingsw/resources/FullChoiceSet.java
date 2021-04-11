package it.polimi.ingsw.resources;

/**
 * FullChoiceSet is a ChoiceSet that allows to choose from all possible ConcreteResources
 */
public class FullChoiceSet extends ChoiceSet {
    /**
     * The constructor allows to build a ChoiceSet that contains all possible ConcreteResources
     */
    public FullChoiceSet() {
        super();
        try {
            for(ConcreteResource concreteResource: ConcreteResource.values()) {
                addChoice(concreteResource);
            }
        } catch (InvalidResourceException e) {}
    }
}
