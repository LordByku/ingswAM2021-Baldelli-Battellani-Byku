package it.polimi.ingsw.gameZone.marbles;

import it.polimi.ingsw.playerBoard.Board;
import it.polimi.ingsw.playerBoard.InvalidBoardException;
import it.polimi.ingsw.resources.resourceSets.ObtainableResourceSet;

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
     * @param board The Board of the player collecting this Marble (this is used for applying ConversionEffects)
     * @return An ObtainableResourceSet with at most one resource or faith point representing the resources
     * obtained by collecting this Marble
     * @throws InvalidBoardException board is null
     */
    public abstract ObtainableResourceSet collect(Board board) throws InvalidBoardException;

    /**
     * getColour returns the colour of this Marble
     * @return The MarbleColour of this Marble
     */
    public MarbleColour getColour() {
        return colour;
    }
}
