package it.polimi.ingsw.gameTest;

import it.polimi.ingsw.game.actionTokens.ActionToken;
import it.polimi.ingsw.game.actionTokens.ActionTokenDeck;
import it.polimi.ingsw.playerBoard.Board;
import it.polimi.ingsw.playerBoard.faithTrack.FaithTrack;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ActionTokenDeckTest {
    @Test
    public void constructorTest() {
        Board board = new Board();
        ActionTokenDeck actionTokenDeck = new ActionTokenDeck(board.getFaithTrack());

        assertEquals(7, actionTokenDeck.size());
    }

    @Test
    public void flipFirstTokenTest() {
        Board board = new Board();
        ActionTokenDeck actionTokenDeck = new ActionTokenDeck(board.getFaithTrack());

        int faithPoints = 0;
        for(int i = 6; i >= 0; --i) {
            actionTokenDeck = actionTokenDeck.flipFirstToken();
            if(actionTokenDeck.size() == i) {
                assertTrue(board.getFaithTrack().getMarkerPosition() == faithPoints ||
                           board.getFaithTrack().getMarkerPosition() == faithPoints + 2);
                faithPoints = board.getFaithTrack().getMarkerPosition();
            } else {
                assertEquals(board.getFaithTrack().getMarkerPosition(), faithPoints + 1);
                assertEquals(7, actionTokenDeck.size());
                break;
            }
        }
    }

    @Test
    public void removeTopTokenTest() {
        Board board = new Board();
        ActionTokenDeck actionTokenDeck = new ActionTokenDeck(board.getFaithTrack());

        actionTokenDeck.removeTopToken();
        assertEquals(6, actionTokenDeck.size());

        actionTokenDeck.removeTopToken();
        actionTokenDeck.removeTopToken();
        assertEquals(4, actionTokenDeck.size());
    }
}
