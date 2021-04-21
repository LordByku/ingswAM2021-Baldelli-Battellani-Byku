package it.polimi.ingsw.model.resources;

/**
 * ConcreteResource represents all possible in-game resources
 */
public enum ConcreteResource implements Resource {
    COIN,
    STONE,
    SHIELD,
    SERVANT;

    /**
     * getResource() returns the value of the ConcreteResource
     * @return The ConcreteResource
     */
    @Override
    public ConcreteResource getResource() {
        return this;
    }

    /**
     * isConcrete() always returns True for ConcreteResources
     * @return True
     */
    @Override
    public boolean isConcrete() {
        return true;
    }

    /**
     * cleanClone simply return this value of the enum
     * @return The value of this ConcreteResource
     */
    @Override
    public Resource cleanClone() {
        return this;
    }
}