package it.polimi.ingsw.editor.model.simplifiedModel.leaderCards.effects;

import it.polimi.ingsw.editor.model.resources.ConcreteResource;

public class DepotEffect extends Effect {
    private ConcreteResource resource;
    private int slots;

    public DepotEffect() {
        super(EffectType.depot);
        resource = ConcreteResource.COIN;
        slots = 1;
    }

    public ConcreteResource getResource() {
        return resource;
    }

    public int getSlots() {
        return slots;
    }

    public void setResource(ConcreteResource resource) {
        this.resource = resource;
    }

    public void setSlots(int slots) {
        this.slots = slots;
    }
}
