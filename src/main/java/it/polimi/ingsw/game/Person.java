package it.polimi.ingsw.game;

import it.polimi.ingsw.playerBoard.Board;

/**
 * Person represents human players
 */
public class Person extends Player {
    /**
     * nickname is the identifier of this Person
     */
    private final String nickname;
    /**
     * isActivePlayer is a flag that tells whether it's currently
     * the turn of this Player
     */
    boolean isActivePlayer;
    /**
     * board is the personal Board of the player
     */
    private Board board;

    /**
     * The constructor creates a new Person given his/her nickname
     * @param nickname The nickname of the Person
     * @throws InvalidNicknameException nickname is null
     */
    public Person(String nickname) throws InvalidNicknameException {
        if(nickname == null) {
            throw new InvalidNicknameException();
        }
        this.nickname = nickname;
        isActivePlayer = false;
        board = new Board();
    }

    /**
     * getNickname returns the nickname of this Person
     * @return The nickname of this Person
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * startTurn sets the isActivePlayer flag to true
     */
    @Override
    protected void startTurn() {
        isActivePlayer = true;
    }

    /**
     * endTurn sets the isActivePlayer flag to false and calls
     * the turn handler method in Game
     * @throws GameEndedException The Game already ended
     * @throws GameNotStartedException The Game did not start yet
     */
    @Override
    public void endTurn() throws GameEndedException, GameNotStartedException {
        isActivePlayer = false;
        Game.getInstance().handleTurnOrder();
    }

    /**
     * getBoard returns a copy of the Board of this Player
     * @return A copy of the Board of this Player
     */
    public Board getBoard() {
        return board.clone();
    }
}
