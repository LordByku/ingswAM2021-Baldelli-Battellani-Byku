package it.polimi.ingsw.model.playerBoard.faithTrack;

import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.playerBoard.Scoring;
import it.polimi.ingsw.model.resources.resourceSets.InvalidQuantityException;
import it.polimi.ingsw.parsing.BoardParser;

import java.util.ArrayList;

/**
 * FaithTrack represents the on-board faith track
 */
public class FaithTrack implements Scoring {
    private final int finalPosition;
    private final ArrayList<CheckPoint> checkPoints;
    /**
     * receivedPopeFavors represents a list of the pope favors card obtained
     * on the faith track
     */
    private final ArrayList<PopeFavor> receivedPopeFavors;
    /**
     * markerPosition represents the position on the faith track
     */
    private int markerPosition;

    /**
     * The constructor initializes receivedPopeFavors to an empty list
     */
    private FaithTrack() {
        finalPosition = 0;
        checkPoints = new ArrayList<>();
        markerPosition = 0;
        receivedPopeFavors = new ArrayList<>();
        VRSObserver.getInstance().addFaithTrack(this);
    }

    public static FaithTrack builder() {
        return BoardParser.getInstance().getFaithTrack();
    }

    /**
     * receivePopeFavor inserts a 'pope favor' into the list
     *
     * @param popeFavor the PopeFavor to add
     */
    public void addPopeFavor(PopeFavor popeFavor) throws InvalidPopeFavorException {
        if (popeFavor == null)
            throw new InvalidPopeFavorException();
        receivedPopeFavors.add(popeFavor);
    }

    /**
     * notifyEndOfTrack notifies the player's last turn
     * calls setLastTurn of Board
     */
    private void notifyEndOfTrack() {
        Game.getInstance().setLastTurn();
    }

    /**
     * getMarkerPosition returns the position of the player on the faith track
     *
     * @return markerPosition of the object
     */
    public int getMarkerPosition() {
        return markerPosition;
    }

    public boolean containsPopeFavor(PopeFavor popeFavor) {
        return receivedPopeFavors.contains(popeFavor);
    }

    /**
     * addFaithPoints adds "points" FaithPoints
     *
     * @param points to add.
     */
    public void addFaithPoints(int points) throws InvalidQuantityException {
        if (points < 0) {
            throw new InvalidQuantityException();
        } else {
            markerPosition += points;
            if (markerPosition >= finalPosition) {
                markerPosition = finalPosition;
                notifyEndOfTrack();
            }
        }
    }

    /**
     * addFaithPoints() adds one faith point
     */
    public void addFaithPoints() {
        addFaithPoints(1);
    }

    /**
     * getPoints represents sum of pope favor card's points
     *
     * @return sum of pope favor card's points
     */
    @Override
    public int getPoints() {
        int points = 0;
        for (CheckPoint checkPoint : checkPoints) {
            if (checkPoint.getPosition() <= markerPosition) {
                points = Math.max(points, checkPoint.getPoints());
            }
        }
        for (PopeFavor popeFavor : receivedPopeFavors) {
            points += popeFavor.getPoints();
        }
        return points;
    }

    public int getFinalPosition() {
        return finalPosition;
    }

    public ArrayList<PopeFavor> getReceivedPopeFavors() {
        return (ArrayList<PopeFavor>) receivedPopeFavors.clone();
    }
}
