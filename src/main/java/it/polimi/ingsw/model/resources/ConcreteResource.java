package it.polimi.ingsw.model.resources;

import it.polimi.ingsw.view.cli.TextColour;

/**
 * ConcreteResource represents all possible in-game resources
 */
public enum ConcreteResource implements Resource {
    COIN(TextColour.ANSI_YELLOW),
    STONE(TextColour.ANSI_WHITE),
    SHIELD(TextColour.ANSI_BLUE),
    SERVANT(TextColour.ANSI_PURPLE);

    private final TextColour colour;

    ConcreteResource(TextColour colour) {
        this.colour = colour;
    }

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

    @Override
    public String toString() {
        return colour.escape() + "\u2b24" + TextColour.RESET;
    }
}