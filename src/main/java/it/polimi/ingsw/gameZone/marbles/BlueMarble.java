package it.polimi.ingsw.gameZone.marbles;

import it.polimi.ingsw.playerBoard.Board;
import it.polimi.ingsw.playerBoard.InvalidBoardException;
import it.polimi.ingsw.resources.ConcreteResource;
import it.polimi.ingsw.resources.resourceSets.ChoiceResourceSet;
import it.polimi.ingsw.resources.resourceSets.ObtainableResourceSet;

/**
 * BlueMarble is the subclass for blue Marbles
 */
public class BlueMarble extends Marble {
    /**
     * The constructor builds a Marble with MarbleColour BLUE
     */
    public BlueMarble() {
        super(MarbleColour.BLUE);
    }

    /**
     * collect returns an ObtainableResourceSet containing a SHIELD
     * @param board The Board of the player collecting this Marble (this is used for applying ConversionEffects)
     * @return An ObtainableResourceSet containing a SHIELD
     * @throws InvalidBoardException board is null
     */
    @Override
    public ObtainableResourceSet collect(Board board) throws InvalidBoardException {
        if(board == null) {
            throw new InvalidBoardException();
        }
        ChoiceResourceSet choiceResourceSet = new ChoiceResourceSet();
        choiceResourceSet.addResource(ConcreteResource.SHIELD);
        return new ObtainableResourceSet(choiceResourceSet);
    }
}
