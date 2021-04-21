package it.polimi.ingsw.model.playerBoard;

/**
 * Scoring is the interface for classes that contribute points to a player
 */
public interface Scoring {
    /**
     * getPoints returns the points awarded by the object implementing this interface
     * @return The points awarded by the object
     */
    int getPoints();
}
