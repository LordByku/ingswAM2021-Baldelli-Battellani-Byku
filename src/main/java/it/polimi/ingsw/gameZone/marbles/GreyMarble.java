package it.polimi.ingsw.gameZone.marbles;

import it.polimi.ingsw.playerBoard.Board;
import it.polimi.ingsw.playerBoard.InvalidBoardException;
import it.polimi.ingsw.resources.ConcreteResource;
import it.polimi.ingsw.resources.resourceSets.ChoiceResourceSet;
import it.polimi.ingsw.resources.resourceSets.ObtainableResourceSet;

/**
 * GreyMarble is the subclass for grey Marbles
 */
public class GreyMarble extends Marble {
    /**
     * The constructor builds a Marble with MarbleColour GREY
     */
    public GreyMarble() {
        super(MarbleColour.GREY);
    }

    /**
     * collect returns an ObtainableResourceSet containing a STONE
     * @param board The Board of the player collecting this Marble (this is used for applying ConversionEffects)
     * @return An ObtainableResourceSet containing a STONE
     * @throws InvalidBoardException board is null
     */
    @Override
    public ObtainableResourceSet collect(Board board) throws InvalidBoardException {
        if(board == null) {
            throw new InvalidBoardException();
        }
        ChoiceResourceSet choiceResourceSet = new ChoiceResourceSet();
        choiceResourceSet.addResource(ConcreteResource.STONE);
        return new ObtainableResourceSet(choiceResourceSet);
    }
}
