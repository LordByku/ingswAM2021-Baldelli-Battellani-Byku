package it.polimi.ingsw.editor.model.simplifiedModel.leaderCards.requirements;

import com.google.gson.JsonArray;
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

    @Override
    public JsonArray serialize() {
        return resources.serialize();
    }
}
