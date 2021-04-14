package it.polimi.ingsw.devCards;

import it.polimi.ingsw.playerBoard.Board;
import it.polimi.ingsw.resources.resourceSets.ConcreteResourceSet;

/**
 * DevCardLev3 is a subclass of DevCard and represents development cards at level 3
 */
public class DevCardLev3 extends DevCard{

    DevCardLev3 (ConcreteResourceSet reqResources, CardColour colour){
        super(reqResources, colour);
        this.level=CardLevel.III;
    }

    @Override
    public void buy(Board board) {
        super.buy(board);
    }
}
