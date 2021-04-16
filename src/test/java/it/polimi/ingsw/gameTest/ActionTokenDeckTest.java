package it.polimi.ingsw.gameTest;

import it.polimi.ingsw.game.actionTokens.ActionToken;
import it.polimi.ingsw.game.actionTokens.ActionTokenDeck;
import it.polimi.ingsw.playerBoard.Board;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ActionTokenDeckTest {
    @Test
    public void constructorTest() {
        Board board = new Board();
        ActionTokenDeck actionTokenDeck = new ActionTokenDeck(board);

        assertEquals(7, actionTokenDeck.size());
    }

    @Test
    public void flipFirstTokenTest() {
        Board board = new Board();
        ActionTokenDeck actionTokenDeck = new ActionTokenDeck(board);

        int faithPoints = 0;
        for(int i = 6; i >= 0; --i) {
            actionTokenDeck = actionTokenDeck.flipFirstToken();
            if(actionTokenDeck.size() == i) {
                assertTrue(board.getFaithPoints() == faithPoints || board.getFaithPoints() == faithPoints + 2);
                faithPoints = board.getFaithPoints();
            } else {
                assertEquals(board.getFaithPoints(), faithPoints + 1);
                assertEquals(7, actionTokenDeck.size());
                break;
            }
        }
    }

    @Test
    public void removeTopTokenTest() {
        Board board = new Board();
        ActionTokenDeck actionTokenDeck = new ActionTokenDeck(board);

        actionTokenDeck.removeTopToken();
        assertEquals(6, actionTokenDeck.size());

        actionTokenDeck.removeTopToken();
        actionTokenDeck.removeTopToken();
        assertEquals(4, actionTokenDeck.size());
    }
}
