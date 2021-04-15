package it.polimi.ingsw.gameZone.marbles;

import it.polimi.ingsw.playerBoard.Board;
import it.polimi.ingsw.playerBoard.InvalidBoardException;
import it.polimi.ingsw.resources.resourceSets.ChoiceResourceSet;
import it.polimi.ingsw.resources.resourceSets.ObtainableResourceSet;

/**
 * RedMarble is the subclass for red Marbles
 */
public class RedMarble extends Marble {
    /**
     * The constructor builds a Marble with MarbleColour RED
     */
    public RedMarble() {
        super(MarbleColour.RED);
    }

    /**
     * collect returns an ObtainableResourceSet containing a faith point
     * @param board The Board of the player collecting this Marble (this is used for applying ConversionEffects)
     * @return An ObtainableResourceSet containing a faith point
     * @throws InvalidBoardException board is null
     */
    @Override
    public ObtainableResourceSet collect(Board board) throws InvalidBoardException {
        if(board == null) {
            throw new InvalidBoardException();
        }
        ChoiceResourceSet choiceResourceSet = new ChoiceResourceSet();
        return new ObtainableResourceSet(choiceResourceSet, 1);
    }
}
