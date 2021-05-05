package it.polimi.ingsw.network.client;

import com.google.gson.*;
import it.polimi.ingsw.network.client.localModel.LocalModel;

import java.util.ArrayList;

public class ClientParser {
    private static ClientParser instance;
    private final JsonParser parser;
    private final Gson gson;

    private ClientParser() {
        parser = new JsonParser();
        gson = new Gson();
    }

    public static ClientParser getInstance() {
        if(instance == null) {
            instance = new ClientParser();
        }
        return instance;
    }

    public JsonObject parse(String line) {
        return (JsonObject) parser.parse(line);
    }

    public String getStatus(JsonObject json) {
        return json.get("status").getAsString();
    }

    public JsonElement getMessage(JsonObject json) {
        return json.get("message");
    }

    public String getType(JsonObject json) {
        return json.get("type").getAsString();
    }

    public ArrayList<String> getTurnOrder(JsonObject json) {
        JsonArray jsonTurnOrder = json.getAsJsonArray("turnOrder");
        ArrayList<String> turnOrder = new ArrayList<>();
        for(JsonElement jsonElement: jsonTurnOrder) {
            turnOrder.add(jsonElement.getAsString());
        }
        return turnOrder;
    }

    public JsonObject getConfig(JsonObject json) {
        return json.getAsJsonObject("config");
    }

    public LocalModel getLocalModel(JsonObject json) {
        return gson.fromJson(json, LocalModel.class);
    }
}
