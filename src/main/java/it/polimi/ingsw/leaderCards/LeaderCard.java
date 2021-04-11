package it.polimi.ingsw.leaderCards;

import it.polimi.ingsw.playerBoard.Scoring;

public abstract class LeaderCard implements Scoring {
    int points;
    //Board board;
    LeaderCardRequirements requirements;
    boolean  active;

    /*public boolean isPlayable(){
        return requirements.isSatisfied(this.board);
    }*/

    public boolean isActive(){
        return active;
    }

    public abstract void play();

    public void discard(){} //TBD - FaithTrack class needed

}
