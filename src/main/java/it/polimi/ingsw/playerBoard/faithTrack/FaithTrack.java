package it.polimi.ingsw.playerBoard.faithTrack;

import it.polimi.ingsw.playerBoard.Scoring;

import java.util.ArrayList;

/**
 * FaithTrack represents the on-board faith track
 */
public class FaithTrack implements Scoring {
    int markerPosition;
    private ArrayList<PopeFavor> receivedPopeFavors;

    public FaithTrack() {
        receivedPopeFavors = new ArrayList<PopeFavor>();
    }

    /**
     *
     * @param popeFavor
     */
    public void receivePopeFavor (PopeFavor popeFavor) {
        receivedPopeFavors.add(popeFavor);
    }

    /**
     *
     * @return
     */
    private boolean notifyEndOfTrack(){
        if(markerPosition==23)
            return true;
        return false;
        //TBR
    }

    /**
     *
     * @return
     */
    public int getMarkerPosition(){
        return this.markerPosition;
    }
}
