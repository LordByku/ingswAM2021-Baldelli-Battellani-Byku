package it.polimi.ingsw.faithTrack;

import it.polimi.ingsw.playerBoard.faithTrack.InvalidVaticanReportSectionSquarePositions;
import it.polimi.ingsw.playerBoard.faithTrack.VaticanReportSection;
import it.polimi.ingsw.resources.resourceSets.InvalidQuantityException;
import org.junit.Test;
import static org.junit.Assert.*;


public class VaticanReportSectionTest {

    @Test
    public void constructorTest(){
        VaticanReportSection vaticanReportSection;
        vaticanReportSection = new VaticanReportSection(5,8,7);
        assertEquals(5,vaticanReportSection.getFirstSpace());
        assertEquals(8,vaticanReportSection.getPopeSpace());
        assertEquals(7,vaticanReportSection.getPoints());

        try {
            vaticanReportSection = new VaticanReportSection(-5,8,7);
            fail();
        }catch (InvalidQuantityException e){
            assertEquals(5,vaticanReportSection.getFirstSpace());
            assertEquals(8,vaticanReportSection.getPopeSpace());
            assertEquals(7,vaticanReportSection.getPoints());
        }

        try {
            vaticanReportSection = new VaticanReportSection(6,-8,9);
            fail();
        }catch (InvalidQuantityException e){
            assertEquals(5,vaticanReportSection.getFirstSpace());
            assertEquals(8,vaticanReportSection.getPopeSpace());
            assertEquals(7,vaticanReportSection.getPoints());
        }
        try {
            vaticanReportSection = new VaticanReportSection(2,4,-7);
            fail();
        }catch (InvalidQuantityException e){
            assertEquals(5,vaticanReportSection.getFirstSpace());
            assertEquals(8,vaticanReportSection.getPopeSpace());
            assertEquals(7,vaticanReportSection.getPoints());
        }
        try {
            vaticanReportSection = new VaticanReportSection(0,8,7);
            fail();
        }catch (InvalidQuantityException e){
            assertEquals(5,vaticanReportSection.getFirstSpace());
            assertEquals(8,vaticanReportSection.getPopeSpace());
            assertEquals(7,vaticanReportSection.getPoints());
        }
        try {
            vaticanReportSection = new VaticanReportSection(10,8,7);
            fail();
        }catch (InvalidVaticanReportSectionSquarePositions e){
            assertEquals(5,vaticanReportSection.getFirstSpace());
            assertEquals(8,vaticanReportSection.getPopeSpace());
            assertEquals(7,vaticanReportSection.getPoints());
        }

    }

    @Test
    public void isInsideSectionTest(){
        VaticanReportSection vaticanReportSection;
        vaticanReportSection= new VaticanReportSection(5,8,7);
        assertTrue(vaticanReportSection.isInsideSection(8));
        assertTrue(vaticanReportSection.isInsideSection(6));
        assertFalse(vaticanReportSection.isInsideSection(-2));
        assertTrue(vaticanReportSection.isInsideSection(10));
    }

    @Test
    public void reachedPopeSpaceTest(){
        VaticanReportSection vaticanReportSection;
        vaticanReportSection = new VaticanReportSection(14,18,10);
        assertTrue(vaticanReportSection.reachedPopeSpace(18));
        assertTrue(vaticanReportSection.reachedPopeSpace(20));
        assertFalse(vaticanReportSection.reachedPopeSpace(15));
        assertFalse(vaticanReportSection.reachedPopeSpace(13));
    }


}
