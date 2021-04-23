package it.polimi.ingsw.parsing;

import com.google.gson.Gson;
import it.polimi.ingsw.model.playerBoard.faithTrack.FaithTrack;
import it.polimi.ingsw.model.playerBoard.faithTrack.PopeFavor;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BoardParserTest {
    @Test
    public void faithTrackTest() {
        Gson gson = new Gson();
        FaithTrack faithTrack = BoardParser.getInstance().getFaithTrack();

        assertEquals(0, faithTrack.getMarkerPosition());
        assertEquals(24, faithTrack.getFinalPosition());

        assertEquals(0, faithTrack.getPoints());

        faithTrack.addFaithPoints(3);
        assertEquals(1, faithTrack.getPoints());

        faithTrack.addFaithPoints(7);
        assertEquals(4, faithTrack.getPoints());

        faithTrack.addPopeFavor(new PopeFavor(2));
        assertEquals(6, faithTrack.getPoints());
    }
}
