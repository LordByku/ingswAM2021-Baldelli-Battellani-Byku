package it.polimi.ingsw.resources;

public enum ConcreteResource implements Resource {
    COIN,
    STONE,
    SHIELD,
    SERVANT;

    @Override
    public ConcreteResource getResource() {
        return this;
    }

    @Override
    public boolean isConcrete() {
        return true;
    }
}