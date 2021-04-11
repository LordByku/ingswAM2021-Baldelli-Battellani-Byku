package it.polimi.ingsw.leaderCards;

import it.polimi.ingsw.playerBoard.Board;
import it.polimi.ingsw.playerBoard.InvalidBoardException;

/**
 * LeaderCardRequirements represents the requirements needed to play a LeaderCard
 */

public interface LeaderCardRequirements {

    /**
     * isSatisfied calls the board to check if it match the requirements.
     * @param board the board of the current player.
     * @return true if the board matches the requirements, false otherwise.
     */
    boolean isSatisfied(Board board) throws InvalidBoardException;
}
