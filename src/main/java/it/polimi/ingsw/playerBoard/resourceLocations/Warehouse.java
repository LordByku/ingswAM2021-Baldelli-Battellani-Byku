package it.polimi.ingsw.playerBoard.resourceLocations;

import it.polimi.ingsw.leaderCards.LeaderCardDepot;
import it.polimi.ingsw.resources.InvalidChoiceSetException;
import it.polimi.ingsw.resources.resourceSets.ConcreteResourceSet;
import it.polimi.ingsw.resources.resourceSets.InvalidResourceSetException;
import it.polimi.ingsw.resources.resourceSets.NotSingleTypeException;

import java.util.ArrayList;

public class Warehouse implements ResourceLocation {
    private ArrayList<Depot> depots;
    private final int initialDepots;

    public Warehouse() {
        initialDepots = 3;
        depots = new ArrayList<>();
        try {
            depots.add(new Depot(1));
            depots.add(new Depot(2));
            depots.add(new Depot(3));
        } catch (InvalidDepotSizeException e) {}
    }

    public void addLeaderCardDepot(LeaderCardDepot leaderCardDepot) {
        depots.add(leaderCardDepot);
    }

    @Override
    public boolean containsResources(ConcreteResourceSet concreteResourceSet) throws InvalidResourceSetException {
        ConcreteResourceSet resources = getResources();
        return resources.contains(concreteResourceSet);
    }

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

    public boolean canAdd(int depotIndex, ConcreteResourceSet concreteResourceSet)
            throws InvalidDepotIndexException, InvalidResourceSetException {
        if(depotIndex < 0 || depotIndex >= depots.size()) {
            throw new InvalidDepotIndexException();
        }
        if(concreteResourceSet == null) {
            throw new InvalidResourceSetException();
        }

        if(depotIndex < initialDepots) {
            for(int i = 0; i < initialDepots; ++i) {
                try {
                    if(i != depotIndex && depots.get(i).getResourceType() == concreteResourceSet.getResourceType()) {
                        return false;
                    }
                } catch (NotSingleTypeException e) {
                    return false;
                }
            }
        }

        return depots.get(depotIndex).canAdd(concreteResourceSet);
    }

    public void addResources(int depotIndex, ConcreteResourceSet concreteResourceSet)
            throws InvalidDepotIndexException, InvalidResourceSetException, InvalidResourceLocationOperationException {
        if(!canAdd(depotIndex, concreteResourceSet)) {
            throw new InvalidResourceLocationOperationException();
        } else {
            depots.get(depotIndex).addResources(concreteResourceSet);
        }
    }

    public void removeResource(int depotIndex, ConcreteResourceSet concreteResourceSet)
            throws InvalidDepotIndexException, InvalidResourceSetException, InvalidResourceLocationOperationException {

        if(depotIndex < 0 || depotIndex >= depots.size()) {
            throw new InvalidDepotIndexException();
        }
        depots.get(depotIndex).removeResources(concreteResourceSet);
    }
}
