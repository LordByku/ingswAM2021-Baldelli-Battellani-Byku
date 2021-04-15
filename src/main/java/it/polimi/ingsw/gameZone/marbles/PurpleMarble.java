package it.polimi.ingsw.gameZone.marbles;

import it.polimi.ingsw.playerBoard.Board;
import it.polimi.ingsw.playerBoard.InvalidBoardException;
import it.polimi.ingsw.resources.ConcreteResource;
import it.polimi.ingsw.resources.resourceSets.ChoiceResourceSet;
import it.polimi.ingsw.resources.resourceSets.ObtainableResourceSet;

/**
 * PurpleMarble is the subclass for purple Marbles
 */
public class PurpleMarble extends Marble {
    /**
     * The constructor builds a Marble with MarbleColour PURPLE
     */
    public PurpleMarble() {
        super(MarbleColour.PURPLE);
    }

    /**
     * collect returns an ObtainableResourceSet containing a SERVANT
     * @param board The Board of the player collecting this Marble (this is used for applying ConversionEffects)
     * @return An ObtainableResourceSet containing a SERVANT
     * @throws InvalidBoardException board is null
     */
    @Override
    public ObtainableResourceSet collect(Board board) throws InvalidBoardException {
        if(board == null) {
            throw new InvalidBoardException();
        }
        ChoiceResourceSet choiceResourceSet = new ChoiceResourceSet();
        choiceResourceSet.addResource(ConcreteResource.SERVANT);
        return new ObtainableResourceSet(choiceResourceSet);
    }
}
