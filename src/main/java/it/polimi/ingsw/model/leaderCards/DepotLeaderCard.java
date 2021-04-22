package it.polimi.ingsw.model.leaderCards;

import it.polimi.ingsw.model.resources.ConcreteResource;
import it.polimi.ingsw.model.resources.InvalidResourceException;

/**
 * DepotLeaderCard represents all LeaderCards with a depot power.
 */

public class DepotLeaderCard extends LeaderCard{
    /**
     * the type of ConcreteResource that can be stored in the depot.
     */
    private final ConcreteResource type;

    /**
     * The constructor sets the parameters of the leader cards.
     * @param points victory points given by the leader card.
     * @param requirements needed to play the leader card.
     * @param type of resource that can be stored into the depot.
     * @throws InvalidPointsValueException points are less or equal to zero.
     * @throws InvalidRequirementsException requirements is null.
     * @throws InvalidResourceException type is null.
     */
    public DepotLeaderCard(int points, LeaderCardRequirements requirements, ConcreteResource type)
            throws InvalidPointsValueException, InvalidRequirementsException, InvalidResourceException{
        super(points, requirements);
        if(type == null){
            throw new InvalidResourceException();
        }

        this.type=type;
    }

    /**
     * Check if the card is playable and plays it. Adds a new LeaderCardDepot to the Warehouse.
     */
    @Override
    public void play() {
        if(isPlayable()) {
            active = true;
            board.getWarehouse().addLeaderCardDepot(new LeaderCardDepot(this.type));
        }
    }

}