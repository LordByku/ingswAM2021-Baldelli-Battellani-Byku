package it.polimi.ingsw.model.leaderCards;

import it.polimi.ingsw.model.playerBoard.resourceLocations.Depot;
import it.polimi.ingsw.model.resources.ConcreteResource;
import it.polimi.ingsw.model.resources.InvalidResourceException;

/**
 * A special depot created by the activation of a DepotLeaderCard, it can store just 2 resource of a defined type.
 */
public class LeaderCardDepot extends Depot {

    /**
     * The type of resource storable into the depot.
     */
    private final ConcreteResource type;

    /**
     * Constructor sets the type of resources that can be stored and calls the Depot constructor with 2 slots.
     *
     * @param type of resources that can be stored.
     * @throws InvalidResourceException type is null.
     */
    public LeaderCardDepot(ConcreteResource type, int depotSize) throws InvalidResourceException {
        super(depotSize);
        if (type == null) {
            throw new InvalidResourceException();
        }
        this.type = type;
    }

    /**
     * getResourceType returns the type of resource that can be stored in this Depot
     *
     * @return The type of ConcreteResource that can be stored in this Depot
     */
    @Override
    public ConcreteResource getResourceType() {
        return type;
    }
}
