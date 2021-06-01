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
    private static Config instance;
    private static String path = "src/main/resources/config.json";
    private final Gson gson;
    private final JsonObject json;
    private String outFilename;
    private final FaithTrackEditor faithTrackEditor;
    private final InitGameEditor initGameEditor;
    private final BoardEditor boardEditor;
    private final DevCardsEditor devCardsEditor;

    public static final int MAXPLAYERS = 4;

    public Config() throws FileNotFoundException {
        gson = new Gson();
        JsonParser parser = new JsonParser();
        FileReader reader = new FileReader(path);
        json = (JsonObject) parser.parse(reader);

        faithTrackEditor = gson.fromJson(json.getAsJsonObject("board").getAsJsonObject("faithTrack"), FaithTrackEditor.class);
        for(JsonElement jsonVRS: json.getAsJsonArray("vaticanReportSections")) {
            faithTrackEditor.addVRS(gson.fromJson(jsonVRS, VaticanReportSection.class));
        }

        initGameEditor = gson.fromJson(json.getAsJsonObject("initGame"), InitGameEditor.class);

        boardEditor = new BoardEditor(json.getAsJsonObject("board"));

        devCardsEditor = new DevCardsEditor(json.getAsJsonArray("developmentCards"));
    }

    public static void setPath(String path) {
        Config.path = path;
    }

    public static Config getInstance() {
        if(instance == null) {
            try {
                instance = new Config();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return instance;
    }

    public void setOutFilename(String outFilename) {
        this.outFilename = outFilename;
    }

    public void save() throws IOException {
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
}
