package it.polimi.ingsw.model.faithTrack;

import it.polimi.ingsw.model.game.GameNotStartedException;
import it.polimi.ingsw.model.playerBoard.faithTrack.FaithTrack;
import it.polimi.ingsw.model.playerBoard.faithTrack.InvalidPopeFavorException;
import it.polimi.ingsw.model.playerBoard.faithTrack.PopeFavor;
import it.polimi.ingsw.model.resources.resourceSets.InvalidQuantityException;
import org.junit.Test;

import static org.junit.Assert.*;

public class FaithTrackTest {

    @Test
    public void addFaithPointsTest (){
        FaithTrack faithTrack = FaithTrack.builder();
        try{
            faithTrack.addFaithPoints(-5);
        }catch (InvalidQuantityException e){
            assertEquals(0, faithTrack.getMarkerPosition());
        }

        faithTrack.addFaithPoints(5);
        faithTrack.addFaithPoints(6);
        faithTrack.addFaithPoints(10);
        assertEquals(21,faithTrack.getMarkerPosition());

        try {
            faithTrack.addFaithPoints(20);
        } catch (GameNotStartedException e) {
        }
        assertEquals(24,faithTrack.getMarkerPosition());
        assertNotEquals(41,faithTrack.getMarkerPosition());
    }
    @Test
    public void receivePopeFavorTest(){
        FaithTrack faithTrack = FaithTrack.builder();
        PopeFavor p1 = new PopeFavor(1, 1003);
        PopeFavor p2 = new PopeFavor(5, 1004);
        faithTrack.addPopeFavor(p1);
        assertTrue(faithTrack.containsPopeFavor(p1));
        faithTrack.addPopeFavor(p2);
        assertTrue(faithTrack.containsPopeFavor(p1));
        assertTrue(faithTrack.containsPopeFavor(p2));
        try {
            faithTrack.addPopeFavor(null);
            fail();
        } catch (InvalidPopeFavorException e){
            assertTrue(faithTrack.containsPopeFavor(p1));
            assertTrue(faithTrack.containsPopeFavor(p2));
        }

    }

    @Test
    public void getPointsTest(){
        FaithTrack faithTrack = FaithTrack.builder();
        PopeFavor p1 = new PopeFavor(4, 1000);
        PopeFavor p2 = new PopeFavor(5, 1001);
        faithTrack.addPopeFavor(p1);
        faithTrack.addPopeFavor(p2);
        assertEquals(9,faithTrack.getPoints());
        try {
            faithTrack.addPopeFavor(null);
            fail();
        } catch (InvalidPopeFavorException e){
            PopeFavor p3 = new PopeFavor(3, 1002);
            faithTrack.addPopeFavor(p3);
            assertEquals(12,faithTrack.getPoints());

        }
    }



}
