package it.polimi.ingsw.network.server;

import com.google.gson.*;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.game.Person;
import it.polimi.ingsw.model.game.Player;
import it.polimi.ingsw.model.resources.resourceSets.ConcreteResourceSet;

import java.util.ArrayList;

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

    public JsonElement parseLine(String line) {
        return parser.parse(line);
    }

    public String getCommand(JsonObject json) {
        return json.get("command").getAsString();
    }

    public JsonElement getMessage(JsonObject json) {
        return json.get("message");
    }

    public JsonObject getJsonPlayerList() {
        ArrayList<Player> players = Game.getInstance().getPlayers();
        JsonObject jsonObject = new JsonObject();
        JsonArray playerList = new JsonArray();

        for(Player player: players) {
            Person person = (Person) player;

            JsonObject playerObject = new JsonObject();
            playerObject.addProperty("nickname", person.getNickname());
            playerObject.addProperty("isHost", person.isHost());

            playerList.add(playerObject);
        }

        jsonObject.add("playerList", playerList);

        return jsonObject;
    }

    public int[] parseIntArray(JsonArray jsonArray) {
        return gson.fromJson(jsonArray, int[].class);
    }

    public ConcreteResourceSet[] parseConcreteResourceSetArray(JsonArray jsonArray) {
        return gson.fromJson(jsonArray, ConcreteResourceSet[].class);
    }
}
