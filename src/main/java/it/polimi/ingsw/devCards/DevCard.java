package it.polimi.ingsw.devCards;

import it.polimi.ingsw.resources.ConcreteResourceSet;

/**
 * DevCard represents development cards
 */

public class DevCard {
    ConcreteResourceSet reqResources;
    CardColour colour;
    CardLevel level;
    ProductionDetails productionPower;

    DevCard(ConcreteResourceSet reqResources,CardColour colour){
        this.reqResources = reqResources;
        this.colour=colour;
       // this.level=level;
    }

    public void buy(Board board) {
        //TBD
    }

    public CardLevel getLevel() {
        return level;
    }

    public CardColour getColour() {

        return colour;
    }
}
