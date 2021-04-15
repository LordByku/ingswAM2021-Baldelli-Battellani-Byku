package it.polimi.ingsw.gameZone.marbles;

import it.polimi.ingsw.playerBoard.Board;
import it.polimi.ingsw.playerBoard.InvalidBoardException;
import it.polimi.ingsw.resources.ConcreteResource;
import it.polimi.ingsw.resources.resourceSets.ChoiceResourceSet;
import it.polimi.ingsw.resources.resourceSets.ObtainableResourceSet;

/**
 * Yellow is the subclass for yellow Marbles
 */
public class YellowMarble extends Marble {
    /**
     * The constructor builds a Marble with MarbleColour YELLOW
     */
    public YellowMarble() {
        super(MarbleColour.YELLOW);
    }

    /**
     * collect returns an ObtainableResourceSet containing a COIN
     * @param board The Board of the player collecting this Marble (this is used for applying ConversionEffects)
     * @return An ObtainableResourceSet containing a COIN
     * @throws InvalidBoardException board is null
     */
    @Override
    public ObtainableResourceSet collect(Board board) throws InvalidBoardException {
        if(board == null) {
            throw new InvalidBoardException();
        }
        ChoiceResourceSet choiceResourceSet = new ChoiceResourceSet();
        choiceResourceSet.addResource(ConcreteResource.COIN);
        return new ObtainableResourceSet(choiceResourceSet);
    }
}
