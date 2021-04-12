package it.polimi.ingsw.devCards;

import it.polimi.ingsw.playerBoard.Board;
import it.polimi.ingsw.playerBoard.Scoring;
import it.polimi.ingsw.resources.resourceSets.ConcreteResourceSet;

/**
 * DevCard represents development cards
 */

public class DevCard implements Scoring {
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
        //TODO
    }

    /**
     * getLevel() is a getter of the class
     * @return the level of the development card
     */
    public CardLevel getLevel() {
        return level;
    }

    /**
     * @return the colour of the development card
     */
    public CardColour getColour() {

        return colour;
    }
}
