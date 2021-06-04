package it.polimi.ingsw.editor.model.simplifiedModel.leaderCards.requirements;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.function.Function;

public enum RequirementType {
    resources((jsonArray) -> {
        ResourcesRequirements requirements = new ResourcesRequirements();
        requirements.getResources().parse(jsonArray);
        return requirements;
    }),
    cardSet((jsonArray) -> {
        Gson gson = new Gson();
        CardSetRequirements requirements = new CardSetRequirements();
        for(JsonElement jsonElement: jsonArray) {
            CardSet cardSet = gson.fromJson(jsonElement, CardSet.class);
            requirements.setCardSet(cardSet);
        }

        return requirements;
    });

    private final Function<JsonArray, Requirements> supplier;

    RequirementType(Function<JsonArray, Requirements> supplier) {
        this.supplier = supplier;
    }

    public Requirements buildRequirements(JsonArray jsonArray) {
        return supplier.apply(jsonArray);
    }
}
