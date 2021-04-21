package it.polimi.ingsw.model.leaderCards;


import it.polimi.ingsw.model.devCards.ProductionDetails;
import it.polimi.ingsw.model.resources.resourceSets.InvalidResourceSetException;
import it.polimi.ingsw.model.resources.resourceSets.ObtainableResourceSet;
import it.polimi.ingsw.model.resources.resourceSets.SpendableResourceSet;

/**
 * ProductionLeaderCard represents all LeaderCards with a production power.
 */
public class ProductionLeaderCard extends LeaderCard{

    /**
     * in is a set of resource needed to activate this production power.
     */
    private final SpendableResourceSet in;

    /**
     * out is a set of resource obtainable by this production power.
     */
    private final ObtainableResourceSet out;

    /**
     * The constructor sets the parameters of the leader cards.
     * @param points victory points given by the leader card.
     * @param requirements needed to play the leader card.
     * @param in is a set of resource needed to activate the production power.
     * @param out is a set of resource obtainable by the production power.
     * @throws InvalidPointsValueException points are less or equal to zero.
     * @throws InvalidRequirementsException requirements is null.
     * @throws InvalidResourceSetException in is null or out is null.
     */
    public ProductionLeaderCard(int points, LeaderCardRequirements requirements, SpendableResourceSet in, ObtainableResourceSet out)
            throws InvalidPointsValueException, InvalidRequirementsException, InvalidResourceSetException {
        super(points, requirements);
        if(in == null || out == null){
            throw new InvalidResourceSetException();
        }

        this.in = in.clone();
        this.out = out.clone();
    }

    /**
     * Check if the card is playable and plays it. Adds a new ProductionDetails to the ProductionArea
     */
    @Override
    public void play() {
        if(isPlayable()){
            active = true;
            board.getProductionArea().addLeaderCardProduction(new ProductionDetails(in, out));
        }
    }
}
