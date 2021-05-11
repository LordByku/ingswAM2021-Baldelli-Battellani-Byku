package it.polimi.ingsw.model.playerBoard.faithTrack;

import it.polimi.ingsw.model.devCards.InvalidIdException;
import it.polimi.ingsw.model.resources.resourceSets.InvalidQuantityException;

import java.util.HashSet;

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
    private boolean isVisited = false;
    /**
     * id is the unique id for VaticanReportSections
     */
    private final int id;
    /**
     * usedIds is a container for the ids already used for VaticanReportSections
     */
    private static HashSet<Integer> usedIds = new HashSet<>();

    /**
     * @param firstSpace the position of the start of the Vatican Report section
     * @param popeSpace the position of the end of the Vatican Report section
     * @param points the points received by the section's pope card
     */
    public VaticanReportSection (int firstSpace, int popeSpace, int points, int id)
            throws InvalidQuantityException, InvalidVaticanReportSectionSquarePositions, InvalidIdException {
        if(firstSpace <= 0 || popeSpace <= 0 || points <= 0) {
           throw new InvalidQuantityException();
        }
        if (firstSpace > popeSpace) {
            throw new InvalidVaticanReportSectionSquarePositions();
        }
        if(id < 0 || usedIds.contains(id)) {
            throw new InvalidIdException();
        }
        this.firstSpace = firstSpace;
        this.popeSpace = popeSpace;
        this.points = points;
        this.id = id;
        usedIds.add(id);
    }

    /**
     * getPopeSpace is a getter of the attribute popeSpace
     * @return the attribute popeSpace of the object
     */
    public int getPopeSpace() {
        return popeSpace;
    }

    /**
     * getPoints is a getter of the attribute points
     * @return the attribute points of the object
     */
    public int getPoints() {
        return points;
    }

    /**
     * getFirstSpace() is a getter of the attribute firstSpace
     * @return the attribute firstSpace of the object
     */
    public int getFirstSpace() {
        return firstSpace;
    }

    /**
     * setVisitedTrue sets true the boolean attribute isVisited of the object
     */
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

    public PopeFavor getPopeFavor() {
        return new PopeFavor(points, id);
    }

    public int getId() {
        return id;
    }
}
