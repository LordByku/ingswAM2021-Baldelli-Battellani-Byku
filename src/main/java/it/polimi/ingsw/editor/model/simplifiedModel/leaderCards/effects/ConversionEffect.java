package it.polimi.ingsw.editor.model.simplifiedModel.leaderCards.effects;

import it.polimi.ingsw.editor.model.resources.ConcreteResource;

public class ConversionEffect extends Effect {
    private ConcreteResource resource;

    public ConversionEffect() {
        super(EffectType.conversion);
        resource = ConcreteResource.COIN;
    }

    public ConcreteResource getResource() {
        return resource;
    }

    public void setResource(ConcreteResource resource) {
        this.resource = resource;
    }
}
