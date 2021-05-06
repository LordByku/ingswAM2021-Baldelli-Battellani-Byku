package it.polimi.ingsw.network.client.localModel;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;

public class LocalModel implements LocalModelElement {
    private GameZone gameZone;
    private ArrayList<Player> players;

    public Player getPlayer(String nickname) {
        for(Player player: players) {
            if(player.getNickname().equals(nickname)) {
                return player;
            }
        }
        return null;
    }

    @Override
    public void updateModel(JsonObject modelJson) {
        if(modelJson.has("gameZone")) {
            gameZone.updateModel(modelJson.getAsJsonObject("gameZone"));
        }
        if(modelJson.has("players")) {
            JsonArray playersJson = modelJson.getAsJsonArray("players");
            for(JsonElement playerJsonElement: playersJson) {
                JsonObject playerJson = (JsonObject) playerJsonElement;
                String nickname = playerJson.get("nickname").getAsString();
                getPlayer(nickname).updateModel(playerJson);
            }
        }
    }
}
