package it.polimi.ingsw.view.localModel;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.ingsw.network.client.Client;

import java.util.ArrayList;

public class EndGame {
    private ArrayList<EndGameResult> results;
    private boolean computerWins;

    public void computeVictoryPoints() {
        for(EndGameResult endGameResult: results) {
            endGameResult.computeVictoryPoints();
        }
    }

    public ArrayList<EndGameResult> getResults(){
        return results;
    }

    public boolean getComputerWins(){
        return computerWins;
    }
}
