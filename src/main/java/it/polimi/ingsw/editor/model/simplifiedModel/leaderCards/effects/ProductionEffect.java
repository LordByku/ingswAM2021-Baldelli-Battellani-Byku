package it.polimi.ingsw.editor.model.simplifiedModel.leaderCards.effects;

import it.polimi.ingsw.editor.model.resources.ObtainableResourceSet;
import it.polimi.ingsw.editor.model.resources.SpendableResourceSet;

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
}
