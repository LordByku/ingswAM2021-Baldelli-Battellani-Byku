package it.polimi.ingsw.view.localModel;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.ingsw.view.cli.CLIPrintable;

import java.util.ArrayList;

public class LocalModel implements LocalModelElement, CLIPrintable {
    private GameZone gameZone;
    private ArrayList<Player> players;
    private boolean endGame;

    public Player getPlayer(String nickname) {
        for (Player player : players) {
            if (player.getNickname().equals(nickname)) {
                return player;
            }
        }
        return null;
    }

    @Override
    public void updateModel(JsonObject modelJson) {
        if (modelJson.has("gameZone")) {
            gameZone.updateModel(modelJson.getAsJsonObject("gameZone"));
        }
        if (modelJson.has("players")) {
            JsonArray playersJson = modelJson.getAsJsonArray("players");
            for (JsonElement playerJsonElement : playersJson) {
                JsonObject playerJson = (JsonObject) playerJsonElement;
                String nickname = playerJson.get("nickname").getAsString();
                getPlayer(nickname).updateModel(playerJson);
            }
        }
    }

    public GameZone getGameZone() {
        return gameZone;
    }

    public void setEndGame() {
        endGame = true;
    }

    public boolean getEndGame() {
        return endGame;
    }

    public boolean allInitDiscard() {
        for (Player player : players) {
            if (!player.initDiscard()) {
                return false;
            }
        }
        return true;
    }

    public boolean allInitResources() {
        for (Player player : players) {
            if (!player.initResources()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String getCLIString() {
        StringBuilder result = new StringBuilder("[0] Check Marble Market\n[1] Check Card Market\n");
        for (int i = 0; i < players.size(); ++i) {
            String nickname = players.get(i).getNickname();
            result.append("[").append(i + 2).append("] Check ").append(nickname).append("'s board\n");
        }
        result.append("[x] Back");

        return result.toString();
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }
}
