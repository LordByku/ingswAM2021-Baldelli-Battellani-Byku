package it.polimi.ingsw.parsing;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class InitGameParser {
    private static InitGameParser instance;
    private JsonObject initGame;
    private final Gson gson;
    private final Parser parser;

    private InitGameParser() {
        gson = new Gson();
        parser = Parser.getInstance();
        initGame = parser.getConfig().getAsJsonObject("initGame");
    }

    public static InitGameParser getInstance() {
        if(instance == null) {
            instance = new InitGameParser();
        }
        return instance;
    }

    public void setConfig(JsonObject config) {
        initGame = config.getAsJsonObject("initGame");
    }

    public int getLeaderCardsToAssign() throws NoConfigFileException {
        if(initGame == null) {
            throw new NoConfigFileException();
        }
        return initGame.get("leaderCardsToAssign").getAsInt();
    }

    public int getLeaderCardsToDiscard() throws NoConfigFileException {
        if(initGame == null) {
            throw new NoConfigFileException();
        }
        return initGame.get("leaderCardsToDiscard").getAsInt();
    }

    public int getInitResources(int playerIndex) {
        if(initGame == null) {
            throw new NoConfigFileException();
        }
        JsonArray initResources = initGame.getAsJsonArray("resources");
        if(playerIndex < 0 || playerIndex >= initResources.size()) {
            return 0;
        }
        return initResources.get(playerIndex).getAsInt();
    }

    public int getInitFaithPoints(int playerIndex) {
        if(initGame == null) {
            throw new NoConfigFileException();
        }
        JsonArray initFaithPoints = initGame.getAsJsonArray("faithPoints");
        if(playerIndex < 0 || playerIndex >= initFaithPoints.size()) {
            return 0;
        }
        return initFaithPoints.get(playerIndex).getAsInt();
    }
}
