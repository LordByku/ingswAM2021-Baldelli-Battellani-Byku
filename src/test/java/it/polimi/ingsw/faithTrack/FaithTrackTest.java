package it.polimi.ingsw.faithTrack;

import it.polimi.ingsw.playerBoard.faithTrack.FaithTrack;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FaithTrackTest {

    @Test
    public void addFaithPointsTest (){
        FaithTrack faithTrack = new FaithTrack();
        faithTrack.addFaithPoints(5);
        faithTrack.addFaithPoints(6);
        faithTrack.addFaithPoints(10);
        assertEquals(21,faithTrack.getMarkerPosition());

    }



}
