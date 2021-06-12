package it.polimi.ingsw.editor.model.simplifiedModel.leaderCards.effects;


import it.polimi.ingsw.editor.model.resources.ConcreteResource;

public class DiscountEffect extends Effect {
    private ConcreteResource resource;
    private int discount;

    public DiscountEffect() {
        super(EffectType.discount);
        resource = ConcreteResource.COIN;
        discount = 1;
    }

    public ConcreteResource getResource() {
        return resource;
    }

    public void setResource(ConcreteResource resource) {
        this.resource = resource;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }
}
