package it.polimi.ingsw.editor.model.simplifiedModel.leaderCards.effects;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.function.Function;

public enum EffectType {
    discount((jsonObject) -> {
        Gson gson = new Gson();
        return gson.fromJson(jsonObject, DiscountEffect.class);
    }),
    depot((jsonObject) -> {
        Gson gson = new Gson();
        return gson.fromJson(jsonObject, DepotEffect.class);
    }),
    conversion((jsonObject) -> {
        Gson gson = new Gson();
        return gson.fromJson(jsonObject, ConversionEffect.class);
    }),
    production((jsonObject) -> {
        ProductionEffect effect = new ProductionEffect();
        effect.getProductionIn().parse(jsonObject.getAsJsonObject("productionDetails").getAsJsonArray("productionIn"));
        effect.getProductionOut().parse(jsonObject.getAsJsonObject("productionDetails").getAsJsonArray("productionOut"));
        return effect;
    });

    private final Function<JsonObject, Effect> supplier;

    EffectType(Function<JsonObject, Effect> supplier) {
        this.supplier = supplier;
    }

    public Effect getEffect(JsonObject jsonObject) {
        return supplier.apply(jsonObject);
    }
}
