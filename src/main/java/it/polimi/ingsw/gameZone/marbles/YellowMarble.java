package it.polimi.ingsw.gameZone.marbles;

import it.polimi.ingsw.playerBoard.Board;
import it.polimi.ingsw.playerBoard.InvalidBoardException;
import it.polimi.ingsw.resources.ConcreteResource;
import it.polimi.ingsw.resources.resourceSets.ChoiceResourceSet;
import it.polimi.ingsw.resources.resourceSets.ObtainableResourceSet;

public class YellowMarble extends Marble {
    public YellowMarble() {
        super(MarbleColour.YELLOW);
    }

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
