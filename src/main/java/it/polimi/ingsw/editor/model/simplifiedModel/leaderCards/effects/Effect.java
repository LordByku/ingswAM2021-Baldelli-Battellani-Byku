package it.polimi.ingsw.editor.model.simplifiedModel.leaderCards.effects;

import com.google.gson.JsonObject;

public abstract class Effect {
    private final EffectType effectType;

    protected Effect(EffectType effectType) {
        this.effectType = effectType;
    }

    public static Effect build(EffectType effectType) {
        switch (effectType) {
            case discount: {
                return new DiscountEffect();
            }
            case depot: {
                return new DepotEffect();
            }
            case conversion: {
                return new ConversionEffect();
            }
            case production: {
                return new ProductionEffect();
            }
            default: {
                return null;
            }
        }
    }

    public EffectType getEffectType() {
        return effectType;
    }
}
