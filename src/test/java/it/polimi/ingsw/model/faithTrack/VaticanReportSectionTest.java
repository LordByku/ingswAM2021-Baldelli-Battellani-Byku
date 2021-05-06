package it.polimi.ingsw.model.faithTrack;

import it.polimi.ingsw.model.playerBoard.faithTrack.InvalidVaticanReportSectionSquarePositions;
import it.polimi.ingsw.model.playerBoard.faithTrack.VaticanReportSection;
import it.polimi.ingsw.model.resources.resourceSets.InvalidQuantityException;
import org.junit.Test;
import static org.junit.Assert.*;


public class VaticanReportSectionTest {

    @Test
    public void constructorTest(){
        VaticanReportSection vaticanReportSection;
        vaticanReportSection = new VaticanReportSection(5,8,7, 1002);
        assertEquals(5,vaticanReportSection.getFirstSpace());
        assertEquals(8,vaticanReportSection.getPopeSpace());
        assertEquals(7,vaticanReportSection.getPoints());

        try {
            vaticanReportSection = new VaticanReportSection(-5,8,7, 1003);
            fail();
        }catch (InvalidQuantityException e){
            assertEquals(5,vaticanReportSection.getFirstSpace());
            assertEquals(8,vaticanReportSection.getPopeSpace());
            assertEquals(7,vaticanReportSection.getPoints());
        }

        try {
            vaticanReportSection = new VaticanReportSection(6,-8,9, 1004);
            fail();
        }catch (InvalidQuantityException e){
            assertEquals(5,vaticanReportSection.getFirstSpace());
            assertEquals(8,vaticanReportSection.getPopeSpace());
            assertEquals(7,vaticanReportSection.getPoints());
        }
        try {
            vaticanReportSection = new VaticanReportSection(2,4,-7, 1005);
            fail();
        }catch (InvalidQuantityException e){
            assertEquals(5,vaticanReportSection.getFirstSpace());
            assertEquals(8,vaticanReportSection.getPopeSpace());
            assertEquals(7,vaticanReportSection.getPoints());
        }
        try {
            vaticanReportSection = new VaticanReportSection(0,8,7, 1006);
            fail();
        }catch (InvalidQuantityException e){
            assertEquals(5,vaticanReportSection.getFirstSpace());
            assertEquals(8,vaticanReportSection.getPopeSpace());
            assertEquals(7,vaticanReportSection.getPoints());
        }
        try {
            vaticanReportSection = new VaticanReportSection(10,8,7, 1007);
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
        vaticanReportSection= new VaticanReportSection(5,8,7, 1008);
        assertTrue(vaticanReportSection.isInsideSection(8));
        assertTrue(vaticanReportSection.isInsideSection(6));
        assertFalse(vaticanReportSection.isInsideSection(-2));
        assertTrue(vaticanReportSection.isInsideSection(10));
    }

    @Test
    public void reachedPopeSpaceTest(){
        VaticanReportSection vaticanReportSection;
        vaticanReportSection = new VaticanReportSection(14,18,10, 1009);
        assertTrue(vaticanReportSection.reachedPopeSpace(18));
        assertTrue(vaticanReportSection.reachedPopeSpace(20));
        assertFalse(vaticanReportSection.reachedPopeSpace(15));
        assertFalse(vaticanReportSection.reachedPopeSpace(13));
    }


}
