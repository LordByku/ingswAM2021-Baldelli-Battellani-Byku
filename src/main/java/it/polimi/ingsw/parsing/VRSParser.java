package it.polimi.ingsw.parsing;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import it.polimi.ingsw.model.playerBoard.faithTrack.VaticanReportSection;

import java.util.ArrayList;

public class VRSParser {
    private static VRSParser instance;
    private final Gson gson;
    private final Parser parser;
    private JsonArray vaticanReportSections;
    private int currentVRS;

    private VRSParser() {
        gson = new Gson();
        parser = Parser.getInstance();

        vaticanReportSections = parser.getConfig().getAsJsonArray("vaticanReportSections");
        currentVRS = 0;
    }

    public static VRSParser getInstance() {
        if(instance == null) {
            instance = new VRSParser();
        }
        return instance;
    }

    public void setConfig(JsonObject config) {
        vaticanReportSections = (JsonArray) config.get("vaticanReportSections");
    }

    public VaticanReportSection getNextVRS() throws NoConfigFileException {
        if(vaticanReportSections == null) {
            throw new NoConfigFileException();
        }

        if(currentVRS >= vaticanReportSections.size()) {
            return null;
        }

        JsonObject jsonVRS = (JsonObject) vaticanReportSections.get(currentVRS);

        int firstSpace = jsonVRS.get("firstSpace").getAsInt();
        int popeSpace = jsonVRS.get("popeSpace").getAsInt();
        int points = jsonVRS.get("points").getAsInt();

        return new VaticanReportSection(firstSpace, popeSpace, points, currentVRS++);
    }
}
