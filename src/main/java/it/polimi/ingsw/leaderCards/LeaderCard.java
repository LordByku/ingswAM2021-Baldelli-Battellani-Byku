package it.polimi.ingsw.leaderCards;

import it.polimi.ingsw.playerBoard.Board;
import it.polimi.ingsw.playerBoard.Scoring;

/**
 * LeaderCard refers to all the leader cards.
 */

public abstract class LeaderCard implements Scoring {

    /**
     * points are the victory points of the card.
     */
    int points;

    /**
     * board is the reference to the board.
     */
    Board board;

    /**
     * requirements needed to play the card.
     */
    LeaderCardRequirements requirements;

    /**
     * attribute active used to know if the card's been played.
     */
    boolean active = false;

    /**
     * attribute discard used to know if the card's been discarded.
     */
    boolean discarded = false;


    /**
     * isPlayable calls the Board to check if we match the requirements
     * @return false if we don't match the requirements or the card's been already played or discarded, true otherwise.
     */
    public boolean isPlayable(){
        if(active || discarded)
            return false;
        return requirements.isSatisfied(this.board);
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
    public void play(){
        if(!active && !discarded && isPlayable())
            active = true;
    }

    /**
     * Discard the leader card to get 1 faith point.
     */
    public void discard(){
        if(!active) {
            discarded = true;
            //TODO:
            // method addFaithPoints() in Board

            // board.addFaithPoints();
        }
    }

}
