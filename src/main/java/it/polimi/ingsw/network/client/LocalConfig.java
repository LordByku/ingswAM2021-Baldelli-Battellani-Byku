package it.polimi.ingsw.network.client;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.ingsw.model.playerBoard.faithTrack.CheckPoint;
import it.polimi.ingsw.model.playerBoard.faithTrack.VaticanReportSection;

import java.util.ArrayList;

public class LocalConfig {
    private static LocalConfig instance;
    private JsonObject config;
    private ArrayList<String> turnOrder;

    private LocalConfig() {}

    public static LocalConfig getInstance() {
        if(instance == null) {
            instance = new LocalConfig();
        }
        return instance;
    }

    public void setConfig(JsonObject config) {
        this.config = config;
    }

    public void setTurnOrder(ArrayList<String> turnOrder) {
        this.turnOrder = (ArrayList<String>) turnOrder.clone();
    }

    public int getInitialResources(String nickname) {
        switch(turnOrder.indexOf(nickname)) {
            case 1:
            case 2:
                return 1;
            case 3:
                return 2;
            default:
                return 0;
        }
    }

    public int getNumberOfDepots() {
        return config.getAsJsonObject("board").getAsJsonArray("depotSizes").size();
    }

    public ArrayList<Integer> getDepotSizes() {
        JsonArray depotSizesJson = config.getAsJsonObject("board").getAsJsonArray("depotSizes");
        ArrayList<Integer> depotSizes = new ArrayList<>();
        for(JsonElement depotSize: depotSizesJson) {
            depotSizes.add(depotSize.getAsInt());
        }
        return depotSizes;
    }

    public ArrayList<String> getTurnOrder() {
        return (ArrayList<String>) turnOrder.clone();
    }

    public int getFaithTrackFinalPosition() {
        return config.getAsJsonObject("board").getAsJsonObject("faithTrack").get("finalPosition").getAsInt();
    }

    public ArrayList<CheckPoint> getFaithTrackCheckPoints() {
        ArrayList<CheckPoint> checkPoints = new ArrayList<>();
        for(JsonElement checkPointJson: config.getAsJsonObject("board").getAsJsonObject("faithTrack").getAsJsonArray("checkPoints")) {
            checkPoints.add(ClientParser.getInstance().getCheckPoint((JsonObject) checkPointJson));
        }
        return checkPoints;
    }
}
