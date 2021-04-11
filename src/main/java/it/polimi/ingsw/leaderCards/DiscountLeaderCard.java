package it.polimi.ingsw.leaderCards;

import it.polimi.ingsw.playerBoard.Board;
import it.polimi.ingsw.resources.ConcreteResource;

import java.util.ArrayList;

/**
 * DiscountLeaderCard represents all LeaderCards with a discount power.
 */

public class DiscountLeaderCard extends LeaderCard{

    /**
     * The type of ConcreteResource discounted.
     */
    private final ConcreteResource type;

    /**
     * The constructor sets the parameters of the leader cards and add a new discountEffect to the DiscountArea.
     * @param points victory points given by the leader card.
     * @param board the board of the current player.
     * @param requirements needed to play the leader card.
     * @param type of resource to discount.
     * @param discountEffects reference of the ArrayList of discountEffects of the DiscountArea.
     */
    DiscountLeaderCard(int points, Board board, LeaderCardRequirements requirements, ConcreteResource type, ArrayList<DiscountEffect> discountEffects){
        this.points=points;
        this.board = board;
        this.requirements=requirements;
        this.type=type;

        discountEffects.add(new DiscountEffect(this.type));
    }
}
