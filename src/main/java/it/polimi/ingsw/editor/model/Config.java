package it.polimi.ingsw.editor.model;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.polimi.ingsw.editor.model.simplifiedModel.VaticanReportSection;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;

public class Config {
    private static final String defaultConfig = "config.json";
    private static FileReader reader;

    private static Config instance;
    private final Gson gson;
    private final JsonObject json;
    private final FaithTrackEditor faithTrackEditor;
    private final InitGameEditor initGameEditor;
    private final BoardEditor boardEditor;
    private final DevCardsEditor devCardsEditor;
    private final LeaderCardsEditor leaderCardsEditor;

    public static final int MAXPLAYERS = 4;

    public Config() {
        if(reader == null) {
            setDefaultPath();
        }

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
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL resource = classLoader.getResource(defaultConfig);
        try {
            File file = new File(resource.toURI());
            reader = new FileReader(file);
        } catch (URISyntaxException | FileNotFoundException e) {
        }
    }

    public static void setPath(String filename) throws FileNotFoundException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL resource = classLoader.getResource("custom/" + filename);
        if (resource == null) {
            throw new FileNotFoundException();
        } else {
            try {
                File file = new File(resource.toURI());
                reader = new FileReader(file);
            } catch (URISyntaxException e) {
                throw new FileNotFoundException();
            }
        }
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
        JsonObject out = new JsonObject();
        faithTrackEditor.write(out);
        // TODO: write json

        String path = "src/main/resources/custom";
        File directory = new File(path);

        if(!directory.exists()) {
            directory.mkdir();
        }

        FileWriter writer = new FileWriter(path + "/" + outFilename + ".json");
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
