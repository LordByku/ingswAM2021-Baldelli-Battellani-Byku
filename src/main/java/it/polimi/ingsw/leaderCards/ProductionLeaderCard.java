package it.polimi.ingsw.leaderCards;


import it.polimi.ingsw.devCards.ProductionDetails;
import it.polimi.ingsw.playerBoard.Board;
import it.polimi.ingsw.resources.resourceSets.ObtainableResourceSet;
import it.polimi.ingsw.resources.resourceSets.SpendableResourceSet;

import java.util.ArrayList;

/**
 * ProductionLeaderCard represents all LeaderCards with a production power.
 */
public class ProductionLeaderCard extends LeaderCard{

    /**
     * in is a set of resource needed to activate this production power.
     */
    private SpendableResourceSet in;

    /**
     * out is a set of resource obtainable by this production power.
     */
    private ObtainableResourceSet out;

    /**
     * The constructor sets the parameters of the leader cards and add a new ProductionDetails to the ProductionArea.
     * @param points victory points given by the leader card.
     * @param board the board of the current player.
     * @param requirements needed to play the leader card.
     * @param in is a set of resource needed to activate the production power.
     * @param out is a set of resource obtainable by the production power.
     * @param productions reference of the ArrayList of ProductionDetails of the ProductionArea.
     */
    ProductionLeaderCard(int points, Board board, LeaderCardRequirements requirements,SpendableResourceSet in, ObtainableResourceSet out, ArrayList<ProductionDetails> productions){
        this.points=points;
        this.board = board;
        this.requirements=requirements;
        this.in = in;
        this.out = out;

        productions.add(new ProductionDetails(in, out));
    }

}
