package it.polimi.ingsw.resources;

public class ChoiceResource implements Resource {
    private final ChoiceSet choices;
    private ConcreteResource finalChoice;

    ChoiceResource(ChoiceSet choices) throws InvalidChoiceSetException {
        if(choices == null || choices.empty()) {
            throw new InvalidChoiceSetException();
        }
        this.choices = new ChoiceSet(choices);
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

    public boolean canChoose(ConcreteResource resource) {
        return choices.containsResource(resource);
    }

    public void makeChoice(ConcreteResource resource) throws InvalidResourceException {
        if(!canChoose(resource)) {
            throw new InvalidResourceException();
        } else {
            finalChoice = resource;
        }
    }

    @Override
    public ConcreteResource getResource() {
        return finalChoice;
    }

    @Override
    public boolean isConcrete() {
        return finalChoice != null;
    }
}