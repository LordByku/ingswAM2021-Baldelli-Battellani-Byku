package it.polimi.ingsw.editor.model.simplifiedModel.leaderCards.requirements;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public abstract class Requirements {
    private final RequirementType requirementType;

    protected Requirements(RequirementType requirementType) {
        this.requirementType = requirementType;
    }

    public static Requirements build(RequirementType requirementType) {
        switch (requirementType) {
            case cardSet: {
                return new CardSetRequirements();
            }
            case resources: {
                return new ResourcesRequirements();
            }
            default: {
                return null;
            }
        }
    }

    public RequirementType getRequirementType() {
        return requirementType;
    }

    public abstract JsonElement serialize();
}
