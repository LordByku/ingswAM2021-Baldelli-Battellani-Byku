package it.polimi.ingsw.editor.model.simplifiedModel.leaderCards.effects;

import com.google.gson.JsonObject;
import it.polimi.ingsw.editor.model.Config;
import it.polimi.ingsw.editor.model.resources.ObtainableResourceSet;
import it.polimi.ingsw.editor.model.resources.SpendableResourceSet;
import it.polimi.ingsw.utility.JsonUtil;

public class ProductionEffect extends Effect {
    private SpendableResourceSet productionIn;
    private ObtainableResourceSet productionOut;

    public ProductionEffect() {
        super(EffectType.production);
        productionIn = new SpendableResourceSet();
        productionOut = new ObtainableResourceSet();
    }

    public SpendableResourceSet getProductionIn() {
        return productionIn;
    }

    public ObtainableResourceSet getProductionOut() {
        return productionOut;
    }

    @Override
    public JsonObject serialize() {
        JsonObject json = new JsonObject();

        json.add("effectType", JsonUtil.getInstance().serialize(getEffectType()));
        json.add("productionDetails", Config.writeProductionPower(productionIn, productionOut));

        return json;
    }
}
