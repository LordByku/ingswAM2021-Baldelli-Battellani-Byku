package it.polimi.ingsw.game;

import it.polimi.ingsw.playerBoard.Board;

/**
 * Player represents a player of the game
 */
public abstract class Player {
    /**
     * board is the personal Board of the player
     */
    private Board board;

    /**
     * The constructor creates a new Board for this player
     */
    public Player() {
        board = new Board();
    }

    /**
     * startTurn informs this Player that his/her turn started
     */
    protected abstract void startTurn();

    /**
     * endTurn is called by this Player to confirm that his/her turn ended
     * @throws GameEndedException The game already ended
     * @throws GameNotStartedException The game did not start yet
     */
    public abstract void endTurn() throws GameEndedException, GameNotStartedException;

    /**
     * getBoard returns a copy of the Board of this Player
     * @return A copy of the Board of this Player
     */
    public Board getBoard() {
        return board.clone();
    }
}
