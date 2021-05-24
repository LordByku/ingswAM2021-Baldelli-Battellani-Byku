package it.polimi.ingsw.network.client;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.ingsw.model.devCards.ProductionDetails;
import it.polimi.ingsw.model.playerBoard.faithTrack.CheckPoint;
import it.polimi.ingsw.model.playerBoard.faithTrack.VaticanReportSection;
import it.polimi.ingsw.parsing.Parser;
import it.polimi.ingsw.utility.Deserializer;

import java.util.ArrayList;

public class LocalConfig {
    private static LocalConfig instance;
    private JsonObject config;
    private ArrayList<String> turnOrder;
    private boolean host;

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

    public void setHost() {
        host = true;
    }

    public boolean isHost() {
        return host;
    }

    public int getInitialDiscards() {
        return config.getAsJsonObject("initGame").get("leaderCardsToDiscard").getAsInt();
    }

    public int getInitialResources(String nickname) {
        int index = turnOrder.indexOf(nickname);

        return config.getAsJsonObject("initGame").getAsJsonArray("resources").get(index).getAsInt();
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
            checkPoints.add(Deserializer.getInstance().getCheckPoint(checkPointJson));
        }
        return checkPoints;
    }

    public ProductionDetails getDefaultProductionPower() {
        return Parser.getInstance().parseProductionDetails(config.getAsJsonObject("board").getAsJsonObject("defaultProductionPower"));
    }
}
