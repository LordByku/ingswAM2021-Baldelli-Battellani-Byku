package it.polimi.ingsw.leaderCards;

public class ProductionLeaderCard extends LeaderCard{

    ProductionLeaderCard(int points, LeaderCardRequirements requirements, boolean active){
        this.points=points;
        //this.board = board;
        this.requirements=requirements;
        this.active=active;
    }

    @Override
    public void play() {
        active=true;
        //TBD - Board class needed
    }
}
