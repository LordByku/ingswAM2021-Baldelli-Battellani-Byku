package it.polimi.ingsw.gameZone.marbles;

import it.polimi.ingsw.playerBoard.Board;
import it.polimi.ingsw.playerBoard.InvalidBoardException;
import it.polimi.ingsw.resources.ChoiceResource;
import it.polimi.ingsw.resources.ChoiceSet;
import it.polimi.ingsw.resources.resourceSets.ChoiceResourceSet;
import it.polimi.ingsw.resources.resourceSets.InvalidResourceSetException;
import it.polimi.ingsw.resources.resourceSets.ObtainableResourceSet;

public class WhiteMarble extends Marble {
    public WhiteMarble() {
        super(MarbleColour.WHITE);
    }

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
