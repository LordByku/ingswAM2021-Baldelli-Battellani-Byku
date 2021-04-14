package it.polimi.ingsw.playerBoard.faithTrack;

import it.polimi.ingsw.resources.resourceSets.InvalidQuantityException;

/**
 * VaticanReportSection represents the sections of the faith track where
 * happens the Vatican Report
 */
public class VaticanReportSection {

    private int popeSpace;
    private int points;
    private int firstSpace;
    private boolean isVisited=false;


    /**
     * @param firstSpace the position of the start of the Vatican Report section
     * @param popeSpace the position of the end of the Vatican Report section
     * @param points
     */
    public VaticanReportSection (int firstSpace, int popeSpace, int points) throws InvalidQuantityException {
       if(firstSpace<=0 || popeSpace<=0 || points<=0)
        this.firstSpace = firstSpace;
        this.popeSpace = popeSpace;
        this.points = points;
    }


    /**
     * @return true iff the attribute isVisited is true
     */
    public boolean isVisited(){
        if(isVisited==true)
            return true;
        return false;
    }

    /**
     * @param position The position on the faith track
     * @return true iff the position of the player is inside the section
     */
    public boolean isInsideSection(int position){
        if(position>=firstSpace && position<=popeSpace) {
            return true;
        }
        return false;
    }

    /**
     * @param position The position on the faith track
     * @return true iff the player's marker position is either on or past the Pope Space
     */
    public boolean reachedPopeSpace(int position){
        if(position>=popeSpace) {
            return true;
        }        return false;
    }
}
