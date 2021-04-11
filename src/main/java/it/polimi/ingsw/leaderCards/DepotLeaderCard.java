package it.polimi.ingsw.leaderCards;

import it.polimi.ingsw.playerBoard.Board;
import it.polimi.ingsw.playerBoard.resourceLocations.Depot;
import it.polimi.ingsw.playerBoard.resourceLocations.Warehouse;
import it.polimi.ingsw.resources.ConcreteResource;

import java.util.ArrayList;

/**
 * DepotLeaderCard represents all LeaderCards with a depot power.
 */

public class DepotLeaderCard extends LeaderCard{
    /**
     * the type of ConcreteResource that can be stored in the depot.
     */
    private final ConcreteResource type;


    DepotLeaderCard(int points, Board board, LeaderCardRequirements requirements, ConcreteResource type){
        this.points=points;
        this.board = board;
        this.requirements=requirements;
        this.type=type;

        //TODO: create LeaderCardDepot into the Warehouse
        // decide if make LeaderCardDepot subclass of Depot or create an abstract class.
        // Create JAVADOC for constructor
    }

}
