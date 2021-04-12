package it.polimi.ingsw.leaderCards;

import it.polimi.ingsw.playerBoard.resourceLocations.Depot;
import it.polimi.ingsw.playerBoard.resourceLocations.InvalidDepotSizeException;
import it.polimi.ingsw.resources.ConcreteResource;
import it.polimi.ingsw.resources.InvalidResourceException;
import it.polimi.ingsw.resources.resourceSets.ConcreteResourceSet;

/**
 * A special depot created by the activation of a DepotLeaderCard, it can store just 2 resource of a defined type.
 */
public class LeaderCardDepot extends Depot {

    /**
     * The type of resource storable into the depot.
     */
    private ConcreteResource type;

    /**
     * Dimension of the depot
     */
    private final int slots = 2;

    /**
     * Set of Resources stored into the depot.
     */
    private ConcreteResourceSet resources;

    /**
     * Constructor sets the type of resources that can be stored and calls the Depot constructor with 2 slots.
     * @param type of resources that can be stored.
     * @throws InvalidResourceException type is null.
     */
    LeaderCardDepot(ConcreteResource type) throws InvalidDepotSizeException , InvalidResourceException {
        super(2);
        if(type == null){
            throw new InvalidResourceException();
        }
        this.type = type;
    }
}
