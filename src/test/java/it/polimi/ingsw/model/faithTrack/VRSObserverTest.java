package it.polimi.ingsw.model.faithTrack;

import it.polimi.ingsw.model.playerBoard.faithTrack.*;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class VRSObserverTest {
    @Test
    public void updateVRSTest() {
        VRSObserver.getInstance().resetObserver();

        FaithTrack faithTrack1 = new FaithTrack();
        FaithTrack faithTrack2 = new FaithTrack();
        FaithTrack faithTrack3 = new FaithTrack();

        assertTrue(VRSObserver.getInstance().getTracks().contains(faithTrack1));
        assertTrue(VRSObserver.getInstance().getTracks().contains(faithTrack2));
        assertTrue(VRSObserver.getInstance().getTracks().contains(faithTrack3));
        try{
            FaithTrack faithTrack = null;
        }catch (InvalidFaithTrackException e){

            assertTrue(VRSObserver.getInstance().getTracks().contains(faithTrack1));
            assertTrue(VRSObserver.getInstance().getTracks().contains(faithTrack2));
            assertTrue(VRSObserver.getInstance().getTracks().contains(faithTrack3));
        }

        VaticanReportSection vaticanReportSection = new VaticanReportSection(5,8,7);
        VaticanReportSection vaticanReportSection2 = new VaticanReportSection(16,20,2);
        VRSObserver.getInstance().addVaticanReportSection(vaticanReportSection2);
        VRSObserver.getInstance().addVaticanReportSection(vaticanReportSection);
        try{
            VRSObserver.getInstance().addVaticanReportSection(null);
            fail();
        }catch (InvalidVaticanReportSectionException e){
            assertTrue(true);
        }

        faithTrack1.addFaithPoints(2);
        faithTrack2.addFaithPoints(6);
        faithTrack3.addFaithPoints(7);
        faithTrack3.addFaithPoints(5);

        VRSObserver.getInstance().updateVRS();

        ArrayList<FaithTrack> faithTracks = VRSObserver.getInstance().getTracks();
        assertEquals(3, faithTracks.size());
        assertEquals(2, faithTracks.get(0).getMarkerPosition());
        assertEquals(6, faithTracks.get(1).getMarkerPosition());
        assertEquals(12, faithTracks.get(2).getMarkerPosition());

        assertNotSame(vaticanReportSection.getPoints(),faithTrack1.getPoints());
        assertSame(vaticanReportSection.getPoints(),faithTrack2.getPoints());
        assertSame(vaticanReportSection.getPoints(),faithTrack3.getPoints());

        faithTrack1.addFaithPoints(5);
        assertNotSame(vaticanReportSection.getPoints(),faithTrack1.getPoints());
        assertSame(0,faithTrack1.getPoints());
        assertSame(vaticanReportSection.getPoints(),faithTrack2.getPoints());
        assertSame(vaticanReportSection.getPoints(),faithTrack3.getPoints());

        faithTrack2.addFaithPoints(1);
        faithTrack3.addFaithPoints(1);
        faithTrack1.addFaithPoints(6);
        VRSObserver.getInstance().updateVRS();
        assertEquals(3, faithTracks.size());
        assertEquals(13, faithTracks.get(0).getMarkerPosition());
        assertEquals(7, faithTracks.get(1).getMarkerPosition());
        assertEquals(13, faithTracks.get(2).getMarkerPosition());
        assertNotSame(vaticanReportSection2.getPoints(),faithTrack1.getPoints());
        assertNotSame(vaticanReportSection2.getPoints()+vaticanReportSection.getPoints(),faithTrack2.getPoints());
        assertNotSame(vaticanReportSection2.getPoints()+vaticanReportSection.getPoints(),faithTrack3.getPoints());

        faithTrack3.addFaithPoints(3);
        VRSObserver.getInstance().updateVRS();

        assertNotSame(vaticanReportSection2.getPoints(),faithTrack1.getPoints());
        assertNotSame(vaticanReportSection2.getPoints()+vaticanReportSection.getPoints(),faithTrack1.getPoints());
        assertNotSame(vaticanReportSection2.getPoints()+vaticanReportSection.getPoints(),faithTrack1.getPoints());
        faithTrack1.addFaithPoints(5);
        faithTrack3.addFaithPoints(6);
        faithTrack2.addFaithPoints(20);
        VRSObserver.getInstance().updateVRS();
        assertSame(vaticanReportSection2.getPoints()+vaticanReportSection.getPoints(),faithTrack3.getPoints());
        assertSame(vaticanReportSection2.getPoints(),faithTrack1.getPoints());
        assertSame(vaticanReportSection.getPoints(),faithTrack2.getPoints());



    }
}
