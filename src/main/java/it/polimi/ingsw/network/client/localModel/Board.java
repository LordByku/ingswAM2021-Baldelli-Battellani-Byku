package it.polimi.ingsw.network.client.localModel;

import it.polimi.ingsw.model.resources.resourceSets.ConcreteResourceSet;

import java.util.ArrayList;

public class Board {
    private FaithTrack faithTrack;
    private ArrayList<ConcreteResourceSet> warehouse;
    private ConcreteResourceSet strongbox;
    private ArrayList<ArrayList<Integer>> devCards;
    private ArrayList<Integer> playedLeaderCards;
    private ArrayList<Integer> handLeaderCards;

    public ArrayList<Integer> getHandLeaderCards() {
        return handLeaderCards;
    }
}
