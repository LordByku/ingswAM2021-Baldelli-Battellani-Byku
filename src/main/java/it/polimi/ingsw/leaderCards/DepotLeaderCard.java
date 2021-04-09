package it.polimi.ingsw.leaderCards;

import it.polimi.ingsw.resources.ConcreteResource;
import it.polimi.ingsw.resources.resourceSets.ConcreteResourceSet;

public class DepotLeaderCard extends LeaderCard{
    private final ConcreteResource type;
    private final ConcreteResourceSet resources;

    DepotLeaderCard(int points, LeaderCardRequirements requirements, boolean active, ConcreteResource type, ConcreteResourceSet resources){
        this.points=points;
        //this.board = board;
        this.requirements=requirements;
        this.active=active;
        this.type=type;
        this.resources=resources;
    }

    @Override
    public void play() {
        active=true;
        //TBD - Board class needed
    }
}
