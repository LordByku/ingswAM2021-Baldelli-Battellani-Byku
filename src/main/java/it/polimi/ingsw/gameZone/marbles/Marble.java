package it.polimi.ingsw.gameZone.marbles;

import it.polimi.ingsw.playerBoard.Board;
import it.polimi.ingsw.playerBoard.InvalidBoardException;
import it.polimi.ingsw.resources.resourceSets.ObtainableResourceSet;

public abstract class Marble {
    private final MarbleColour colour;

    public Marble(MarbleColour colour) {
        this.colour = colour;
    }

    public abstract ObtainableResourceSet collect(Board board) throws InvalidBoardException;

    public MarbleColour getColour() {
        return colour;
    }
}
