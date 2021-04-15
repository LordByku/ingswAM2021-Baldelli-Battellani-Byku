package it.polimi.ingsw.playerBoard.faithTrack;
import java.util.ArrayList;

/**
 * VRSObserver is a singleton class representing an observer of the Vatican Report Section
 */
public class VRSObserver {

    private static VRSObserver instance = null;


    private ArrayList<FaithTrack> tracks = new ArrayList<>();
    private ArrayList<VaticanReportSection> vaticanReportSections = new ArrayList<>();

    private VRSObserver() {
    }


    public static VRSObserver getInstance() {
        if (instance == null) {
            instance = new VRSObserver();

        }
        return instance;
    }

    public ArrayList<FaithTrack> getTracks() {
        return tracks;
    }

    public void addFaithTrack(FaithTrack faithTrack){
        this.tracks.add(faithTrack);
    }

    public void addVaticanReportSection(VaticanReportSection vaticanReportSection){
        this.vaticanReportSections.add(vaticanReportSection);
    }

    private boolean someoneReachedPopeSpace( ArrayList<FaithTrack> tracks ,VaticanReportSection vaticanReportSection){
        for (FaithTrack faithTrack: tracks)
            if(vaticanReportSection.reachedPopeSpace(faithTrack.getMarkerPosition()))
                return true;
        return false;
    }


    /**
     * updateVRS()
     * The method represents the activation of the Vatican Report
     */
   public void updateVRS() {

       for (VaticanReportSection vaticanReportSection : vaticanReportSections) {
           if (!vaticanReportSection.isVisited()) {
               if (someoneReachedPopeSpace(tracks,vaticanReportSection)) {
                   vaticanReportSection.setVisitedTrue();
                   PopeFavor popeFavor = new PopeFavor(vaticanReportSection.getPoints());
                   for (FaithTrack faithTrack : tracks) {
                               if (vaticanReportSection.isInsideSection(faithTrack.getMarkerPosition())) {
                                   faithTrack.receivePopeFavor(popeFavor);
                               }
                           }
                       }
                    }
           }
       }
   }