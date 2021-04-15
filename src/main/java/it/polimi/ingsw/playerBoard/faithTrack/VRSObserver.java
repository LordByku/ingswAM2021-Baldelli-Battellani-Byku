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

    public void resetObserver() {
        tracks = new ArrayList<>();
        vaticanReportSections = new ArrayList<>();
    }

    public ArrayList<FaithTrack> getTracks() {
        return tracks;
    }

    /**
     * addFaithTrack adds a new faith track to be observed
     * @param faithTrack the new faith track to be observed
     * @throws InvalidFaithTrackException if faithTrack is null
     */
    public void addFaithTrack(FaithTrack faithTrack) throws InvalidFaithTrackException{
        if (faithTrack == null)
            throw new InvalidFaithTrackException();
        if(!this.getTracks().contains(faithTrack))
            tracks.add(faithTrack);
    }

    /**
     * addVaticanReportSection adds a new VRS to be observed
     * @param vaticanReportSection the new VRS to be observed
     * @throws InvalidVaticanReportSectionException if vaticanReportSection is null
     */
    public void addVaticanReportSection(VaticanReportSection vaticanReportSection) throws InvalidVaticanReportSectionException{
        if (vaticanReportSection==null)
            throw new InvalidVaticanReportSectionException();
        if(!this.vaticanReportSections.contains(vaticanReportSection))
            vaticanReportSections.add(vaticanReportSection);
    }

    /**
     * someoneReachedPopeSpace checks if someone has reached the Pope Space in a certain
     * Vatican ReportSection
     * @param vaticanReportSection the VRS checked
     * @return true iff exists a faith track's marker position that has reached
     * the Pope Space of the given vaticanReportSection
     */
    private boolean someoneReachedPopeSpace(VaticanReportSection vaticanReportSection){
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
                if (someoneReachedPopeSpace(vaticanReportSection)) {
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