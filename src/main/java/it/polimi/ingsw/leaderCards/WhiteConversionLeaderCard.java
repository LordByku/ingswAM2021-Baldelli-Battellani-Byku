package it.polimi.ingsw.leaderCards;

import it.polimi.ingsw.playerBoard.Board;
import it.polimi.ingsw.resources.ConcreteResource;

import java.util.HashSet;


/**
 * WhiteConversionLeaderCard represents all LeaderCards with a conversion power.
 */
public class WhiteConversionLeaderCard extends LeaderCard{

    /**
     * The type of ConcreteResource to convert the white marble into.
     */
    private final ConcreteResource type;

    /**
     * The constructor sets the parameters of the leader cards and add a new ConversionEffect to the ConversionEffectArea.
     * @param points victory points given by the leader card.
     * @param board the board of the current player.
     * @param requirements needed to play the leader card.
     * @param type of resource to convert the white marble into.
     */
    WhiteConversionLeaderCard(int points, Board board, LeaderCardRequirements requirements, ConcreteResource type){
        this.points=points;
        this.board = board;
        this.requirements=requirements;
        this.type=type;

        board.addConversionEffect(new ConversionEffect(this.type));
    }

}
