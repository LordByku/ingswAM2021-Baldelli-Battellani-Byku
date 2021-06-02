package it.polimi.ingsw.editor.model;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.polimi.ingsw.editor.model.simplifiedModel.VaticanReportSection;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Config {
    private static final String defaultPath = "src/main/resources/config.json";
    private static FileReader reader;
    static {
        try {
            reader = new FileReader(defaultPath);
        } catch (FileNotFoundException e) {
        }
    }

    private static Config instance;
    private static String path = defaultPath;
    private final Gson gson;
    private final JsonObject json;
    private final FaithTrackEditor faithTrackEditor;
    private final InitGameEditor initGameEditor;
    private final BoardEditor boardEditor;
    private final DevCardsEditor devCardsEditor;
    private final LeaderCardsEditor leaderCardsEditor;

    public static final int MAXPLAYERS = 4;

    public Config() {
        gson = new Gson();
        JsonParser parser = new JsonParser();
        json = (JsonObject) parser.parse(reader);

        faithTrackEditor = gson.fromJson(json.getAsJsonObject("board").getAsJsonObject("faithTrack"), FaithTrackEditor.class);
        for(JsonElement jsonVRS: json.getAsJsonArray("vaticanReportSections")) {
            faithTrackEditor.addVRS(gson.fromJson(jsonVRS, VaticanReportSection.class));
        }

        initGameEditor = gson.fromJson(json.getAsJsonObject("initGame"), InitGameEditor.class);

        boardEditor = new BoardEditor(json.getAsJsonObject("board"));

        devCardsEditor = new DevCardsEditor(json.getAsJsonArray("developmentCards"));

        leaderCardsEditor = new LeaderCardsEditor(json.getAsJsonArray("leaderCards"));
    }

    public static void setDefaultPath() {
        path = defaultPath;
        try {
            reader = new FileReader(path);
        } catch (FileNotFoundException e) {
        }
    }

    public static void setPath(String filename) throws FileNotFoundException {
        Config.path = "src/main/resources/custom/" + filename + ".json";
        reader = new FileReader(path);
    }

    public static Config getInstance() {
        if(instance == null) {
            instance = new Config();
        }
        return instance;
    }

    public static void reload() {
        instance = new Config();
    }

    public void save(String outFilename) throws IOException {
        // TODO: write json
        FileWriter writer = new FileWriter("src/main/resources/custom/" + outFilename + ".json");
        writer.write(json.toString());
        writer.flush();
    }

    public FaithTrackEditor getFaithTrackEditor() {
        return faithTrackEditor;
    }

    public InitGameEditor getInitGameEditor() {
        return initGameEditor;
    }

    public BoardEditor getBoardEditor() {
        return boardEditor;
    }

    public DevCardsEditor getDevCardsEditor() {
        return devCardsEditor;
    }

    public LeaderCardsEditor getLeaderCardsEditor() {
        return leaderCardsEditor;
    }
}
