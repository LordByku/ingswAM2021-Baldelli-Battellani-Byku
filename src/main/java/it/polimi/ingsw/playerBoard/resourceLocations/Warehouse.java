package it.polimi.ingsw.playerBoard.resourceLocations;

import it.polimi.ingsw.leaderCards.LeaderCardDepot;
import it.polimi.ingsw.resources.InvalidChoiceSetException;
import it.polimi.ingsw.resources.resourceSets.ConcreteResourceSet;
import it.polimi.ingsw.resources.resourceSets.InvalidResourceSetException;

import java.util.ArrayList;

public class Warehouse implements ResourceLocation {
    private ArrayList<Depot> depots;

    public Warehouse() {
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
}
