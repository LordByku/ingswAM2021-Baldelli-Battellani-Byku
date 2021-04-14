package it.polimi.ingsw.gameZone.marbles;

import it.polimi.ingsw.playerBoard.Board;
import it.polimi.ingsw.playerBoard.InvalidBoardException;
import it.polimi.ingsw.resources.resourceSets.ChoiceResourceSet;
import it.polimi.ingsw.resources.resourceSets.ObtainableResourceSet;

public class RedMarble extends Marble {
    public RedMarble() {
        super(MarbleColour.RED);
    }

    @Override
    public ObtainableResourceSet collect(Board board) throws InvalidBoardException {
        if(board == null) {
            throw new InvalidBoardException();
        }
        ChoiceResourceSet choiceResourceSet = new ChoiceResourceSet();
        return new ObtainableResourceSet(choiceResourceSet, 1);
    }
}
