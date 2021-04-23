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
    private static final String path = "src/resources/board.json";
    private final JsonObject board;
    private final Gson gson;
    private final Parser parser;

    private BoardParser() throws FileNotFoundException {
        JsonParser parser = new JsonParser();
        FileReader reader = new FileReader(path);
        board = (JsonObject) parser.parse(reader);

        gson = new Gson();
        this.parser = Parser.getInstance();
    }

    public static BoardParser getInstance() {
        if(instance == null) {
            try {
                instance = new BoardParser();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return instance;
    }

    public FaithTrack getFaithTrack() {
        return gson.fromJson(board.getAsJsonObject("faithTrack"), FaithTrack.class);
    }

    public ArrayList<Integer> getDepotSizes() {
        ArrayList<Integer> depotSizes = new ArrayList<>();
        for(JsonElement element: board.getAsJsonArray("depotSizes")) {
            depotSizes.add(element.getAsInt());
        }
        return depotSizes;
    }

    public ProductionDetails getDefaultProductionPower() {
        return Parser.getInstance().parseProductionDetails(board.getAsJsonObject("defaultProductionPower"));
    }

    public int getDevelopmentCardsSlots() {
        return board.get("developmentCardsSlots").getAsInt();
    }
}
