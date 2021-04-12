package it.polimi.ingsw.playerBoard.faithTrack;

import it.polimi.ingsw.playerBoard.Board;
import it.polimi.ingsw.playerBoard.Scoring;

import java.util.ArrayList;

/**
 * FaithTrack represents the on-board faith track
 *
 */
public class FaithTrack implements Scoring {
    int markerPosition;
    private ArrayList<PopeFavor> receivedPopeFavors;

    /**
     * The constructor initializes receivedPopeFavors to an empty list
     */
    public FaithTrack() {
        receivedPopeFavors = new ArrayList<PopeFavor>();
    }

    /**
     * receivePopeFavor inserts a 'pope favor' into the list
     * @param popeFavor the PopeFavor to add
     */
    public void receivePopeFavor (PopeFavor popeFavor) {
        receivedPopeFavors.add(popeFavor);
    }

    /**
     * notifyEndOfTrack notifies the player's last turn
     * calls setLastTurn of Board
     *
     */
    private void notifyEndOfTrack(){
        if(markerPosition==23) Board.setLastTurn();

    }

    /**
     * getMarkerPosition returns the position of the player on the faith track
     * @return markerPosition of the object
     */
    public int getMarkerPosition(){
        return this.markerPosition;
    }
}
