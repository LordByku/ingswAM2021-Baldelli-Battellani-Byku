package it.polimi.ingsw.leaderCards;


import it.polimi.ingsw.devCards.ProductionDetails;
import it.polimi.ingsw.resources.resourceSets.InvalidResourceSetException;
import it.polimi.ingsw.resources.resourceSets.ObtainableResourceSet;
import it.polimi.ingsw.resources.resourceSets.SpendableResourceSet;

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
    ProductionLeaderCard(int points, LeaderCardRequirements requirements,SpendableResourceSet in, ObtainableResourceSet out) throws InvalidPointsValueException, InvalidRequirementsException, InvalidResourceSetException {

        if(points<=0){
            throw new InvalidPointsValueException();
        }
        if(requirements == null){
            throw new InvalidRequirementsException();
        }
        if(in == null){
            throw new InvalidResourceSetException();
        }
        if(out == null){
            throw new InvalidResourceSetException();
        }

        this.points=points;
        this.requirements= (LeaderCardRequirements) requirements.clone();
        this.in = (SpendableResourceSet) in.clone();
        this.out = (ObtainableResourceSet) out.clone();
    }

    /**
     * Check if the card is playable and plays it. Adds a new ProductionDetails to the ProductionArea
     */
    @Override
    public void play() {
        if(isPlayable()){
            active = true;
            board.addProduction(new ProductionDetails(in, out));
        }
    }
}
