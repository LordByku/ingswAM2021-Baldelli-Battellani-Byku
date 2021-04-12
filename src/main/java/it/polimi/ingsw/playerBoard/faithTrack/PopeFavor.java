package it.polimi.ingsw.playerBoard.faithTrack;

import it.polimi.ingsw.playerBoard.Scoring;

/**
 * PopeFavor represents the pope favor card
 */
public class PopeFavor implements Scoring {
    /**
     * The pope favor card's points
     */
    int points;

    /**
     * @param points The pope favor card's points
     */
    public PopeFavor(int points){
        this.points=points;
    }
}
