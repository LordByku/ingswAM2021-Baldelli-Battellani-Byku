package it.polimi.ingsw.model.playerBoard.faithTrack;

import it.polimi.ingsw.model.playerBoard.Scoring;
import it.polimi.ingsw.model.resources.resourceSets.InvalidQuantityException;

/**
 * PopeFavor represents the pope favor card
 */
public class PopeFavor implements Scoring {
    /**
     * The pope favor card's points
     */
    int points;

    /**
     * The constructor creates a new Pope Favor
     * @param points The pope favor card's points
     */
    public PopeFavor(int points) throws InvalidQuantityException {
        if(points<=0)
            throw new InvalidQuantityException();
        this.points=points;

    }

    /**
     * getPoints is an overridden getter of the attribute points of the object
     * @return the points of the Pope Favor card
     */
    @Override
    public int getPoints() {
        return points;
    }
}
