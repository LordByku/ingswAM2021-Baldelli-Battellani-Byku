package it.polimi.ingsw.network.server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ServerParser {
    private static ServerParser instance;
    private final JsonParser parser;
    private final Gson gson;

    private ServerParser() {
        parser = new JsonParser();
        gson = new Gson();
    }

    public static ServerParser getInstance() {
        if(instance == null) {
            instance = new ServerParser();
        }
        return instance;
    }

    public JsonObject parseLine(String line) {
        return (JsonObject) parser.parse(line);
    }

    public String getCommand(JsonObject clientMessage) {
        return clientMessage.get("command").getAsString();
    }
}
