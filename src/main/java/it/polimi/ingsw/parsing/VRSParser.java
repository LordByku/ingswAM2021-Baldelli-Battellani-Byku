package it.polimi.ingsw.parsing;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import it.polimi.ingsw.model.playerBoard.faithTrack.VaticanReportSection;

public class VRSParser {
    private static VRSParser instance;
    private final Gson gson;
    private final Parser parser;
    private final VaticanReportSection[] map;
    private JsonArray vaticanReportSections;
    private int currentVRS;

    private VRSParser() {
        gson = new Gson();
        parser = Parser.getInstance();

        vaticanReportSections = parser.getConfig().getAsJsonArray("vaticanReportSections");
        currentVRS = 0;
        map = new VaticanReportSection[vaticanReportSections.size()];
    }

    public static VRSParser getInstance() {
        if (instance == null) {
            instance = new VRSParser();
        }
        return instance;
    }

    public void setConfig(JsonObject config) {
        vaticanReportSections = (JsonArray) config.get("vaticanReportSections");
    }

    public VaticanReportSection getVRS(int index) throws NoConfigFileException {
        if (vaticanReportSections == null) {
            throw new NoConfigFileException();
        }

        if (index < 0 || index >= vaticanReportSections.size()) {
            return null;
        }

        if (map[index] != null) {
            return map[index];
        }

        JsonObject jsonVRS = (JsonObject) vaticanReportSections.get(index);

        int firstSpace = jsonVRS.get("firstSpace").getAsInt();
        int popeSpace = jsonVRS.get("popeSpace").getAsInt();
        int points = jsonVRS.get("points").getAsInt();

        return map[index] = new VaticanReportSection(firstSpace, popeSpace, points, index);
    }

    public VaticanReportSection getNextVRS() throws NoConfigFileException {
        VaticanReportSection vaticanReportSection = getVRS(currentVRS++);
        if (vaticanReportSection == null) {
            currentVRS = 0;
        }
        return vaticanReportSection;
    }
}
