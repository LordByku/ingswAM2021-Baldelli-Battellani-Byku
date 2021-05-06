package it.polimi.ingsw.parsing;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.polimi.ingsw.model.devCards.ProductionDetails;
import it.polimi.ingsw.model.playerBoard.faithTrack.FaithTrack;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

public class BoardParser {
    private static BoardParser instance;
    private JsonObject board;
    private final Gson gson;
    private final Parser parser;

    private BoardParser() {
        gson = new Gson();
        parser = Parser.getInstance();
        board = parser.getConfig().getAsJsonObject("board");
    }

    public static BoardParser getInstance() {
        if(instance == null) {
            instance = new BoardParser();
        }
        return instance;
    }

    public void setConfig(JsonObject config) {
        board = config.getAsJsonObject("board");
    }

    public FaithTrack getFaithTrack() throws NoConfigFileException {
        if(board == null) {
            throw new NoConfigFileException();
        }
        return gson.fromJson(board.getAsJsonObject("faithTrack"), FaithTrack.class);
    }

    public ArrayList<Integer> getDepotSizes() throws NoConfigFileException {
        if(board == null) {
            throw new NoConfigFileException();
        }
        ArrayList<Integer> depotSizes = new ArrayList<>();
        for(JsonElement element: board.getAsJsonArray("depotSizes")) {
            depotSizes.add(element.getAsInt());
        }
        return depotSizes;
    }

    public ProductionDetails getDefaultProductionPower() throws NoConfigFileException {
        if(board == null) {
            throw new NoConfigFileException();
        }
        return parser.parseProductionDetails(board.getAsJsonObject("defaultProductionPower"));
    }

    public int getDevelopmentCardsSlots() throws NoConfigFileException {
        if(board == null) {
            throw new NoConfigFileException();
        }
        return board.get("developmentCardsSlots").getAsInt();
    }
}
