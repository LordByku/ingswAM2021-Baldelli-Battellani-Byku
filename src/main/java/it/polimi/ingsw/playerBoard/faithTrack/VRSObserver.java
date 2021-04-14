package it.polimi.ingsw.playerBoard.faithTrack;
import java.util.ArrayList;

/**
 * VRSObserver is a singleton class representing an observer of the Vatican Report Section
 */
public class VRSObserver {

    private static VRSObserver instance = null;
    private ArrayList<FaithTrack> tracks;
    private ArrayList<VaticanReportSection> vaticanReportSections;

    private VRSObserver() {
    }

    public static VRSObserver getInstance() {
        if (instance == null) {
            instance = new VRSObserver();

        }
        return instance;
    }


    /**
     * updateVRS()
     */
   public void updateVRS() {

   //    if ()
   }
}