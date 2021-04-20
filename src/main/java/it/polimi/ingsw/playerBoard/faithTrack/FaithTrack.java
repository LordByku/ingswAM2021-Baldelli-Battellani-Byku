package it.polimi.ingsw.playerBoard.faithTrack;

import it.polimi.ingsw.playerBoard.Board;
import it.polimi.ingsw.playerBoard.Scoring;
import it.polimi.ingsw.resources.resourceSets.InvalidQuantityException;

import java.util.ArrayList;

/**
 * FaithTrack represents the on-board faith track
 *
 */
public class FaithTrack implements Scoring {
    /**
     * markerPosition represents the position on the faith track
     */
    int markerPosition=0;
    /**
     * receivedPopeFavors represents a list of the pope favors card obtained
     * on the faith track
     */
    private ArrayList<PopeFavor> receivedPopeFavors = new ArrayList<>();

    /**
     * The constructor initializes receivedPopeFavors to an empty list
     */

    public FaithTrack() {
        VRSObserver.getInstance().addFaithTrack(this);
    }

    /**
     * receivePopeFavor inserts a 'pope favor' into the list
     * @param popeFavor the PopeFavor to add
     */
    public void addPopeFavor(PopeFavor popeFavor) throws InvalidPopeFavorException {
        if(popeFavor==null)
            throw new InvalidPopeFavorException();
        receivedPopeFavors.add(popeFavor);
    }

    /**
     * notifyEndOfTrack notifies the player's last turn
     * calls setLastTurn of Board
     *
     */
    private void notifyEndOfTrack(){
        Board.setLastTurn();
    }

    /**
     * getMarkerPosition returns the position of the player on the faith track
     * @return markerPosition of the object
     */
    public int getMarkerPosition(){
        return markerPosition;
    }

    public boolean containsPopeFavor(PopeFavor popeFavor){
        return receivedPopeFavors.contains(popeFavor);
    }
    /**
     * addFaithPoints adds "points" FaithPoints
     * @param points to add.
     */
    public void addFaithPoints(int points) throws InvalidQuantityException {
        if (points<=0) {
            throw new InvalidQuantityException();
        } else {
            markerPosition += points;
            if (markerPosition >= 24) {
                markerPosition=24;
                notifyEndOfTrack();
            }
            VRSObserver.getInstance().updateVRS();
        }
    }

    public void addFaithPoints() {
        addFaithPoints(1);
    }

    /**
     * getPoints represents sum of pope favor card's points
     * @return sum of pope favor card's points
     */
    @Override
    public int getPoints() {
        int points=0;
        for(PopeFavor popeFavor: receivedPopeFavors)
            points+=popeFavor.getPoints();
        return points;
    }
}
