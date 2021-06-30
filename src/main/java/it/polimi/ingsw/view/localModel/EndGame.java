package it.polimi.ingsw.view.localModel;

import java.util.ArrayList;

public class EndGame {
    private ArrayList<EndGameResult> results;
    private boolean computerWin;

    public void computeVictoryPoints() {
        for (EndGameResult endGameResult : results) {
            endGameResult.computeVictoryPoints();
        }
    }

    public ArrayList<EndGameResult> getResults() {
        return results;
    }

    public boolean getComputerWin() {
        return computerWin;
    }
}
