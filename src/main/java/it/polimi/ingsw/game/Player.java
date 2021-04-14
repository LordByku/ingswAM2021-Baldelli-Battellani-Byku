package it.polimi.ingsw.game;

import it.polimi.ingsw.playerBoard.Board;

public abstract class Player {
    private Board board;

    public Player() {
        board = new Board();
    }

    protected abstract void startTurn();

    public abstract void endTurn() throws GameEndedException, GameNotStartedException;

    public Board getBoard() {
        return board.clone();
    }
}
