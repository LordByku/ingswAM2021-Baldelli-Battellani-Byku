package it.polimi.ingsw.model.gameZone.marbles;

import it.polimi.ingsw.model.resources.ChoiceSet;
import it.polimi.ingsw.model.resources.InvalidChoiceSetException;
import it.polimi.ingsw.model.resources.resourceSets.ObtainableResourceSet;
import it.polimi.ingsw.view.cli.TextColour;

/**
 * Marble is the class for marbles in the resources market
 */
public abstract class Marble {
    /**
     * colour is the colour of this marble
     */
    private final MarbleColour colour;

    /**
     * The constructor initializes colour to a given colour
     * @param colour The colour of this marble
     */
    public Marble(MarbleColour colour) throws InvalidMarbleColourException {
        if(colour == null) {
            throw new InvalidMarbleColourException();
        }
        this.colour = colour;
    }

    /**
     * collect returns an ObtainableResourceSet representing the resources obtained by collecting this Marble
     * @param choiceSet The ChoiceSet of possible conversions for this operation
     * @return An ObtainableResourceSet with at most one resource or faith point representing the resources
     * obtained by collecting this Marble
     * @throws InvalidChoiceSetException choiceSet is null
     */
    public abstract ObtainableResourceSet collect(ChoiceSet choiceSet) throws InvalidChoiceSetException;

    /**
     * getColour returns the colour of this Marble
     * @return The MarbleColour of this Marble
     */
    public MarbleColour getColour() {
        return colour;
    }

    @Override
    public String toString() {
        return getColour().getTextColour().escape() + "\u25c9" + TextColour.RESET;
    }
}
