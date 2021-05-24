package it.polimi.ingsw.model.playerBoard.faithTrack;

import it.polimi.ingsw.parsing.VRSParser;

import java.util.ArrayList;

/**
 * VRSObserver is a singleton class representing an observer of the Vatican Report Section
 */
public class VRSObserver {
    /**
     * instance represents the singleton instance
     */
    private static VRSObserver instance = null;

    /**
     * tracks represents a list of the faith tracks observed
     */
    private ArrayList<FaithTrack> tracks = new ArrayList<>();
    /**
     * vaticanReportSections represents a list of the Vatican Report Section observed
     */
    private ArrayList<VaticanReportSection> vaticanReportSections = new ArrayList<>();

    /**
     * The constructor initialises a singleton observer
     */
    private VRSObserver() {
        VaticanReportSection vaticanReportSection;

        while ((vaticanReportSection = VRSParser.getInstance().getNextVRS()) != null) {
            vaticanReportSections.add(vaticanReportSection);
        }
    }

    /**
     * @return the instance "VRSObserver" to the caller
     */
    public static VRSObserver getInstance() {
        if (instance == null) {
            instance = new VRSObserver();
        }
        return instance;
    }

    /**
     * resetObserver empties both tracks and vaticanReportSections,
     * re-initialising both attributes to new empty lists
     */
    public void resetObserver() {
        tracks = new ArrayList<>();
        vaticanReportSections = new ArrayList<>();
    }

    /**
     * getTracks() is a getter of the attribute "tracks"
     *
     * @return a list of the tracks observed
     */
    public ArrayList<FaithTrack> getTracks() {
        return tracks;
    }

    /**
     * addFaithTrack adds a new faith track to be observed
     *
     * @param faithTrack the new faith track to be observed
     * @throws InvalidFaithTrackException if faithTrack is null
     */
    public void addFaithTrack(FaithTrack faithTrack) throws InvalidFaithTrackException {
        if (faithTrack == null)
            throw new InvalidFaithTrackException();
        if (!this.getTracks().contains(faithTrack))
            tracks.add(faithTrack);
    }

    /**
     * addVaticanReportSection adds a new VRS to be observed
     *
     * @param vaticanReportSection the new VRS to be observed
     * @throws InvalidVaticanReportSectionException if vaticanReportSection is null
     */
    public void addVaticanReportSection(VaticanReportSection vaticanReportSection) throws InvalidVaticanReportSectionException {
        if (vaticanReportSection == null)
            throw new InvalidVaticanReportSectionException();
        if (!this.vaticanReportSections.contains(vaticanReportSection))
            vaticanReportSections.add(vaticanReportSection);
    }

    /**
     * someoneReachedPopeSpace checks if someone has reached the Pope Space in a certain
     * Vatican ReportSection
     *
     * @param vaticanReportSection the VRS checked
     * @return true iff exists a faith track's marker position that has reached
     * the Pope Space of the given vaticanReportSection
     */
    private boolean someoneReachedPopeSpace(VaticanReportSection vaticanReportSection) {
        for (FaithTrack faithTrack : tracks)
            if (vaticanReportSection.reachedPopeSpace(faithTrack.getMarkerPosition()))
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
                    PopeFavor popeFavor = vaticanReportSection.getPopeFavor();
                    for (FaithTrack faithTrack : tracks) {
                        if (vaticanReportSection.isInsideSection(faithTrack.getMarkerPosition())) {
                            faithTrack.addPopeFavor(popeFavor);
                        }
                    }
                }
            }
        }
    }
}