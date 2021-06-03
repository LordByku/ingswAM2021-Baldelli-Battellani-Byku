package it.polimi.ingsw.model.resources;

import it.polimi.ingsw.view.cli.TextColour;
import it.polimi.ingsw.view.gui.images.resources.ResourceImageType;

/**
 * ConcreteResource represents all possible in-game resources
 */
public enum ConcreteResource implements Resource {
    COIN(TextColour.YELLOW, "COIN", ResourceImageType.COIN),
    STONE(TextColour.GREY, "STONE", ResourceImageType.STONE),
    SHIELD(TextColour.BLUE, "SHIELD", ResourceImageType.SHIELD),
    SERVANT(TextColour.PURPLE, "SERVANT", ResourceImageType.SERVANT);

    private final TextColour colour;
    private final String extendedCLIString;
    private final ResourceImageType resourceImageType;

    ConcreteResource(TextColour colour, String extendedCLIString, ResourceImageType resourceImageType) {
        this.colour = colour;
        this.extendedCLIString = extendedCLIString;
        this.resourceImageType = resourceImageType;
    }

    /**
     * getResource() returns the value of the ConcreteResource
     *
     * @return The ConcreteResource
     */
    @Override
    public ConcreteResource getResource() {
        return this;
    }

    /**
     * isConcrete() always returns True for ConcreteResources
     *
     * @return True
     */
    @Override
    public boolean isConcrete() {
        return true;
    }

    public TextColour getColour() {
        return colour;
    }

    @Override
    public String getCLIString() {
        return colour.escape() + "\u25cf" + TextColour.RESET;
    }

    @Override
    public ResourceImageType getResourceImageType() {
        return resourceImageType;
    }

    public String getExtendedCLIString() {
        return extendedCLIString;
    }
}