package it.polimi.ingsw.leaderCards;

import it.polimi.ingsw.playerBoard.Board;
import it.polimi.ingsw.playerBoard.InvalidBoardException;
import it.polimi.ingsw.playerBoard.Scoring;
import it.polimi.ingsw.resources.InvalidResourceException;

/**
 * LeaderCard refers to all the leader cards.
 */

public abstract class LeaderCard implements Scoring {

    /**
     * points are the victory points of the card.
     */
    private int points;

    /**
     * requirements needed to play the card.
     */
    private LeaderCardRequirements requirements;

    /**
     * board is the reference to the board.
     */
    protected Board board;

    /**
     * attribute active used to know if the card's been played.
     */
    protected boolean active = false;

    /**
     * attribute discard used to know if the card's been discarded.
     */
    protected boolean discarded = false;

    public LeaderCard(int points, LeaderCardRequirements requirements)
            throws InvalidPointsValueException, InvalidRequirementsException {
        if(points<=0){
            throw new InvalidPointsValueException();
        }
        if(requirements == null){
            throw new InvalidRequirementsException();
        }
        this.points = points;
        this.requirements = (LeaderCardRequirements) requirements.clone();
    }

    /**
     * isPlayable calls the Board to check if we match the requirements
     * @return false if we don't match the requirements or the card's been already played or discarded, true otherwise.
     */
    public boolean isPlayable(){
        if(active || discarded)
            return false;
        try {
            return requirements.isSatisfied(this.board);
        } catch (InvalidBoardException e) {
            return false;
        }
    }

    /**
     * @return if the card's been played.
     */
    public boolean isActive(){
        return active;
    }

    /**
     * Check if the card is playable and plays it.
     */
    public abstract void play() throws InvalidResourceException;

    /**
     * Discard the leader card to get 1 faith point.
     */
    public void discard(){
        if(!active && !discarded) {
            discarded = true;
            board.getFaithTrack().addFaithPoints();
            board.getLeaderCardArea().removeLeaderCard(this);
        }
    }

    /**
     * initDiscard allows to discard the leaderCard without getting faithPoints.
     * Used when the game starts and the leaderCards are hand out.
     */
    public void initDiscard(){
        if(!active && !discarded) {
            discarded = true;
            board.getLeaderCardArea().removeLeaderCard(this);
        }
    }

    /**
     * Assign the LeaderCard to the board.
     * @param board containing the LeaderCard.
     * @throws InvalidBoardException board is null
     */
    public void assignToBoard(Board board) throws InvalidBoardException {
        if(board == null) {
            throw new InvalidBoardException();
        }
        this.board = board.clone();
        board.getLeaderCardArea().addLeaderCard(this);
    }

    /**
     * getPoints returns the amount of points awarded by this LeaderCard
     * @return The amount of points awarded by this LeaderCard
     */
    public int getPoints() {
        return points;
    }
}
