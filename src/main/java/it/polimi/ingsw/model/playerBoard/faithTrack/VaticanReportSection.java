package it.polimi.ingsw.model.playerBoard.faithTrack;

import it.polimi.ingsw.model.resources.resourceSets.InvalidQuantityException;

/**
 * VaticanReportSection represents the sections of the faith track where
 * happens the Vatican Report
 */
public class VaticanReportSection {

    /**
     * popeSpace is the position of the start of the Vatican Report section
     */
    private final int firstSpace;
    /**
     * the position of the end of the Vatican Report section
     */
    private final int popeSpace;
    /**
     * the points received by the section's pope card
     */
    private final int points;
    /**
     * isVisited is a flag for a section where occurred already a Vatican Report
     */
    private boolean isVisited=false;


    /**
     * @param firstSpace the position of the start of the Vatican Report section
     * @param popeSpace the position of the end of the Vatican Report section
     * @param points the points received by the section's pope card
     */
    public VaticanReportSection (int firstSpace, int popeSpace, int points) throws InvalidQuantityException,InvalidVaticanReportSectionSquarePositions {
       if(firstSpace<=0 || popeSpace<=0 || points<=0)
           throw new InvalidQuantityException();
       else
        {
            if (firstSpace > popeSpace)
                throw new InvalidVaticanReportSectionSquarePositions();
            else {
                this.firstSpace = firstSpace;
                this.popeSpace = popeSpace;
                this.points = points;
            }
        }
    }


    public int getPopeSpace() {
        return popeSpace;
    }

    public int getPoints() {
        return points;
    }

    public int getFirstSpace() {
        return firstSpace;
    }

    public void setVisitedTrue() {
        this.isVisited = true;
    }

    /**
     * @return true iff the attribute isVisited is true
     */
    public boolean isVisited(){
        return isVisited;
    }

    /**
     * @param position The position on the faith track
     * @return true iff the position of the player is inside the section
     */
    public boolean isInsideSection(int position){
        return position >= firstSpace;
    }

    /**
     * @param position The position on the faith track
     * @return true iff the player's marker position is either on or past the Pope Space
     */
    public boolean reachedPopeSpace(int position){
        return position >= popeSpace;
    }
}
