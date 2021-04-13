package it.polimi.ingsw.playerBoard.resourceLocations;

import it.polimi.ingsw.leaderCards.InvalidLeaderCardDepotException;
import it.polimi.ingsw.leaderCards.LeaderCardDepot;
import it.polimi.ingsw.resources.ConcreteResource;
import it.polimi.ingsw.resources.resourceSets.ConcreteResourceSet;
import it.polimi.ingsw.resources.resourceSets.InvalidResourceSetException;
import it.polimi.ingsw.resources.resourceSets.NotSingleTypeException;

import java.util.ArrayList;

/**
 * Warehouse is the class for the player board's warehouse
 */
public class Warehouse implements ResourceLocation {
    /**
     * depots is the container for all the Depots in this Warehouse
     */
    private ArrayList<Depot> depots;
    /**
     * initialDepots is the amount of default depots
     */
    private final int initialDepots;

    /**
     * The constructor initializes depots to contain three empty Depots of
     * capacity respectively 1, 2, 3 and initializes initialDepots to 3
     */
    public Warehouse() {
        initialDepots = 3;
        depots = new ArrayList<>();
        try {
            depots.add(new Depot(1));
            depots.add(new Depot(2));
            depots.add(new Depot(3));
        } catch (InvalidDepotSizeException e) {}
    }

    /**
     * addLeaderCardDepot adds a LeaderCardDepot to this Warehouse
     * @param leaderCardDepot The LeaderCardDepot to add
     * @throws InvalidLeaderCardDepotException leaderCardDepot is null
     */
    public void addLeaderCardDepot(LeaderCardDepot leaderCardDepot) throws InvalidLeaderCardDepotException {
        if(leaderCardDepot == null) {
            throw new InvalidLeaderCardDepotException();
        }
        depots.add(leaderCardDepot);
    }

    /**
     * containsResources checks whether a given ConcreteResourceSet
     * is contained in this Warehouse
     * @param concreteResourceSet The ConcreteResourceSet to check
     * @return True iff this Warehouse contains concreteResourceSet
     * @throws InvalidResourceSetException concreteResourceSet is null
     */
    @Override
    public boolean containsResources(ConcreteResourceSet concreteResourceSet) throws InvalidResourceSetException {
        ConcreteResourceSet resources = getResources();
        return resources.contains(concreteResourceSet);
    }

    /**
     * getResources returns a copy of the resources contained in this Warehouse
     * @return A ConcreteResourceSet representing the resources in this Warehouse
     */
    @Override
    public ConcreteResourceSet getResources() {
        ConcreteResourceSet result = new ConcreteResourceSet();
        for(Depot depot: depots) {
            try {
                result.union(depot.getResources());
            } catch (InvalidResourceSetException e) {}
        }
        return result;
    }

    /**
     * canAdd checks whether a given ConcreteResourceSet can be added to a given Depot
     * @param depotIndex The index of the Depot to check
     * @param concreteResourceSet The ConcreteResourceSet to check
     * @return True iff concreteResourceSet can be added to the given Depot
     * @throws InvalidDepotIndexException depotIndex is outside the range of depots
     * @throws InvalidResourceSetException concreteResourceSet is null
     */
    public boolean canAdd(int depotIndex, ConcreteResourceSet concreteResourceSet)
            throws InvalidDepotIndexException, InvalidResourceSetException {
        if(depotIndex < 0 || depotIndex >= depots.size()) {
            throw new InvalidDepotIndexException();
        }
        if(concreteResourceSet == null) {
            throw new InvalidResourceSetException();
        }

        ConcreteResource resourceSetType;
        try {
            resourceSetType = concreteResourceSet.getResourceType();
        } catch (NotSingleTypeException e) {
            return false;
        }

        if(resourceSetType == null) {
            return true;
        }

        if(depotIndex < initialDepots) {
            for(int i = 0; i < initialDepots; ++i) {
                if(i != depotIndex && depots.get(i).getResourceType() == resourceSetType) {
                    return false;
                }
            }
        }

        return depots.get(depotIndex).canAdd(concreteResourceSet);
    }

    /**
     * addResources adds a given ConcreteResourceSet to a given Depot
     * @param depotIndex The index of the Depot to add resources into
     * @param concreteResourceSet The ConcreteResourceSet to add
     * @throws InvalidDepotIndexException depotIndex is outside the range of depots
     * @throws InvalidResourceSetException concreteResourceSet is null
     * @throws InvalidResourceLocationOperationException concreteResourceSet cannot
     * be added to the given Depot
     */
    public void addResources(int depotIndex, ConcreteResourceSet concreteResourceSet)
            throws InvalidDepotIndexException, InvalidResourceSetException, InvalidResourceLocationOperationException {
        if(!canAdd(depotIndex, concreteResourceSet)) {
            throw new InvalidResourceLocationOperationException();
        } else {
            depots.get(depotIndex).addResources(concreteResourceSet);
        }
    }

    /**
     * removeResources removes a given ConcreteResourceSet from a given Depot
     * @param depotIndex The index of the Depot to remove resources from
     * @param concreteResourceSet The concreteResourceSet to remove
     * @throws InvalidDepotIndexException depotIndex is outside the range of depots
     * @throws InvalidResourceSetException concreteResourceSet is null
     * @throws InvalidResourceLocationOperationException concreteResourceSet cannot
     * be removed from the given Depot
     */
    public void removeResources(int depotIndex, ConcreteResourceSet concreteResourceSet)
            throws InvalidDepotIndexException, InvalidResourceSetException, InvalidResourceLocationOperationException {

        if(depotIndex < 0 || depotIndex >= depots.size()) {
            throw new InvalidDepotIndexException();
        }
        depots.get(depotIndex).removeResources(concreteResourceSet);
    }
}
