package it.polimi.ingsw.leaderCards;

import it.polimi.ingsw.playerBoard.Board;
import it.polimi.ingsw.playerBoard.InvalidBoardException;

/**
 * LeaderCardRequirements represents the requirements needed to play a LeaderCard
 */

public interface LeaderCardRequirements extends Cloneable {

    /**
     * isSatisfied calls the board to check if it match the requirements.
     *
     * @param board the board of the current player.
     * @return true if the board matches the requirements, false otherwise.
     * @throws InvalidBoardException board is null.
     */
    boolean isSatisfied(Board board) throws InvalidBoardException;

    /**
     * clone returns a copy of the object
     * @return A copy of the object
     */
    Object clone();
}
