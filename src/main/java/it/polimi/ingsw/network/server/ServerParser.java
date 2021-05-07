package it.polimi.ingsw.network.server;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.game.Person;
import it.polimi.ingsw.model.game.Player;

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

    public JsonObject parseLine(String line) {
        return (JsonObject) parser.parse(line);
    }

    public String getCommand(JsonObject clientMessage) {
        return clientMessage.get("command").getAsString();
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

    public int[] parseIntArray(JsonArray value) {
        return gson.fromJson(value, int[].class);
    }
}
