package it.polimi.ingsw.model.playerBoard.resourceLocations;

import it.polimi.ingsw.model.leaderCards.InvalidLeaderCardDepotException;
import it.polimi.ingsw.model.leaderCards.LeaderCardDepot;
import it.polimi.ingsw.model.resources.ConcreteResource;
import it.polimi.ingsw.model.resources.resourceSets.ConcreteResourceSet;
import it.polimi.ingsw.model.resources.resourceSets.InvalidResourceSetException;
import it.polimi.ingsw.parsing.BoardParser;

import java.util.ArrayList;

/**
 * Warehouse is the class for the player board's warehouse
 */
public class Warehouse implements ResourceLocation {
    /**
     * depots is the container for all the Depots in this Warehouse
     */
    private final ArrayList<Depot> depots;
    /**
     * initialDepots is the amount of default depots
     */
    private final int initialDepots;

    public Warehouse() {
        ArrayList<Integer> depotSizes = BoardParser.getInstance().getDepotSizes();

        initialDepots = depotSizes.size();
        depots = new ArrayList<>();
        for (Integer depotSize : depotSizes) {
            depots.add(new Depot(depotSize));
        }
    }

    /**
     * addLeaderCardDepot adds a LeaderCardDepot to this Warehouse
     *
     * @param leaderCardDepot The LeaderCardDepot to add
     * @throws InvalidLeaderCardDepotException leaderCardDepot is null
     */
    public void addLeaderCardDepot(LeaderCardDepot leaderCardDepot) throws InvalidLeaderCardDepotException {
        if (leaderCardDepot == null) {
            throw new InvalidLeaderCardDepotException();
        }
        depots.add(leaderCardDepot);
    }

    /**
     * containsResources checks whether a given ConcreteResourceSet
     * is contained in this Warehouse
     *
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
     *
     * @return A ConcreteResourceSet representing the resources in this Warehouse
     */
    @Override
    public ConcreteResourceSet getResources() {
        ConcreteResourceSet result = new ConcreteResourceSet();
        for (Depot depot : depots) {
            result.union(depot.getResources());
        }
        return result;
    }

    /**
     * canAdd checks whether a given ConcreteResourceSet can be added to a given Depot
     *
     * @param depotIndex          The index of the Depot to check
     * @param concreteResourceSet The ConcreteResourceSet to check
     * @return True iff concreteResourceSet can be added to the given Depot
     * @throws InvalidDepotIndexException  depotIndex is outside the range of depots
     * @throws InvalidResourceSetException concreteResourceSet is null
     */
    public boolean canAdd(int depotIndex, ConcreteResourceSet concreteResourceSet)
            throws InvalidDepotIndexException, InvalidResourceSetException {
        if (depotIndex < 0 || depotIndex >= depots.size()) {
            throw new InvalidDepotIndexException();
        }
        if (concreteResourceSet == null) {
            throw new InvalidResourceSetException();
        }

        // concreteResourceSet has more than one type of resources: return false
        if (concreteResourceSet.hasMultipleTypes()) {
            return false;
        }

        ConcreteResource resourceSetType = concreteResourceSet.getResourceType();

        // concreteResourceSet has no resources: return true
        if (resourceSetType == null) {
            return true;
        }

        // concreteResourceSet is single type
        if (depotIndex < initialDepots) {
            for (int i = 0; i < initialDepots; ++i) {
                // There is another depot with the same resource
                // type as concreteResourceSet: return false
                if (i != depotIndex && depots.get(i).getResourceType() == resourceSetType) {
                    return false;
                }
            }
        }

        // Return true if the given depot can add concreteResourceSet
        return depots.get(depotIndex).canAdd(concreteResourceSet);
    }

    /**
     * addResources adds a given ConcreteResourceSet to a given Depot
     *
     * @param depotIndex          The index of the Depot to add resources into
     * @param concreteResourceSet The ConcreteResourceSet to add
     * @throws InvalidDepotIndexException                depotIndex is outside the range of depots
     * @throws InvalidResourceSetException               concreteResourceSet is null
     * @throws InvalidResourceLocationOperationException concreteResourceSet cannot
     *                                                   be added to the given Depot
     */
    public void addResources(int depotIndex, ConcreteResourceSet concreteResourceSet)
            throws InvalidDepotIndexException, InvalidResourceSetException, InvalidResourceLocationOperationException {
        if (!canAdd(depotIndex, concreteResourceSet)) {
            throw new InvalidResourceLocationOperationException();
        }
        depots.get(depotIndex).addResources(concreteResourceSet);
    }

    /**
     * removeResources removes a given ConcreteResourceSet from a given Depot
     *
     * @param depotIndex          The index of the Depot to remove resources from
     * @param concreteResourceSet The concreteResourceSet to remove
     * @throws InvalidDepotIndexException                depotIndex is outside the range of depots
     * @throws InvalidResourceSetException               concreteResourceSet is null
     * @throws InvalidResourceLocationOperationException concreteResourceSet cannot
     *                                                   be removed from the given Depot
     */
    public void removeResources(int depotIndex, ConcreteResourceSet concreteResourceSet)
            throws InvalidDepotIndexException, InvalidResourceSetException, InvalidResourceLocationOperationException {
        if (depotIndex < 0 || depotIndex >= depots.size()) {
            throw new InvalidDepotIndexException();
        }
        depots.get(depotIndex).removeResources(concreteResourceSet);
    }

    /**
     * canSwap checks whether the resources in two given depots can be swapped
     *
     * @param depotIndexA The index of the first depot
     * @param depotIndexB The index of the second depot
     * @return True iff the content of the two depots can be swapped
     * @throws InvalidDepotIndexException depotIndexA or depotIndexB is outside the range of depots
     */
    public boolean canSwap(int depotIndexA, int depotIndexB) throws InvalidDepotIndexException {
        if (depotIndexA < 0 || depotIndexA >= depots.size() ||
                depotIndexB < 0 || depotIndexB >= depots.size()) {
            throw new InvalidDepotIndexException();
        }

        // do not allow swap of a depot with itself
        if (depotIndexA == depotIndexB) {
            return false;
        }

        ConcreteResourceSet resourceSetA = depots.get(depotIndexA).getResources();
        ConcreteResourceSet resourceSetB = depots.get(depotIndexB).getResources();

        depots.get(depotIndexA).removeResources(resourceSetA);
        depots.get(depotIndexB).removeResources(resourceSetB);

        boolean result = canAdd(depotIndexA, resourceSetB) && canAdd(depotIndexB, resourceSetA);

        depots.get(depotIndexA).addResources(resourceSetA);
        depots.get(depotIndexB).addResources(resourceSetB);

        return result;
    }

    /**
     * swapResources swaps the content of two given depots
     *
     * @param depotIndexA The index of the first depot
     * @param depotIndexB The index of the second depot
     * @throws InvalidDepotIndexException                depotIndexA or depotIndexB is outside the range of depots
     * @throws InvalidResourceLocationOperationException The content of the two depots
     *                                                   cannot be swapped
     */
    public void swapResources(int depotIndexA, int depotIndexB) throws InvalidDepotIndexException, InvalidResourceLocationOperationException {
        if (!canSwap(depotIndexA, depotIndexB)) {
            throw new InvalidResourceLocationOperationException();
        }

        ConcreteResourceSet resourceSetA = depots.get(depotIndexA).getResources();
        ConcreteResourceSet resourceSetB = depots.get(depotIndexB).getResources();

        depots.get(depotIndexA).removeResources(resourceSetA);
        depots.get(depotIndexB).removeResources(resourceSetB);

        depots.get(depotIndexA).addResources(resourceSetB);
        depots.get(depotIndexB).addResources(resourceSetA);
    }

    /**
     * getDepotResources returns the resources contained in a given depot
     *
     * @param depotIndex The depot to get resources from
     * @return The ConcreteResourceSet contained in the given depot
     * @throws InvalidDepotIndexException depotIndex is outside the range of depots
     */
    public ConcreteResourceSet getDepotResources(int depotIndex) throws InvalidDepotIndexException {
        if (depotIndex < 0 || depotIndex >= depots.size()) {
            throw new InvalidDepotIndexException();
        }

        return depots.get(depotIndex).getResources();
    }

    /**
     * numberOfDepots returns the number of depots currently in this warehouse
     *
     * @return The size of depots
     */
    public int numberOfDepots() {
        return depots.size();
    }
}

