package it.polimi.ingsw.faithTrack;

import it.polimi.ingsw.playerBoard.faithTrack.FaithTrack;
import it.polimi.ingsw.playerBoard.faithTrack.InvalidPopeFavorException;
import it.polimi.ingsw.playerBoard.faithTrack.PopeFavor;
import org.junit.Test;

import static org.junit.Assert.*;

public class FaithTrackTest {

    @Test
    public void addFaithPointsTest (){
        FaithTrack faithTrack = new FaithTrack();
        faithTrack.addFaithPoints(5);
        faithTrack.addFaithPoints(6);
        faithTrack.addFaithPoints(10);
        assertEquals(21,faithTrack.getMarkerPosition());

        faithTrack.addFaithPoints(20);
        assertEquals(24,faithTrack.getMarkerPosition());
        assertNotEquals(41,faithTrack.getMarkerPosition());

    }
    @Test
    public void receivePopeFavorTest(){
        FaithTrack faithTrack=new FaithTrack();
        PopeFavor p1 = new PopeFavor(1);
        PopeFavor p2 = new PopeFavor(5);
        faithTrack.receivePopeFavor(p1);
        assertTrue(faithTrack.containsPopeFavor(p1));
        faithTrack.receivePopeFavor(p2);
        assertTrue(faithTrack.containsPopeFavor(p1));
        assertTrue(faithTrack.containsPopeFavor(p2));
        try {
            faithTrack.receivePopeFavor(null);
            fail();
        } catch (InvalidPopeFavorException e){
            assertTrue(faithTrack.containsPopeFavor(p1));
            assertTrue(faithTrack.containsPopeFavor(p2));
        }

    }

    @Test
    public void getPointsTest(){
        FaithTrack faithTrack= new FaithTrack();
        PopeFavor p1 = new PopeFavor(4);
        PopeFavor p2 = new PopeFavor(5);
        faithTrack.receivePopeFavor(p1);
        faithTrack.receivePopeFavor(p2);
        assertEquals(9,faithTrack.getPoints());
        try {
            faithTrack.receivePopeFavor(null);
            fail();
        } catch (InvalidPopeFavorException e){
            PopeFavor p3 = new PopeFavor(3);
            faithTrack.receivePopeFavor(p3);
            assertEquals(12,faithTrack.getPoints());

        }
    }



}
