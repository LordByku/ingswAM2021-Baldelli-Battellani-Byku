package it.polimi.ingsw.editor.model.simplifiedModel.leaderCards.requirements;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.ingsw.model.devCards.CardColour;
import it.polimi.ingsw.model.devCards.CardLevel;

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
            JsonObject cardSetObject = jsonElement.getAsJsonObject();
            CardColour colour = gson.fromJson(cardSetObject.get("colour"), CardColour.class);
            CardLevel[] levels = gson.fromJson(cardSetObject.get("levelSet"), CardLevel[].class);
            int quantity = cardSetObject.get("quantity").getAsInt();

            CardSet cardSet = requirements.getCardSet(colour);
            for(CardLevel level: levels) {
                cardSet.toggle(level);
            }
            cardSet.setQuantity(quantity);
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
