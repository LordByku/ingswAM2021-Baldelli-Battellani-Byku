package it.polimi.ingsw.leaderCards;

import it.polimi.ingsw.resources.ConcreteResource;

public class DiscountLeaderCard extends LeaderCard{
    private final ConcreteResource type;

    DiscountLeaderCard(int points, LeaderCardRequirements requirements, boolean active, ConcreteResource type){
        this.points=points;
        //this.board = board;
        this.requirements=requirements;
        this.active=active;
        this.type=type;
    }

    @Override
    public void play() {
        active=true;
        //TBD - Board class needed
    }
}
