package it.polimi.ingsw.leaderCards;

public abstract class LeaderCard {
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
