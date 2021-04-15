package it.polimi.ingsw.devCards;

import it.polimi.ingsw.playerBoard.Board;
import it.polimi.ingsw.resources.resourceSets.ConcreteResourceSet;

/**
 * DevCardLev2 is a subclass of DevCard and represents development cards at level 2
 */
public class DevCardLev2 extends DevCard{

    public DevCardLev2(ConcreteResourceSet reqResources, CardColour colour){
        super(reqResources, colour);
        this.level=CardLevel.II;
    }

    @Override
    public void buy(Board board) {
        super.buy(board);
    }
}
