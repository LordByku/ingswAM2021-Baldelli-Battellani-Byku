package it.polimi.ingsw.editor.model.simplifiedModel.leaderCards.requirements;

import it.polimi.ingsw.editor.model.resources.ConcreteResourceSet;

public class ResourcesRequirements extends Requirements {
    private final ConcreteResourceSet resources;

    public ResourcesRequirements() {
        super(RequirementType.resources);
        resources = new ConcreteResourceSet();
    }

    public ConcreteResourceSet getResources() {
        return resources;
    }
}
