package it.polimi.ingsw.playerBoard.resourceLocations;

import java.util.ArrayList;

public class Warehouse implements ResourceLocation {
    private ArrayList<Depot> depots;

    public Warehouse() {
        depots = new ArrayList<>();
        depots.add(new Depot(1));
        depots.add(new Depot(2));
        depots.add(new Depot(3));
    }
}
