package it.polimi.ingsw.faithTrack;

import it.polimi.ingsw.playerBoard.faithTrack.FaithTrack;
import it.polimi.ingsw.playerBoard.faithTrack.VRSObserver;
import it.polimi.ingsw.playerBoard.faithTrack.VaticanReportSection;
import org.junit.Test;

import static org.junit.Assert.*;

public class VRSObserverTest {

    @Test
    public void constructor (){
        FaithTrack faithTrack1 = new FaithTrack();
        FaithTrack faithTrack2 = new FaithTrack();
        FaithTrack faithTrack3 = new FaithTrack();



        assertTrue(VRSObserver.getInstance().getTracks().contains(faithTrack1));
        assertTrue(VRSObserver.getInstance().getTracks().contains(faithTrack3));
        assertTrue(VRSObserver.getInstance().getTracks().contains(faithTrack2));

        VaticanReportSection vaticanReportSection;
        vaticanReportSection = new VaticanReportSection(5,8,7);

        faithTrack1.addFaithPoints(2);
        faithTrack2.addFaithPoints(6);
        faithTrack3.addFaithPoints(7);

        faithTrack3.addFaithPoints(5);

        VRSObserver.getInstance().updateVRS();

        assertNotSame(vaticanReportSection.getPoints(),faithTrack1.getPoints());
     //   assertSame(vaticanReportSection.getPoints(),faithTrack2.getPoints());
       // assertSame(vaticanReportSection.getPoints(),faithTrack3.getPoints());

    }
}
