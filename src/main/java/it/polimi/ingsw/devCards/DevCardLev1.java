package it.polimi.ingsw.devCards;

import it.polimi.ingsw.playerBoard.Board;
import it.polimi.ingsw.resources.resourceSets.ConcreteResourceSet;

/**
 * DevCardLev1 is a subclass of DevCard and represents development cards at level 1
 */
public class DevCardLev1 extends DevCard{

    public DevCardLev1(ConcreteResourceSet reqResources, CardColour colour, ProductionDetails productionDetails) {
        super(reqResources, colour,productionDetails);
        this.level=CardLevel.I;
    }

    @Override
    public void buy(Board board) {
                //TODO

    }

}
