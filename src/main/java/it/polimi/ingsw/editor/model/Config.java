package it.polimi.ingsw.editor.model;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.polimi.ingsw.editor.model.resources.ObtainableResourceSet;
import it.polimi.ingsw.editor.model.resources.SpendableResourceSet;
import it.polimi.ingsw.editor.model.simplifiedModel.VaticanReportSection;

import java.io.*;
import java.net.URL;

public class Config {
    public static final int MAXPLAYERS = 4;
    private static final String defaultConfig = "config.json";
    private static InputStreamReader reader;
    private static Config instance;
    private final Gson gson;
    private final JsonObject json;
    private final FaithTrackEditor faithTrackEditor;
    private final InitGameEditor initGameEditor;
    private final BoardEditor boardEditor;
    private final DevCardsEditor devCardsEditor;
    private final LeaderCardsEditor leaderCardsEditor;

    public Config() {
        if (reader == null) {
            setDefaultPath();
        }

        gson = new Gson();
        JsonParser parser = new JsonParser();
        json = (JsonObject) parser.parse(reader);

        faithTrackEditor = gson.fromJson(json.getAsJsonObject("board").getAsJsonObject("faithTrack"), FaithTrackEditor.class);
        for (JsonElement jsonVRS : json.getAsJsonArray("vaticanReportSections")) {
            faithTrackEditor.addVRS(gson.fromJson(jsonVRS, VaticanReportSection.class));
        }

        initGameEditor = gson.fromJson(json.getAsJsonObject("initGame"), InitGameEditor.class);

        boardEditor = new BoardEditor(json.getAsJsonObject("board"));

        devCardsEditor = new DevCardsEditor(json.getAsJsonArray("developmentCards"));

        leaderCardsEditor = new LeaderCardsEditor(json.getAsJsonArray("leaderCards"));
    }

    public static void setDefaultPath() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL resource = classLoader.getResource(defaultConfig);
        try {
            reader = new InputStreamReader(resource.openStream());
        } catch (IOException e) {
        }
    }

    public static void setPath(String filename) throws FileNotFoundException {
        File file = new File(filename + ".json");
        reader = new FileReader(file);
    }

    public static Config getInstance() {
        if (instance == null) {
            instance = new Config();
        }
        return instance;
    }

    public static void reload() {
        instance = new Config();
    }

    public static JsonObject writeProductionPower(SpendableResourceSet spendable, ObtainableResourceSet obtainable) {
        JsonObject json = new JsonObject();

        json.add("productionIn", spendable.serialize());
        json.add("productionOut", obtainable.serialize());

        return json;
    }

    public void save(String outFilename) throws IOException {
        JsonObject out = new JsonObject();
        faithTrackEditor.write(out);
        boardEditor.write(out);
        initGameEditor.write(out);
        devCardsEditor.write(out);
        leaderCardsEditor.write(out);

        FileWriter writer = new FileWriter(outFilename + ".json");
        writer.write(out.toString());
        writer.flush();
        writer.close();
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
