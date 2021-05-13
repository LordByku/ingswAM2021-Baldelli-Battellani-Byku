package it.polimi.ingsw.model.resources;

import it.polimi.ingsw.view.cli.TextColour;

/**
 * ConcreteResource represents all possible in-game resources
 */
public enum ConcreteResource implements Resource {
    COIN(TextColour.YELLOW, "COIN"),
    STONE(TextColour.GREY, "STONE"),
    SHIELD(TextColour.BLUE, "SHIELD"),
    SERVANT(TextColour.PURPLE, "SERVANT");

    private final TextColour colour;
    private final String extendedCLIString;

    ConcreteResource(TextColour colour, String extendedCLIString) {
        this.colour = colour;
        this.extendedCLIString = extendedCLIString;
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

    public TextColour getColour() {
        return colour;
    }

    @Override
    public String getCLIString() {
        return colour.escape() + "\u25cf" + TextColour.RESET;
    }

    public String getExtendedCLIString() {
        return extendedCLIString;
    }
}