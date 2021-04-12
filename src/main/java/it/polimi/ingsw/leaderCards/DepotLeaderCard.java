package it.polimi.ingsw.leaderCards;

import it.polimi.ingsw.playerBoard.Board;
import it.polimi.ingsw.playerBoard.resourceLocations.InvalidDepotSizeException;
import it.polimi.ingsw.resources.ConcreteResource;

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

        try {
            board.addLeaderCardDepot(new LeaderCardDepot(this.type));
        } catch (InvalidDepotSizeException e) {}
    }

}
