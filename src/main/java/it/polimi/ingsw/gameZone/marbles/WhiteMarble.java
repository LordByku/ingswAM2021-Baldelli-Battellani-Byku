package it.polimi.ingsw.gameZone.marbles;

import it.polimi.ingsw.playerBoard.Board;
import it.polimi.ingsw.playerBoard.InvalidBoardException;
import it.polimi.ingsw.resources.ChoiceResource;
import it.polimi.ingsw.resources.ChoiceSet;
import it.polimi.ingsw.resources.resourceSets.ChoiceResourceSet;
import it.polimi.ingsw.resources.resourceSets.InvalidResourceSetException;
import it.polimi.ingsw.resources.resourceSets.ObtainableResourceSet;

/**
 * WhiteMarble is the subclass for white Marbles
 */
public class WhiteMarble extends Marble {
    /**
     * The constructor builds a Marble with MarbleColour WHITE
     */
    public WhiteMarble() {
        super(MarbleColour.WHITE);
    }

    /**
     * collect returns an empty ObtainableResourceSet if the given board has no ConversionEffects,
     * otherwise an ObtainableResourceSet containing a ChoiceResource where possible choices are
     * those of ConversionEffects
     * @param board The Board of the player collecting this Marble
     * @return An empty ObtainableResourceSet or an ObtainableResourceSet containing a ChoiceResource
     * @throws InvalidBoardException board is null
     */
    @Override
    public ObtainableResourceSet collect(Board board) throws InvalidBoardException {
        if(board == null) {
            throw new InvalidBoardException();
        }
        ChoiceResourceSet choiceResourceSet = new ChoiceResourceSet();

        ChoiceSet conversionChoices = board.getConversionEffects();

        if(!conversionChoices.empty()) {
            choiceResourceSet.addResource(new ChoiceResource(conversionChoices));
        }

        return new ObtainableResourceSet(choiceResourceSet);
    }
}
