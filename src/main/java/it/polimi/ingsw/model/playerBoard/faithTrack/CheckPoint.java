package it.polimi.ingsw.model.playerBoard.faithTrack;

import it.polimi.ingsw.model.playerBoard.Scoring;

public class CheckPoint implements Scoring {
    private final int position;
    private final int points;

    public CheckPoint(int position, int points) {
        this.position = position;
        this.points = points;
    }

    public int getPosition() {
        return position;
    }

    @Override
    public int getPoints() {
        return points;
    }
}
